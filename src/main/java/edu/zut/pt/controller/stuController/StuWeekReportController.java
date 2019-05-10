package edu.zut.pt.controller.stuController;
import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.UploadFile;
import edu.zut.pt.pojo.*;
import edu.zut.pt.service.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
//@RestController
public class StuWeekReportController {

    @GetMapping("stu_weekReport")
//    @PostMapping(value = "/stu_weekReport")
    public String week(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        getWeekPageInfo(sno,httpSession,model);
        return "stu/stu_weekReport";
    }

    //提交周报
    @RequestMapping(value="/submitWeekReport",method = RequestMethod.POST)
//    @ResponseBody
    public String  doWeekSubmitWR(@RequestParam("uploadWeekFile") MultipartFile uploadWeekFile,
                                  WebRequest webRequest, HttpServletRequest httpServletRequest,
                                  Model model, HttpSession httpSession){
        System.out.println("进入到提交周报的方法中");
        String weekFile = uploadWeekFile.getOriginalFilename();
        String weekMessageStr = httpServletRequest.getParameter("weekMessage");
        String weekSno = httpServletRequest.getSession().getAttribute("login").toString();
        TimeMessage timeMessage = findWeekTimeMessage(1);
        String weekContent = webRequest.getParameter("weekReportContent");
        System.out.println("weekContent:"+weekContent+"；weekFile:"+weekFile);
        int nullFlag ;

        if(weekContent.length()==0){
            if(weekFile.length()==0){
                nullFlag = 0;
            }else{
                nullFlag = 1;
            }
        }else{
            nullFlag = 1;
        }
        if("请选择".equals(weekMessageStr)==false && nullFlag==1){

            Integer weekMessage = Integer.valueOf(weekMessageStr);
            getAllTime(httpSession,timeMessage);
            String weekIsAfter = httpServletRequest.getParameter("weekBan");
            System.out.println("本次周报是补交吗？"+weekIsAfter);
            if("无法提交".equals(weekIsAfter)){
                System.out.println("无法提交！");
            }else {
                String weekFlag = "";
                if ("补交".equals(weekIsAfter)) {
                    weekFlag = "是";
                } else if ("正常提交".equals(weekIsAfter)) {
                    weekFlag = "否";
                }

                Date weekDate = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String weekTime = df.format(weekDate).toString();

                WeekReport weekReport = new WeekReport();
                weekReport.setWeekSno(weekSno);
                weekReport.setContent_weekReport(weekContent);
                weekReport.setWeekMessage(weekMessage);
                weekReport.setTime_submit(weekTime);
                weekReport.setIsAfter(weekFlag);
                weekReport.setWeekReview("");
                if(weekFile.length()==0){
                    weekReport.setWeekReportFilePath("未提交文件");
                }else {
                    String uploadWeekFilePath = upload(weekSno,weekMessage,uploadWeekFile).toString();
                    weekReport.setWeekReportFilePath(uploadWeekFilePath);
                }
                //向周报信息表中插入一条周报信息
                insertWeekReport(weekReport);
                CountScore countScore = new CountScore();
                Map<String,Object> scoreMap = countScore.countAllScore(weekSno);
                int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
                int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
                int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
                int score = Integer.valueOf(scoreMap.get("score").toString());
                //同时更新学生成绩表中的平时成绩和实训成绩
                if(selectStuScoreInSubmitWeekReport(weekSno)==null){
                    //实训成绩表中没有该生的成绩信息
                    //说明这是该生第一次提交周报，也是第一次提交报告，那么成绩必定都为0
                    insertStuScoreInfoInSubmitWeekReport(weekSno,dailyScore,middleScore,finalScore,score);
                }else{
                    //实训成绩表中有该生的成绩信息
                    //说明该生之前提交过报告，那么此时就需要先找到该生的每周的周报成绩、每月的月报成绩、中期报告成绩和实训报告成绩，并按比例对这些成绩做处理
                    //插入到成绩信息表中
                    updateStuDailyScoreInSubmitWeekReport(weekSno,dailyScore,score);
                }
            }
        }

        List<WeekReport> list = selectWeekReportBySno(weekSno);
        httpSession.setAttribute("weekNum",list.size());
        model.addAttribute("resultList",list);

        List<Week> week = getWeek(list,timeMessage);

        for(int i = 0;i<list.size();i++){
            if(list.get(i).getWeekReview()==null){
                list.get(i).setWeekReview("");
            }
        }
        int startBJ = findWeekTimeMessage(1).getStartBuJiao();
        httpSession.setAttribute("startBJ",startBJ);
        model.addAttribute("weekList",week);

        return "stu/stu_weekReport";
    }

    //下载报告
    @RequestMapping(value = "/weekFileReportDownload",method = RequestMethod.POST)
//    @ResponseBody
    public String  weekFileReportDownload(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
                                          HttpSession httpSession,Model model){
        String weekFilePath = httpServletRequest.getParameter("weekReportFilePath");
        DownloadFile downloadFile = new DownloadFile();
        String sno = httpServletRequest.getSession().getAttribute("login").toString();

        if ("未提交文件".equals(weekFilePath) == false) {
            downloadFile.download(weekFilePath,httpServletResponse);
        }else{
            System.out.println("未提交报告");
        }
        getWeekPageInfo(sno,httpSession,model);
        return "stu/stu_weekReport";
    }

    //得到页面信息
    public void getWeekPageInfo(String sno,HttpSession httpSession,Model model){
        List<WeekReport> list = selectWeekReportBySno(sno);
        httpSession.setAttribute("weekNum",list.size());
        model.addAttribute("resultList",list);
        TimeMessage timeMessage = findWeekTimeMessage(1);
        int startBJ = findWeekTimeMessage(1).getStartBuJiao();
        System.out.println("是否可以补交之前的周报："+startBJ);
        List<Week> week = getWeek(list,timeMessage);
        getAllTime(httpSession,timeMessage);

        model.addAttribute("weekList",week);
        httpSession.setAttribute("weekListSize",week.size());
        httpSession.setAttribute("startBJ",startBJ);
    }

    //上传报告
    public Object upload(String sno,int weekMessage,MultipartFile weekFile){
        System.out.print("进入上传方法！");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        //保存时的文件名dataName
        String dateName = "week"+weekMessage+"_"+sno+"_"+df.format(calendar.getTime())+weekFile.getOriginalFilename();
        String realPath = "F:/uploadWeekReportTest/";
        try{
            //调用上传文件的方法
            //上传周报
            UploadFile.uploadFile(weekFile.getBytes(),realPath,dateName);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return realPath+dateName;
    }

    //得到周信息
    public List<Week> getWeek(List<WeekReport> list, TimeMessage timeMessage){
        List<Week> week = new ArrayList<>();
        //应提交的周报次数
        int weekAll1 = timeMessage.getWeekNums();
        int flag = 0;
        for(int i = 1;i<=weekAll1;i++){
            Week week1 = new Week();
            for(int j = 0;j<list.size();j++){
                if(i == list.get(j).getWeekMessage()){
                    flag = 1;
                }
            }
            if(flag == 0){
                week1.setId(i);
                week1.setWeekMessage(i);
                week1.setWeekValue(i);
                week.add(week1);
            }
            flag = 0;
        }
        return week;
    }

    //计算所有周报提交及截止时间
    public void getAllTime(HttpSession httpSession,TimeMessage timeMessage){
        Date firstWeekBeginTime = timeMessage.getFirstWeekBeginTime();
        Date firstWeekEndTime = timeMessage.getFirstWeekEndTime();

        Date weekEndTime2 = new Date();
        weekEndTime2.setTime(firstWeekEndTime.getTime()+1000*60*60*24*7);
        Date weekEndTime3 = new Date();
        weekEndTime3.setTime(weekEndTime2.getTime()+1000*60*60*24*7);
        Date weekEndTime4 = new Date();
        weekEndTime4.setTime(weekEndTime3.getTime()+1000*60*60*24*7);
        Date weekEndTime5 = new Date();
        weekEndTime5.setTime(weekEndTime4.getTime()+1000*60*60*24*7);
        Date weekEndTime6 = new Date();
        weekEndTime6.setTime(weekEndTime5.getTime()+1000*60*60*24*7);
        Date weekEndTime7 = new Date();
        weekEndTime7.setTime(weekEndTime6.getTime()+1000*60*60*24*7);
        Date weekEndTime8 = new Date();
        weekEndTime8.setTime(weekEndTime7.getTime()+1000*60*60*24*7);
        Date weekEndTime9 = new Date();
        weekEndTime9.setTime(weekEndTime8.getTime()+1000*60*60*24*7);
        Date weekEndTime10 = new Date();
        weekEndTime10.setTime(weekEndTime9.getTime()+1000*60*60*24*7);
        Date weekEndTime11 = new Date();
        weekEndTime11.setTime(weekEndTime10.getTime()+1000*60*60*24*7);
        Date weekEndTime12 = new Date();
        weekEndTime12.setTime(weekEndTime11.getTime()+1000*60*60*24*7);
        Date weekEndTime13 = new Date();
        weekEndTime13.setTime(weekEndTime12.getTime()+1000*60*60*24*7);


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstWeekBegin = sdf1.format(firstWeekBeginTime);
        String firstWeekEnd = sdf1.format(firstWeekEndTime);
        String WeekEndTime2 = sdf1.format(weekEndTime2);
        String WeekEndTime3 = sdf1.format(weekEndTime3);
        String WeekEndTime4 = sdf1.format(weekEndTime4);
        String WeekEndTime5 = sdf1.format(weekEndTime5);
        String WeekEndTime6 = sdf1.format(weekEndTime6);
        String WeekEndTime7 = sdf1.format(weekEndTime7);
        String WeekEndTime8 = sdf1.format(weekEndTime8);
        String WeekEndTime9 = sdf1.format(weekEndTime9);
        String WeekEndTime10 = sdf1.format(weekEndTime10);
        String WeekEndTime11 = sdf1.format(weekEndTime11);
        String WeekEndTime12 = sdf1.format(weekEndTime12);
        String WeekEndTime13 = sdf1.format(weekEndTime13);

        httpSession.setAttribute("firstWeekBeginTime",firstWeekBegin);
        httpSession.setAttribute("firstWeekEndTime",firstWeekEnd);
        httpSession.setAttribute("WeekEndTime2",WeekEndTime2);
        httpSession.setAttribute("WeekEndTime3",WeekEndTime3);
        httpSession.setAttribute("WeekEndTime4",WeekEndTime4);
        httpSession.setAttribute("WeekEndTime5",WeekEndTime5);
        httpSession.setAttribute("WeekEndTime6",WeekEndTime6);
        httpSession.setAttribute("WeekEndTime7",WeekEndTime7);
        httpSession.setAttribute("weekEndTime8",weekEndTime8);
        httpSession.setAttribute("WeekEndTime9",WeekEndTime9);
        httpSession.setAttribute("WeekEndTime10",WeekEndTime10);
        httpSession.setAttribute("WeekEndTime11",WeekEndTime11);
        httpSession.setAttribute("WeekEndTime12",WeekEndTime12);
        httpSession.setAttribute("WeekEndTime13",WeekEndTime13);
    }


    @Autowired
    WeekReportService weekReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuScoreService stuScoreService;



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

    @GetMapping("/findWeekTimeMessage/{id}")
    public TimeMessage findWeekTimeMessage(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }

    @GetMapping("/selectStuScoreInSubmitWeekReport/{sno}")
    public StudentScore selectStuScoreInSubmitWeekReport(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitWeekReport/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitWeekReport(@PathVariable("sno") String sno,
                                                    @PathVariable("dailyScore") int dailyScore,
                                                    @PathVariable("middleScore") int middleScore,
                                                    @PathVariable("ptScore") int ptScore,
                                                    @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @GetMapping("/updateStuDailyScoreInSubmitWeekReport/{sno},{dailyScore},{score}")
    public int updateStuDailyScoreInSubmitWeekReport(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.updateStuDailyScore(sno,dailyScore,score);

    }


}
