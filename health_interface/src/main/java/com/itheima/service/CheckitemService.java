package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {
    //添加用户
    void add(CheckItem checkItem);
    //分页查找用户
    PageResult findPage(QueryPageBean queryPageBean);
    /*根虎id删除数据*/
    void deleteById(int id);
    /*根据id查数据*/
    CheckItem findById(Integer id);
    //修改数据
    void edit(CheckItem checkItem);
    //查询所有检查项
    List<CheckItem> findAll();
}
