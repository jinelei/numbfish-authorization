package com.jinelei.numbfish.authorization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.authorization.convertor.RoleConvertor;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.entity.RoleEntity;
import com.jinelei.numbfish.authorization.entity.RolePermissionEntity;
import com.jinelei.numbfish.authorization.mapper.RoleMapper;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.authorization.service.RolePermissionService;
import com.jinelei.numbfish.authorization.service.RoleService;
import com.jinelei.numbfish.authorization.enumeration.RolePermissionType;
import com.jinelei.numbfish.common.entity.BaseEntity;
import com.jinelei.numbfish.common.exception.InvalidArgsException;
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

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity>
        implements RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    protected Snowflake snowflake = Snowflake.DEFAULT;

    @Autowired
    protected RoleConvertor roleConvertor;
    @Autowired
    protected RolePermissionService rolePermissionService;
    @Autowired
    protected PermissionService permissionService;

    private final Function<RoleQueryRequest, LambdaQueryWrapper<RoleEntity>> buildQueryWrapper = (RoleQueryRequest request) -> {
        LambdaQueryWrapper<RoleEntity> wrapper = Wrappers.lambdaQuery(RoleEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), RoleEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), RoleEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), RoleEntity::getCode, request.getCode());
        wrapper.eq(Objects.nonNull(request.getType()), RoleEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), RoleEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), RoleEntity::getSortValue, request.getSortValue());
        wrapper.like(Objects.nonNull(request.getRemark()), RoleEntity::getRemark, request.getRemark());
        return wrapper;
    };

    @Override
    public void create(RoleCreateRequest request) {
        final RoleEntity entity = roleConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("角色信息不合法"));
        Optional.of(entity).map(RoleEntity::getParentId).ifPresent(parentId -> {
            Optional.of(parentId).map(baseMapper::selectById).orElseThrow(() -> new NotExistException("父级角色不存在"));
            entity.setSortValue(Optional.ofNullable(request.getSortValue()).orElseGet(() -> baseMapper.selectMaxSortValue(parentId) + 1));
        });
        int inserted = baseMapper.insert(entity);
        log.debug("创建角色: {}", inserted);
        Optional.ofNullable(request.getWhitePermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<RolePermissionEntity> rolePermissionEntities = list.parallelStream()
                            .map(id -> {
                                RolePermissionEntity rolePermission = new RolePermissionEntity();
                                rolePermission.setRoleId(entity.getId());
                                rolePermission.setPermissionId(id);
                                rolePermission.setType(RolePermissionType.WHITE);
                                return rolePermission;
                            }).toList();
                    List<BatchResult> insert = rolePermissionService.getBaseMapper()
                            .insert(rolePermissionEntities);
                    log.debug("创建角色权限关系(白名单): {}", insert.size());
                });
        Optional.ofNullable(request.getBlackPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<RolePermissionEntity> rolePermissionEntities = list.parallelStream()
                            .map(id -> {
                                RolePermissionEntity rolePermission = new RolePermissionEntity();
                                rolePermission.setRoleId(entity.getId());
                                rolePermission.setPermissionId(id);
                                rolePermission.setType(RolePermissionType.BLACK);
                                return rolePermission;
                            }).toList();
                    List<BatchResult> insert = rolePermissionService.getBaseMapper()
                            .insert(rolePermissionEntities);
                    log.debug("创建角色权限关系(黑名单): {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(RoleDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.debug("删除角色: {}", deleted);
            Assert.state(deleted == 1, "角色删除失败");
            LambdaUpdateWrapper<RolePermissionEntity> wrapper = Wrappers
                    .lambdaUpdate(RolePermissionEntity.class)
                    .eq(RolePermissionEntity::getRoleId, request.getId());
            int deletedRolePermission = rolePermissionService.getBaseMapper().delete(wrapper);
            log.debug("删除角色权限关系: {}", deletedRolePermission);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.debug("删除角色: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "权限删除失败");
            int deletedRolePermission = rolePermissionService.getBaseMapper().delete(Wrappers
                    .lambdaUpdate(RolePermissionEntity.class)
                    .in(RolePermissionEntity::getRoleId, request.getIds()));
            log.debug("删除角色权限关系: {}", deletedRolePermission);
        } else if (Objects.nonNull(request.getParentId())) {
            List<RoleEntity> entities = baseMapper.selectList(
                    Wrappers.lambdaQuery(RoleEntity.class).eq(RoleEntity::getParentId,
                            request.getParentId()));
            if (CollectionUtils.isEmpty(entities)) {
                throw new InvalidArgsException("该权限下没有子权限");
            }
            final List<Long> idList = entities.parallelStream().map(BaseEntity::getId).toList();
            int deleted = baseMapper.deleteByIds(idList);
            log.debug("删除角色: {}", deleted);
            Assert.state(deleted == entities.size(), "权限删除失败");
            int deletedRolePermission = rolePermissionService.getBaseMapper().delete(
                    Wrappers.lambdaUpdate(RolePermissionEntity.class)
                            .in(RolePermissionEntity::getRoleId, idList));
            log.debug("删除角色权限关系: {}", deletedRolePermission);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated RoleUpdateRequest request) {
        LambdaUpdateWrapper<RoleEntity> wrapper = Wrappers.lambdaUpdate(RoleEntity.class);
        wrapper.eq(RoleEntity::getId, request.getId());
        wrapper.set(RoleEntity::getName, request.getName());
        wrapper.set(RoleEntity::getCode, request.getCode());
        wrapper.set(RoleEntity::getType, request.getType());
        wrapper.set(RoleEntity::getParentId, request.getParentId());
        wrapper.set(RoleEntity::getSortValue, request.getSortValue());
        wrapper.set(RoleEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        log.debug("更新角色: {}", updated);
        int deleted = rolePermissionService.getBaseMapper().delete(
                Wrappers.lambdaUpdate(RolePermissionEntity.class).eq(RolePermissionEntity::getRoleId,
                        request.getId()));
        log.debug("删除角色权限关系: {}", deleted);
        Optional.ofNullable(request.getWhitePermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<RolePermissionEntity> rolePermissionEntities = list.parallelStream()
                            .map(id -> {
                                RolePermissionEntity rolePermission = new RolePermissionEntity();
                                rolePermission.setRoleId(request.getId());
                                rolePermission.setPermissionId(id);
                                rolePermission.setType(RolePermissionType.WHITE);
                                return rolePermission;
                            }).toList();
                    List<BatchResult> insert = rolePermissionService.getBaseMapper()
                            .insert(rolePermissionEntities);
                    log.debug("创建角色权限关系(白名单): {}", insert.size());
                });
        Optional.ofNullable(request.getBlackPermissionIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<RolePermissionEntity> rolePermissionEntities = list.parallelStream()
                            .map(id -> {
                                RolePermissionEntity rolePermission = new RolePermissionEntity();
                                rolePermission.setRoleId(request.getId());
                                rolePermission.setPermissionId(id);
                                rolePermission.setType(RolePermissionType.BLACK);
                                return rolePermission;
                            }).toList();
                    List<BatchResult> insert = rolePermissionService.getBaseMapper()
                            .insert(rolePermissionEntities);
                    log.debug("创建角色权限关系(黑名单): {}", insert.size());
                });
        Assert.state(updated == 1, "权限更新失败");
    }

    @Override
    public RoleEntity get(RoleQueryRequest request) {
        LambdaQueryWrapper<RoleEntity> wrapper = buildQueryWrapper.apply(request);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<RoleEntity> tree(RoleQueryRequest request) {
        List<RoleEntity> list = this.list(request);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return baseMapper.getRoleTreeByIds(
                list.parallelStream().map(BaseEntity::getId).toList(),
                request.getMode());
    }

    @Override
    public List<RoleEntity> list(RoleQueryRequest request) {
        LambdaQueryWrapper<RoleEntity> wrapper = buildQueryWrapper.apply(request);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<RoleEntity> page(IPage<RoleEntity> page, RoleQueryRequest request) {
        LambdaQueryWrapper<RoleEntity> wrapper = buildQueryWrapper.apply(request);
        wrapper.isNull(Objects.isNull(request.getParentId()), RoleEntity::getParentId);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public RoleResponse convert(RoleEntity entity) {
        return roleConvertor.entityToResponse(entity);
    }

    @Override
    public List<RoleResponse> convertTree(List<RoleEntity> entity) {
        List<RoleEntity> tree = roleConvertor.tree(entity);
        return tree.parallelStream().map(roleConvertor::entityToResponse).toList();
    }

}
