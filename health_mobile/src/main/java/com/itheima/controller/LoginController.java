package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

import static com.itheima.constant.MessageConstant.*;
import static com.itheima.constant.RedisMessageConstant.SENDTYPE_LOGIN;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @RequestMapping("/check.do")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        String validateCode = (String) map.get("validateCode");//获取输入的验证码
        String telephone1 = (String) map.get("telephone");
        if (validateCode==null&&validateCode.length()<0&&validateCode==""&&validateCode==telephone1){
           //手机和验证码不能为空
            return new Result(false,TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //对比验证码
        String telephone = jedisPool.getResource().get( telephone1+ SENDTYPE_LOGIN);
        if (validateCode!=telephone){
            //验证码输入有误
            new Result(false,VALIDATECODE_ERROR);
        }
        //登录
        Member member = memberService.findByTel(telephone1);
        if (member==null){
            //还不是会员,自动注册会员
           member=new Member();
            member.setPhoneNumber(telephone1);
            member.setRegTime(new Date());
            memberService.add(member);

        }
        //已经是会员,直接登录
        //将用户信息保存在cookie中
        Cookie cookie = new Cookie("login_member_telephone",telephone1);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);//30天
        response.addCookie(cookie);
        //存入redis
        String s = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone1,60*30,s);//将用户信息保存在redis中30分钟的存活

        return  new Result(true,LOGIN_SUCCESS);



    }
}
