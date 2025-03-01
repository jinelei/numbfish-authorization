package com.jinelei.iotgenius.exception;

import java.util.Objects;

/**
 * @Author: jinelei
 * @Description: 基础异常
 * @Date: 2023/7/12
 * @Version: 1.0.0
 */
public class CoreException extends RuntimeException {
    protected final int code;
    protected final String message;

    public CoreException(int code) {
        super();
        this.code = code;
        this.message = this.getClass().getSimpleName();
    }

    public CoreException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CoreException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreException that = (CoreException) o;
        return code == that.code && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, getMessage());
    }

    @Override
    public String toString() {
        return "CoreException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                "} " + super.toString();
    }
}