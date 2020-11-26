package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-26 20:04
 */
@Component("cleanImgJob")
public class CleanImgJob {
    @Reference
    private SetmealService setmealService;

    public void cleaning() {

        System.out.println("删除垃圾图片开始执行");

        //查询七牛云存储图片
        List<String> QiNiuImgs = QiNiuUtils.listFile();

        //查询数据库存储图片
        List<String> DBImgs = setmealService.findImgs();

        //七牛云图片-数据库图片=垃圾图片
        QiNiuImgs.removeAll(DBImgs);

        //转成数组
        String[] strings = QiNiuImgs.toArray(new String[]{});

        //删除
        QiNiuUtils.removeFiles(strings);

        System.out.println("删除垃圾图片完毕");
    }
}
