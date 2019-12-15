package com.itheima.controller;

import com.itheima.entity.Result;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    //检查项增删改权限验证
    @RequestMapping("/checkitem")
    public Result checkitem(){
        //获取安全框架的上下文对象获取储存的User对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user==null){
            return null;
        }
        //获取用户对象的权限集合
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities==null){
            return null;
        }
        Map map = new HashMap();
        map.put("add",false);
        map.put("edit",false);
        map.put("delete",false);
        //判断是否有该权限,决定是否展示该按钮
        for (GrantedAuthority authority : authorities) {
            String authority1 = authority.getAuthority();
            if ("CHECKITEM_ADD".equals(authority1)){
                map.put("add",true);
                continue;
            }
            if ("CHECKITEM_EDIT".equals(authority1)){
                map.put("edit",true);
                continue;
            }
            if ("CHECKITEM_DELETE".equals(authority1)){
                map.put("delete",true);
                continue;
            }
        }
        return new Result(true,"查询权限成功",map);
    }
}
