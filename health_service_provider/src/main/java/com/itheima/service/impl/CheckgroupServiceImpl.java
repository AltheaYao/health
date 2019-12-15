package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass= CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {
    @Autowired
    private CheckgroupDao checkgroupDao;

    //添加检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        /*这个需要操作两张表,先操作checkgroup表的添加*/
       checkgroupDao.addCheckGroup(checkGroup);
        /*调用添加中间表数据方法*/
        setCheckgtoupAndCheckitem(checkGroup.getId(),checkitemIds);

    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        Page<CheckGroup> pages;
        if (queryString!=null&&queryString.length()>0){
            PageHelper.startPage(1,pageSize);
             pages = checkgroupDao.findByString(queryString);
        }else {
            /*条件为空*/

            PageHelper.startPage(queryPageBean.getCurrentPage(),pageSize);
             pages = checkgroupDao.findPage();
        }
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    //删除检查组
    @Override
    public void delete(Integer id) {
        /*通过id先去删除中间表不然会删除不成功*/
        checkgroupDao.delCheckgroupAndCheckitem(id);
        /*通过id在去删除和套餐的中间表*/
        delCheckgroupAndCheckitem(id);
        /*在去通过id删除checkgroup表*/
        checkgroupDao.delCheckgroup(id);
    }

    /*根据id查询检查组*/
    @Override
    public CheckGroup findOne(Integer id) {
        return checkgroupDao.findOne(id);

    }

    //根据id查询检查组关联的检查项id
    @Override
    public List<Integer> findById(Integer id) {
       return checkgroupDao.findById(id);

    }

    //修改
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        /*编辑检查组先添加到检查组表*/
        checkgroupDao.updataCheckGroup(checkGroup);
        /*在删除中间表的关联*/
        delCheckgroupAndCheckitem(checkGroup.getId());
        /*在添加中间表的关联*/
        setCheckgtoupAndCheckitem(checkGroup.getId(),checkitemIds);
    }

    //查询所有
    @Override
    public List<CheckGroup> findAll() {
        return checkgroupDao.findAll();

    }

    //添加中间表数据
    public void setCheckgtoupAndCheckitem(Integer checkGroupId, Integer[] checkitemIds ){
        Map<String,Integer> map = new HashMap<>();
        for (Integer checkitemId : checkitemIds) {
            map.put("checkGtoupId",checkGroupId);
            map.put("checkitemId",checkitemId);
            checkgroupDao.setCheckgtoupAndCheckitem(map);
        }
    }
    //删除中间表数据
    private void delCheckgroupAndCheckitem(Integer id){

        checkgroupDao.delCheckgroupAndCheckitem(id);
    }
}
