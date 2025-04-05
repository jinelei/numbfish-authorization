package com.jinelei.iotgenius.auth.helper;

import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;
import com.jinelei.iotgenius.auth.property.AdminProperty;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
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

@SuppressWarnings("unused")
public class AuthorizationHelper {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);
    private AuthorizationProperty property;

    public void setAuthApiProperty(AuthorizationProperty property) {
        this.property = property;
    }

    /**
     * 请求头中获取用户信息
     *
     * @return 用户信息
     */
    public static UserResponse currentUser() throws BaseException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            log.debug("获取当前登陆用户信息失败: RequestAttributes is null");
            throw new InternalException("获取当前登陆用户信息失败: RequestAttributes is null", new RuntimeException());
        } else if (requestAttributes instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            UserResponse user = (UserResponse) request.getAttribute(AuthorizationProperty.USER);
            log.debug("获取当前登陆用户信息成功: {}", user);
            return user;
        } else {
            log.debug("获取当前登陆用户信息失败: 不支持的RequestAttributes: {}", requestAttributes);
            throw new InternalException("获取当前登陆用户信息失败: 不支持的RequestAttributes", new RuntimeException());
        }
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
    public static Boolean hasPermission(UserResponse user, PermissionDeclaration<?> permission) {
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
    public static Boolean hasRole(UserResponse user, RoleDeclaration<?> role) {
        return Optional.ofNullable(user)
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> r.getCode().equals(role.getCode()));
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param user  用户信息
     * @param roles 角色
     * @return 是否包含
     */
    public static Boolean hasRoles(UserResponse user, RoleDeclaration<?>... roles) {
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
    public static void checkPermission(PermissionDeclaration<?> permission) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response)
                .map(UserResponse::getPermissions)
                .orElse(new ArrayList<>())
                .stream()
                .noneMatch(r -> r.getCode().equals(permission.getCode()))) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少权限: " + permission.getRemark()));
        }
    }

    /**
     * 检查当前登录用户是否具有指定角色
     *
     * @param role 角色
     */
    public static void checkRole(PermissionDeclaration<?> role) {
        UserResponse response = currentUser();
        if (Optional.ofNullable(response)
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .noneMatch(r -> r.getCode().equals(role.getCode()))) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少角色: " + role.getRemark()));
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
     * @return 是否是超级管理员
     */
    public static Boolean isAdmin(UserResponse user, AuthorizationProperty property) {
        final String username = Optional.ofNullable(property)
                .map(AuthorizationProperty::getAdmin)
                .map(AdminProperty::getUsername)
                .orElseThrow(() -> new BaseException(500, "未找到权限属性配置"));
        return username.equals(Optional.ofNullable(user).map(UserResponse::getUsername).orElse(""));
    }

    /**
     * 是否是超级管理员
     *
     * @param property 权限属性
     * @return 是否是超级管理员
     */
    public static Boolean isAdmin(AuthorizationProperty property) {
        return isAdmin(currentUser(), property);
    }

    /**
     * 是否是超级管理员
     *
     * @return 是否是超级管理员
     */
    public Boolean isAdmin() {
        return AuthorizationHelper.isAdmin(currentUser(), property);
    }

    /**
     * 是否是超级管理员
     *
     * @param user 用户
     * @return 是否是超级管理员
     */
    public Boolean isAdmin(UserResponse user) {
        return AuthorizationHelper.isAdmin(user, property);
    }

}
