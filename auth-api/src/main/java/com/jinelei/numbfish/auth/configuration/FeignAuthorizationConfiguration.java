package com.jinelei.numbfish.auth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.numbfish.auth.configuration.authentication.CoreRequestInterceptor;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SuppressWarnings("unused")
@Configuration
@EnableMethodSecurity
@Import({AuthorizationProperty.class})
public class FeignAuthorizationConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FeignAuthorizationConfiguration.class);

    public FeignAuthorizationConfiguration() {
        log.info("FeignAuthorizationConfiguration initialized");
    }

    @Bean
    public RequestInterceptor coreRequestInterceptor(ObjectMapper objectMapper, AuthorizationProperty property) {
        return new CoreRequestInterceptor(objectMapper, property);
    }

}
