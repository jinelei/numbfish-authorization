package com.jinelei.numbfish.authorization.service.impl;

import com.jinelei.numbfish.authorization.configuration.authentication.instance.PermissionInstance;
import com.jinelei.numbfish.authorization.configuration.authentication.instance.RoleInstance;
import com.jinelei.numbfish.authorization.dto.SetupRequest;
import com.jinelei.numbfish.authorization.dto.UserCreateRequest;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.authorization.service.RoleService;
import com.jinelei.numbfish.authorization.service.SetupService;
import com.jinelei.numbfish.authorization.service.UserService;
import com.jinelei.numbfish.common.exception.BaseException;
import com.jinelei.numbfish.common.exception.InternalException;
import com.jinelei.numbfish.common.exception.InvalidArgsException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class SetupServiceImpl implements SetupService {
    private static final Logger log = LoggerFactory.getLogger(SetupServiceImpl.class);
    public static final String SHOW_TABLES_LIKE = "SHOW TABLES LIKE '%s'";

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;
    private final SqlSessionFactory sqlSessionFactory;
    private final PasswordEncoder passwordEncoder;

    public SetupServiceImpl(RoleService roleService, PermissionService permissionService, UserService userService,
            SqlSessionFactory sqlSessionFactory, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
        this.sqlSessionFactory = sqlSessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 检查表是否存在
     *
     * @param tableName  表名
     * @param connection 数据库连接
     * @return true:存在 false:不存在
     */
    private static boolean checkTableExist(String tableName, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SHOW_TABLES_LIKE.formatted(tableName))) {
                return resultSet.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载sql文件
     *
     * @param directory 目录
     * @param sqlFile   sql文件路径
     * @return sql文件内容
     */
    private static String loadDdlSql(String directory, String sqlFile) {
        StringBuffer path = new StringBuffer();
        path.append("sql/");
        Optional.ofNullable(directory)
                .map(String::trim)
                .map(s -> s.startsWith("/") ? s.substring(1) : s)
                .map(s -> s.endsWith("/") ? s.substring(0, s.length() - 1) : s)
                .filter(StringUtils::hasLength)
                .ifPresent(d -> {
                    path.append(d);
                    path.append("/");
                });
        path.append(Optional.ofNullable(sqlFile)
                .map(String::trim)
                .filter(StringUtils::hasLength)
                .orElseThrow(() -> new InvalidArgsException("sql文件路径不能为空")));
        final ClassPathResource resource = new ClassPathResource(path.toString());
        final StringBuilder buffer = new StringBuilder();
        try (InputStream in = resource.getInputStream()) {
            // 读取文件内容
            byte[] bytes = in.readAllBytes();
            buffer.append(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new InvalidArgsException("无法加载sql/ddl.sql文件: " + e.getMessage());
        }
        return buffer.toString();
    }

    /**
     * 执行SQL
     *
     * @param createSql  执行SQL语句
     * @param connection 数据库连接
     */
    private static void executeSql(String createSql, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(createSql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void permissionRegist() {
        permissionService.regist(List.of(PermissionInstance.class.getEnumConstants()));
    }

    @Override
    public Boolean checkIsSetup() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            final Connection connection = session.getConnection();
            boolean result = checkTableExist("permission", connection) &&
                    checkTableExist("role", connection) &&
                    checkTableExist("user", connection) &&
                    checkTableExist("client", connection) &&
                    checkTableExist("role_permission", connection) &&
                    checkTableExist("user_role", connection) &&
                    checkTableExist("client_permission", connection);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new InternalException("检查数据库表是否存在失败", e);
        }
    }

    @Override
    public Boolean init(SetupRequest request) {
        try (SqlSession session = sqlSessionFactory.openSession();
                Connection connection = session.getConnection()) {
            connection.setAutoCommit(false);
            boolean result = checkTableExist("permission", connection) &&
                    checkTableExist("role", connection) &&
                    checkTableExist("user", connection) &&
                    checkTableExist("client", connection) &&
                    checkTableExist("role_permission", connection) &&
                    checkTableExist("user_role", connection) &&
                    checkTableExist("client_permission", connection);
            if (!result) {
                // 创建表
                executeSql(loadDdlSql("ddl", "1_permission.sql"), connection);
                executeSql(loadDdlSql("ddl", "2_role.sql"), connection);
                executeSql(loadDdlSql("ddl", "3_user.sql"), connection);
                executeSql(loadDdlSql("ddl", "4_client.sql"), connection);
                executeSql(loadDdlSql("ddl", "5_role_permission.sql"), connection);
                executeSql(loadDdlSql("ddl", "6_user_role.sql"), connection);
                executeSql(loadDdlSql("ddl", "7_client_permission.sql"), connection);
            }

            // 初始化数据

            // 创建超级管理员用户
            final UserCreateRequest createUserRequest = new UserCreateRequest();
            Optional.ofNullable(request).orElseThrow(() -> new InvalidArgsException("初始化参数不能为空"));
            final String username = Optional.of(request)
                    .map(SetupRequest::getUsername)
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .orElseThrow(() -> new InvalidArgsException("用户名称不能为空"));
            final String password = Optional.of(request)
                    .map(SetupRequest::getPassword)
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .orElseThrow(() -> new InvalidArgsException("用户密码不能为空"));
            final String remark = "超级管理员";
            final String email = Optional.of(request)
                    .map(SetupRequest::getEmail)
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .orElse("");
            final String phone = Optional.of(request)
                    .map(SetupRequest::getPhone)
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .orElse("");

            final String sql = loadDdlSql("dml", "3_user.sql")
                    .replace("{USERNAME}", username)
                    .replace("{PASSWORD}", passwordEncoder.encode(password))
                    .replace("{REMARK}", remark)
                    .replace("{EMAIL}", email)
                    .replace("{PHONE}", phone);
            executeSql(sql, connection);

            // 注册所有权限、角色
            executeSql(loadDdlSql("dml", "1_permission.sql"), connection);
            executeSql(loadDdlSql("dml", "2_role.sql"), connection);

            // 提交事务
            connection.commit();
            session.commit();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalException("初始化系统失败", e);
        }
        return true;
    }

}