package com.jinelei.iotgenius.auth.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.iotgenius.auth.client.convertor.UserConvertor;
import com.jinelei.iotgenius.auth.client.domain.UserEntity;
import com.jinelei.iotgenius.auth.client.domain.UserRoleEntity;
import com.jinelei.iotgenius.auth.client.mapper.UserMapper;
import com.jinelei.iotgenius.auth.client.service.UserRoleService;
import com.jinelei.iotgenius.auth.client.service.UserService;
import com.jinelei.iotgenius.auth.dto.user.*;
import com.jinelei.iotgenius.common.exception.InvalidArgsException;
import org.apache.ibatis.executor.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    public static final String PASSWORD = "123456";

    @Autowired
    protected UserConvertor userConvertor;
    @Autowired
    protected UserRoleService userRoleService;

    @Override
    public void create(UserCreateRequest request) {
        final UserEntity entity = userConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("角色信息不合法"));
        Optional.ofNullable(entity).map(UserEntity::getPassword).ifPresentOrElse(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
        }, () -> {
            entity.setPassword(PASSWORD);
        });
        int inserted = baseMapper.insert(entity);
        log.info("创建用户: {}", inserted);
        Optional.ofNullable(request.getRoleIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<UserRoleEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                UserRoleEntity userRole = new UserRoleEntity();
                                userRole.setUserId(entity.getId());
                                userRole.setRoleId(id);
                                return userRole;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.info("创建用户角色关系: {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(UserDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.info("删除用户: {}", deleted);
            Assert.state(deleted == 1, "角色删除失败");
            LambdaUpdateWrapper<UserRoleEntity> wrapper = Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId());
            int deletedUserRole = userRoleService.getBaseMapper().delete(wrapper);
            log.info("删除用户角色关系: {}", deletedUserRole);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.info("删除用户: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "角色删除失败");
            int deletedUserRole = userRoleService.getBaseMapper().delete(Wrappers.lambdaUpdate(UserRoleEntity.class).in(UserRoleEntity::getUserId, request.getIds()));
            log.info("删除用户角色关系: {}", deletedUserRole);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated UserUpdateRequest request) {
        LambdaUpdateWrapper<UserEntity> wrapper = Wrappers.lambdaUpdate(UserEntity.class);
        wrapper.eq(UserEntity::getId, request.getId());
        wrapper.set(UserEntity::getUsername, request.getUsername());
        wrapper.set(UserEntity::getPassword, request.getPassword());
        wrapper.set(UserEntity::getAvatar, request.getAvatar());
        wrapper.set(UserEntity::getEmail, request.getEmail());
        wrapper.set(UserEntity::getPhone, request.getPhone());
        wrapper.set(UserEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        log.info("更新用户: {}", updated);
        int deleted = userRoleService.getBaseMapper().delete(Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId()));
        log.info("删除用户角色关系: {}", deleted);
        Optional.ofNullable(request.getRoleIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<UserRoleEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                UserRoleEntity userRole = new UserRoleEntity();
                                userRole.setUserId(request.getId());
                                userRole.setRoleId(id);
                                return userRole;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.info("创建用户角色关系: {}", insert.size());
                });
        Assert.state(updated == 1, "角色更新失败");
    }

    @Override
    public UserEntity get(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<UserEntity> list(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public UserResponse convert(UserEntity entity) {
        UserResponse response = userConvertor.entityToResponse(entity);
        return response;
    }

}
