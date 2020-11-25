package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-25 18:21
 */
public interface OrderSettingDao {

    OrderSetting findByOrderDate(Date orderDate);

    void updateNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map<String, Integer>> getDataByMonth(String month);
}
