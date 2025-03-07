package com.jinelei.iotgenius.auth.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IotgeniusAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotgeniusAuthApplication.class, args);
        System.out.println("IoTGenius auth service startup!!!");
    }

}
