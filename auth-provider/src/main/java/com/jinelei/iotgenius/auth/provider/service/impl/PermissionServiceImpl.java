package com.jinelei.iotgenius.auth.provider.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.provider.convertor.PermissionConvertor;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;
import com.jinelei.iotgenius.auth.provider.mapper.PermissionMapper;
import com.jinelei.iotgenius.auth.provider.service.PermissionService;
import com.jinelei.iotgenius.common.exception.InvalidArgsException;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity>
        implements PermissionService {
    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    protected PermissionConvertor permissionConvertor;

    @Override
    public Long create(@Validated({ PermissionRequest.Create.class }) PermissionRequest request) {
        final PermissionEntity entity = permissionConvertor.requestToEntity(request);
        checkParentIdExist(entity);
        resetSortValue(entity);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 检查父级id是否存在
     * 
     * @param parentId 父级id
     * @throws 权限父级id不合法
     */
    public void checkParentIdExist(PermissionEntity entity) {
        if (!ObjectUtils.isEmpty(entity) && ObjectUtils.isArray(entity.getParentId())) {
            Optional.ofNullable(baseMapper.selectById(entity.getParentId()))
                    .filter(i -> i.getEnabled())
                    .orElseThrow(() -> new InvalidArgsException("权限父级id不合法"));
        }
    }

    public void resetSortValue(PermissionEntity entity) {
        final AtomicInteger sortValue = new AtomicInteger(
                Optional.ofNullable(entity).map(PermissionEntity::getSortValue).orElse(0));
        LambdaQueryWrapper<PermissionEntity> query = Wrappers.lambdaQuery(PermissionEntity.class);
        if (!ObjectUtils.isEmpty(entity) && ObjectUtils.isArray(entity.getParentId())) {
            query.eq(PermissionEntity::getParentId, entity.getParentId());
        } else {
            query.isNull(PermissionEntity::getParentId);
        }
        Optional.ofNullable(baseMapper.selectList(query))
                .stream()
                .flatMap(List::stream)
                .filter(i -> Objects.nonNull(i.getSortValue()))
                .mapToInt(i -> i.getSortValue())
                .max()
                .ifPresent(sortValue::set);
        entity.setSortValue(sortValue.get());
    }

}
