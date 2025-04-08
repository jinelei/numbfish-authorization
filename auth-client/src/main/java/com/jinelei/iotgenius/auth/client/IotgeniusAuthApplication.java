package com.jinelei.iotgenius.auth.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.jinelei.iotgenius.auth.client.configuration.authentication.permission.instance.PermissionInstance;
import com.jinelei.iotgenius.auth.client.configuration.authentication.permission.instance.RoleInstance;
import com.jinelei.iotgenius.auth.client.helper.SpringHelper;
import com.jinelei.iotgenius.auth.client.service.PermissionService;
import com.jinelei.iotgenius.auth.client.service.RoleService;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;

@EnableConfigurationProperties({AuthorizationProperty.class})
@SpringBootApplication(scanBasePackageClasses = {IotgeniusAuthApplication.class})
@MapperScan("com.jinelei.iotgenius.auth.client.mapper")
public class IotgeniusAuthApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(IotgeniusAuthApplication.class);
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(IotgeniusAuthApplication.class, args);
        Environment env = run.getEnvironment();
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
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    @Override
    public void run(String... args) {
        executorService.schedule(() -> {
            SpringHelper.getBean(PermissionService.class).regist(List.of(PermissionInstance.class.getEnumConstants()));
            SpringHelper.getBean(RoleService.class).regist(List.of(RoleInstance.class.getEnumConstants()));
        }, 30, TimeUnit.SECONDS);
    }

}
