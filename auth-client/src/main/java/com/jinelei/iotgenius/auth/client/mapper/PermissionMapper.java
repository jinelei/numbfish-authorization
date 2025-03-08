package com.jinelei.iotgenius.auth.client.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    @Select("select IFNULL(MAX(sort_value), 0) from permission")
    int selectMaxSortValue();

    @Select("select IFNULL(MAX(sort_value), 0) from permission where parent_id = #{parentId}")
    int selectMaxSortValue(@Param("parentId") Long parentId);

    List<PermissionEntity> getPermissionTreeByIds(@Param("ids") List<Long> ids);

}
