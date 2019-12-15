package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;


import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    //安全框架认证和授权
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //获取到用户名,后台查询用户消息
        com.itheima.pojo.User  user = userService.findByUserName(username);
       if (user==null){
           //认证不通过
           return null;
       }
       //认证通过
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            String keyword = role.getKeyword();
            list.add(new SimpleGrantedAuthority(keyword));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                String keyword1 = permission.getKeyword();
                list.add(new SimpleGrantedAuthority(keyword1));
            }
        }
        UserDetails userDetails = new User(username,user.getPassword(),list);

        return userDetails;
    }
}
