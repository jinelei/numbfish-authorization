package com.jinelei.iotgenius.exception;

/**
 * @Author: jinelei
 * @Description: 数据/记录不存在异常
 * @Date: 2023/7/12
 * @Version: 1.0.0
 */
public class NotExistException extends CoreException {
    private static final int code = 101003;

    public NotExistException() {
        super(code, NotExistException.class.getSimpleName());
    }

    public NotExistException(String message) {
        super(code, message);
    }

    public NotExistException(String message, Throwable cause) {
        super(code, message, cause);
    }
}