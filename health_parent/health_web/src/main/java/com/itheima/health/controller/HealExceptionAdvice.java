package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * before
 * after finally
 * after returning return
 * around
 * RestControllerAdvice after throwing controller抛出的异常
 *
 * 日志的打印:
 * info: 打印流程性的东西
 * debug: 记录关键的数据key 订单id
 * error: 打印异常，代替e.printStackTrace, System.out.println
 * </p>
 *  try{} catch(异常的类型)
 * @author: Eric
 * @since: 2020/11/22
 */
@RestControllerAdvice
public class HealExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    /***
     * catch(MyException)
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        return new Result(false, "权限不足");
    }

    /***
     * catch(MyException)
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        //e.printStackTrace();
        //System.out.println();
        log.error("发知未知异常",e);
        return new Result(false, "发知未知异常，请稍后重试");
    }
}
