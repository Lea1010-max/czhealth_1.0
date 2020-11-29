package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 20:26
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paraMap){
        //校验验证码是否正确
        Jedis jedis = jedisPool.getResource();
        //key拼接业务标识
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + paraMap.get("telephone");
        String code = jedis.get(key);
        //判断code是否存在
        if (StringUtil.isEmpty(code)){
            return new Result(false,"验证码已失效或不存在！请重新获取");
        }
        String validateCode = paraMap.get("validateCode");
        if (!validateCode.equals(code)){
            // 不正确=>抛异常
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //正确=>移除redis中的验证码，防止一码多用
        //jedis.del(key);
        //设置预约的类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        Integer id = orderService.submit(paraMap);
        return new Result(true,MessageConstant.ORDER_SUCCESS,id);
    }

    /**
     * 成功页面显示
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map<String, Object> resultMap = orderService.findOrderDetailById(id);
        return new Result(true,MessageConstant.ORDER_SUCCESS,resultMap);
    }
}
