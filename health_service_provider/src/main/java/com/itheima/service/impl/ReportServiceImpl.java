package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import com.sun.org.apache.bcel.internal.generic.IfInstruction;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itheima.constant.MessageConstant.GET_BUSINESS_REPORT_SUCCESS;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SetmealDao setmealDao;

    @Override
    public Result getBusinessReportData() throws Exception{
        /*
        "data":{ "todayVisitsNumber":0,今日到诊数0
        "reportDate":"2019‐04‐25",当前日期0
        "thisWeekVisitsNumber":0,本周到诊数0
        "thisMonthVisitsNumber":0,本月到诊数0
         "thisMonthNewMember":2,本月新增会员数0
          "thisWeekNewMember":0,本周新增会员数0
           "todayNewMember":0,//今日新增会员数0
          "totalMember":10,总会员数0
          "thisMonthOrderNumber":2,本月预约数0
           "todayOrderNumber":0,今日预约数0
           "thisWeekOrderNumber":0,本周预约数0
            "hotSetmeal":套餐
            [ {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套 餐","setmeal_count":5},
             {"proportion":0.1818,"name":"阳光爸妈升级肿瘤12项筛查体检套 餐","setmeal_count":2},
              {"proportion":0.1818,"name":"珍爱高端升级肿瘤12项筛 查","setmeal_count":2},
              {"proportion":0.0909,"name":"孕前检查套餐","setmeal_count":1}
              ], },
              "flag":true,
              "message":"获取运营统计数据成功" }
         */
        Map<String,Object> map = new HashMap<>();
        String today = DateUtils.parseDate2String(DateUtils.getToday());//当前日期
        String month = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//本月的第一天
        String finallyMonth= DateUtils.parseDate2String(DateUtils.getFinallyDay4ThisMonth());//本月的最后天
        String week = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//日期的本周一是几号
        String Sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());//日期的本周周末是几号

        map.put("reportDate",today);//当前日期
       Integer todayVisitsNumber = orderDao.todayVisitsNumber(today);//今日已到诊数
        map.put("todayVisitsNumber",todayVisitsNumber);
        Integer thisWeekVisitsNumber = orderDao.thisWeekVisitsNumber(week);//查询本周已到诊人数
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        Integer thisMonthVisitsNumber = orderDao.thisMonthVisitsNumber(month);//查询本月已到诊人数
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        Map monthMap = new HashMap();
        monthMap.put("month",month);
        monthMap.put("finallyMonth",finallyMonth);
        Integer thisMonthOrderNumber = orderDao.thisMonthOrderNumber(monthMap);//查询本月预约人数
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        monthMap.put("week",week);
        monthMap.put("Sunday",Sunday);
        Integer thisWeekOrderNumber = orderDao.thisWeekOrderNumber(monthMap);//查询本周预约人数
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        Integer todayOrderNumber = orderDao.todayOrderNumber(today);//查询今日已预约人数
        map.put("todayOrderNumber",todayOrderNumber);
        Integer totalMember = memberDao.totalMember();//总会员数
        map.put("totalMember",totalMember);
        Integer todayNewMember = memberDao.todayNewMember(today);//今日新增会员数数
        map.put("todayNewMember",todayNewMember);
        Integer thisWeekNewMember = memberDao.thisWeekNewMember(week);//本周新增会员数数
        map.put("thisWeekNewMember",thisWeekNewMember);
       Integer thisMonthNewMember = memberDao.thisMonthNewMember(month);//本月新增会员数数
       map.put("thisMonthNewMember",thisMonthNewMember);
        List<Map> hotSetmeal= new ArrayList<>(4);
        List<Integer> ids = orderDao.findMax();//预约最多的前四个套餐id
        Integer count = orderDao.findCount();//总的套餐预约数
        for (Integer id : ids) {
            Setmeal one = setmealDao.findOne(id);
            Map  map1 = new HashMap();
            String name = one.getName();
            map1.put("name",name);
            Integer countByName = setmealDao.findCountByName(name);
            map1.put("setmeal_count",countByName);
            float size = (float)countByName/count;
            DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
            String filesize = df.format(size);//返回的是String类型的
            map1.put("proportion",filesize);
            hotSetmeal.add(map1);
        }
        map.put("hotSetmeal",hotSetmeal);
        return new Result(true,GET_BUSINESS_REPORT_SUCCESS,map);
    }
}
