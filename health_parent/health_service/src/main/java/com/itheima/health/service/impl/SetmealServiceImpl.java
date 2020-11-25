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

        //字符串非空判断，拼接"%"进行模糊查询
        String queryString = queryPageBean.getQueryString();
        if (!StringUtil.isEmpty(queryString)) {
            queryPageBean.setQueryString("%" + queryString + "%");
        }

        //通过字符串进行模糊查询
        Page<Setmeal> page = setmealDao.findCondition(queryPageBean.getQueryString());

        // 封装到分页结果对象中
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    /**
     * 通过套餐ID查询检查组ID集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        List<Integer> checkGroupIds = setmealDao.findCheckgroupIdsBySetmealId(id);
        return checkGroupIds;
    }

    /**
     * 更新套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void update(Setmeal setmeal, Integer[] checkGroupIds) {
        //1、更新套餐
        setmealDao.update(setmeal);

        //2.1、获取套餐ID
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());

        //2.2、绑定套餐相关检查组
        if (checkGroupIds != null) {
            for (Integer checkGroupId : checkGroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkGroupId);
            }
        }
    }

    /**
     * 通过ID删除套餐
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //判断是否有订单使用套餐
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0) {
            throw new MyException("已有订单使用该套餐，不可删除");
        }

        //删除套餐相关检查组关系
        setmealDao.deleteSetmealCheckGroup(id);

        //通过ID删除套餐
        setmealDao.deleteSetmealById(id);
    }
}
