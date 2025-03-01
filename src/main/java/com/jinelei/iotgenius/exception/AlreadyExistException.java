package com.jinelei.iotgenius.exception;

/**
 * @Author: jinelei
 * @Description: 数据/记录已存在异常
 * @Date: 2023/7/12
 * @Version: 1.0.0
 */
public class AlreadyExistException extends CoreException {
    public static final int code = 101002;

    public AlreadyExistException() {
        super(code, AlreadyExistException.class.getSimpleName());
    }

    public AlreadyExistException(String message) {
        super(code, message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(code, message, cause);
    }
}
