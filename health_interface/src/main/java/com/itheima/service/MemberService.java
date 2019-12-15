package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findByTel(String telephone1);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> monthsList);
}
