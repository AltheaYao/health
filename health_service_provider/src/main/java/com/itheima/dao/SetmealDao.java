package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    Page<Setmeal> findByString(String queryString);

    Page<Setmeal> findPage();
    //添加套餐数据
    void add(Setmeal setmeal);
    //添加中间表数据
    void setSetmealAndCheckgroup(Map<String, Integer> map);
    //查询套餐数据
    Setmeal findOne(Integer id);
    //查询套餐数据关联的检查组数据
    List<Integer> findById(Integer id);
    //修改数据
    void edit(Setmeal setmeal);
    //删除中间表数据
    void deleteSetmealAndCheckgroup(Integer semealId);

    void delete(Integer id);

    List<Setmeal> findAll();

    List<CheckGroup> findCheckgroupById(Integer id);

    List<CheckItem> findCheckitemById(Integer id);

    List<String> findName();

    Integer findCountByName(String setmealName);

    List<String> findName4();
}
