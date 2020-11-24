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

    /**
     * 增加套餐，绑定关联检查组
     * @param setmeal
     * @param checkgroupIds
     */
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

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //分页工具 PageHelper
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //字符串非空判断,拼接%模糊查询
        String queryString = queryPageBean.getQueryString();
        if (!StringUtil.isEmpty(queryString)) {
            queryPageBean.setQueryString("%" + queryString + "%");
        }

        //通过字符串进行模糊查询
        Page<Setmeal> page = setmealDao.findCondition(queryPageBean.getQueryString());

        // 封装到分页结果对象中
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
