package com.jinelei.numbfish.authorization.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.authorization.dto.SetupRequest;
import com.jinelei.numbfish.authorization.service.SetupService;
import com.jinelei.numbfish.common.view.BaseView;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/check")
    public BaseView<Boolean> checkIsSetup() {
        log.info("checkIsSetup");
        return new BaseView<>(setupService.checkIsSetup());
    }

    @PostMapping("/init")
    public BaseView<Boolean> init(@RequestBody SetupRequest request) {
        log.info("init");
        if (!setupService.init(request)) {
            throw new RuntimeException("初始化失败");
        }
        return new BaseView<>(true);
    }

}
