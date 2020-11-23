package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-21 17:40
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //PageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //字符串非空判断
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //模糊查询，拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //通过字符串进行模糊查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        // 封装到分页结果对象中
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public void deleteById(int id) throws MyException{
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            throw new MyException(MessageConstant.DELETE_CHECKITEM_FAIL);
        }else {
            checkItemDao.deleteById(id);
        }
    }

    @Override
    public CheckItem findById(int id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
