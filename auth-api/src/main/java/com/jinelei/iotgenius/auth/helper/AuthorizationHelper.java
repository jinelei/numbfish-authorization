package com.jinelei.iotgenius.auth.helper;

import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.common.exception.InternalException;
import jakarta.servlet.http.HttpServletRequest;
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

}
