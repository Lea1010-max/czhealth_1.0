package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 23:23
 */
public interface OrderDao {
    List<Order> findByCondition(Order order);
    void add(Order order);
    Map<String, Object> findOrderDetailById(Integer id);
}
