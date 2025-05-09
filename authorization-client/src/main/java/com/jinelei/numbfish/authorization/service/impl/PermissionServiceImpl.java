package com.jinelei.numbfish.authorization.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.common.exception.NotExistException;
import com.jinelei.numbfish.common.helper.Snowflake;

import org.apache.ibatis.executor.BatchResult;
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

    @Override
    public void create(PermissionCreateRequest request) {
        final PermissionEntity entity = permissionConvertor.entityFromCreateRequest(request);
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
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getType()), PermissionEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<PermissionEntity> tree(PermissionQueryRequest request) {
        List<PermissionEntity> list = this.list(request);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return baseMapper
                .getPermissionTreeByIds(list.stream().map(BaseEntity::getId).toList(), request.getMode());
    }

    @Override
    public List<PermissionEntity> list(PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getType()), PermissionEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<PermissionEntity> page(IPage<PermissionEntity> page, PermissionQueryRequest request) {
        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), PermissionEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), PermissionEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), PermissionEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getType()), PermissionEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), PermissionEntity::getParentId, request.getParentId());
        wrapper.isNull(Objects.isNull(request.getParentId()), PermissionEntity::getParentId);
        wrapper.eq(Objects.nonNull(request.getSortValue()), PermissionEntity::getSortValue, request.getSortValue());
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public PermissionResponse convert(PermissionEntity entity) {
        return permissionConvertor.entityToResponse(entity);
    }

    @Override
    public List<PermissionResponse> convertTree(List<PermissionEntity> entity) {
        List<PermissionEntity> tree = permissionConvertor.tree(entity);
        return tree.parallelStream().map(permissionConvertor::entityToResponse).toList();
    }

    @SuppressWarnings("all")
    @Override
    public <T extends PermissionDeclaration<?>> Boolean regist(List<T> permissions) {
        final List<PermissionEntity> updateEntities = new CopyOnWriteArrayList<>();
        final Map<T, PermissionEntity> entitiesMap = new ConcurrentHashMap<>();
        final List<T> rootNodes = new CopyOnWriteArrayList<>();
        permissions.stream()
                .filter(i -> Objects.isNull(i.getParent()))
                .forEach(rootNodes::add);
        final Map<Object, List<T>> byParentMap = permissions.stream()
                .filter(i -> Objects.nonNull(i.getParent()))
                .collect(Collectors.groupingBy(i -> i.getParent()));
        if (rootNodes.isEmpty()) {
            log.error("权限树错误: 缺失根节点");
        }

        LambdaQueryWrapper<PermissionEntity> wrapper = Wrappers.lambdaQuery(PermissionEntity.class);
        wrapper.in(PermissionEntity::getCode, permissions.stream().map(i -> i.getCode()).toList());
        final List<PermissionEntity> existEntities = baseMapper.selectList(wrapper);

        final CountDownLatch countDownLatch = new CountDownLatch(permissions.size());

        final List<T> workNodes = new CopyOnWriteArrayList<>(rootNodes);
        while (countDownLatch.getCount() != 0) {
            final List<T> tempNodes = new CopyOnWriteArrayList<>();
            workNodes.forEach(node -> {
                List<PermissionEntity> list = existEntities.stream()
                        .filter(i -> i.getCode().equals(node.getCode())).toList();
                if (list.size() > 1) {
                    log.error("查询权限编码不唯一: {}", node.getCode());
                }
                final PermissionEntity entity = fromDeclaration(
                        list.stream().findFirst().orElse(new PermissionEntity()),
                        node);
                Optional.of(node)
                        .map(i -> i.getParent())
                        .map(entitiesMap::get)
                        .map(BaseEntity::getId)
                        .ifPresent(entity::setParentId);
                entitiesMap.putIfAbsent(node, entity);
                updateEntities.add(entity);
                countDownLatch.countDown();
                Optional.ofNullable(byParentMap.get(node)).ifPresent(tempNodes::addAll);
            });
            workNodes.clear();
            workNodes.addAll(tempNodes);
        }
        List<BatchResult> results = baseMapper.insertOrUpdate(updateEntities);
        return true;
    }

    public <T extends PermissionDeclaration<?>> PermissionEntity fromDeclaration(PermissionEntity permission, T node) {
        permission.setId(Optional.of(permission).map(BaseEntity::getId).orElse(snowflake.next()));
        permission.setCode(node.getCode());
        permission.setRemark(node.getRemark());
        permission.setName(node.getRemark());
        permission.setSortValue(node.getSortValue());
        permission.setType(node.getType());
        permission.setParentId(Optional.of(permission).map(PermissionEntity::getParentId).orElse(null));
        return permission;
    }

}
