package com.jinelei.numbfish.authorization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.authorization.entity.RoleEntity;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@SuppressWarnings("unused")
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    int selectMaxSortValue(@Param("parentId") Long parentId);

    List<RoleEntity> getRoleTreeByIds(@Param("ids") List<Long> ids, @Param("mode") TreeBuildMode mode);

}
