package com.jinelei.iotgenius.configuration.exception;

import com.jinelei.iotgenius.exception.CoreException;
import com.jinelei.iotgenius.exception.UnknownException;
import com.jinelei.iotgenius.view.BaseView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/7/15
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
@Configuration
@RestControllerAdvice
public class CoreExceptionConfiguration {
    private final static Logger log = LoggerFactory.getLogger(CoreExceptionConfiguration.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseView<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        CoreException unknownException = new UnknownException(e.getClass().getSimpleName(), e);
        Throwable throwable = Optional.ofNullable(e.getCause()).orElse(unknownException);
        while (throwable != null && !(throwable instanceof CoreException)) {
            throwable = throwable.getCause();
        }
        return handleCoreException(
                Optional.ofNullable(throwable).map(it -> ((CoreException) it)).orElse(unknownException));
    }

    @ExceptionHandler(Throwable.class)
    public BaseView<String> handleException(Throwable e) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(out));
        log.error("handleException: {}", out);
        return BaseView.failure(999999, e.getClass().getSimpleName(), e);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public BaseView<String> handleAccessDeniedException(AccessDeniedException e) {
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        e.printStackTrace(new PrintWriter(out));
//        log.error("handleAccessDeniedException: {}", out);
//        return BaseView.failure(403, "暂无访问权限", e);
//    }

    @ExceptionHandler(CoreException.class)
    public BaseView<String> handleCoreException(CoreException e) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(out));
        log.error("handleCoreException: {}", out);
        return BaseView.failure(e.getCode(), e.getClass().getSimpleName(), e);
    }
}
