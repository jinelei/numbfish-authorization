package com.jinelei.numbfish.authorization.configuration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.numbfish.common.helper.ServletHelper;
import com.jinelei.numbfish.common.view.BaseView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@SuppressWarnings("unused")
public class CoreAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ServletHelper servletHelper;

    public CoreAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.servletHelper = new ServletHelper(objectMapper);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        BaseView<String> result = new BaseView<>(500, "无访问权限", authException.getMessage());
        servletHelper.response(request, response, result);
    }
}
