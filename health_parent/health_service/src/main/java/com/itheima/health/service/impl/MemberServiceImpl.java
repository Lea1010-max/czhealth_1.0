package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-29 21:38
 */

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号查询会员
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 新增会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 统计过去一年的会员数，参数是集合类型，可以遍历查询，再添加到要返回的集合
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        //创建集合对象
        List<Integer> memberCount = new ArrayList<>();
        //遍历月份查询
        for (String month : months) {
            //查询方式1：month补上"-31"，sql语句 regTime <= #{month}
            Integer count = memberDao.findMemberCountBeforeDate(month + "-31");
            //查询方式2：模糊查询
            // month+="%"; sql语句 like #{month}
            memberCount.add(count);
        }
        return memberCount;
    }
}
