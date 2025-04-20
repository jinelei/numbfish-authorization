package com.jinelei.numbfish.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.convertor.RoleConvertor;
import com.jinelei.numbfish.auth.entity.PermissionEntity;
import com.jinelei.numbfish.auth.entity.RoleEntity;
import com.jinelei.numbfish.auth.entity.RolePermissionEntity;
import com.jinelei.numbfish.auth.mapper.RoleMapper;
import com.jinelei.numbfish.auth.service.PermissionService;
import com.jinelei.numbfish.auth.service.RolePermissionService;
import com.jinelei.numbfish.auth.service.RoleService;
import com.jinelei.numbfish.auth.dto.role.*;
import com.jinelei.numbfish.auth.enumeration.RolePermissionType;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.auth.permission.declaration.RoleDeclaration;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

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

    @Override
    public void create(RoleCreateRequest request) {
        final RoleEntity entity = roleConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("权限信息不合法"));
        Optional.of(entity).map(RoleEntity::getParentId)
                .ifPresentOrElse(parentId -> {
                    Optional.ofNullable(baseMapper.selectById(parentId))
                            .orElseThrow(() -> new NotExistException("父级权限不存在"));
                    entity.setSortValue(Optional.ofNullable(request.getSortValue())
                            .orElseGet(() -> baseMapper.selectMaxSortValue(parentId) + 1));
                }, () -> entity.setSortValue(Optional.ofNullable(request.getSortValue())
                        .orElseGet(() -> baseMapper.selectMaxSortValue() + 1)));
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
        Assert.state(inserted == 1, "权限创建失败");
    }

    @Override
    public void delete(RoleDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.debug("删除角色: {}", deleted);
            Assert.state(deleted == 1, "权限删除失败");
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
        LambdaQueryWrapper<RoleEntity> wrapper = Wrappers.lambdaQuery(RoleEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), RoleEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), RoleEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), RoleEntity::getCode, request.getCode());
        wrapper.eq(RoleEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), RoleEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), RoleEntity::getSortValue, request.getSortValue());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<RoleEntity> tree(RoleQueryRequest request) {
        List<RoleEntity> list = this.list(request);
        return baseMapper.getRoleTreeByIds(
                list.parallelStream().map(BaseEntity::getId).toList(),
                request.getMode());
    }

    @Override
    public List<RoleEntity> list(RoleQueryRequest request) {
        LambdaQueryWrapper<RoleEntity> wrapper = Wrappers.lambdaQuery(RoleEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), RoleEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), RoleEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), RoleEntity::getCode, request.getCode());
        wrapper.eq(RoleEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), RoleEntity::getParentId, request.getParentId());
        wrapper.eq(Objects.nonNull(request.getSortValue()), RoleEntity::getSortValue, request.getSortValue());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<RoleEntity> page(IPage<RoleEntity> page, RoleQueryRequest request) {
        LambdaQueryWrapper<RoleEntity> wrapper = Wrappers.lambdaQuery(RoleEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), RoleEntity::getId, request.getId());
        wrapper.eq(Objects.nonNull(request.getName()), RoleEntity::getName, request.getName());
        wrapper.eq(Objects.nonNull(request.getCode()), RoleEntity::getCode, request.getCode());
        wrapper.eq(RoleEntity::getType, request.getType());
        wrapper.eq(Objects.nonNull(request.getParentId()), RoleEntity::getParentId, request.getParentId());
        wrapper.isNull(Objects.isNull(request.getParentId()), RoleEntity::getParentId);
        wrapper.eq(Objects.nonNull(request.getSortValue()), RoleEntity::getSortValue, request.getSortValue());
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

    @SuppressWarnings("all")
    @Override
    public <T extends RoleDeclaration<?>> Boolean regist(List<T> roles) {
        final Map<T, RoleEntity> entitiesMap = new ConcurrentHashMap<>();
        final List<RoleEntity> roleEntities = new CopyOnWriteArrayList<>();
        final List<T> rootNodes = new CopyOnWriteArrayList<>();
        roles.parallelStream()
                .filter(i -> Objects.isNull(i.getParent()))
                .forEach(rootNodes::add);
        final Map<Object, List<T>> byParentMap = roles.parallelStream()
                .filter(i -> Objects.nonNull(i.getParent()))
                .collect(Collectors.groupingBy(i -> i.getParent()));
        if (rootNodes.isEmpty()) {
            log.error("权限树错误: 缺失根节点");
        }

        // 查询所有的权限列表
        List<PermissionDeclaration<?>> permissions = new CopyOnWriteArrayList<>();
        roles.parallelStream()
                .map(i -> i.getPermissions())
                .filter(Objects::nonNull)
                .forEach(permissions::addAll);
        final List<String> permissionCodes = permissions.parallelStream().map(PermissionDeclaration::getCode).distinct()
                .toList();
        final List<PermissionEntity> existPermissionList = permissionService
                .list(Wrappers.lambdaQuery(PermissionEntity.class).in(PermissionEntity::getCode,
                        permissionCodes));
        final Map<T, List<PermissionEntity>> rolePermissionMap = new ConcurrentHashMap<>();
        roles.parallelStream()
                .filter(i -> Objects.nonNull(i.getPermissions()))
                .forEach(i -> {
                    List<PermissionEntity> list = i.getPermissions()
                            .parallelStream()
                            .map(j -> existPermissionList.parallelStream()
                                    .filter(it -> j.getCode().equals(it.getCode()))
                                    .toList())
                            .flatMap(Collection::stream)
                            .toList();
                    rolePermissionMap.put(i, list);
                });

        final List<RoleEntity> existEntities = baseMapper.selectList(Wrappers.lambdaQuery(RoleEntity.class)
                .in(RoleEntity::getCode, roles.parallelStream().map(i -> i.getCode()).toList()));

        final List<RolePermissionEntity> rolePermissionEntities = new CopyOnWriteArrayList<>();

        final CountDownLatch countDownLatch = new CountDownLatch(roles.size());

        final List<T> workNodes = new CopyOnWriteArrayList<>(rootNodes);
        while (countDownLatch.getCount() != 0) {
            final List<T> tempNodes = new CopyOnWriteArrayList<>();
            workNodes.forEach(node -> {
                List<RoleEntity> list = existEntities.parallelStream()
                        .filter(i -> i.getCode().equals(node.getCode())).toList();
                if (list.size() > 1) {
                    log.error("查询权限编码不唯一: {}", node.getCode());
                }
                final RoleEntity entity = fromDeclaration(
                        list.stream().findFirst().orElse(new RoleEntity()),
                        node);
                Optional.of(node)
                        .map(i -> i.getParent())
                        .map(entitiesMap::get)
                        .map(BaseEntity::getId)
                        .ifPresent(entity::setParentId);
                Optional.of(node)
                        .map(rolePermissionMap::get)
                        .ifPresent(l -> l.parallelStream().forEach(p -> {
                            RolePermissionEntity r = new RolePermissionEntity();
                            r.setId(snowflake.next());
                            r.setRoleId(entity.getId());
                            r.setPermissionId(p.getId());
                            r.setType(RolePermissionType.WHITE);
                            rolePermissionEntities.add(r);
                        }));
                entitiesMap.put(node, entity);
                roleEntities.add(entity);
                countDownLatch.countDown();
                Optional.ofNullable(byParentMap.get(node)).ifPresent(tempNodes::addAll);
            });
            workNodes.clear();
            workNodes.addAll(tempNodes);
        }
        List<Long> roleIds = roleEntities.parallelStream().map(BaseEntity::getId).toList();
        List<RolePermissionEntity> existRolePermissions = rolePermissionService
                .list(Wrappers.lambdaQuery(RolePermissionEntity.class)
                        .in(!roleIds.isEmpty(), RolePermissionEntity::getRoleId, roleIds)
                        .eq(roleIds.isEmpty(), RolePermissionEntity::getId, 0L));
        final List<RolePermissionEntity> updatEntities = rolePermissionEntities
                .parallelStream()
                .map(e -> existRolePermissions.parallelStream()
                        .filter(i -> i.isSimilar(e))
                        .findAny()
                        .map(ie -> ie.cover(e))
                        .orElse(e))
                .toList();
        List<Long> deleteRolePermissionIds = existRolePermissions.parallelStream()
                .filter(i -> updatEntities.parallelStream().noneMatch(j -> j.isSimilar(i)))
                .map(BaseEntity::getId)
                .toList();
        List<BatchResult> results = baseMapper.insertOrUpdate(roleEntities);
        rolePermissionService.getBaseMapper().insertOrUpdate(updatEntities);
        rolePermissionService.getBaseMapper().deleteByIds(deleteRolePermissionIds);
        return true;
    }

    public <T extends RoleDeclaration<?>> RoleEntity fromDeclaration(RoleEntity role, T node) {
        role.setId(Optional.of(role).map(BaseEntity::getId).orElse(snowflake.next()));
        role.setCode(node.getCode());
        role.setRemark(node.getRemark());
        role.setName(node.getName());
        role.setSortValue(node.getSortValue());
        role.setType(node.getType());
        role.setParentId(Optional.of(role).map(RoleEntity::getParentId).orElse(null));
        return role;
    }

}
