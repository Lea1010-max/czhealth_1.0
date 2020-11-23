package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-21 17:47
 */
public interface CheckItemDao {

    void add(CheckItem checkItem);

    List<CheckItem> findAll();

    Page<CheckItem> findByCondition(String queryString);

    int findCountByCheckItemId(int id);

    void deleteById(int id);

    CheckItem findById(int id);

    void update(CheckItem checkItem);
}
