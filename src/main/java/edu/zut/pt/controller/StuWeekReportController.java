package edu.zut.pt.controller;


import edu.zut.pt.pojo.Week;
import edu.zut.pt.pojo.WeekReport;
import edu.zut.pt.service.WeekReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
//@RestController
public class StuWeekReportController {

    @GetMapping("stu_weekReport")
//    @PostMapping(value = "/stu_weekReport")
    public String week(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        List<WeekReport> list = selectWeekReportBySno(sno);
        httpSession.setAttribute("weekNum",list.size());
        model.addAttribute("resultList",list);
        List<Week> week = new ArrayList<>();
        for(int i = 0;i<12;i++){
            Week week1 = new Week();
            week1.setId(i+1);
            week1.setWeekMessage(i+1);
            week1.setWeekValue(i+1);
            week.add(week1);
        }
//        week.remove(0);
        for(int i = 0;i<list.size();i++){
            int m = list.get(i).getWeekMessage()-1;
            System.out.println(m);
//            week.remove(m);
//            week.remove(list.get(i).getWeekMessage()-1);
        }

        for(int i = 0;i<week.size();i++){
            System.out.print(week.get(i).getId());
        }

        model.addAttribute("weekList",week);
        return "stu/stu_weekReport";
    }

    @RequestMapping(value="/submitWeekReport",method = RequestMethod.POST)
//    @ResponseBody
    public String  doWeekSubmitWR(WebRequest webRequest, HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){
        Integer weekMessage = Integer.valueOf(httpServletRequest.getParameter("weekMessage"));
        String weekIsAfter = httpServletRequest.getParameter("isAfter");
        String weekFlag;
        if(weekIsAfter==null){
            weekFlag = "weekFalse";
        }else{
            weekFlag = "weekTrue";
        }
        String weekSno = httpServletRequest.getSession().getAttribute("login").toString();
        String weekContent = webRequest.getParameter("weekReportContent");
        Date weekDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String weekTime = df.format(weekDate).toString();

        WeekReport weekReport = new WeekReport();
        weekReport.setWeekSno(weekSno);
        weekReport.setContent_weekReport(weekContent);
        weekReport.setWeekMessage(weekMessage);
        weekReport.setTime_submit(weekTime);
        weekReport.setIsAfter(weekFlag);
        insertWeekReport(weekReport);

        List<WeekReport> list = selectWeekReportBySno(weekSno);
        httpSession.setAttribute("weekNum",list.size());
        model.addAttribute("resultList",list);
        return "stu/stu_weekReport";
    }

    @Autowired
    WeekReportService weekReportService;

    @GetMapping("/insertWeekReport")
    public int insertWeekReport(WeekReport weekReport){

        return weekReportService.insertWeekReport(weekReport);
    }

    @GetMapping("/selectWeekReportBySno/{weekSno}")
    public List<WeekReport> selectWeekReportBySno(@PathVariable("weekSno") String weekSno){
        return weekReportService.findWeekReportBySno(weekSno);
    }

    @GetMapping("/selectWeekReportBySWM/{weekSno},{weekMessage}")
    public WeekReport selectWeekReportBySWM(@PathVariable("weekSno") String weekSno,
                                            @PathVariable("weekMessage") String weekMessage){
        return weekReportService.findWeekReportByweekSWM(weekSno,weekMessage);
    }

}
