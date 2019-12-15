package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckgroupDao {

    void addCheckGroup(CheckGroup checkGroup);

    void setCheckgtoupAndCheckitem(Map<String, Integer> map);

    Page<CheckGroup> findByString(String queryString);

    Page<CheckGroup> findPage();

    void delCheckgroupAndCheckitem(Integer id);

    void delSetmealAndCheckgroup(Integer id);

    void delCheckgroup(Integer id);

    CheckGroup findOne(Integer id);

    List<Integer> findById(Integer id);

    void updataCheckGroup(CheckGroup checkGroup);

    List<CheckGroup> findAll();
}
