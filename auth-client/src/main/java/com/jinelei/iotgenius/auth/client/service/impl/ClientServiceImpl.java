package com.jinelei.iotgenius.auth.client.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.executor.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.client.configuration.authentication.ClientDetailService;
import com.jinelei.iotgenius.auth.client.configuration.authentication.permission.instance.PermissionInstance;
import com.jinelei.iotgenius.auth.client.convertor.ClientConvertor;
import com.jinelei.iotgenius.auth.client.convertor.PermissionConvertor;
import com.jinelei.iotgenius.auth.client.convertor.RoleConvertor;
import com.jinelei.iotgenius.auth.client.domain.ClientEntity;
import com.jinelei.iotgenius.auth.client.domain.ClientPermissionEntity;
import com.jinelei.iotgenius.auth.client.mapper.ClientMapper;
import com.jinelei.iotgenius.auth.client.service.ClientPermissionService;
import com.jinelei.iotgenius.auth.client.service.ClientService;
import com.jinelei.iotgenius.auth.client.service.PermissionService;
import com.jinelei.iotgenius.auth.client.service.RolePermissionService;
import com.jinelei.iotgenius.auth.client.service.RoleService;
import com.jinelei.iotgenius.auth.dto.client.ClientCreateRequest;
import com.jinelei.iotgenius.auth.dto.client.ClientDeleteRequest;
import com.jinelei.iotgenius.auth.dto.client.ClientQueryRequest;
import com.jinelei.iotgenius.auth.dto.client.ClientResponse;
import com.jinelei.iotgenius.auth.dto.client.ClientUpdateRequest;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientEntity>
        implements ClientService, ClientDetailService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    protected ClientConvertor userConvertor;
    @Autowired
    protected RoleConvertor roleConvertor;
    @Autowired
    protected PermissionConvertor permissionConvertor;
    @Autowired
    protected ClientPermissionService userRoleService;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected RolePermissionService rolePermissionService;
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
    @Autowired
    protected RedisTemplate<String, ClientResponse> userRedisTemplate;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AuthorizationHelper authorizationHelper;

    @Override
    public void create(ClientCreateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_CREATE);
        final ClientEntity entity = userConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("角色信息不合法"));
        Optional.of(entity).map(ClientEntity::getSecretKey).ifPresentOrElse(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
            entity.setSecretKey(passwordEncoder.encode(password));
        }, () -> entity.setSecretKey(passwordEncoder.encode(UUID.randomUUID().toString().replace("-", ""))));
        int inserted = baseMapper.insert(entity);
        log.debug("创建客户端: {}", inserted);
        Optional.ofNullable(request.getPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<ClientPermissionEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                ClientPermissionEntity clientPermissionEntity = new ClientPermissionEntity();
                                clientPermissionEntity.setClientId(entity.getId());
                                clientPermissionEntity.setPermissionId(id);
                                return clientPermissionEntity;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.debug("创建客户端角色关系: {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(ClientDeleteRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_DELETE);
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.debug("删除客户端: {}", deleted);
            Assert.state(deleted == 1, "角色删除失败");
            LambdaUpdateWrapper<ClientPermissionEntity> wrapper = Wrappers.lambdaUpdate(ClientPermissionEntity.class)
                    .eq(ClientPermissionEntity::getClientId, request.getId());
            int deletedClientPermission = userRoleService.getBaseMapper().delete(wrapper);
            log.debug("删除客户端角色关系: {}", deletedClientPermission);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.debug("删除客户端: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "角色删除失败");
            int deletedClientPermission = userRoleService.getBaseMapper().delete(
                    Wrappers.lambdaUpdate(ClientPermissionEntity.class).in(ClientPermissionEntity::getClientId,
                            request.getIds()));
            log.debug("删除客户端角色关系: {}", deletedClientPermission);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated ClientUpdateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_UPDATE);
        final String accessKey = Optional.ofNullable(request).map(ClientUpdateRequest::getAccessKey)
                .orElseThrow(() -> new InvalidArgsException("客户端密钥不能为空"));
        LambdaUpdateWrapper<ClientEntity> wrapper = Wrappers.lambdaUpdate(ClientEntity.class);
        wrapper.eq(ClientEntity::getId, request.getId());
        wrapper.set(ClientEntity::getAccessKey, request.getAccessKey());
        wrapper.set(ClientEntity::getSecretKey, request.getSecretKey());
        wrapper.set(ClientEntity::getExpiredAt, request.getExpiredAt());
        wrapper.set(ClientEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        log.debug("更新客户端: {}", updated);
        int deleted = userRoleService.getBaseMapper()
                .delete(Wrappers.lambdaUpdate(ClientPermissionEntity.class).eq(ClientPermissionEntity::getClientId,
                        request.getId()));
        log.debug("删除客户端角色关系: {}", deleted);
        Optional.ofNullable(request.getPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<ClientPermissionEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                ClientPermissionEntity clientPermissionEntity = new ClientPermissionEntity();
                                clientPermissionEntity.setClientId(request.getId());
                                clientPermissionEntity.setPermissionId(id);
                                return clientPermissionEntity;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.debug("创建客户端角色关系: {}", insert.size());
                });
        Assert.state(updated == 1, "角色更新失败");
    }

    @Override
    public ClientEntity get(ClientQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_DETAIL);
        LambdaQueryWrapper<ClientEntity> wrapper = Wrappers.lambdaQuery(ClientEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), ClientEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getAccessKey()), ClientEntity::getAccessKey, request.getAccessKey());
        wrapper.like(Objects.nonNull(request.getSecretKey()), ClientEntity::getSecretKey, request.getSecretKey());
        wrapper.like(Objects.nonNull(request.getExpiredAt()), ClientEntity::getExpiredAt, request.getExpiredAt());
        wrapper.like(Objects.nonNull(request.getRemark()), ClientEntity::getRemark, request.getRemark());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<ClientEntity> list(ClientQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_SUMMARY);
        LambdaQueryWrapper<ClientEntity> wrapper = Wrappers.lambdaQuery(ClientEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), ClientEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getAccessKey()), ClientEntity::getAccessKey, request.getAccessKey());
        wrapper.like(Objects.nonNull(request.getSecretKey()), ClientEntity::getSecretKey, request.getSecretKey());
        wrapper.like(Objects.nonNull(request.getExpiredAt()), ClientEntity::getExpiredAt, request.getExpiredAt());
        wrapper.like(Objects.nonNull(request.getRemark()), ClientEntity::getRemark, request.getRemark());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<ClientEntity> page(IPage<ClientEntity> page, ClientQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_SUMMARY);
        LambdaQueryWrapper<ClientEntity> wrapper = Wrappers.lambdaQuery(ClientEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), ClientEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getAccessKey()), ClientEntity::getAccessKey, request.getAccessKey());
        wrapper.like(Objects.nonNull(request.getSecretKey()), ClientEntity::getSecretKey, request.getSecretKey());
        wrapper.like(Objects.nonNull(request.getExpiredAt()), ClientEntity::getExpiredAt, request.getExpiredAt());
        wrapper.like(Objects.nonNull(request.getRemark()), ClientEntity::getRemark, request.getRemark());
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public ClientResponse convert(ClientEntity entity) {
        return userConvertor.entityToResponse(entity);
    }

    @Override
    public ClientResponse loadClientByAccessKey(String accessKey) {
        Optional.ofNullable(accessKey).orElseThrow(() -> new UsernameNotFoundException("AccessKey不能为空"));
        ClientQueryRequest clientQueryRequest = new ClientQueryRequest();
        clientQueryRequest.setAccessKey(accessKey);
        ClientEntity clientEntity = this.get(clientQueryRequest);
        Optional.ofNullable(clientEntity).orElseThrow(() -> new UsernameNotFoundException("AccessKey不存在"));
        return convert(clientEntity);
    }

}
