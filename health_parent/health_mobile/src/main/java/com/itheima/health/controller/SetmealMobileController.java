package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-27 17:07
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private SetmealService setmealService;
    ;

    /**
     * 微信端查询所有套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        //从数据库中查询所有套餐
        List<Setmeal> setmealList = setmealService.getSetmeal();
        //补全图片路径
        setmealList.forEach(s -> {
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        //返回
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmealList);
    }

    /**
     * 查询套餐详细信息
     * @param id
     * @return
     */
    @RequestMapping("/findDetailById")
    public Result findDetailById(int id) {
        Setmeal setmeal = setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
