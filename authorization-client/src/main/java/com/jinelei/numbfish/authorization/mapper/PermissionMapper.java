package com.jinelei.numbfish.authorization.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.authorization.entity.PermissionEntity;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@SuppressWarnings("unused")
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    int selectMaxSortValue(@Param("parentId") Long parentId);

    List<PermissionEntity> getPermissionTreeByIds(@Param("ids") List<Long> ids, @Param("mode") TreeBuildMode mode);

}
