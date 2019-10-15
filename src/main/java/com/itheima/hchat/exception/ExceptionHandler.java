package com.itheima.hchat.exception;

import com.itheima.hchat.pojo.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理器
 * @author qinshiji
 * @data 2019/7/19 10:59
 */
@ControllerAdvice
public class ExceptionHandler {

    /**
     * Exception异常处理
     * @param e
     * @return
     */
   @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return new Result(false, "系统异常");
    }

    /**
     * CustomException异常处理
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result exception(CustomException e){
        return new Result(e.isSuccess(),e.getMessage());
    }
}
