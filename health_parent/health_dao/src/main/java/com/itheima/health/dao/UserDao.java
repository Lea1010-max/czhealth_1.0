package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-30 11:00
 */
public interface UserDao {

    User findByUsername(String username);
}
