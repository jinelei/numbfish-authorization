package com.jinelei.iotgenius.exception;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/7/12
 * @Version: 1.0.0
 */
public class DatabaseException extends CoreException {
    private static final int code = 102000;

    public DatabaseException() {
        super(code, DatabaseException.class.getSimpleName());
    }

    public DatabaseException(String message) {
        super(code, message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(code, message, cause);
    }
}
