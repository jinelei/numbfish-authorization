package com.jinelei.iotgenius.auth.client.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.authentication.ClientAuthenticationFilter;
import com.jinelei.iotgenius.auth.authentication.ClientAuthenticationProvider;
import com.jinelei.iotgenius.auth.authentication.ClientDetailService;
import com.jinelei.iotgenius.auth.client.configuration.authentication.BaseAuthenticationEntryPoint;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;

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
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder, ClientAuthenticationProvider clientAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        builder.authenticationProvider(clientAuthenticationProvider);
        return builder.build();
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
    public BaseAuthenticationEntryPoint baseAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new BaseAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationProperty property,
            ClientAuthenticationFilter clientAuthenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new)).permitAll()
                .requestMatchers(new AntPathRequestMatcher(property.getLoginUrl())).permitAll()
                .anyRequest().authenticated())
                .addFilterAfter(clientAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(c -> c.disable())
                .httpBasic(c -> c.disable())
                .csrf(c -> c.disable())
                .formLogin(c -> c.disable())
                .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

}
