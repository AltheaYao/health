package com.itheima.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass= MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;//会员管理

    //根据手机号查询会员信息
    @Override
    public Member findByTel(String telephone1) {
        return memberDao.findByTel(telephone1);
    }

    //添加会员
    public  void add(Member member){
        memberDao.add(member);
    }

    //根据月查询会员数
    @Override
    public List<Integer> findMemberCountByMonth(List<String> monthsList) {
        if (monthsList==null){
            return null;
        }
        List<Integer> monthsCountList = new ArrayList<>();
        for (String s : monthsList) {
            String m = s+".31";
           Integer count =  memberDao.findMemberCountByMonth(m);
           monthsCountList.add(count);
        }
        return monthsCountList;
    }
}
