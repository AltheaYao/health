package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import static com.itheima.constant.MessageConstant.*;
import static com.itheima.constant.RedisMessageConstant.SENDTYPE_LOGIN;
import static com.itheima.constant.RedisMessageConstant.SENDTYPE_ORDER;
import static com.itheima.utils.SMSUtils.VALIDATE_CODE;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone){
        //使用工具类获取一个长度为4的随机验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(VALIDATE_CODE,telephone,integer.toString());
            //发送成功
            jedisPool.getResource().setex(telephone+SENDTYPE_ORDER,300,integer.toString());
            return new Result(true,SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            //发送失败
            return new Result(false,SEND_VALIDATECODE_FAIL);
        }

    }
    @RequestMapping("/send4Login.do")
    public Result send4Login(String telephone){
        //使用工具类获取一个长度为4的随机验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(VALIDATE_CODE,telephone,integer.toString());
            //发送成功
            jedisPool.getResource().setex(telephone+SENDTYPE_LOGIN,300,integer.toString());
            return new Result(true,SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            //发送失败
            return new Result(false,SEND_VALIDATECODE_FAIL);
        }

    }

}
