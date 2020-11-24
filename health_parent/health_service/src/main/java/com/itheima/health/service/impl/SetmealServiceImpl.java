package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-24 17:40
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);

        Integer setmealId = setmeal.getId();
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        if (!StringUtil.isEmpty(queryString)) {
            queryPageBean.setQueryString("%" + queryString + "%");
        }
        Page<Setmeal> page = setmealDao.findCondition(queryPageBean.getQueryString());
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        List<Integer> checkGroupIds = setmealDao.findCheckgroupIdsBySetmealId(id);
        return checkGroupIds;
    }

    @Override
    public void update(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.update(setmeal);
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        if (checkGroupIds != null) {
            for (Integer checkGroupId : checkGroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkGroupId);
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0) {
            throw new MyException("已有订单使用该套餐，不可删除");
        }
        setmealDao.deleteSetmealCheckGroup(id);
        setmealDao.deleteSetmealById(id);
    }
}
