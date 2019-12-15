package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    User findUserName(String username);

}
