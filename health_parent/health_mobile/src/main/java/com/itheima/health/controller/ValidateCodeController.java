package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 18:00
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 1.1通过redis获取，key为手机号码，看验证码是否存在
        Jedis jedis = jedisPool.getResource();
        // 1.2key要加业务标签(区分其他业务验证码)
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (codeInRedis == null) {
            // 不存在：没发送或已清除=>生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //调用SMSUtils.发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 把验证码存入redis(设置有效期10分钟)，value:验证码, key:手机号码
                jedis.setex(key, 10 * 60, code + "");
                // 返回成功
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        // 存在：验证码已经发送了，请注意查收
        return new Result(false, "验证码已发送，请注意查收");
    }


    @RequestMapping("/send4login")
    public Result send4login(String telephone) {
        // 1.1通过redis获取，key为手机号码，看验证码是否存在
        Jedis jedis = jedisPool.getResource();
        // 1.2key要加业务标签(区分其他业务验证码)
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (codeInRedis == null) {
            // 不存在：没发送或已清除=>生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //调用SMSUtils.发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 把验证码存入redis(设置有效期10分钟)，value:验证码, key:手机号码
                jedis.setex(key, 10 * 60, code + "");
                // 返回成功
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        // 存在：验证码已经发送了，请注意查收
        return new Result(false, "验证码已发送，请注意查收");
    }
}
