package edu.zut.pt.controller;

import edu.zut.pt.mapper.MiddleReportMapper;
import edu.zut.pt.mapper.TeaSchMiddleReportMapper;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.TeaSchMiddleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaSch_MiddleReportController {

    @GetMapping("tea_school_midReportReview")
    public String tea_school_middleReview(HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> stuInfoList = selectMiddleMyStu(tno);
        List<MiddleReport> middleReportList = getStuMiddleReport(stuInfoList);

        httpSession.setAttribute("teaSchStuMiddleNum",stuInfoList.size());
        model.addAttribute("middleReportList",middleReportList);

        return "tea_school/tea_school_midReportReview";
    }


    @RequestMapping(value="/teaSchStuMiddleInsertBySno",method = RequestMethod.POST)
    public String teaSchStuMiddleInsertBySno(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String middleSno1 = httpServletRequest.getParameter("middleSno").replace("\"","");
        int score = Integer.valueOf(httpServletRequest.getParameter("middleScore"));
        String middleReview = httpServletRequest.getParameter("middleReview");
        if(!"".equals(middleSno1)&&!"".equals(score)){
            if("".equals(middleReview)){
                middleReview="";
            }
            System.out.println(middleSno1+score+middleReview);
            insertMiddleScoreBySno(middleSno1,score,middleReview);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> list = selectMiddleMyStu(tno);
        List<MiddleReport> stuReportList = getStuMiddleReport(list);
        httpSession.setAttribute("teaSchStuMiddleNum",list.size());
        model.addAttribute("middleReportList",stuReportList);

        return "tea_school/tea_school_midReportReview";
    }

    public List<MiddleReport> getStuMiddleReport(List<StudentInfo> list){
        List<MiddleReport> middleList = new ArrayList<MiddleReport>();
        for(int i = 0;i<list.size();i++){
            MiddleReport middleReport = new MiddleReport();
            if(selectMiddleBySno(list.get(i).getSno())==null){
                middleReport.setMiddleSno(list.get(i).getSno());
                middleReport.setTime_submit("未提交");
                middleReport.setIsAfter("--");
                middleReport.setScore(0);
            }else{
                middleReport = selectMiddleBySno(list.get(i).getSno());
            }
            middleList.add(middleReport);
        }
        return middleList;
    }


    @Autowired
    TeaSchMiddleReportService teaSchMiddleReportService;

    @RequestMapping("/selectMiddleMyStu/{tno}")
    public List<StudentInfo> selectMiddleMyStu(@PathVariable("tno") String tno){
        return teaSchMiddleReportService.findStuByTno(tno);
    }

    @RequestMapping("/selectMiddleBySno/{sno}")
    public MiddleReport selectMiddleBySno(@PathVariable("sno") String sno){
        return teaSchMiddleReportService.findBySno(sno);
    }

    @RequestMapping("/insertMiddleScore/{sno},{score},{middleReview}")
    public int insertMiddleScoreBySno(@PathVariable("sno") String sno,
                                      @PathVariable("score") int score,
                                      @PathVariable("middleReview") String middleReview){
        return teaSchMiddleReportService.updateMiddleScoreBySno(sno,score,middleReview);
    }



}
