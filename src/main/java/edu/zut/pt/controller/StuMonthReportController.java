package edu.zut.pt.controller;

import edu.zut.pt.mapper.MonthReportMapper;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.service.MonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
public class StuMonthReportController {

    @GetMapping("stu_monthReport")
    public String month(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        List<MonthReport> list = selectMonthReportBySno(sno);
        model.addAttribute("resultList",list);
        httpSession.setAttribute("monthNum",list.size());
        return "/stu/stu_monthReport";
    }

    @RequestMapping(value="/submitMonthReport",method = RequestMethod.POST)
//    @ResponseBody
    public String  doMonthSubmitWR(WebRequest webRequest, HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){
        Integer monthMessage = Integer.valueOf(httpServletRequest.getParameter("monthMessage"));
        String monthIsAfter = httpServletRequest.getParameter("isAfter");
        String monthFlag;
        if(monthIsAfter==null){
            monthFlag = "monthFalse";
        }else{
            monthFlag = "monthTrue";
        }
        String monthSno = httpServletRequest.getSession().getAttribute("login").toString();
        String monthContent = webRequest.getParameter("monthReportContent");
        Date monthDate = new Date();
        SimpleDateFormat monthDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String monthTime = monthDf.format(monthDate).toString();

        MonthReport monthReport = new MonthReport();
        monthReport.setMonthSno(monthSno);
        monthReport.setContent_monthReport(monthContent);
        monthReport.setMonthMessage(monthMessage);
        monthReport.setTime_submit(monthTime);
        monthReport.setIsAfter(monthFlag);
        insertMonthReport(monthReport);

        List<MonthReport> list = selectMonthReportBySno(monthSno);
        httpSession.setAttribute("monthNum",list.size());

        model.addAttribute("resultList",list);
        return "/stu/stu_monthReport";
    }

    @Autowired
    MonthReportService monthReportService;

    @GetMapping("/insertMonthReport")
    public int insertMonthReport(MonthReport monthReport){
        return monthReportService.insertMonthReport(monthReport);
    }

    @GetMapping("/selectMonthReportBySno/{monthSno}")
    public List<MonthReport> selectMonthReportBySno(@PathVariable("monthSno") String monthSno){
        return monthReportService.findMonthReportBySno(monthSno);
    }

    @GetMapping("/selectMonthReportBySWM/{monthSno},{monthMessage}")
    public MonthReport selectMonthReportBySWM(@PathVariable("monthSno") String monthSno,
                                               @PathVariable("monthMessage") String monthMessage){
        return monthReportService.findMonthReportBymonthSWM(monthSno,monthMessage);
    }

}
