package com.jinelei.iotgenius.auth.client.configuration.handler.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.jinelei.iotgenius.auth.enumeration.TreeBuildMode;

@SuppressWarnings("unused")
@MappedTypes(TreeBuildMode.class)
@MappedJdbcTypes(JdbcType.VARBINARY)
public class TreeBuildModeTypeHandler extends BaseTypeHandler<TreeBuildMode> {
    @Override
    public void setNonNullParameter(java.sql.PreparedStatement ps, int i, TreeBuildMode parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public TreeBuildMode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : TreeBuildMode.valueOf(value);
    }

    @Override
    public TreeBuildMode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : TreeBuildMode.valueOf(value);
    }

    @Override
    public TreeBuildMode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : TreeBuildMode.valueOf(value);
    }
}
