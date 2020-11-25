package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-24 16:29
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 上传图片不需要调用service层方法，原因是：
     *      1、没有把图片数据存到数据库，而是云存储
     *      2、调用service层方法，会有数据传输，步骤冗余，影响响应性能
     */
    @RequestMapping("/upload")
    public Result updataImg(@RequestBody MultipartFile imgFile) {
        /**
         * 图片存储使用唯一ID
         */
        //获得图片名
        String originalFilename = imgFile.getOriginalFilename();
        //截取文件后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //UUID生成唯一ID
        String fileName = UUID.randomUUID() + extension;

        //上传，返回图片名
        try {
            //上传图片数据
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), fileName);

            /**
             * 存储到数据库时（前端双向绑定），不带域名，原因是更改云存储时，可以不必修改数据库
             */
            Map<String, String> map = new HashMap<>();
            map.put("imgName", fileName);
            map.put("domain", QiNiuUtils.DOMAIN);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**增加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        setmealService.add(setmeal, checkgroupIds);
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        Setmeal setmeal = setmealService.findById(id);
        Map resultMap = new HashMap<String, Object>();
        resultMap.put("domain", QiNiuUtils.DOMAIN);
        resultMap.put("setmeal", setmeal);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
    }

    /**
     * 通过套餐ID查询检查组ID
     * @param id
     * @return
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id) {
        List<Integer> checkGroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, checkGroupIds);
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        setmealService.update(setmeal, checkGroupIds);
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**
     * 通过ID删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        setmealService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
