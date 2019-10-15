package com.itheima.hchat.exception;


/**
 * 自定义异常
 *
 * @author qinshiji
 * @data 2019/7/19 11:03
 */
public class CustomException extends RuntimeException {
    private boolean success;
    private String message;

    public CustomException(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
