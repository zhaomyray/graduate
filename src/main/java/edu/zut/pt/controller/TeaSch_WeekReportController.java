package edu.zut.pt.controller;

import edu.zut.pt.mapper.TeaSchWeekReportMapper;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;
import edu.zut.pt.service.TeaSchWeekReportService;
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
public class TeaSch_WeekReportController {

    @GetMapping("tea_school_weekReportReview")
    public String tea_school_weekReview(HttpSession httpSession, HttpServletRequest httpServletRequest, Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
//        int weekMessage = 1;
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<WeekReport> reportList = getStuWeekReport(list,1);

        httpSession.setAttribute("teaSchStuWeekNum",list.size());
        model.addAttribute("weekReportList",reportList);
        return "tea_school/tea_school_weekReportReview";
    }

    @RequestMapping(value="/teaSchStuWeekSelectByWM",method = RequestMethod.POST)
    public String teaSchStuWeekSelectByWM(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int weekMessage = Integer.valueOf(httpServletRequest.getParameter("weekMessage"));
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<WeekReport> stuReportList = getStuWeekReport(list,weekMessage);
        httpSession.setAttribute("teaSchStuWeekNum",list.size());
        httpSession.setAttribute("week",weekMessage);
        System.out.println(weekMessage);
        model.addAttribute("weekReportList",stuReportList);

        return "tea_school/tea_school_weekReportReview";
    }

    @RequestMapping(value="/teaSchStuWeekInsertScore",method = RequestMethod.POST)
    public String teaSchWeekInsertScore(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){

        int weekMessage = Integer.valueOf(httpServletRequest.getParameter("weekMessage2"));
        String weekSno1 = httpServletRequest.getParameter("weekSno").replace("\"","");
        int score = Integer.valueOf(httpServletRequest.getParameter("weekScore"));
        String weekReview = httpServletRequest.getParameter("weekReview");
        if(!"".equals(weekMessage)&&!"".equals(weekSno1)&&!"".equals(score)&&!"".equals(weekReview)){

            System.out.println(weekSno1+weekMessage+score+weekReview);
            updateWeekScore(weekSno1,weekMessage,score,weekReview);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<WeekReport> stuReportList = getStuWeekReport(list,weekMessage);
        httpSession.setAttribute("teaSchStuWeekNum",list.size());
        httpSession.setAttribute("week",weekMessage);
        model.addAttribute("weekReportList",stuReportList);

        return "tea_school/tea_school_weekReportReview";
    }

    public List<WeekReport> getStuWeekReport(List<StudentInfo> list,int weekMessage){
        List<WeekReport> weekList = new ArrayList<WeekReport>();
        for(int i = 0;i<list.size();i++){
            WeekReport weekReport = new WeekReport();
            if(findBySno(list.get(i).getSno(),weekMessage)==null){
                weekReport.setWeekSno(list.get(i).getSno());
                weekReport.setTime_submit("未提交");
                weekReport.setIsAfter("--");
                weekReport.setScore(0);
            }else{
                weekReport = findBySno(list.get(i).getSno(),weekMessage);
            }
            weekList.add(weekReport);
        }
        return weekList;
    }

    @Autowired
    TeaSchWeekReportService teaSchWeekReportService;

    @RequestMapping("/selectMyStu/{tno},{type}")
    public List<StudentInfo> findByTno(@PathVariable("tno") String tno,
                                       @PathVariable("type") String type){
        return teaSchWeekReportService.findStuByTno(tno,type);
    }

    @RequestMapping("/selectMyStuWeekReport/{sno},{weekMessage}")
    public WeekReport findBySno(@PathVariable("sno") String sno,
                                @PathVariable("weekMessage") int weekMessage){
        return teaSchWeekReportService.findBySno(sno,weekMessage);
    }

    @RequestMapping("/updateStuWeekScore/{sno},{weekMessage},{score},{weekReview}")
    public int updateWeekScore(@PathVariable("sno") String sno,
                               @PathVariable("weekMessage") int weekMessage,
                               @PathVariable("score") int score,
                               @PathVariable("weekReview") String weekReview){
        return teaSchWeekReportService.updateScoreBySno(sno,weekMessage,score,weekReview);
    }

}
