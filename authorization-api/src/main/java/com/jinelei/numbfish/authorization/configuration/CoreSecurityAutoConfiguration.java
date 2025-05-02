package com.jinelei.numbfish.authorization.configuration;

import com.jinelei.numbfish.authorization.property.AuthorizationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SuppressWarnings("unused")
@Configuration
@EnableMethodSecurity
@Import({CoreSecurityConfiguration.class, AuthorizationProperty.class})
public class CoreSecurityAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CoreSecurityAutoConfiguration.class);

    public CoreSecurityAutoConfiguration() {
        log.info("CoreSecurityAutoConfiguration initialized");
    }
}
