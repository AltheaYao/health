package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(List<String[]> strings);

    List<Map> find(String date);

    void edit(OrderSetting orderSetting);
}
