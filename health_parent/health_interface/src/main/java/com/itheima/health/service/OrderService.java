package com.itheima.health.service;

import com.itheima.health.exception.MyException;

import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 22:07
 */
public interface OrderService {
    Integer submit(Map<String, String> paraMap) throws MyException;

    Map<String, Object> findOrderDetailById(Integer id);

}
