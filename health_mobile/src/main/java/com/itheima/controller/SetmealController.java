package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal (){
        try {
            List<Setmeal> setmealList = setmealService.findAll();
            return new Result(true,QUERY_SETMEALLIST_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_SETMEALLIST_SUCCESS);
        }

    }
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        if (id!=null&&id>0){
        try {
            Setmeal setmeal = setmealService.findSetmealById(id);
                return new Result(true,QUERY_SETMEALLIST_SUCCESS,setmeal);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_SETMEALLIST_FAIL);
        }
            }else {
            return new Result(false,QUERY_SETMEALLIST_FAIL);}
    }
    @RequestMapping("/findOne.do")
    public Result findOne(Integer id){
        try {
            Setmeal one = setmealService.findOne(id);
            return new Result(true,QUERY_SETMEAL_SUCCESS,one);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,QUERY_SETMEAL_FAIL);
        }

    }


}
