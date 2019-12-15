package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;

import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itheima.pojo.RedisConstant.SETMEAL_PIC_DB_RESOURCES;


@Service(interfaceClass= SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    String lodimgName;

   //分页数据
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();//每页显示条数
        String queryString = queryPageBean.getQueryString();//查询条件
        Page<Setmeal> pages;//定义一个page对象
        if (queryString!=null&&queryString.length()>0){//判断查询条件是否为空
            PageHelper.startPage(1,pageSize);//因为是绝对查询,所以肯定只有一条数据
             pages = setmealDao.findByString(queryString);//根据条件查询
        }else {
            /*条件为空*/
            PageHelper.startPage(queryPageBean.getCurrentPage(),pageSize);
             pages = setmealDao.findPage();
        }
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    //添加套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //先添加套餐
        setmealDao.add(setmeal);
        setSetmealAndCheckgroup(setmeal.getId(),checkgroupIds);
        setredis(setmeal.getImg());
    }

    @Override
    public Setmeal findOne(Integer setmealId) {
        return setmealDao.findOne(setmealId);

    }

    @Override
    public List<Integer> findById(Integer setmealId) {

        return setmealDao.findById(setmealId);
    }
    //修改套餐
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        Setmeal one = findOne(setmeal.getId());
        String img = one.getImg();
        deletredis(img);
        //修改套餐先修修改套餐表
        setmealDao.edit(setmeal);
        //在redis中加入新的img
        setredis(setmeal.getImg());
        //在删除中间表关联
        deleteSetmealAndCheckgroup(setmeal.getId());
        //在添加
        setSetmealAndCheckgroup(setmeal.getId(),checkgroupIds);

    }

    @Override
    public void delete(Integer id) {
        //先删除中间表关联数据
        deleteSetmealAndCheckgroup(id);
        //在删除套餐表
        setmealDao.delete(id);

    }
    //查询所有套餐信息
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findSetmealById(Integer id) {

            Setmeal one = setmealDao.findOne(id);
            List<CheckGroup> checkgroupById = setmealDao.findCheckgroupById(id);
            if(checkgroupById!=null) {
                for (CheckGroup checkGroup : checkgroupById) {
                    List<CheckItem> checkitemById = setmealDao.findCheckitemById(checkGroup.getId());
                    checkGroup.setCheckItems(checkitemById);
                }
            }
            one.setCheckGroups(checkgroupById);


        return one;
    }

    @Override
    public List<String> findName() {
      List<String> names =  setmealDao.findName();
        return names;
    }

    @Override
    public List findCountByName(List<String> setmealNames) {
        if (setmealNames==null){
            return null;
        }
        List<Map> countList = new ArrayList<>();

        for (String setmealName : setmealNames) {
          Integer count=  setmealDao.findCountByName(setmealName);
            Map map = new HashMap(2);
            map.put("name",setmealName);
            map.put("value",count);
          countList.add(map);
        }

        return countList;
    }

    //多对多关系,添加中间表数据
    public void setSetmealAndCheckgroup(Integer setmealId,Integer[] checkgroupIds){
        Map<String,Integer> map = new HashMap<>();
         map.put("setmealId",setmealId);
    for (Integer checkgroupId : checkgroupIds) {
        map.put("checkgroupId",checkgroupId);
        setmealDao.setSetmealAndCheckgroup(map);
    }

}
    public void deleteSetmealAndCheckgroup(Integer semealId){
        setmealDao.deleteSetmealAndCheckgroup(semealId);
    }

    //将添加数据成功的img添加到redis,方便后面清除垃圾照片
    public void setredis(String fileName){
        if (fileName != null && fileName.length() > 0) {
            jedisPool.getResource().sadd(SETMEAL_PIC_DB_RESOURCES, fileName);
        }
}
    //将修改数据,也要讲就的img名称从redis删除
    public void deletredis(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            jedisPool.getResource().srem(SETMEAL_PIC_DB_RESOURCES, fileName);
        }
    }

}
