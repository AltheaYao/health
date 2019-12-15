package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //查询数据是否有该日期
    long findByDate(Date orderSetting);
    //修改数据
    void edit(OrderSetting orderSetting);
    //添加数据
    void add(OrderSetting orderSetting);

    List<OrderSetting> find(Map<String, String> map);
}
