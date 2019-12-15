package com.itheima.controller;


import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {

    //获取用户名称
    @RequestMapping("/getUsername")
    public Result getUserName(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if (user==null){
           //表示没有用户信息
           return new Result(false,GET_USERNAME_FAIL);
       }
        return new Result(true,GET_USERNAME_SUCCESS,user.getUsername());
    }
}
