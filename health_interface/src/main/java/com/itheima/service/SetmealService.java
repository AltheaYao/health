package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
       /*分页数据*/
    PageResult findPage(QueryPageBean queryPageBean);
    //添加套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);
    //查询套餐数据
    Setmeal findOne(Integer setmealId);
    //查询套餐数据关联的检查组数据
    List<Integer> findById(Integer setmealId);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findSetmealById(Integer id);

    List<String> findName();

    List findCountByName(List<String> setmealNames);
}
