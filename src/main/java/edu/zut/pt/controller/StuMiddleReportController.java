package edu.zut.pt.controller;

import edu.zut.pt.mapper.MiddleReportMapper;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.service.MiddleReportService;
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
import java.util.Date;
import java.util.List;

@Controller
public class StuMiddleReportController {

    @GetMapping("stu_midReport")
    public String middle(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        List<MiddleReport> list = selectMiddleReportBySno(sno);
        model.addAttribute("resultList",list);
        httpSession.setAttribute("middleNum",list.size());
        return "stu/stu_midReport";
    }

    @RequestMapping(value="/submitMiddleReport",method = RequestMethod.POST)
//    @ResponseBody
    public String  doMiddleSubmitWR(WebRequest webRequest, HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){

        String middleIsAfter = httpServletRequest.getParameter("isAfter");
        String middleFlag;
        if(middleIsAfter==null){
            middleFlag = "middleFalse";
        }else{
            middleFlag = "middleTrue";
        }
        String middleSno = httpServletRequest.getSession().getAttribute("login").toString();
        String middleContent = webRequest.getParameter("middleReportContent");
        Date middleDate = new Date();
        SimpleDateFormat middleDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String middleTime = middleDf.format(middleDate).toString();

        MiddleReport middleReport = new MiddleReport();
        middleReport.setMiddleSno(middleSno);
        middleReport.setContent_midReport(middleContent);
        middleReport.setTime_submit(middleTime);
        middleReport.setIsAfter(middleFlag);
        insertMiddleReport(middleReport);

        List<MiddleReport> list = selectMiddleReportBySno(middleSno);
        httpSession.setAttribute("middleNum",list.size());
        model.addAttribute("resultList",list);
        return "stu/stu_midReport";
    }

    @Autowired
    MiddleReportService middleReportService;

    @GetMapping("/insertMiddleReport")
    public int insertMiddleReport(MiddleReport middleReport){
        return middleReportService.insertMiddleReport(middleReport);
    }

    @GetMapping("/selectMiddleReportBySno/{middleSno}")
    public List<MiddleReport> selectMiddleReportBySno(@PathVariable("middleSno") String middleSno){
        return middleReportService.findMiddleReportBySno(middleSno);
    }

    @GetMapping("/selectMiddleReportBySWM/{middleSno}")
    public MiddleReport selectMiddleReportBySWM(@PathVariable("middleSno") String middleSno){
        return middleReportService.findMiddleReportByMidSWM(middleSno);
    }
}
