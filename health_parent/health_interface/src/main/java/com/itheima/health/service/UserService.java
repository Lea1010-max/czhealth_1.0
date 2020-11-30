package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-30 09:52
 */
public interface UserService {
    User findByUsername(String username);
}
