package com.jinelei.iotgenius.auth.helper;

import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.permission.PermissionDeclaration;
import com.jinelei.iotgenius.common.exception.BaseException;
import com.jinelei.iotgenius.common.exception.InternalException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("unused")
public class AuthorizationHelper {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);

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
     * @param permission
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
     * @param permission
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
        return codes.containsAll(permissions);
    }

    /**
     * 检查当前登录用户是否具有指定权限
     * 
     * @param permission
     * @return 是否包含
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

}
