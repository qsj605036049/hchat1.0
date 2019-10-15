package com.itheima.hchat.pojo.vo;

import java.io.Serializable;

/**
 * 返回结果
 * @author qinshiji
 * @data 2019/7/19 10:55
 */
public class Result implements Serializable {
    /**
     * 是否操作成功
     */
    private boolean success;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回结果
     */
    private Object result;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, Object result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
