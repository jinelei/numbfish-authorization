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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RedisTemplate<String, UserResponse> redisTemplate) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(registry -> {
                    registry.anyRequest().permitAll();
//                            .anyRequest().anonymous();
//                    .requestMatchers(AntPathRequestMatcher.antMatcher("/user/login")).anonymous()
//                            .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/**")).anonymous()
//                            .requestMatchers(AntPathRequestMatcher.antMatcher("/api-docs/**")).anonymous()
//                            .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).anonymous()
//                            .requestMatchers(AntPathRequestMatcher.antMatcher("/doc.html")).anonymous()
                })
                .addFilterAfter(new ReReadableRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginUserFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore((request, response, chain) -> {
                    Optional.ofNullable(AuthorizationHelper.currentUser()).ifPresent(user -> {
                        log.info("当前登陆用户信息: {}", user);
                        final List<SimpleGrantedAuthority> authorities = new CopyOnWriteArrayList<>();
                        Optional.ofNullable(user.getRoles()).orElse(new ArrayList<>())
                                .parallelStream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                                .forEach(authorities::add);
                        Optional.ofNullable(user.getPermissions()).orElse(new ArrayList<>())
                                .parallelStream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                                .forEach(authorities::add);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getUsername(), authorities);
                        authentication.setDetails(new WebAuthenticationDetails((HttpServletRequest) request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                    chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
