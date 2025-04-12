package com.jinelei.numbfish.auth.configuration;

import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SuppressWarnings("unused")
@Configuration
@EnableMethodSecurity
@Import({CoreSecurityConfiguration.class, AuthorizationProperty.class})
public class SecurityAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityAutoConfiguration.class);

    public SecurityAutoConfiguration() {
        log.info("SecurityAutoConfiguration initialized");
    }
}
