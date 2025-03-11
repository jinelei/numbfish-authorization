package com.jinelei.iotgenius.auth.client.configuration;

import com.jinelei.iotgenius.auth.client.configuration.filter.LoginUserFilter;
import com.jinelei.iotgenius.auth.client.configuration.filter.ReReadableRequestFilter;
import com.jinelei.iotgenius.auth.client.helper.SpringHelper;
import com.jinelei.iotgenius.auth.client.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(AntPathRequestMatcher.antMatcher("/user/login")).anonymous()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/**")).anonymous()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/api-docs/**")).anonymous()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).anonymous()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/doc.html")).anonymous()
                            .anyRequest().authenticated();
                })
                .addFilterAfter(new ReReadableRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginUserFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .userDetailsService(SpringHelper.getBean(UserService.class));
        return http.build();
    }

}
