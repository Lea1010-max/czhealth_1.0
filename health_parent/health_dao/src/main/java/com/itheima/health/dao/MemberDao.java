package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-28 23:25
 */
public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);
}
