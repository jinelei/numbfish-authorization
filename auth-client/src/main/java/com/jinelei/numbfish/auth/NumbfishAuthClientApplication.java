package com.jinelei.numbfish.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.jinelei.numbfish.auth.configuration.CoreSecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import com.jinelei.numbfish.common.helper.SpringHelper;

@SuppressWarnings("unused")
@MapperScan("com.jinelei.numbfish.auth.mapper")
@Import({CoreSecurityAutoConfiguration.class, SpringHelper.class})
@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthClientApplication.class})
public class NumbfishAuthClientApplication {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthClientApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(NumbfishAuthClientApplication.class, args);
        log.info("""
                        
                        ----------------------------------------------------------
                        \t\
                        Application '{}' is running!
                        \t\
                        Local: \t\thttp://localhost:{}
                        \t\
                        External: \thttp://{}:{}
                        \t\
                        Doc: \t\thttp://{}:{}/doc.html
                        ----------------------------------------------------------""",
                SpringHelper.getProperty("spring.application.name"),
                SpringHelper.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                SpringHelper.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                SpringHelper.getProperty("server.port"));

    }

}
