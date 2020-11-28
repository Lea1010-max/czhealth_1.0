package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 清理垃圾图片
 * <p>
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-26 20:04
 */
public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-jobs.xml");
        System.in.read();
    }
}
