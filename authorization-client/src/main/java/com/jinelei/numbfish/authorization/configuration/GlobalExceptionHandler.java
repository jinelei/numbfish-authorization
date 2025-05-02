package com.jinelei.numbfish.authorization.configuration;

import com.jinelei.numbfish.common.helper.ThrowableStackTraceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jinelei.numbfish.common.exception.BaseException;
import com.jinelei.numbfish.common.view.BaseView;

@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public BaseView<String> baseExceptionHandler(BaseException exception) {
        log.error("baseException: {}\n{}", exception.getMessage(), ThrowableStackTraceHelper.getStackTraceAsString(exception));
        return new BaseView<>(exception.getCode(), exception.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseView<String> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        final StringBuilder buffer = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            buffer.append(fieldError.getField()).append(": ");
            buffer.append(fieldError.getDefaultMessage()).append(";");
        }
        return new BaseView<>(buffer.toString());
    }

    @ExceptionHandler(Throwable.class)
    public BaseView<String> throwableHandler(Throwable throwable) {
        log.error("throwableHandler: {}\n{}", throwable.getMessage(), ThrowableStackTraceHelper.getStackTraceAsString(throwable));
        return new BaseView<>(throwable);
    }

}
