package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemDao {
    /*添加*/
    void add(CheckItem checkItem);
    /*分页查询*/
    Page<CheckItem> findPage();
    /*条件查询*/
    CheckItem findString(String string);
    /*根据id删除*/
    void deleteById(int id);
    /*根据id查询*/
    CheckItem findById(Integer id);
    //修改数据
    void edit(CheckItem checkItem);
    //查询所有检查项
    List<CheckItem> findAll();
}
