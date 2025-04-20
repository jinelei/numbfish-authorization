package com.jinelei.numbfish.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.auth.entity.PermissionEntity;
import com.jinelei.numbfish.auth.enumeration.TreeBuildMode;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@SuppressWarnings("unused")
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    @Select("select IFNULL(MAX(sort_value), 0) from permission")
    int selectMaxSortValue();

    @Select("select IFNULL(MAX(sort_value), 0) from permission where parent_id = #{parentId}")
    int selectMaxSortValue(@Param("parentId") Long parentId);

    List<PermissionEntity> getPermissionTreeByIds(@Param("ids") List<Long> ids, @Param("mode") TreeBuildMode mode);

}
