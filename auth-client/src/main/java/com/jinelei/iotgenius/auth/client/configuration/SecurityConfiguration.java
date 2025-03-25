package com.jinelei.iotgenius.auth.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.filter.AuthorizeFilter;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.property.AuthApiProperty;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("unused")
@Configuration
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<Filter> loginUserFilter(RedisTemplate<String, UserResponse> redisTemplate,
            ServerProperties serverProperties, AuthApiProperty authApiProperty, ObjectMapper objectMapper) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthorizeFilter(redisTemplate, serverProperties, authApiProperty, objectMapper));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public AuthorizationHelper authorizationHelper(AuthApiProperty apiProperty) {
        AuthorizationHelper authorizationHelper = new AuthorizationHelper();
        authorizationHelper.setAuthApiProperty(apiProperty);
        return authorizationHelper;
    }
}
