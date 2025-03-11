package com.jinelei.iotgenius.auth.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers(AntPathRequestMatcher.antMatcher("/user/login")).anonymous()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/user/create")).anonymous()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/**")).anonymous()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api-docs/**")).anonymous()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).anonymous()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/doc.html")).anonymous()
                    .anyRequest().authenticated();
        });
        return http.build();
    }

}
