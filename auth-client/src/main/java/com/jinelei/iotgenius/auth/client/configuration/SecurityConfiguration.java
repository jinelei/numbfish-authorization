package com.jinelei.iotgenius.auth.client.configuration;

import com.jinelei.iotgenius.auth.client.configuration.authentication.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;

import java.util.Optional;

@SuppressWarnings("unused")
@Configuration
@Import({ AuthorizationHelper.class })
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
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            Optional<ClientAuthenticationProvider> clientAuthenticationProvider,
            Optional<TokenAuthenticationProvider> tokenAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        clientAuthenticationProvider.ifPresent(builder::authenticationProvider);
        tokenAuthenticationProvider.ifPresent(builder::authenticationProvider);
        return builder.build();
    }

    @Bean
    public BaseAuthenticationEntryPoint baseAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new BaseAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public BaseAuthenticationFailureHandler baseAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new BaseAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationProperty property,
            Optional<ClientAuthenticationFilter> clientAuthenticationFilter,
            Optional<TokenAuthenticationFilter> tokenAuthenticationFilter,
            BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
            AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new)
                        .toArray(AntPathRequestMatcher[]::new))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(property.getLoginUrl(), HttpMethod.POST.name()))
                .permitAll()
                .anyRequest().authenticated())
                .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint))
                .requestCache(c -> c.requestCache(new HttpSessionRequestCache()))
                .csrf(AbstractHttpConfigurer::disable);
        clientAuthenticationFilter.ifPresent(f -> http.addFilterBefore(f, UsernamePasswordAuthenticationFilter.class));
        tokenAuthenticationFilter.ifPresent(f -> http.addFilterBefore(f, UsernamePasswordAuthenticationFilter.class));

        return http.build();
    }

}
