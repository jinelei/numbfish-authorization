package com.jinelei.numbfish.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jinelei.numbfish.auth.api.permission.PermissionApi;
import com.jinelei.numbfish.auth.api.role.RoleApi;
import com.jinelei.numbfish.auth.configuration.CoreSecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jinelei.numbfish.auth.configuration.authentication.instance.PermissionInstance;
import com.jinelei.numbfish.auth.configuration.authentication.instance.RoleInstance;
import com.jinelei.numbfish.common.helper.SpringHelper;

@SuppressWarnings("unused")
@MapperScan("com.jinelei.numbfish.auth.client.mapper")
@Import({CoreSecurityAutoConfiguration.class, SpringHelper.class})
@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthClientApplication.class})
public class NumbfishAuthClientApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthClientApplication.class);
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(NumbfishAuthClientApplication.class, args);
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
            SpringHelper.getBean(PermissionApi.class).regist(List.of(PermissionInstance.class.getEnumConstants()));
            SpringHelper.getBean(RoleApi.class).regist(List.of(RoleInstance.class.getEnumConstants()));
        }, 1, TimeUnit.MINUTES);
    }

}
