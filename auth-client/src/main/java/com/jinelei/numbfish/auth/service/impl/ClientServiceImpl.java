package com.jinelei.numbfish.auth.service.impl;

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
import com.jinelei.numbfish.auth.configuration.authentication.ClientDetailService;
import com.jinelei.numbfish.auth.convertor.ClientConvertor;
import com.jinelei.numbfish.auth.convertor.PermissionConvertor;
import com.jinelei.numbfish.auth.convertor.RoleConvertor;
import com.jinelei.numbfish.auth.entity.ClientEntity;
import com.jinelei.numbfish.auth.entity.ClientPermissionEntity;
import com.jinelei.numbfish.auth.mapper.ClientMapper;
import com.jinelei.numbfish.auth.service.ClientPermissionService;
import com.jinelei.numbfish.auth.service.ClientService;
import com.jinelei.numbfish.auth.service.PermissionService;
import com.jinelei.numbfish.auth.service.RolePermissionService;
import com.jinelei.numbfish.auth.service.RoleService;
import com.jinelei.numbfish.auth.dto.ClientCreateRequest;
import com.jinelei.numbfish.auth.dto.ClientDeleteRequest;
import com.jinelei.numbfish.auth.dto.ClientQueryRequest;
import com.jinelei.numbfish.auth.dto.ClientResponse;
import com.jinelei.numbfish.auth.dto.ClientUpdateRequest;
import com.jinelei.numbfish.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientEntity>
        implements ClientService, ClientDetailService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    protected ClientConvertor clientConvertor;
    @Autowired
    protected RoleConvertor roleConvertor;
    @Autowired
    protected PermissionConvertor permissionConvertor;
    @Autowired
    protected ClientPermissionService clientPermissionService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected RolePermissionService rolePermissionService;
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
    @Autowired
    protected RedisTemplate<String, ClientResponse> clientRedisTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    @Override
    public void create(ClientCreateRequest request) {
        final ClientEntity entity = clientConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("客户端信息不合法"));
        Optional.of(entity).map(ClientEntity::getSecretKey).ifPresentOrElse(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
            entity.setSecretKey(password);
        }, () -> entity.setSecretKey(UUID.randomUUID().toString().replace("-", "")));
        int inserted = baseMapper.insert(entity);
        log.debug("创建客户端: {}", inserted);
        Optional.ofNullable(request.getPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<ClientPermissionEntity> clientRoleEntities = list.parallelStream()
                            .map(id -> {
                                ClientPermissionEntity clientPermissionEntity = new ClientPermissionEntity();
                                clientPermissionEntity.setClientId(entity.getId());
                                clientPermissionEntity.setPermissionId(id);
                                return clientPermissionEntity;
                            }).toList();
                    List<BatchResult> insert = clientPermissionService.getBaseMapper().insert(clientRoleEntities);
                    log.debug("创建客户端角色关系: {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(ClientDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.debug("删除客户端: {}", deleted);
            Assert.state(deleted == 1, "客户端删除失败");
            LambdaUpdateWrapper<ClientPermissionEntity> wrapper = Wrappers.lambdaUpdate(ClientPermissionEntity.class)
                    .eq(ClientPermissionEntity::getClientId, request.getId());
            int deletedClientPermission = clientPermissionService.getBaseMapper().delete(wrapper);
            log.debug("删除客户端权限关系: {}", deletedClientPermission);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.debug("删除客户端: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "客户端删除失败");
            int deletedClientPermission = clientPermissionService.getBaseMapper().delete(
                    Wrappers.lambdaUpdate(ClientPermissionEntity.class).in(ClientPermissionEntity::getClientId,
                            request.getIds()));
            log.debug("删除客户端权限关系: {}", deletedClientPermission);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated ClientUpdateRequest request) {
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
        int deleted = clientPermissionService.getBaseMapper()
                .delete(Wrappers.lambdaUpdate(ClientPermissionEntity.class).eq(ClientPermissionEntity::getClientId,
                        request.getId()));
        log.debug("删除客户端权限关系: {}", deleted);
        Optional.ofNullable(request.getPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<ClientPermissionEntity> clientRoleEntities = list.parallelStream()
                            .map(id -> {
                                ClientPermissionEntity clientPermissionEntity = new ClientPermissionEntity();
                                clientPermissionEntity.setClientId(request.getId());
                                clientPermissionEntity.setPermissionId(id);
                                return clientPermissionEntity;
                            }).toList();
                    List<BatchResult> insert = clientPermissionService.getBaseMapper().insert(clientRoleEntities);
                    log.debug("创建客户端权限关系: {}", insert.size());
                });
        Assert.state(updated == 1, "客户端更新失败");
    }

    @Override
    public ClientEntity get(ClientQueryRequest request) {
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
        return clientConvertor.entityToResponse(entity);
    }

    @Override
    public ClientResponse loadClientByAccessKey(String accessKey) {
        Optional.ofNullable(accessKey).orElseThrow(() -> new UsernameNotFoundException("AccessKey不能为空"));
        LambdaQueryWrapper<ClientEntity> wrapper = Wrappers.lambdaQuery(ClientEntity.class);
        wrapper.eq(ClientEntity::getAccessKey, accessKey);
        ClientEntity clientEntity = baseMapper.selectOne(wrapper);
        Optional.ofNullable(clientEntity).orElseThrow(() -> new UsernameNotFoundException("AccessKey不存在"));
        return convert(clientEntity);
    }

}
