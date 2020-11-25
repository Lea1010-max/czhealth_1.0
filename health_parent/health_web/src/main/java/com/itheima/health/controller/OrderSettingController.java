package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            //返回数据
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @GetMapping("/getDataByMonth")
    public Result getDataByMonth(String month){
        List<Map<String, Integer>> data = orderSettingService.getDataByMonth(month);
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,data);
    }

}
