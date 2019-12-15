package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

import static com.itheima.constant.MessageConstant.*;
import static com.itheima.constant.RedisMessageConstant.SENDTYPE_ORDER;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    @RequestMapping("/submit.do")
   public Result submit(@RequestBody Map map){
        //拿到表单数据先效验验证码是否正确,
        String validateCode = (String) map.get("validateCode");
        String telephone1 = (String) map.get("telephone");
        //判断手机和验证码不能为空
        if(validateCode==null||telephone1==null){
            return new Result(false,TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //判断验证码是否正确
     //   String telephone = jedisPool.getResource().get(map.get("telephone") + SENDTYPE_ORDER);
      //  if (telephone!=null && telephone.length()>0 && telephone.equals(validateCode)){
            try {
                //验证码正确请求后台
                Result result = orderService.addOrder(map);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,VALIDATECODE_ERROR);
            }
        //}
        //验证码验证不通过
       // return new Result(false,VALIDATECODE_ERROR);
   }
   @RequestMapping("/findById")
   public Result findById(Integer id){
       try {
           Map map = orderService.findById(id);
           return new Result(true,QUERY_ORDER_SUCCESS,map);
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false,QUERY_ORDER_FAIL);
       }

   }
}
