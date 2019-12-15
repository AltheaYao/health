package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/checkitem")
public class CheckitemController {
    @Reference
    private CheckitemService checkitemService;

    /*添加checkitem*/
    @RequestMapping("/add.do")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){

        try {
            checkitemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,ADD_CHECKITEM_FAIL);
        }
        return new Result(true,ADD_CHECKITEM_SUCCESS);
    }

    /*分页查询数据*/
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return   checkitemService.findPage(queryPageBean);

    }

    /*根据提供的id删除数据*/
    @RequestMapping("deleteById.do")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result deleteById(int id){

        try {
            checkitemService.deleteById(id);
            return new Result(true,DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,DELETE_CHECKITEM_FAIL);
        }

    }

    /*根据提供的id查询数据*/
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            CheckItem byId = checkitemService.findById(id);
            return new Result(true,QUERY_CHECKITEM_SUCCESS,byId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_CHECKITEM_FAIL);
        }
    }

    /*修改数据*/
    @RequestMapping("/edit.do")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkitemService.edit(checkItem);
            return new Result(true,EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,EDIT_CHECKITEM_FAIL);
        }
    }

    /*查询所有检查项*/
    @RequestMapping("/findAll.do")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAll(){
        List<CheckItem> all = null;
        try {
            all = checkitemService.findAll();
            return new Result(true,QUERY_CHECKITEM_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_CHECKITEM_FAIL);
        }

    }
}
