package com.jinelei.iotgenius.auth.helper;

import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;
import com.jinelei.iotgenius.auth.property.AuthApiProperty;
import com.jinelei.iotgenius.common.exception.BaseException;
import com.jinelei.iotgenius.common.exception.InternalException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("all")
public class AuthorizationHelper {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);
    private AuthApiProperty authApiProperty;

    public void setAuthApiProperty(AuthApiProperty authApiProperty) {
        this.authApiProperty = authApiProperty;
    }

    /**
     * 请求头中获取用户信息
     *
     * @return 用户信息
     */
    public static UserResponse currentUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            UserResponse user = (UserResponse) request.getAttribute("user");
            log.info("获取当前登陆用户信息成功: {}", user);
            return user;
        }
        log.error("获取当前登陆用户信息失败: 不支持的RequestAttributes: {}", requestAttributes);
        throw new InternalException("获取当前登陆用户信息失败: 不支持的RequestAttributes", new RuntimeException());
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param permission 权限
     * @return 是否包含
     */
    public static Boolean hasPermission(String permission) {
        UserResponse response = currentUser();
        return Optional.ofNullable(response)
                .map(UserResponse::getPermissions)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> r.getCode().equals(permission));
    }

    /**
     * 检查当前登录用户是否具有指定权限列表
     *
     * @param permissions 权限列表
     * @return 是否包含
     */
    public static Boolean hasPermission(List<String> permissions) {
        UserResponse response = currentUser();
        List<String> codes = Optional.ofNullable(response)
                .map(UserResponse::getPermissions)
                .orElse(new ArrayList<>())
                .parallelStream()
                .map(PermissionResponse::getCode)
                .toList();
        return new HashSet<>(codes).containsAll(permissions);
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param user       用户信息
     * @param permission 权限
     * @return 是否包含
     */
    public static Boolean hasPermission(UserResponse user, PermissionDeclaration permission) {
        return Optional.ofNullable(user)
                .map(UserResponse::getPermissions)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> r.getCode().equals(permission.getCode()));
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param user 用户信息
     * @param role 角色
     * @return 是否包含
     */
    public static Boolean hasRole(UserResponse user, RoleDeclaration role) {
        return Optional.ofNullable(user)
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> r.getCode().equals(role.getCode()));
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param user 用户信息
     * @param role 角色
     * @return 是否包含
     */
    public static Boolean hasRoles(UserResponse user, RoleDeclaration... roles) {
        return Optional.ofNullable(user)
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> Arrays.stream(roles).anyMatch(e -> e.getCode().equals(r.getCode())));
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param permission 权限
     */
    public static void checkPermission(PermissionDeclaration permission) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response)
                .map(UserResponse::getPermissions)
                .orElse(new ArrayList<>())
                .stream()
                .noneMatch(r -> r.getCode().equals(permission.getCode()))) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少权限: " + permission.getDescription()));
        }
    }

    /**
     * 检查当前登录用户是否具有指定角色
     *
     * @param role 角色
     */
    public static void checkRole(PermissionDeclaration role) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response)
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .noneMatch(r -> r.getCode().equals(role.getCode()))) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少角色: " + role.getDescription()));
        }
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param predicate 断言
     */
    public static void check(Predicate<UserResponse> predicate) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response).filter(predicate).isEmpty()) {
            throw new BaseException(500, "无访问权限", new Throwable("无访问权限"));
        }
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param predicate       断言
     * @param messageSupplier 消息提供器
     */
    public static void check(Predicate<UserResponse> predicate, Supplier<String> messageSupplier) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response).filter(predicate).isEmpty()) {
            throw new BaseException(500, "无访问权限", new Throwable(messageSupplier.get()));
        }
    }

    /**
     * 是否是超级管理员
     * 
     * @param user 用户
     */
    public static Boolean isAdmin(UserResponse user, AuthApiProperty property) {
        final String username = Optional.ofNullable(property)
                .map(i -> i.getAdmin())
                .map(i -> i.getUsername())
                .orElseThrow(() -> new BaseException(500, "未找到权限属性配置"));
        return username.equals(Optional.ofNullable(user).map(i -> i.getUsername()).orElse(""));
    }

    /**
     * 是否是超级管理员
     * 
     * @param user 用户
     */
    public static Boolean isAdmin(AuthApiProperty property) {
        return isAdmin(currentUser(), property);
    }

    /**
     * 是否是超级管理员
     * 
     * @param user 用户
     */
    public Boolean isAdmin() {
        return AuthorizationHelper.isAdmin(currentUser(), authApiProperty);
    }

    /**
     * 是否是超级管理员
     * 
     * @param user 用户
     */
    public Boolean isAdmin(UserResponse user) {
        return AuthorizationHelper.isAdmin(user, authApiProperty);
    }

}
