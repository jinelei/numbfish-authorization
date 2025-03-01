package com.jinelei.iotgenius.exception;

/**
 * @Author: jinelei
 * @Description: 参数不合法异常
 * @Date: 2023/7/12
 * @Version: 1.0.0
 */
public class InvalidArgsException extends CoreException {
    private static final int code = 101001;

    public InvalidArgsException() {
        super(code, InvalidArgsException.class.getSimpleName());
    }

    public InvalidArgsException(String message) {
        super(code, message);
    }

    public InvalidArgsException(String message, Throwable cause) {
        super(code, message, cause);
    }
}
