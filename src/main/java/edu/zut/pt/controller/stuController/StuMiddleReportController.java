package edu.zut.pt.controller.stuController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.UploadFile;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.pojo.TimeMessage;
import edu.zut.pt.service.MiddleReportService;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class StuMiddleReportController {

    @GetMapping("stu_midReport")
    public String middle(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        getMiddlePageInfo(sno,httpSession,model);
        return "stu/stu_midReport";
    }

    @RequestMapping(value="/submitMiddleReport",method = RequestMethod.POST)
    public String  doMiddleSubmitWR(@RequestParam("uploadMiddleFile") MultipartFile uploadMiddleFile, WebRequest webRequest,
                                    HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String middleSno = httpServletRequest.getSession().getAttribute("login").toString();
        String middleBan = httpServletRequest.getParameter("middleBan");
        String middleFile = uploadMiddleFile.getOriginalFilename();
        String middleContent = webRequest.getParameter("middleReportContent");
        int nullFlag ;
        if(middleContent.length()==0){
            if(middleFile.length()==0){
                nullFlag = 0;
            }else{
                nullFlag = 1;
            }
        }else{
            nullFlag = 1;
        }

        if("无法提交".equals(middleBan)){
            //未到提交时间或者已经超过提交时间，无法提交！
            System.out.println("不在操作时间内，无法提交！");
        }else {
            if(nullFlag==1){
                String middleFlag = "";
                if ("补交".equals(middleBan)) {
                    middleFlag = "是";
                } else if("正常提交".equals(middleBan)){
                    middleFlag = "否";
                }

                Date middleDate = new Date();
                SimpleDateFormat middleDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String middleTime = middleDf.format(middleDate).toString();

                MiddleReport middleReport = new MiddleReport();
                middleReport.setMiddleSno(middleSno);
                middleReport.setContent_midReport(middleContent);
                middleReport.setTime_submit(middleTime);
                middleReport.setScore(0);
                middleReport.setIsAfter(middleFlag);
                middleReport.setMiddleReview("");
                List<MiddleReport> list = selectMiddleReportBySno(middleSno);
                if (list.size() == 0) {
                    if(middleFile.length()==0){
                        middleReport.setMiddleReportFilePath("未提交文件");
                    }else{
                        String middleFilePath = upload(middleSno,uploadMiddleFile).toString();
                        middleReport.setMiddleReportFilePath(middleFilePath);
                    }
                    middleReport.setIsUpdate("刚刚提交");
                    //插入一条中期报告信息
                    insertMiddleReport(middleReport);
                    stuScoreInsertOrUpdate(middleSno);
                } else if (list.get(0).getScore() == 0) {
                    String middleReportFilePath;
                    if(middleFile.length()==0){
                        middleReportFilePath = "未提交文件";
                    }else {
                        middleReportFilePath = upload(middleSno, uploadMiddleFile).toString();
                    }
                    String middleReportIsUpdate = "更新";
                    //更新一条中期报告信息
                    updateMiddleReport(middleSno, middleContent, middleTime, middleFlag,middleReportFilePath,middleReportIsUpdate);
                    stuScoreInsertOrUpdate(middleSno);

                }else{
                    System.out.println("报告已阅，无法修改！");
                }
            }else{
                System.out.println("报告内容与附件不能同时为空！");
            }

        }
        List<MiddleReport> middleReportList = selectMiddleReportBySno(middleSno);
        for(int i = 0;i<middleReportList.size();i++){
            middleReportList.get(i).setId(i+1);
            System.out.println(middleReportList.get(i).getId());
        }
        int startMiddleBJ = findMiddleTime(1).getStartBuJiao();

        httpSession.setAttribute("startMiddleBJ",startMiddleBJ);
        httpSession.setAttribute("middleNum",middleReportList.size());
        model.addAttribute("resultList",middleReportList);

        return "stu/stu_midReport";
    }

    //下载报告
    @RequestMapping(value = "/middleFileReportDownload",method = RequestMethod.POST)
    public String  middleFileReportDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                            HttpSession httpSession,Model model){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String middleFilePath = httpServletRequest.getParameter("middleReportFilePath");

        DownloadFile downloadFile = new DownloadFile();
        if("未提交文件".equals(middleFilePath)==false){
            downloadFile.download(middleFilePath,httpServletResponse);
        }else{
            System.out.println("未提交报告");
        }
        getMiddlePageInfo(sno,httpSession,model);
        return "stu/stu_midReport";

    }

    //获取中期报告页面信息
    public void getMiddlePageInfo(String sno,HttpSession httpSession,Model model){
        List<MiddleReport> list = selectMiddleReportBySno(sno);
        for(int i = 0;i<list.size();i++){
            list.get(i).setId(i+1);
            System.out.println(list.get(i).getId());
        }
        model.addAttribute("resultList",list);
        httpSession.setAttribute("middleNum",list.size());
        Date weekBeginTime = findMiddleTime(1).getFirstWeekBeginTime();
        Date weekEndTime = findMiddleTime(1).getFirstWeekEndTime();
        int weekAll = findMiddleTime(1).getWeekNums()/2;
        Date middleEndTime1 = new Date();
        //计算中期报告的正常提交时间的截止时间，超过这个时间，就是补交
        for(int i = 0;i<weekAll-1;i++){
            weekEndTime = new Date(weekEndTime.getTime()+1000*60*60*24*7);
            middleEndTime1.setTime(weekEndTime.getTime());
        }

        //计算中期报告的正常提交时间的开始时间,到这个时间，才能提交报告
        Date middleBegin = new Date(middleEndTime1.getTime()-1000*60*60*24*7);
        Date middleBeginTime1 = new Date();
        middleBeginTime1.setTime(middleBegin.getTime());

        //中期报告的补交截止时间，超过补交时间，就不能提交
        Date middleBan = new Date();
        middleBan.setTime(middleEndTime1.getTime()+1000*60*60*24*7);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String middleBeginTime = sdf1.format(middleBeginTime1);
        String middleEndTime = sdf1.format(middleEndTime1);
        String middleBanTime = sdf1.format(middleBan);
        int startMiddleBJ = findMiddleTime(1).getStartBuJiao();

        httpSession.setAttribute("startMiddleBJ",startMiddleBJ);
        httpSession.setAttribute("middleBeginTime",middleBeginTime);
        httpSession.setAttribute("middleEndTime",middleEndTime);
        httpSession.setAttribute("middleBanTime",middleBanTime);
        if(list.size()>0){
            httpSession.setAttribute("middleScore",list.get(0).getScore());
        }else {
            httpSession.setAttribute("middleScore",0);
        }
        System.out.println(httpSession.getAttribute("middleScore"));
    }

    //上传报告
    public Object upload(String sno,MultipartFile weekFile){
        System.out.print("进入上传方法！");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        //保存时的文件名dataName
        String dateName = "middle_"+sno+"_"+df.format(calendar.getTime())+weekFile.getOriginalFilename();
        String realPath = "F:/uploadMiddleReportTest/";
        try{
            //调用上传文件的方法
            //上传周报
            UploadFile.uploadFile(weekFile.getBytes(),realPath,dateName);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return realPath+dateName;
    }

    public void stuScoreInsertOrUpdate(String middleSno){
        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitMiddleReport(middleSno).getTypeName();
        //创建获取实训成绩的对象
        CountScore countScore = new CountScore();
        //获取平时成绩、中期成绩、实训报告成绩、实训成绩
        Map<String,Object> scoreMap = new HashMap<>();
        if("校外自行联系".equals(type)){
            scoreMap = countScore.countAllScore(middleSno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitMiddleReport(middleSno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleReport(middleSno,dailyScore,middleScore,finalScore,score);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitMiddleReport(middleSno,middleScore,score);
            }
        }else if("校企合作".equals(type)){
            scoreMap = countScore.countComAllScore(middleSno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score = Integer.valueOf(scoreMap.get("score").toString());
            if(selectStuScoreInSubmitMiddleReport(middleSno)==null){//实训成绩表中没有该生的成绩信息，需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleReport(middleSno,dailyScore,middleScore,finalScore,score);
            }else{
                updateStuMiddleScoreInSubmitMiddleReport(middleSno,middleScore,score);
            }
        }
    }


    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    MiddleReportService middleReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuScoreService stuScoreService;

    @GetMapping("/getStuPTInfoInSubmitMiddleReport/{sno}")
    public StudentInfo getStuPTInfoInSubmitMiddleReport(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

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

    @GetMapping("/findMiddleTime/{id}")
    public TimeMessage findMiddleTime(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }

    @GetMapping("updateMiddleReport/{middleSno},{content_midReport},{time_submit},{isAfter},{middleReportFilePath},{middleReportIsUpdate}")
    public int updateMiddleReport(@PathVariable("middleSno") String middleSno,
                                  @PathVariable("content_midReport") String content_midReport,
                                  @PathVariable("time_submit") String time_submit,
                                  @PathVariable("isAfter") String isAfter,
                                  @PathVariable("middleReportFilePath") String middleReportFilePath,
                                  @PathVariable("middleReportIsUpdate") String middleReportIsUpdate){
        return middleReportService.updateMiddleReport(middleSno,content_midReport,time_submit,isAfter,middleReportFilePath,middleReportIsUpdate);
    }

    @GetMapping("/insertStuScoreInfoInSubmitMiddleReport/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitMiddleReport(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @GetMapping("/updateStuMiddleScoreInSubmitMiddleReport/{sno},{middleScore},{score}")
    public int updateStuMiddleScoreInSubmitMiddleReport(@PathVariable("sno") String sno,
                                                      @PathVariable("middleScore") int middleScore,
                                                      @PathVariable("score") int score){
        return stuScoreService.updateStuMiddleScore(sno,middleScore,score);

    }

    @GetMapping("/selectStuScoreInSubmitMiddleReport/{sno}")
    public StudentScore selectStuScoreInSubmitMiddleReport(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }
}
