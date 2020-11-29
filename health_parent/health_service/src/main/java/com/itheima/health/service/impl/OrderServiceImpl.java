package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 22:11
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 预约提交业务
     * @param paraMap
     * @return
     */
    @Override
    public Integer submit(Map<String, String> paraMap) {
        //通过日期查询当日是否可以预约
        String orderDateString = paraMap.get("orderDate");
        //解析日期
        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date(orderDateString);
        } catch (Exception e) {
            throw new MyException("预约日期格式不正确");
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            //不可以=>抛异常
            throw new MyException("所选日期不能预约，请选择其它日期");
        }
        //可以=>查询当日是否预约满员
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            //满员=>抛异常
            throw new MyException("所选日期已满员，请修改日期");
        }

        //构建重复查询条件
        Order order = new Order();
        //设置订单包含的套餐ID（前端来）
        Integer setmealId = Integer.valueOf(paraMap.get("setmealId"));
        order.setSetmealId(setmealId);
        //设置订单预约日期
        order.setOrderDate(orderDate);

        //未满=>通过手机号判断是否已注册
        String telephone = paraMap.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //未注册
        if (member == null) {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("微信预约注册");
            String idCardNo = paraMap.get("idCard");
            member.setPassword(idCardNo.substring(idCardNo.length() - 6));
            member.setIdCard(idCardNo);
            member.setSex(paraMap.get("sex"));
            member.setName(paraMap.get("name"));
            memberDao.add(member);
            order.setMemberId(member.getId());
        }
        //已注册
        else {
            Integer menberId = member.getId();
            order.setMemberId(menberId);
            //通过用户ID、日期、套餐ID，查询当日该用户是否已预约相同套餐
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                //有=>抛异常
                throw new MyException("已预约，请勿重复操作");
            }
        }
        //没有=>增加预约
        //更新当日的已预约人数
        int count = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (count == 0) {    //如果更新成功，mybatis则会返回1
            throw new MyException("预约已满，请选择其它日期");
        }
        //可以增加预约
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(paraMap.get("orderType"));
        orderDao.add(order);
        //预约成功页面展示时需要用到ID,返回ID
        return order.getId();
    }

    /**
     * 预约成功页面返回
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findOrderDetailById(Integer id) {
        return orderDao.findOrderDetailById(id);
    }
}