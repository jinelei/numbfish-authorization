package com.jinelei.iotgenius.auth.client.configuration;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jinelei.iotgenius.auth.client.configuration.filter.ReReadableRequestFilter;
import com.jinelei.iotgenius.auth.client.domain.*;
import com.jinelei.iotgenius.auth.client.helper.SpringHelper;
import com.jinelei.iotgenius.auth.client.service.UserService;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.filter.LoginUserFilter;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@SuppressWarnings("unused")
@Configuration
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
