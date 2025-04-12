package com.jinelei.numbfish.auth.client.configuration;

import com.jinelei.numbfish.auth.client.configuration.authentication.*;
import com.jinelei.numbfish.common.filter.RepeatRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.numbfish.auth.helper.AuthorizationHelper;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;

import java.util.Optional;

@SuppressWarnings("unused")
@Configuration
@Import({AuthorizationHelper.class})
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientAuthenticationProvider clientAuthenticationProvider(AuthorizationProperty property) {
        return new ClientAuthenticationProvider(property);
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider(StringRedisTemplate redisTemplate,
                                                                   UserDetailsService userDetailsService) {
        return new TokenAuthenticationProvider(redisTemplate, userDetailsService);
    }

    @Bean
    public ClientAuthenticationFilter clientAuthenticationFilter(AuthorizationProperty property,
                                                                 ObjectMapper objectMapper,
                                                                 ServerProperties serverProperties,
                                                                 ClientDetailService clientDetailService,
                                                                 AuthenticationManager authenticationManager) {
        return new ClientAuthenticationFilter(property, objectMapper, clientDetailService, authenticationManager);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(AuthorizationProperty property,
                                                               AuthenticationManager authenticationManager) {
        return new TokenAuthenticationFilter(property, authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       @Autowired(required = false) UserDetailsService userDetailsService,
                                                       @Autowired(required = false) PasswordEncoder passwordEncoder,
                                                       @Autowired(required = false) ClientAuthenticationProvider clientAuthenticationProvider,
                                                       @Autowired(required = false) TokenAuthenticationProvider tokenAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        Optional.ofNullable(userDetailsService).ifPresent(u ->
                Optional.ofNullable(passwordEncoder).ifPresent(p -> {
                    try {
                        builder.userDetailsService(u).passwordEncoder(p);
                    } catch (Exception e) {
                        log.error("初始化AuthenticationManagerBuilder失败", e);
                        throw new RuntimeException(e);
                    }
                }));
        Optional.ofNullable(tokenAuthenticationProvider).ifPresent(builder::authenticationProvider);
        Optional.ofNullable(clientAuthenticationProvider).ifPresent(builder::authenticationProvider);
        return builder.build();
    }

    @Bean
    public BaseAuthenticationEntryPoint baseAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new BaseAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Autowired(required = false) AuthorizationProperty property,
                                                   @Autowired(required = false) ClientAuthenticationFilter clientAuthenticationFilter,
                                                   @Autowired(required = false) TokenAuthenticationFilter tokenAuthenticationFilter,
                                                   @Autowired(required = false) AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http.authorizeHttpRequests(a -> a
                .requestMatchers(property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new)).permitAll()
                .requestMatchers(new AntPathRequestMatcher(property.getLoginUrl(), HttpMethod.POST.name())).permitAll()
                .anyRequest().authenticated());
        http.requestCache(c -> c.requestCache(new HttpSessionRequestCache()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new RepeatRequestFilter(), ChannelProcessingFilter.class);
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(clientAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(c -> Optional.ofNullable(authenticationEntryPoint).ifPresent(c::authenticationEntryPoint));
        return http.build();
    }

}
