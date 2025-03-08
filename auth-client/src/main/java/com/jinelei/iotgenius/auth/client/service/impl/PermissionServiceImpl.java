package com.jinelei.iotgenius.auth.client.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jinelei.iotgenius.auth.client.domain.BaseEntity;
import com.jinelei.iotgenius.auth.dto.permission.*;
import com.jinelei.iotgenius.common.exception.NotExistException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.iotgenius.auth.client.convertor.PermissionConvertor;
import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;
import com.jinelei.iotgenius.auth.client.mapper.PermissionMapper;
import com.jinelei.iotgenius.auth.client.service.PermissionService;
import com.jinelei.iotgenius.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity>
        implements PermissionService {
    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    protected PermissionConvertor permissionConvertor;

    @Override
    public void create(@Validated PermissionCreateRequest request) {
        final PermissionEntity entity = permissionConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("权限信息不合法"));
        Optional.of(entity).map(PermissionEntity::getParentId)
                .ifPresentOrElse(parentId -> {
                    Optional.ofNullable(baseMapper.selectById(parentId)).orElseThrow(() -> new NotExistException("父级权限不存在"));
                    entity.setSortValue(Optional.ofNullable(request.getSortValue()).orElseGet(() -> baseMapper.selectMaxSortValue(parentId) + 1));
                }, () -> {
                    entity.setSortValue(Optional.ofNullable(request.getSortValue()).orElseGet(() -> baseMapper.selectMaxSortValue() + 1));
                });
        int inserted = baseMapper.insert(entity);
        Assert.state(inserted == 1, "权限创建失败");
    }

    @Override
    public void delete(@NotNull PermissionDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            Assert.state(deleted == 1, "权限删除失败");
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            Assert.state(deleted == request.getIds().size(), "权限删除失败");
        } else if (Objects.nonNull(request.getParentId())) {
            List<PermissionEntity> entities = baseMapper.selectList(Wrappers.lambdaQuery(PermissionEntity.class).eq(PermissionEntity::getParentId, request.getParentId()));
            if (CollectionUtils.isEmpty(entities)) {
                throw new InvalidArgsException("该权限下没有子权限");
            }
            int deleted = baseMapper.deleteByIds(entities.parallelStream().map(BaseEntity::getId).toList());
            Assert.state(deleted == entities.size(), "权限删除失败");
        }
    }

    @Override
    public void update(@Validated PermissionUpdateRequest request) {
        LambdaUpdateWrapper<PermissionEntity> wrapper = Wrappers.lambdaUpdate(PermissionEntity.class);
        wrapper.eq(PermissionEntity::getId, request.getId());
        wrapper.set(PermissionEntity::getName, request.getName());
        wrapper.set(PermissionEntity::getCode, request.getCode());
        wrapper.set(PermissionEntity::getParentId, request.getParentId());
        wrapper.set(PermissionEntity::getSortValue, request.getSortValue());
        wrapper.set(PermissionEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        Assert.state(updated == 1, "权限更新失败");
    }

    @Override
    public PermissionEntity get(@NotNull PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<PermissionEntity> tree(@NotNull PermissionQueryRequest request) {
        List<PermissionEntity> list = this.list(request);
        List<PermissionEntity> tree = baseMapper.getPermissionTreeByIds(list.parallelStream().map(BaseEntity::getId).toList());
        return tree;
    }

    @Override
    public List<PermissionEntity> list(@NotNull PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public PermissionResponse convert(@NotNull PermissionEntity entity) {
        PermissionResponse response = permissionConvertor.entityToResponse(entity);
        return response;
    }

    @Override
    public List<PermissionResponse> convertTree(List<PermissionEntity> entity) {
        List<PermissionEntity> tree = permissionConvertor.tree(entity);
        List<PermissionResponse> response = tree.parallelStream().map(permissionConvertor::entityToResponse).toList();
        return response;
    }

}
