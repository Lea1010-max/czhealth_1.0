package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 清理垃圾图片
 * <p>
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-26 20:04
 */
@Component
public class CleanImgJob {
    private static final Logger log = LoggerFactory.getLogger(CleanImgJob.class);

    @Reference
    private SetmealService setmealService;


    //@Scheduled(cron = "0 0 2 * * ? *")  定时凌晨两点执行
    @Scheduled(initialDelay = 2000, fixedDelay = 1800000)
    public void cleaning() {

        log.info("删除垃圾图片开始执行");

        //查询七牛云存储图片
        List<String> QiNiuImgs = QiNiuUtils.listFile();

        log.debug("七牛云存有{}张照片", null == QiNiuImgs ? 0 : QiNiuImgs.size());

        //查询数据库存储图片
        List<String> DBImgs = setmealService.findImgs();

        log.debug("数据库存有{}张照片", null == DBImgs ? 0 : DBImgs.size());

        //七牛云图片-数据库图片=垃圾图片
        QiNiuImgs.removeAll(DBImgs);

        log.debug("删除{}张照片", QiNiuImgs.size());

        //转成数组
        String[] strings = QiNiuImgs.toArray(new String[]{});

        //删除
        QiNiuUtils.removeFiles(strings);

        log.info("删除{}张照片", QiNiuImgs.size());
    }
}
