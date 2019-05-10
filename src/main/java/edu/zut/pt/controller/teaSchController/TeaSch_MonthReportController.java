package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.mapper.TeaSchMonthReportMapper;
import edu.zut.pt.mapper.TeaSchWeekReportMapper;
import edu.zut.pt.pojo.*;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TeaSchMonthReportService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class TeaSch_MonthReportController {

    @GetMapping("tea_school_monthReportReview")
    public String tea_school_monthReview(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int monthMessage = 1;
        httpSession.setAttribute("month",monthMessage);
        getSchTeaMonthReportPageInfo(tno,httpSession,model,monthMessage);
        return "tea_school/tea_school_monthReportReview";
    }

    //根据下拉框中选中的值作为月期信息值，并根据此值获取这个月的学生的报告信息
    @RequestMapping(value="/teaSchStuMonthSelectByWM",method = RequestMethod.POST)
    public String teaSchStuWeekSelectByWM(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int monthMessage = Integer.valueOf(httpServletRequest.getParameter("monthMessage"));
        httpSession.setAttribute("month",monthMessage);
        getSchTeaMonthReportPageInfo(tno,httpSession,model,monthMessage);
        return "tea_school/tea_school_monthReportReview";
    }

    //根据月期信息文本框中的值更新该生该月的成绩信息
    @RequestMapping(value="/teaSchStuMonthInsertScore/{monthMessage2},{sno},{review},{isSubmit},{score}",method = RequestMethod.POST)
    public String teaSchMonthInsertScore(@PathVariable("monthMessage2") String monthMessage2,
                                         @PathVariable("sno") String sno,
                                         @PathVariable("review") String review,
                                         @PathVariable("isSubmit") String isSubmit,
                                         @PathVariable("score") String monthScore,
                                         HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        int monthMessage = Integer.valueOf(monthMessage2);
//        String monthSno1 = httpServletRequest.getParameter("monthSno");
//        String monthReview = httpServletRequest.getParameter("monthReview");
//        String isSubmit = httpServletRequest.getParameter("isSubmit");
        if("提交".equals(isSubmit)){
            int score = Integer.valueOf(monthScore);
            if(findBySno(sno,monthMessage)!=null){
                System.out.println(sno+monthMessage+score+review);
                updateMonthScore(sno,monthMessage,score,review);
                //创建获取实训成绩的对象
                CountScore countScore = new CountScore();
                //获取平时成绩、中期成绩、实训报告成绩、实训成绩
                Map<String,Object> scoreMap = countScore.countAllScore(sno);
                int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
                int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
                int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
                int ptScore = Integer.valueOf(scoreMap.get("score").toString());
                //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
                if(selectStuScoreInSubmitMonthScore(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                    insertStuScoreInfoInSubmitMonthScore(sno,dailyScore,middleScore,finalScore,ptScore);
                }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                    updateStuDailyScoreInSubmitMonthScore(sno,dailyScore,ptScore);
                }
            }else{
                System.out.println("未找到该生的第"+monthMessage+"月月报信息");
            }
        }else{
            System.out.println("无法进行月报信息成绩插入操作！");
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getSchTeaMonthReportPageInfo(tno,httpSession,model,monthMessage);
        httpSession.setAttribute("month",monthMessage);
        System.out.println("获取到页面的信息");
        return "tea_school/tea_school_monthReportReview";
    }

    DownloadFile downloadFile = new DownloadFile();

    //下载文件：如果该生提交文件，就下载；否则，提醒该生未提交文件。以monthReportFileSchTeaMonthMessage中的值作为月期信息的值
    @RequestMapping(value = "/monthFileReportSchTeaDownload",method = RequestMethod.POST)
    public String monthFileReportSchTeaDownload(HttpServletRequest httpServletRequest, HttpSession httpSession,
                                               Model model, HttpServletResponse httpServletResponse){
        String monthSno = httpServletRequest.getParameter("monthReportFileSchTeaSno");
        System.out.println("该生学号为："+monthSno);
        String monthFilePath = httpServletRequest.getParameter("monthReportSchTeaFilePath");
        System.out.println("月报路径为："+monthFilePath);
        if("未提交文件".equals(monthFilePath)){
            System.out.println("该生未提交月报文件");
        }else{
            downloadFile.download(monthFilePath,httpServletResponse);
        }

        int monthMessage = Integer.valueOf(httpServletRequest.getParameter("monthReportMessage"));
        String tno = httpServletRequest.getSession().getAttribute("login").toString();

        httpSession.setAttribute("month",monthMessage);
        getSchTeaMonthReportPageInfo(tno,httpSession,model,monthMessage);
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
                monthReport.setContent_monthReport("");
                monthReport.setMonthReportFilePath("未提交文件");
                monthReport.setMonthMessage(monthMessage);
            }else{
                monthReport = findBySno(list.get(i).getSno(),monthMessage);
            }
            monthList.add(monthReport);
        }
        return monthList;
    }

    //获取月报页面信息
    public void getSchTeaMonthReportPageInfo(String tno ,HttpSession httpSession,Model model,int monthMessage){
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<MonthReport> reportList = getStuMonthReport(list,monthMessage);
        int monthNum = findMonthReportNum(1).getMonthNums();
        List<Month> monthList = new ArrayList<>();
        for(int i = 0;i<monthNum;i++){
            Month month = new Month();
            month.setId(i+1);
            month.setMonthMessage(i+1);
            month.setMonthValue(i+1);
            monthList.add(month);
        }

        model.addAttribute("monthList",monthList);
        httpSession.setAttribute("teaSchStuMonthNum",list.size());
        model.addAttribute("monthReportList",reportList);
    }

    @Autowired
    TeaSchMonthReportService teaSchMonthReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuScoreService stuScoreService;

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

    @RequestMapping("/findMonthReportNum/{id}")
    public TimeMessage findMonthReportNum(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }
//
//    @RequestMapping("/findWeekReportNum/{id}")
//    public TimeMessage findWeekReportNum(@PathVariable("id") int id){
//        return timeMessageService.getTimeMessage(id);
//    }


    @GetMapping("/insertStuScoreInfoInSubmitMonthScore/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitMonthScore(@PathVariable("sno") String sno,
                                                   @PathVariable("dailyScore") int dailyScore,
                                                   @PathVariable("middleScore") int middleScore,
                                                   @PathVariable("ptScore") int ptScore,
                                                   @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @GetMapping("/updateStuDailyScoreInSubmitMonthScore/{sno},{dailyScore},{score}")
    public int updateStuDailyScoreInSubmitMonthScore(@PathVariable("sno") String sno,
                                                    @PathVariable("dailyScore") int dailyScore,
                                                    @PathVariable("score") int score){
        return stuScoreService.updateStuDailyScore(sno,dailyScore,score);

    }

    @GetMapping("/selectStuScoreInSubmitMonthScore/{sno}")
    public StudentScore selectStuScoreInSubmitMonthScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }


}
