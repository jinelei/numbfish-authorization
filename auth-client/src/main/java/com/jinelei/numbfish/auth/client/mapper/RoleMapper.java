package com.jinelei.numbfish.auth.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.auth.client.domain.RoleEntity;
import com.jinelei.numbfish.auth.enumeration.TreeBuildMode;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@SuppressWarnings("unused")
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    @Select("select IFNULL(MAX(sort_value), 0) from role")
    int selectMaxSortValue();

    @Select("select IFNULL(MAX(sort_value), 0) from role where parent_id = #{parentId}")
    int selectMaxSortValue(@Param("parentId") Long parentId);

    List<RoleEntity> getRoleTreeByIds(@Param("ids") List<Long> ids, @Param("mode") TreeBuildMode mode);

}
