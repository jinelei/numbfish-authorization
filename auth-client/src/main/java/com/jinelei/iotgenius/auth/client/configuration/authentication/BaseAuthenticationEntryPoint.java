package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.helper.ServletHelper;
import com.jinelei.iotgenius.common.view.BaseView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BaseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ServletHelper servletHelper;

    public BaseAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.servletHelper = new ServletHelper(objectMapper);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        BaseView<String> result = new BaseView<>(500, "暂无访问权限", authException.getMessage());
        servletHelper.response(request, response, MediaType.APPLICATION_JSON, HttpStatus.UNAUTHORIZED, result);
    }
}
