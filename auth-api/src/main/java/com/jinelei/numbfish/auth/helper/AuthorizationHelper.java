package com.jinelei.numbfish.auth.helper;

import com.jinelei.numbfish.auth.dto.client.ClientResponse;
import com.jinelei.numbfish.auth.dto.permission.PermissionResponse;
import com.jinelei.numbfish.auth.dto.user.UserResponse;
import com.jinelei.numbfish.auth.enumeration.AuthorizeType;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.auth.permission.declaration.RoleDeclaration;
import com.jinelei.numbfish.auth.property.AdminProperty;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import com.jinelei.numbfish.common.exception.BaseException;
import com.jinelei.numbfish.common.exception.InternalException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("unused")
@Component
public class AuthorizationHelper {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);
    private AuthorizationProperty property;

    @Autowired
    public void setAuthApiProperty(AuthorizationProperty property) {
        this.property = property;
    }

    /**
     * 获取RequestAttributes
     *
     * @return RequestAttributes
     */
    private static Optional<ServletRequestAttributes> getRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            log.debug("获取RequestAttributes失败: RequestAttributes is null");
            return Optional.empty();
        } else if (requestAttributes instanceof ServletRequestAttributes attributes) {
            return Optional.of(attributes);
        } else {
            log.debug("获取RequestAttributes失败: 不支持的RequestAttributes: {}", requestAttributes);
            return Optional.empty();
        }
    }

    /**
     * 获取RequestAttributes
     *
     * @return RequestAttributes
     * @throws BaseException 异常
     */
    private static ServletRequestAttributes getRequestAttributesWithException() throws BaseException {
        return getRequestAttributes().orElseThrow(() -> new InternalException("获取RequestAttributes失败: RequestAttributes is null", new RuntimeException()));
    }

    /**
     * 获取当前登录用户的授权类型
     *
     * @return 授权类型
     * @throws BaseException 异常
     */
    public static AuthorizeType currentAuthorizeType() throws BaseException {
        return Optional.ofNullable(getRequestAttributesWithException())
                .map(ServletRequestAttributes::getRequest)
                .map(r -> r.getAttribute(AuthorizationProperty.AUTHORIZE_TYPE))
                .filter(AuthorizeType.class::isInstance)
                .map(AuthorizeType.class::cast)
                .orElseThrow(() -> new InternalException("获取当前登录用户的授权类型失败: AuthorizeType is null", new RuntimeException()));
    }

    /**
     * 请求头中获取客户端信息
     *
     * @return 客户端信息
     * @throws BaseException 异常
     */
    public static ClientResponse currentClient() throws BaseException {
        if (AuthorizeType.CLIENT != currentAuthorizeType()) {
            throw new InternalException("当前登录用户不是客户端模式: AuthorizeType is not CLIENT", new RuntimeException());
        }
        return Optional.ofNullable(getRequestAttributesWithException())
                .map(ServletRequestAttributes::getRequest)
                .map(r -> r.getAttribute(AuthorizationProperty.CLIENT))
                .filter(ClientResponse.class::isInstance)
                .map(ClientResponse.class::cast)
                .orElseThrow(() -> new InternalException("获取当前登录用户的客户端信息失败: Client is null", new RuntimeException()));
    }

    /**
     * 请求头中获取用户信息
     *
     * @return 用户信息
     * @throws BaseException 异常
     */
    public static UserResponse currentUser() throws BaseException {
        if (AuthorizeType.CLIENT != currentAuthorizeType()) {
            throw new InternalException("当前登录用户不是客户端模式: AuthorizeType is not CLIENT", new RuntimeException());
        }
        return Optional.ofNullable(getRequestAttributesWithException())
                .map(ServletRequestAttributes::getRequest)
                .map(r -> r.getAttribute(AuthorizationProperty.USER))
                .filter(UserResponse.class::isInstance)
                .map(UserResponse.class::cast)
                .orElseThrow(() -> new InternalException("获取当前登录用户的用户信息失败: User is null", new RuntimeException()));
    }

    /**
     * 检查当前登录用户是否具有指定权限列表
     *
     * @param permissions 权限列表
     * @return 是否包含
     */
    public static Boolean hasPermissions(String... permissions) {
        List<String> codes = new CopyOnWriteArrayList<>();
        switch (currentAuthorizeType()) {
            case CLIENT:
                Optional.ofNullable(currentClient())
                        .map(ClientResponse::getPermissions)
                        .stream()
                        .flatMap(List::stream)
                        .map(PermissionResponse::getCode)
                        .distinct().forEach(codes::add);
                break;
            case USER:
                Optional.ofNullable(currentUser())
                        .map(UserResponse::getPermissions)
                        .stream()
                        .flatMap(List::stream)
                        .map(PermissionResponse::getCode)
                        .distinct()
                        .forEach(codes::add);
                break;
            case UNKNOWN:
            default:
                return false;
        }
        return Arrays.stream(permissions).allMatch(codes::contains);
    }

    /**
     * 检查当前登录用户是否具有指定权限列表
     *
     * @param permissions 权限列表
     * @throws BaseException 异常
     */
    public static void checkPermissions(String... permissions) throws BaseException {
        if (!hasPermissions(permissions)) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少权限: " + String.join(",", permissions)));
        }
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param permissions 权限
     * @return 是否包含
     */
    public static Boolean hasPermissions(PermissionDeclaration<?>... permissions) {
        return hasPermissions(Arrays.stream(permissions).map(PermissionDeclaration::getCode).toArray(String[]::new));
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param permissions 权限
     * @throws BaseException 异常
     */
    public static void checkPermissions(PermissionDeclaration<?>... permissions) throws BaseException {
        if (!hasPermissions(permissions)) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少下列权限之一: " + Arrays.stream(permissions).map(Objects::toString).collect(Collectors.joining(","))));
        }
    }

    /**
     * 检查当前登录用户是否具有指定权限
     *
     * @param roles 角色
     * @return 是否包含
     */
    public static Boolean hasRoles(RoleDeclaration<?>... roles) {
        return Optional.ofNullable(currentUser())
                .map(UserResponse::getRoles)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(r -> Arrays.stream(roles).anyMatch(e -> e.getCode().equals(r.getCode())));
    }

    /**
     * 检查当前登录用户是否具有指定角色
     *
     * @param roles 角色
     * @throws BaseException 异常
     */
    public static void checkRole(RoleDeclaration<?>... roles) throws BaseException {
        if (!hasRoles(roles)) {
            throw new BaseException(500, "无访问权限", new Throwable("缺少角色: " + Arrays.stream(roles).map(Objects::toString).toList()));
        }
    }

    /**
     * 是否是超级管理员
     *
     * @return 是否是超级管理员
     */
    public Boolean isAdmin() {
        return isAdmin(currentUser());
    }

    /**
     * 是否是超级管理员
     *
     * @param user 用户
     * @return 是否是超级管理员
     */
    public Boolean isAdmin(UserResponse user) {
        final String username = Optional.ofNullable(property)
                .map(AuthorizationProperty::getAdmin)
                .map(AdminProperty::getUsername)
                .orElseThrow(() -> new BaseException(500, "未找到权限属性配置"));
        return username.equals(Optional.ofNullable(user).map(UserResponse::getUsername).orElse(""));
    }

    /**
     * 方法参数签名校验
     *
     * @param params    方法参数字典
     * @param accessKey 访问密钥
     * @param secretKey 签名密钥
     * @param signature 签名
     */
    public static void checkSignature(Map<String, Object> params, String accessKey, String secretKey, String signature) throws BaseException {
        // 从参数中移除signature、accessKey、secretKey、timestamp
        params.remove("signature");
        params.remove("accessKey");
        params.remove("secretKey");
        // 对参数进行排序
        String sortedParams = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        // 生成签名
        String generatedSignature = SignatureHelper.generateSignature(sortedParams, secretKey);
        // 检查签名是否匹配
        if (!generatedSignature.equals(signature)) {
            throw new BaseException(403, "签名验证失败", new Throwable("签名验证失败"));
        }
    }
}
