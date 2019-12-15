package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.*;

@Service(interfaceClass= OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<String[]> strings) {
        List<OrderSetting> orderSettingList = new ArrayList<>();
        if(strings!=null){
            for (String[] string : strings) {

            OrderSetting orderSetting = new OrderSetting(new Date(string[0]),Integer.parseInt(string[1]));
           orderSettingList.add(orderSetting);
            }
            for (OrderSetting orderSetting : orderSettingList) {
               edit(orderSetting);
            }
        }
    }

    @Override
    public List<Map> find(String date) {
            String start = date+"-1";
            String end  = date+"-31";
            Map<String ,String> map = new HashMap<>();
            map.put("start",start);
            map.put("end",end);
        List<OrderSetting> orderSettingList = orderSettingDao.find(map);
        List<Map> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Date orderDate = orderSetting.getOrderDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            int i = calendar.get(calendar.DAY_OF_MONTH);
            Map<String,Integer> maps = new HashMap<>();
            maps.put("date",i);
            maps.put("number",orderSetting.getNumber());
            maps.put("reservations",orderSetting.getReservations());
            mapList.add(maps);
        }

        return mapList;
    }

    @Override
    public void edit(OrderSetting orderSetting) {
        //设置预约
        long count = orderSettingDao.findByDate(orderSetting.getOrderDate());
        if (count>0){
            orderSettingDao.edit(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
