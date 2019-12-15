package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.SetmealService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

@Service(interfaceClass= UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUserName(String username) {
       User user =  userDao.findUserName(username);
       if (user==null){
           return null;
       }
        Set<Role> roleById = roleDao.findRoleById(user.getId());
       if (roleById==null){
           return user;
       }
        for (Role role : roleById) {
            Set<Permission>  permissionsset = permissionDao.findPermissionById(role.getId());
           role.setPermissions(permissionsset);
        }
        user.setRoles(roleById);
        return user;
    }
}
