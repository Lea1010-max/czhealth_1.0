package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-24 17:45
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> findCondition(String queryString);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void update(Setmeal setmeal);

    void deleteSetmealCheckGroup(Integer id);

    int findOrderCountBySetmealId(int id);

    void deleteSetmealById(int id);

    List<String> findImgs();

    List<Setmeal> getSetmeal();

    Setmeal findDetailById(int id);

    List<Map<String, Object>> findSetmealCount();
}
