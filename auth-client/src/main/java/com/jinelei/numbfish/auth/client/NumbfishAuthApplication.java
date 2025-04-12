package com.jinelei.numbfish.auth.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jinelei.numbfish.auth.configuration.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jinelei.numbfish.auth.client.configuration.authentication.instance.PermissionInstance;
import com.jinelei.numbfish.auth.client.configuration.authentication.instance.RoleInstance;
import com.jinelei.numbfish.auth.client.helper.SpringHelper;
import com.jinelei.numbfish.auth.client.service.PermissionService;
import com.jinelei.numbfish.auth.client.service.RoleService;

@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthApplication.class})
@MapperScan("com.jinelei.numbfish.auth.client.mapper")
@Import(SecurityAutoConfiguration.class)
public class NumbfishAuthApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthApplication.class);
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(NumbfishAuthApplication.class, args);
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
        }, 3, TimeUnit.MINUTES);
    }

}
