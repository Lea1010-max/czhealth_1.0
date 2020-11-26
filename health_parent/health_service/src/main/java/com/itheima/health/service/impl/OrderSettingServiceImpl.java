package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-25 18:12
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 更新预约数
     * @param orderSetting
     */
    public void updateOrderSetting(OrderSetting orderSetting){
        //通过日期查询当日是否有数据
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //有数据
        if (osInDB != null) {
            //已预约数大于可预约数，抛出
            if (osInDB.getReservations() > orderSetting.getNumber()) {
                throw new MyException(orderSetting.getOrderDate() + "超过可预约数量");
            }
            //已预约数小于可预约数，更新
            orderSettingDao.updateNumber(orderSetting);
        } else {
            //当日没数据，新增
            orderSettingDao.add(orderSetting);
        }
    }

    /**
     * 读取Excel数据存到数据库
     * 1、通过日期，遍历查询数据库中当天是否有预约
     * 2、有预约=>判断数据中已预约数是否大于可预约数，大于则通过异常抛出
     * 3、没预约=>新增
     * 事务控制（有多组）
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void uploadOrderSetting(List<OrderSetting> orderSettingList) {

        for (OrderSetting orderSetting : orderSettingList) {
           this.updateOrderSetting(orderSetting);
        }
    }

    /**
     * 编辑当日预约
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        this.updateOrderSetting(orderSetting);
    }

    /**
     * 预约数据日历回显
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getDataByMonth(String month) {
        //根据年月模糊查询
        month+="%";
        return orderSettingDao.getDataByMonth(month);
    }
}
