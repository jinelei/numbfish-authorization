package com.jinelei.numbfish.authorization;

import com.jinelei.numbfish.authorization.configuration.CoreSecurityAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.jinelei.numbfish.common.helper.SpringHelper;

@SuppressWarnings("unused")
@Import({CoreSecurityAutoConfiguration.class, SpringHelper.class})
@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthorizationApplication.class})
public class NumbfishAuthorizationApplication {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthorizationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NumbfishAuthorizationApplication.class, args);
    }

}
