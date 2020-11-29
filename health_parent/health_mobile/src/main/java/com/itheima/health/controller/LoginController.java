package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-29 20:51
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/check")
    public Result loginCheck(@RequestBody Map<String, String> paraMap, HttpServletResponse res) {
        //校验验证码是否正确
        Jedis jedis = jedisPool.getResource();
        //key拼接业务标识
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + paraMap.get("telephone");
        String code = jedis.get(key);
        //判断code是否存在
        if (StringUtil.isEmpty(code)) {
            return new Result(false, "验证码已失效或不存在！请重新获取");
        }
        String validateCode = paraMap.get("validateCode");
        if (!validateCode.equals(code)) {
            // 不正确=>抛异常
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //正确=>移除redis中的验证码，防止一码多用
        //jedis.del(key);

        //通过手机号判断是否已注册
        String telephone = paraMap.get("telephone");
        Member member = memberService.findByTelephone(telephone);
        //未注册
        if (member == null) {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("手机快速注册");
            memberService.add(member);
        }
        //跟踪记录的手机号码
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30 * 24 * 60 * 60); // 存1个月
        cookie.setPath("/"); // 访问的路径 根路径下时 网站的所有路径 都会发送这个cookie
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
