package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-22 19:47
 * <p>
 * 全局异常
 */

@RestControllerAdvice
public class MyExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(MyException.class);

    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e) {
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(MyException e) {
        log.error("发生未知异常", e);
        return new Result(false, "发生未知异常，操作失败");
    }
}
