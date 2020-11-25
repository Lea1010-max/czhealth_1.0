package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-25 18:10
 */
public interface OrderSettingService {
    void uploadOrderSetting(List<OrderSetting> orderSettingList) throws MyException;

    List<Map<String, Integer>> getDataByMonth(String month);
}
