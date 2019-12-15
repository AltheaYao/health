package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrderSettingService orderSettingService;
    //上传预约设置文件
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile){
        try {
            List<String[]> strings = POIUtils.readExcel(multipartFile);
            orderSettingService.add(strings);
           return new Result(true,IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
           return new Result(false,IMPORT_ORDERSETTING_FAIL);
        }
    }

    //查询预约
    @RequestMapping("/find.do")
    public Result find(String date){
        try {
            List<Map> maps = orderSettingService.find(date);
            return new Result(true,GET_ORDERSETTING_SUCCESS,maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,GET_ORDERSETTING_FAIL);
        }

    }

    //修改预约
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.edit(orderSetting);
            return new Result(true,ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,ORDERSETTING_FAIL);
        }

    }
}
