package com.jinelei.iotgenius;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "app", autoRefreshed = true)
public class IotgeniusApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotgeniusApplication.class, args);
    }

}
