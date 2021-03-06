package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-25 17:46
 */

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            //1、读取excel
            List<String[]> strings = POIUtils.readExcel(excelFile);

            //2、类型转换
            //2.1、创建List
            List<OrderSetting> orderSettingList = new ArrayList<>();

            //2.2日期格式解析
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            Date orderDate = null;
            OrderSetting os = null;
            for (String[] dataArr : strings) {
                orderDate = sdf.parse(dataArr[0]);

                //2.3可预约数解析
                int number = Integer.valueOf(dataArr[1]);

                //2.4封装到OrderSetting实体类
                os = new OrderSetting(orderDate, number);
                orderSettingList.add(os);
            }
            //调用业务层方法
            orderSettingService.uploadOrderSetting(orderSettingList);
            //返回结果
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 预约数据日历回显
     * @param month
     * @return
     */
    @GetMapping("/getDataByMonth")
    public Result getDataByMonth(String month) {
        /**
         *  前端数据格式：
         *  this.leftobj = [{ date: 6, number: 120, reservations: 1 }]
         */
        List<Map<String, Integer>> data = orderSettingService.getDataByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, data);
    }

    /**
     * 当日预约设置
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
