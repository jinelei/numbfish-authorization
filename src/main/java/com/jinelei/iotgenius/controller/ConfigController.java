package com.jinelei.iotgenius.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${springdoc.api-docs.enabled::false}")
    private String useLocalCache;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public String get() {
        return useLocalCache;
    }

    public void setUseLocalCache(String useLocalCache) {
        this.useLocalCache = useLocalCache;
    }

}