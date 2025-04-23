package com.jinelei.numbfish.auth.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.auth.service.SetupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@ApiSupport(order = 0)
@Tag(name = "任务管理")
@Validated
@RestController
@RequestMapping("/setup")
public class SetupController {
    private static final Logger log = LoggerFactory.getLogger(SetupController.class);

    private final SetupService setupService;

    public SetupController(SetupService setupService) {
        this.setupService = setupService;
    }

    @RequestMapping("/init")
    public void init() {
        log.info("init");
    }

}
