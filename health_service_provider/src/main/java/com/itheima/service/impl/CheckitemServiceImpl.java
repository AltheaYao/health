package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckitemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass=CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {
    @Autowired
    private CheckitemDao checkitemDao;

    /*添加*/
    @Override
    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
    }

    /*分页查询*/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        /*每页显示条数*/
        Integer pageSize = queryPageBean.getPageSize();
        /*查询条件*/
        String queryString = queryPageBean.getQueryString();
        long total = 1;
        List<CheckItem> result = new ArrayList<>(0);
        /*因为不是模糊查询,所有当有查询条件的时候肯定是只有一条数据*/
        if(queryString!=null&&queryString.length()>0){
            /*表示有查询条件,当有查询条件的时候我们根据查询条件直接去后台查询*/
            CheckItem checkItem = checkitemDao.findString(queryString);
            result.add(checkItem);
        }else {
            /*表示没有查询条件,正常查询*/
            /*获取当前页码*/
            Integer currentPage = queryPageBean.getCurrentPage();
            /*page组件*/
            PageHelper.startPage(currentPage,pageSize);
            Page<CheckItem> page = checkitemDao.findPage();
            total = page.getTotal();
             result = page.getResult();

        }
        return new PageResult(total,result);
    }

    /*根据id删除*/
    @Override
    public void deleteById(int id) {
    checkitemDao.deleteById(id);
    }

    /*根据id查询*/
    @Override
    public CheckItem findById(Integer id) {
        return checkitemDao.findById(id);
    }

    /*修改*/
    @Override
    public void edit(CheckItem checkItem) {
        checkitemDao.edit(checkItem);
    }

    /*查询所有*/
    @Override
    public List<CheckItem> findAll() {
        return checkitemDao.findAll();

    }


}
