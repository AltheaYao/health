package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {
    void add (Member member);

    Member findByTel(String telephone1);

    Integer findMemberCountByMonth(String m);
    //总会员数
    Integer totalMember();
    //今日新增会员数数
    Integer todayNewMember(String today);
    //本周新增会员数数
    Integer thisWeekNewMember(String week);
    //本月新增会员数数
    Integer thisMonthNewMember(String month);
}
