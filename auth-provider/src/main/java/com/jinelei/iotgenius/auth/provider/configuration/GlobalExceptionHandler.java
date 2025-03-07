package com.jinelei.iotgenius.auth.provider.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jinelei.iotgenius.common.exception.BaseException;
import com.jinelei.iotgenius.common.view.BaseView;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public BaseView<String> baseExceptionHandler(BaseException exception) {
        log.error("baseException: {}", exception);
        return new BaseView<>(exception.getCode(), exception.getCause());
    }

    @ExceptionHandler(Throwable.class)
    public BaseView<String> throwableHandler(Throwable throwable) {
        log.error("throwableHandler: {}", throwable);
        return new BaseView<>(throwable);
    }

}
