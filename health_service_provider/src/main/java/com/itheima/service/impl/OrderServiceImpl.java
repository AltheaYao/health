package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.MemberService;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Map;
import static com.itheima.constant.MessageConstant.*;
import static com.itheima.pojo.Order.ORDERSTATUS_NO;
import static com.itheima.pojo.Order.ORDERTYPE_WEIXIN;

@Service(interfaceClass= OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public Result addOrder(Map map) throws Exception{

        //检查用户选的日期是否进行了预约设置的
        String orderDatestr = (String) map.get("orderDate");//获取预约日期
        String telephone = (String) map.get("telephone");//获取手机号
        String name = (String) map.get("name");//姓名
        String sex = (String) map.get("sex");//性别
        String idCard = (String) map.get("idCard");//身份证号
        String setmealId = (String) map.get("setmealId");
        //转换成Date类型
   //     Date orderDate = new Date(orderDatestr);
        Date orderDate = DateUtils.parseString2Date(orderDatestr);
        //查询到预约
        OrderSetting orderSetting = orderDao.findOrderDate(orderDate);
        if (orderSetting==null){
            //表示该日期没有预约设置
            return new Result(false,SELECTED_DATE_CANNOT_ORDER);
        }
        int reservations = orderSetting.getReservations();//已预约人数
        int number = orderSetting.getNumber();//可预约人数
        if (number<reservations || number==reservations){
            return new Result(false,ORDER_FULL);
        }
        Order order =null;
                //查询是否同一用户预约同一时间同一个套餐 重复预约

        //查询是否是会员
        Member member = orderDao.findMemberByTel(telephone);
        if (member==null){
            //不是会员
            member=new Member();
            member.setName(name);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setSex(sex);
            member.setRegTime(new Date());
            //注册会员
            memberDao.add(member);
        }else {
            //不为空,表示存在会员
            Integer memberId = member.getId();//获取会员id
            order = new Order(memberId,orderDate,ORDERTYPE_WEIXIN,ORDERSTATUS_NO,Integer.valueOf(setmealId));
            Order orderByNameDateSetmeal = orderDao.findOrderByNameDateSetmeal(order);
            if (orderByNameDateSetmeal!=null){
                return new Result(false,HAS_ORDERED);//返回不可重复预约
            }
        }
        //是会员也没有重复预约,这时候就可以预约了
        Integer memberId = member.getId();//获取会员id
        order = new Order(memberId,orderDate,ORDERTYPE_WEIXIN,ORDERSTATUS_NO,Integer.valueOf(setmealId));
        orderDao.add(order);
        //给预约人数加1
        orderSetting.setNumber(orderSetting.getNumber()+1);
        orderSettingDao.edit(orderSetting);

        return new Result(true,ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) {

        //体检人,体检套餐,体检日期,预约类型
       Map map = orderDao.findById(id);
        return map;
    }
}
