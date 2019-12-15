package com.itheima.dao;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    //当前日期是否进行了预约设置
    OrderSetting findOrderDate(Date parse);
    //根据手机号查询会员
    Member findMemberByTel(String tel);
    //查询是否重复预约
   Order findOrderByNameDateSetmeal( Order order);
    //添加预约信息
    void add(Order order);
    //查询预约人,预约套餐,预约日期,预约类型
    Map findById(Integer id);
    //<!--查询今日已到诊人数-->
    Integer todayVisitsNumber(String today);
    //查询本周已到诊人数
    Integer thisWeekVisitsNumber(String week);
    //查询本月已到诊人数
    Integer thisMonthVisitsNumber(String month);
    //查询本月预约人数
    Integer thisMonthOrderNumber(Map monthMap);
    //查询本周预约人数
    Integer thisWeekOrderNumber(Map monthMap);
    //今日预约数
    Integer todayOrderNumber(String today);
    //总预约数
    Integer findCount();

    List<Integer> findMax();
}
