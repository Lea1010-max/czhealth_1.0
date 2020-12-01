package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-29 21:31
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> getMemberReport(List<String> months);
}
