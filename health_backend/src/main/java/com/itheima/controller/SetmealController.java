package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckgroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.itheima.constant.MessageConstant.*;
import static com.itheima.pojo.RedisConstant.SETMEAL_PIC_RESOURCES;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    //分页查询
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }

    //图片上传
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();//获取文件名称
        int i = originalFilename.lastIndexOf(".");//获取最后一个.的索引
        String substring = originalFilename.substring(i);//获得文件后缀名
        String fileName = UUID.randomUUID().toString() + substring;//	FuM1Sa5TtL_ekLsdkYWcf5pyjKGu.jpg
       //将文件上传七牛云
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            if (fileName!=null&&fileName.length()>0) {
                //存入redis的set集合
                jedisPool.getResource().sadd(SETMEAL_PIC_RESOURCES, fileName);
            }
            //上传成功
            return new Result(true,PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            //上传失败
            return new Result(false,PIC_UPLOAD_FAIL);
        }

    }

    //添加套餐
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);

            return new Result(true,ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,ADD_SETMEAL_FAIL);
        }
    }

    //查询套餐数据回显
    @RequestMapping("/findOne.do")
    public Result findOne(Integer id){
        try {
            Setmeal one = setmealService.findOne(id);
            return new Result(true,QUERY_SETMEAL_SUCCESS,one);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_SETMEAL_FAIL);
        }
    }

    //查询套餐管理的检查组回显数据
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            List<Integer> byId = setmealService.findById(id);
            return new Result(true,QUERY_CHECKGROUP_SUCCESS,byId);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,QUERY_CHECKGROUP_FAIL);
        }
    }

    //修改套餐
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,EDIT_SETMEAL_FAIL);
        }

    }

    //删除套餐
    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try {
            setmealService.delete(id);
            return new Result(true,DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,DELETE_SETMEAL_FAIL);
        }

    }
}
