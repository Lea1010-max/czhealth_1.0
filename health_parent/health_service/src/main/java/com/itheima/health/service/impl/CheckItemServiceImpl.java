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

    /**
     * 增加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //PageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //字符串非空判断,拼接%模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //通过字符串进行模糊查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());

        // 封装到分页结果对象中
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 能否删除要判断是否有套餐使用了该检查组
     * @param id
     * @throws MyException
     */
    @Override
    public void deleteById(int id) throws MyException{
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            throw new MyException(MessageConstant.DELETE_CHECKITEM_FAIL);
        }else {
            checkItemDao.deleteById(id);
        }
    }

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    /**
     * 更新检查组
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
