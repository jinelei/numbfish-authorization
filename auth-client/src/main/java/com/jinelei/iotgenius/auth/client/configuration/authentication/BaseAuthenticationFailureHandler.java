package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.helper.ServletHelper;
import com.jinelei.iotgenius.common.view.BaseView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
public class BaseAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ServletHelper servletHelper;

    public BaseAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.servletHelper = new ServletHelper(objectMapper);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        BaseView<String> result = new BaseView<>(500, "无访问权限", exception.getMessage());
        servletHelper.response(request, response, result);
    }

}
