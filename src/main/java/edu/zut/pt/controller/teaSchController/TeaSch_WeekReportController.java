package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.pojo.*;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TeaSchWeekReportService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TeaSch_WeekReportController {

    DownloadFile downloadFile = new DownloadFile();

    @GetMapping("tea_school_weekReportReview")
    public String tea_school_weekReview(HttpSession httpSession, HttpServletRequest httpServletRequest, Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int weekMessage = 1;
        httpSession.setAttribute("week",weekMessage);
        getSchTeaWeekReportPageInfo(tno,weekMessage,httpSession,model);
        return "tea_school/tea_school_weekReportReview";
    }

    //根据下拉选项选择的周期信息获取这一周学生的周报信息
    @RequestMapping(value="/teaSchStuWeekSelectByWM",method = RequestMethod.POST)
    public String teaSchStuWeekSelectByWM(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        int weekMessage = Integer.valueOf(httpServletRequest.getParameter("weekMessage"));
        System.out.println("获取到的下拉框中的周期信息为："+weekMessage);
        httpSession.setAttribute("week",weekMessage);
        getSchTeaWeekReportPageInfo(tno,weekMessage,httpSession,model);
        return "tea_school/tea_school_weekReportReview";
    }

    //根据周期信息文本框中的值，更新学生的成绩信息
    @RequestMapping(value="/teaSchStuWeekInsertScore/{weekMessage2},{weekSno},{weekReview},{isSubmit},{score}",method = RequestMethod.POST)
//    /{weekMessage2}
    public String teaSchWeekInsertScore(
           @PathVariable("weekMessage2") String weekMessage2,
                                      @PathVariable("weekSno") String sno,
                                      @PathVariable("weekReview") String weekReview,
                                      @PathVariable("isSubmit") String isSubmit,
                                      @PathVariable("score") String weekScore,
                                      HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        System.out.println("判断已经进入到插入成绩的方法");
        System.out.println("周信息为："+weekMessage2);
        int weekMessage = Integer.valueOf(weekMessage2);
        String weekSno1 = sno;

        System.out.println("获取到的周期信息文本框中的周期信息为："+weekMessage);
//        String weekSno1 = httpServletRequest.getParameter("weekSno");
//        String weekReview = httpServletRequest.getParameter("weekReview");
//        String isSubmit = httpServletRequest.getParameter("isSubmit");
        System.out.println("判断是否进入到if方法"+weekSno1+weekReview+isSubmit);
        if("提交".equals(isSubmit)){
//            int score = Integer.valueOf(httpServletRequest.getParameter("weekScore"));
            int score = Integer.valueOf(weekScore);
            System.out.println("判断已经进入到第一重if方法");
            if(findBySno(weekSno1,weekMessage)!=null){
                System.out.println(weekSno1+weekMessage+score+weekReview);
                updateWeekScore(weekSno1,weekMessage,score,weekReview);
                //创建获取实训成绩的对象
                CountScore countScore = new CountScore();
                //获取平时成绩、中期成绩、实训报告成绩、实训成绩
                Map<String,Object> scoreMap = countScore.countAllScore(weekSno1);
                int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
                int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
                int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
                int ptScore = Integer.valueOf(scoreMap.get("score").toString());
                //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
                if(selectStuScoreInSubmitWeekScore(weekSno1)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                    insertStuScoreInfoInSubmitWeekScore(weekSno1,dailyScore,middleScore,finalScore,ptScore);
                }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                    updateStuDailyScoreInSubmitWeekScore(weekSno1,dailyScore,ptScore);
                }
            }else{
                System.out.println("没有该生的第"+weekMessage+"周周报信息");
            }
        }else{
            System.out.println("暂时无法提交该生的成绩！");
        }
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getSchTeaWeekReportPageInfo(tno,weekMessage,httpSession,model);
        httpSession.setAttribute("week",weekMessage);
        System.out.println("拿到了页面的数据");
        return "tea_school/tea_school_weekReportReview";
    }

    //如果该学生上传了文件，就可以下载；否则提示，该生未上传文件。周期信息从weekReportFileSchTeaWeekMessage中获取
    @RequestMapping(value="/weekFileReportSchTeaDownload",method = RequestMethod.POST)
    public String weekFileReportSchTeaDownload(HttpServletRequest httpServletRequest, HttpSession httpSession,
                                               Model model, HttpServletResponse httpServletResponse){

        int weekMessage = Integer.valueOf(httpServletRequest.getParameter("weekReportMessage"));

        System.out.println("进入下载方法中，获取到的table中的周期信息为："+weekMessage);

        //先判断报告路径：如果是“未提交文件”，就提示该生未提交文件；否则，可以下载
        String weekFilePath = httpServletRequest.getParameter("weekReportSchTeaFilePath");
        System.out.println("周报路径为："+weekFilePath);
        if("未提交文件".equals(weekFilePath)){
            System.out.println("该生未提交周报文件");
        }else{
            downloadFile.download(weekFilePath,httpServletResponse);
        }
        String tno = httpServletRequest.getSession().getAttribute("login").toString();

        httpSession.setAttribute("week",weekMessage);
        getSchTeaWeekReportPageInfo(tno,weekMessage,httpSession,model);
        return "tea_school/tea_school_weekReportReview";
    }

    //获取学生周报页面信息
    public void getSchTeaWeekReportPageInfo(String tno,int weekMessage,HttpSession httpSession ,Model model){
        List<StudentInfo> list = findByTno(tno,"校外自行联系");
        List<WeekReport> reportList = getStuWeekReport(list,weekMessage);
        int weekNum = findWeekReportNum(1).getWeekNums();
        List<Week> weekList = new ArrayList<>();
        for(int i = 0;i<weekNum;i++){
            Week week = new Week();
            week.setId(i+1);
            week.setWeekMessage(i+1);
            week.setWeekValue(i+1);
            weekList.add(week);
        }
        httpSession.setAttribute("teaSchStuWeekNum",list.size());
        model.addAttribute("weekReportList",reportList);
        model.addAttribute("weekList",weekList);
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
                weekReport.setContent_weekReport("");
                weekReport.setWeekReportFilePath("未提交文件");
                weekReport.setWeekMessage(weekMessage);
            }else{
                weekReport = findBySno(list.get(i).getSno(),weekMessage);
            }
            weekList.add(weekReport);
        }
        return weekList;
    }

    @Autowired
    TeaSchWeekReportService teaSchWeekReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuScoreService stuScoreService;

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

    @RequestMapping("/findWeekReportNum/{id}")
    public TimeMessage findWeekReportNum(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }


    @GetMapping("/insertStuScoreInfoInSubmitWeekScore/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitWeekScore(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @GetMapping("/updateStuDailyScoreInSubmitWeekScore/{sno},{dailyScore},{score}")
    public int updateStuDailyScoreInSubmitWeekScore(@PathVariable("sno") String sno,
                                                      @PathVariable("dailyScore") int dailyScore,
                                                      @PathVariable("score") int score){
        return stuScoreService.updateStuDailyScore(sno,dailyScore,score);

    }

    @GetMapping("/selectStuScoreInSubmitWeekScore/{sno}")
    public StudentScore selectStuScoreInSubmitWeekScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
