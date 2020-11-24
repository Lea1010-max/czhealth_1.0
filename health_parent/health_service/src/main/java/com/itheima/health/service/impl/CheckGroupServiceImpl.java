package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-23 10:23
 */
@Service(interfaceClass = CheckGroupService.class) //dubbo 2.6.0 要指定
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 增加检查组
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        /**
         * 1、更新检查组表
         */
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        /**
         * 2、检查组&检查项绑定
         */
        // 非空判断
        if (checkItemIds != null) {
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkItemId);
            }
        }
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        /**
         * 分页工具 PageHelper
         */
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        /**
         * 条件非空判断，增加%占位符，方便dao模糊查询
         */
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        /**
         * 返回Page
         */
        Page<CheckGroup> checkGroupPage = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        /**
         * 封装到PageResult
         */
        return new PageResult<CheckGroup>(checkGroupPage.getTotal(), checkGroupPage.getResult());
    }

    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    /**
     * 通过检查组ID查询关联的检查项ID
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        List<Integer> checkItemIds = checkGroupDao.findCheckItemIdsByCheckGroupId(id);
        return checkItemIds;
    }

    /**
     * 更新检查组，多个事务需要加事务管理
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {
        /**
         * 1、更新检查组
         */
        checkGroupDao.update(checkGroup);

        /**
         * 2、删除检查项旧关系
         */
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        /**
         * 3、绑定新的检查项关系
         */
        Integer checkGroupId = checkGroup.getId();
        if (checkItemIds != null) {
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkItemId);
            }
        }
    }

    /**
     * 能否删除要判断是否有套餐使用了该检查组
     * @param id
     * @throws MyException
     */
    @Override
    public void deleteById(int id) throws MyException {
        /**
         * 查询关联的套餐数量进行判断
         */
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if (count > 0) {
            throw new MyException(MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroupList = checkGroupDao.findAll();
        return checkGroupList;
    }
}
