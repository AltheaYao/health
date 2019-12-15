package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.sun.tools.corba.se.idl.InterfaceGen;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.itheima.constant.MessageConstant.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    //统计会员
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        List<String> monthsList = new ArrayList<>();
        for (int i = 1; i <12 ; i++) {
            calendar.add(Calendar.MONTH,1);
            monthsList.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        Map map = new HashMap();
        map.put("months",monthsList);
        List<Integer> memberCountList = null;
        try {
            memberCountList = memberService.findMemberCountByMonth(monthsList);
            map.put("memberCount",memberCountList);
            return new Result(true,GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }

    //统计套餐
    @RequestMapping("/getSetmealReport")
    private Result getSetmealReport(){
        List<String> setmealNames = null;
        try {
            setmealNames = setmealService.findName();
            Map<String,List> map = new HashMap<>();
      map.put("setmealNames",setmealNames);
        List countByName = setmealService.findCountByName(setmealNames);
        map.put("setmealCount",countByName);
        return new Result(true,GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
        e.printStackTrace();
            return new Result(false,GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //统计各项数据
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
    try {
        Result result = reportService.getBusinessReportData();
        return result;
    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false,GET_BUSINESS_REPORT_FAIL);
    }

}

    //统计表格
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        Result businessReportData = null;
        try {
            businessReportData = reportService.getBusinessReportData();

        Map data = (Map) businessReportData.getData();
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
              ], },*/
        String reportDate = (String) data.get("reportDate");//当前日期
        Integer todayVisitsNumber = (Integer) data.get("todayVisitsNumber");//今日到诊数
        Integer thisWeekVisitsNumber = (Integer) data.get("thisWeekVisitsNumber");//本周到诊数
        Integer thisMonthVisitsNumber = (Integer) data.get("thisMonthVisitsNumber");//本月到诊数
        Integer thisMonthOrderNumber = (Integer) data.get("thisMonthOrderNumber");//本月预约数
        Integer thisWeekOrderNumber = (Integer) data.get("thisWeekOrderNumber");//本周预约数
        Integer todayOrderNumber = (Integer) data.get("todayOrderNumber");//今日预约数
        Integer totalMember = (Integer) data.get("totalMember");//总会员数
        Integer todayNewMember = (Integer) data.get("todayNewMember");//今日新增会员数
        Integer thisWeekNewMember = (Integer) data.get("thisWeekNewMember");//本周新增会员数
        Integer thisMonthNewMember = (Integer) data.get("thisMonthNewMember");//本月新增会员数
        List<Map> hotSetmeal = (List) data.get("hotSetmeal");//套餐
        //获取文件的相对路径
      String path = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
       //获取工作薄对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(path)));
        //获取工作表
            XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
            //填入参数
            sheetAt.getRow(2).getCell(5).setCellValue(reportDate);
            sheetAt.getRow(4).getCell(5).setCellValue(todayNewMember);
            sheetAt.getRow(4).getCell(7).setCellValue(totalMember);
            sheetAt.getRow(5).getCell(5).setCellValue(thisWeekNewMember);
            sheetAt.getRow(5).getCell(7).setCellValue(thisMonthNewMember);
            sheetAt.getRow(7).getCell(5).setCellValue(todayOrderNumber);
            sheetAt.getRow(7).getCell(7).setCellValue(todayVisitsNumber);
            sheetAt.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);
            sheetAt.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);
            sheetAt.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);
            sheetAt.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);
            int rowNumber = 12;
            for (Map o : hotSetmeal) {
                String name = (String) o.get("name");
                Integer setmeal_count = (Integer) o.get("setmeal_count");
                String proportion = (String) o.get("proportion");
                sheetAt.getRow(rowNumber).getCell(4).setCellValue(name);
                sheetAt.getRow(rowNumber).getCell(5).setCellValue(setmeal_count);
                sheetAt.getRow(rowNumber).getCell(6).setCellValue(proportion);
                rowNumber++;
            }
            //获取输出流
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms‐excel");
            response.setHeader("Content-Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
    }

}
