package com.jinelei.numbfish.authorization.service.impl;

import java.util.*;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;
import com.jinelei.numbfish.common.exception.NotExistException;
import com.jinelei.numbfish.common.helper.Snowflake;

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
import com.jinelei.numbfish.authorization.convertor.PermissionConvertor;
import com.jinelei.numbfish.authorization.entity.PermissionEntity;
import com.jinelei.numbfish.authorization.mapper.PermissionMapper;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.common.entity.BaseEntity;
import com.jinelei.numbfish.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity>
        implements PermissionService {
    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);
    protected Snowflake snowflake = Snowflake.DEFAULT;

    @Autowired
    protected PermissionConvertor permissionConvertor;

    private final Function<PermissionQueryRequest, LambdaQueryWrapper<PermissionEntity>> buildQueryWrapper = (PermissionQueryRequest request) -> {
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.like(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getType()), PermissionEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        wrapper.like(Objects.nonNull(request.getRemark()), PermissionEntity::getRemark, request.getRemark());
        return wrapper;
    };

    @Override
    public void create(PermissionCreateRequest request) {
        final PermissionEntity entity = permissionConvertor.requestToEntity(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("权限信息不合法"));
        Optional.of(entity).map(PermissionEntity::getParentId).ifPresent(parentId -> {
            Optional.of(parentId).map(baseMapper::selectById).orElseThrow(() -> new NotExistException("父级权限不存在"));
            entity.setSortValue(Optional.ofNullable(request.getSortValue()).orElseGet(() -> baseMapper.selectMaxSortValue(parentId) + 1));
        });
        int inserted = baseMapper.insert(entity);
        Assert.state(inserted == 1, "权限创建失败");
    }

    @Override
    public void delete(PermissionDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            Assert.state(deleted == 1, "权限删除失败");
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            Assert.state(deleted == request.getIds().size(), "权限删除失败");
        } else if (Objects.nonNull(request.getParentId())) {
            List<PermissionEntity> entities = baseMapper.selectList(Wrappers.lambdaQuery(PermissionEntity.class)
                    .eq(PermissionEntity::getParentId, request.getParentId()));
            if (CollectionUtils.isEmpty(entities)) {
                throw new InvalidArgsException("该权限下没有子权限");
            }
            int deleted = baseMapper.deleteByIds(entities.stream().map(BaseEntity::getId).toList());
            Assert.state(deleted == entities.size(), "权限删除失败");
        }
    }

    @Override
    public void update(@Validated PermissionUpdateRequest request) {
        LambdaUpdateWrapper<PermissionEntity> wrapper = Wrappers.lambdaUpdate(PermissionEntity.class);
        wrapper.eq(PermissionEntity::getId, request.getId());
        wrapper.set(PermissionEntity::getName, request.getName());
        wrapper.set(PermissionEntity::getCode, request.getCode());
        wrapper.set(PermissionEntity::getType, request.getType());
        Optional.of(request).map(PermissionUpdateRequest::getParentId).ifPresent(parentId -> {
            Optional.of(parentId).map(baseMapper::selectById).orElseThrow(() -> new NotExistException("父级权限不存在"));
            wrapper.set(PermissionEntity::getParentId, request.getParentId());
        });
        wrapper.set(PermissionEntity::getSortValue, request.getSortValue());
        wrapper.set(PermissionEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        Assert.state(updated == 1, "权限更新失败");
    }

    @Override
    public PermissionEntity get(PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = buildQueryWrapper.apply(request);
        PermissionEntity entity = baseMapper.selectOne(wrapper);
        if (Objects.isNull(entity)) {
            throw new NotExistException("权限不存在");
        }
        if (Objects.nonNull(request.getMode())) {
            if (TreeBuildMode.CHILD_AND_CURRENT.equals(request.getMode())) {
                List<PermissionEntity> tree = baseMapper.selectTree(List.of(entity.getId()), request.getMode());
                tree = permissionConvertor.tree(tree);
                if (CollectionUtils.isEmpty(tree)) {
                    throw new NotExistException("权限不存在");
                } else if (tree.size() != 1) {
                    throw new InvalidArgsException("查询到多个权限");
                } else {
                    entity = tree.getFirst();
                }
            } else {
                throw new InvalidArgsException("不支持的查询模式");
            }
        }
        return entity;
    }

    @Override
    public List<PermissionEntity> list(PermissionQueryRequest request) {
        List<PermissionEntity> list = baseMapper.selectList(buildQueryWrapper.apply(request));
        if (Objects.nonNull(request.getMode())) {
            List<Long> ids = Optional.ofNullable(list)
                    .stream().flatMap(Collection::stream)
                    .map(BaseEntity::getId)
                    .toList();
            list = Optional.of(ids)
                    .filter(i -> !CollectionUtils.isEmpty(i))
                    .map(i -> baseMapper.selectTree(i, request.getMode()))
                    .filter(i -> !CollectionUtils.isEmpty(i))
                    .map(l -> permissionConvertor.tree(l))
                    .orElse(new ArrayList<>());
        }
        return list;
    }

    @Override
    public IPage<PermissionEntity> page(IPage<PermissionEntity> page, PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = buildQueryWrapper.apply(request);
        wrapper.isNull(Objects.isNull(request.getParentId()), PermissionEntity::getParentId);
        final IPage<PermissionEntity> result = baseMapper.selectPage(page, wrapper);
        if (Objects.nonNull(request.getMode())) {
            List<Long> ids = Optional.ofNullable(result.getRecords())
                    .stream().flatMap(Collection::stream)
                    .map(BaseEntity::getId)
                    .toList();
            Optional.of(ids)
                    .filter(i -> !CollectionUtils.isEmpty(i))
                    .map(i -> baseMapper.selectTree(i, request.getMode()))
                    .map(l -> permissionConvertor.tree(l))
                    .ifPresent(result::setRecords);
        }
        return result;
    }

}
