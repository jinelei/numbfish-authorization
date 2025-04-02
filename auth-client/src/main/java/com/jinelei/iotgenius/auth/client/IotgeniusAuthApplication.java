package com.jinelei.iotgenius.auth.client;

import com.jinelei.iotgenius.auth.client.configuration.permission.instance.PermissionInstance;
import com.jinelei.iotgenius.auth.client.configuration.permission.instance.RoleInstance;
import com.jinelei.iotgenius.auth.client.service.PermissionService;
import com.jinelei.iotgenius.auth.client.service.RoleService;
import com.jinelei.iotgenius.auth.property.AuthApiProperty;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@EnableConfigurationProperties({ AuthApiProperty.class })
@SpringBootApplication(scanBasePackageClasses = { IotgeniusAuthApplication.class })
@MapperScan("com.jinelei.iotgenius.auth.client.mapper")
public class IotgeniusAuthApplication {
    private static final Logger log = LoggerFactory.getLogger(IotgeniusAuthApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(IotgeniusAuthApplication.class, args);
        Environment env = run.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running!\n\t" +
                "Local: \t\thttp://localhost:{}\n\t" +
                "External: \thttp://{}:{}\n\t" +
                "Doc: \t\thttp://{}:{}/doc.html\n" +
                "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));

        run.getBean(PermissionService.class).regist(List.of(PermissionInstance.class.getEnumConstants()));
        run.getBean(RoleService.class).regist(List.of(RoleInstance.class.getEnumConstants()));
    }

}
