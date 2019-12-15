package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {
    @Reference
    private CheckgroupService checkgroupService;
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")//需要有添加权限才可以添加

    //添加检查组
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkgroupService.add(checkGroup,checkitemIds);
            return new Result(true,ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,ADD_CHECKGROUP_FAIL);
        }
    }

    /*分页查询*/
    @RequestMapping("/findPage.do")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkgroupService.findPage(queryPageBean);
    }

    //删除检查组
    @RequestMapping("/delete.do")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    public Result delete(Integer id){
        try {
            checkgroupService.delete(id);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,DELETE_CHECKGROUP_SUCCESS);
    }

    //查询检查组
    @RequestMapping("/findOne.do")
    public Result findOne(Integer id){
        CheckGroup one = null;
        try {
            one = checkgroupService.findOne(id);
            return new Result(true,QUERY_CHECKGROUP_SUCCESS,one);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_CHECKGROUP_FAIL,one);
        }
    }

    //查询检查组关联的检查项id展示前台页面
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        List<Integer> byId = checkgroupService.findById(id);
        return new Result(true,QUERY_CHECKGROUP_SUCCESS,byId);
    }

    //修改检查组
    @RequestMapping("/edit.do")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkgroupService.edit(checkGroup,checkitemIds);
            return new Result(true,EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,EDIT_CHECKGROUP_FAIL);
        }
    }

    //查询所有检查组
    @RequestMapping("/findAll.do")
    public Result findAll(){
        try {
            List<CheckGroup> all = checkgroupService.findAll();
            return new Result(true,QUERY_CHECKGROUP_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,QUERY_CHECKGROUP_FAIL);
        }
    }
}
