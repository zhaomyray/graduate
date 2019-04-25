package edu.zut.pt.controller;

import edu.zut.pt.mapper.TeaSchMonthReportMapper;
import edu.zut.pt.mapper.TeaSchWeekReportMapper;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;
import edu.zut.pt.service.TeaSchMonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class TeaSch_MonthReportController {

    @GetMapping("tea_school_monthReportReview")
    public String tea_school_monthReview(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
//        int weekMessage = 1;
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<MonthReport> reportList = getStuMonthReport(list,1);

        httpSession.setAttribute("teaSchStuMonthNum",list.size());
        model.addAttribute("monthReportList",reportList);

        return "tea_school/tea_school_monthReportReview";
    }

    @RequestMapping(value="/teaSchStuMonthSelectByWM",method = RequestMethod.POST)
    public String teaSchStuWeekSelectByWM(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int monthMessage = Integer.valueOf(httpServletRequest.getParameter("monthMessage"));
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<MonthReport> stuReportList = getStuMonthReport(list,monthMessage);
        httpSession.setAttribute("teaSchStuMonthNum",list.size());
        httpSession.setAttribute("month",monthMessage);
        System.out.println(monthMessage);
        model.addAttribute("monthReportList",stuReportList);
        return "tea_school/tea_school_monthReportReview";
    }

    @RequestMapping(value="/teaSchStuMonthInsertScore",method = RequestMethod.POST)
    public String teaSchMonthInsertScore(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){

        int monthMessage = Integer.valueOf(httpServletRequest.getParameter("monthMessage2"));
        String monthSno1 = httpServletRequest.getParameter("monthSno").replace("\"","");
        int score = Integer.valueOf(httpServletRequest.getParameter("monthScore"));
        String monthReview = httpServletRequest.getParameter("monthReview");
        if(!"".equals(monthMessage)&&!"".equals(monthSno1)&&!"".equals(score)){
            if("".equals(monthReview)){
                monthReview="";
            }
            System.out.println(monthSno1+monthMessage+score+monthReview);
            updateMonthScore(monthSno1,monthMessage,score,monthReview);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<MonthReport> stuReportList = getStuMonthReport(list,monthMessage);
        httpSession.setAttribute("teaSchStuMonthNum",list.size());
        httpSession.setAttribute("month",monthMessage);
        model.addAttribute("monthReportList",stuReportList);

        return "tea_school/tea_school_monthReportReview";
    }

    public List<MonthReport> getStuMonthReport(List<StudentInfo> list, int monthMessage){
        List<MonthReport> monthList = new ArrayList<MonthReport>();
        for(int i = 0;i<list.size();i++){
            MonthReport monthReport = new MonthReport();
            if(findBySno(list.get(i).getSno(),monthMessage)==null){
                monthReport.setMonthSno(list.get(i).getSno());
                monthReport.setTime_submit("未提交");
                monthReport.setIsAfter("--");
                monthReport.setScore(0);
            }else{
                monthReport = findBySno(list.get(i).getSno(),monthMessage);
            }
            monthList.add(monthReport);
        }
        return monthList;
    }




    @Autowired
    TeaSchMonthReportService teaSchMonthReportService;

    @RequestMapping("/selectMonthMyStu/{tno},{type}")
    public List<StudentInfo> findByTno(@PathVariable("tno") String tno,
                                       @PathVariable("type") String type){
        return teaSchMonthReportService.findStuByTno(tno,type);
    }

    @RequestMapping("/selectMyStuMonthReport/{sno},{monthMessage}")
    public MonthReport findBySno(@PathVariable("sno") String sno,
                                 @PathVariable("monthMessage") int monthMessage){
        return teaSchMonthReportService.findBySno(sno,monthMessage);
    }

    @RequestMapping("/updateStuMonthScore/{sno},{monthMessage},{score},{monthReview}")
    public int updateMonthScore(@PathVariable("sno") String sno,
                               @PathVariable("monthMessage") int weekMessage,
                               @PathVariable("score") int score,
                               @PathVariable("monthReview") String weekReview){
        return teaSchMonthReportService.updateMonthScoreBySno(sno,weekMessage,score,weekReview);
    }

}
