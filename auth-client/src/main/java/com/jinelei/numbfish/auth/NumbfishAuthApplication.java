package com.jinelei.numbfish.auth;

import com.jinelei.numbfish.auth.configuration.CoreSecurityAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.jinelei.numbfish.common.helper.SpringHelper;

@SuppressWarnings("unused")
@Import({CoreSecurityAutoConfiguration.class, SpringHelper.class})
@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthApplication.class})
public class NumbfishAuthApplication {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NumbfishAuthApplication.class, args);
    }

}
