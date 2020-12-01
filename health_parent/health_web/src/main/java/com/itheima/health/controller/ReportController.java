package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-12-01 16:19
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        //1、统计过去12个月的时间
        List<String> months = new ArrayList<>();
        //创建当前时间的日历对象
        Calendar cal = Calendar.getInstance();
        //为cal日历对象的月字段的值-12（设置时间为12个月前，再遍历添加）
        cal.add(Calendar.MONTH, -12);
        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //遍历添加到时间集合
        for (int i = 0; i < 12; i++) {
            //统计到当前月，遍历前月份增加1
            cal.add(Calendar.MONTH, 1);
            //获得时间，转换格式，放入集合
            Date date = cal.getTime();
            months.add(sdf.format(date));
        }

        //2、查询过去12个月的 memberCount
        List<Integer> memberCount = memberService.getMemberReport(months);

        //3、放入返回的map集合
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("months", months);
        resultMap.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);
    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //套餐数量
        List<Map<String, Object>> setmealCount = setmealService.getSetmealReport();
        //套餐名称集合
        List<String> setmealNames = new ArrayList<>();
        //抽取套餐名称
        if (setmealCount != null) {
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
        }
        //封装返回的结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("setmealNames", setmealNames);
        resultMap.put("setmealCount", setmealCount);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }
}
