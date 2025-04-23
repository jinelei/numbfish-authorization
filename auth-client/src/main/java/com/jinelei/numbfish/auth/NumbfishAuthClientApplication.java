package com.jinelei.numbfish.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jinelei.numbfish.auth.configuration.CoreSecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;

import com.jinelei.numbfish.common.helper.SpringHelper;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

@SuppressWarnings("unused")
@MapperScan("com.jinelei.numbfish.auth.mapper")
@Import({CoreSecurityAutoConfiguration.class, SpringHelper.class})
@SpringBootApplication(scanBasePackageClasses = {NumbfishAuthClientApplication.class})
public class NumbfishAuthClientApplication {
    private static final Logger log = LoggerFactory.getLogger(NumbfishAuthClientApplication.class);

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
        if (env instanceof AbstractEnvironment e) {
            final MutablePropertySources propertySources = e.getPropertySources();
            String content = propertySources.stream()
                    .filter(p -> p.getName().startsWith("Config resource"))
                    .map(PropertySource::getSource)
                    .filter(p -> Map.class.isAssignableFrom(p.getClass()))
                    .map(p -> (Map<String, Object>) p)
                    .map(Map::keySet)
                    .flatMap(Collection::stream)
                    .map(n -> "%s: %s".formatted(n, e.getProperty(n)))
                    .collect(Collectors.joining("\n"));
            log.info("""
                    \n
                    ----------------------------------------------------------
                    \t\
                    Config:
                    {}
                    ----------------------------------------------------------""", content);
        }
    }

}
