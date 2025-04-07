package com.jinelei.iotgenius.auth.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.authentication.ClientAuthenticationFilter;
import com.jinelei.iotgenius.auth.authentication.ClientAuthenticationProvider;
import com.jinelei.iotgenius.auth.authentication.ClientDetailService;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public ClientAuthenticationProvider clientAuthenticationProvider() {
        return new ClientAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        builder.authenticationProvider(clientAuthenticationProvider());
        return builder.build();
    }

    @Bean
    public ClientAuthenticationFilter clientAuthenticationFilter(AuthorizationProperty property,
            ObjectMapper objectMapper,
            ServerProperties serverProperties,
            ClientDetailService clientDetailService,
            AuthenticationManager authorizationManager) {
        final ClientAuthenticationFilter filter = new ClientAuthenticationFilter();
        filter.setProperty(property);
        filter.setObjectMapper(objectMapper);
        filter.setClientDetailService(clientDetailService);
        filter.setAuthenticationManager(authorizationManager);
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationProperty property,
            ClientAuthenticationFilter clientAuthenticationFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(property.getIgnoreUrls().toArray(String[]::new)).permitAll()
                .requestMatchers(property.getLoginUrl()).permitAll()
                .anyRequest().authenticated())
                .addFilterAfter(clientAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(c -> c.disable())
                .httpBasic(c -> c.disable());
        return http.build();
    }

}
