package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-24 16:47
 */
public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void update(Setmeal setmeal, Integer[] checkGroupIds);

    void deleteById(Integer id) throws MyException;

    List<String> findImgs();
}
