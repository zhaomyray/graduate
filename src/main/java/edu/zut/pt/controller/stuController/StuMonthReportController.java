package edu.zut.pt.controller.stuController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.UploadFile;
import edu.zut.pt.pojo.*;
import edu.zut.pt.service.MonthReportService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
public class StuMonthReportController {

    @GetMapping("stu_monthReport")
    public String month(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        getMonthPageInfo(sno,httpSession,model);
        return "/stu/stu_monthReport";
    }

    @RequestMapping(value="/submitMonthReport",method = RequestMethod.POST)
//    @ResponseBody
    public String  doMonthSubmitWR(@RequestParam("uploadMonthFile") MultipartFile uploadMonthFile,WebRequest webRequest,
                                   HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){
        String monthMessageStr = httpServletRequest.getParameter("monthMessage");
        String monthSno = httpServletRequest.getSession().getAttribute("login").toString();
        String monthFile = uploadMonthFile.getOriginalFilename();
        String monthBan = httpServletRequest.getParameter("monthBan");
        String monthContent = webRequest.getParameter("monthReportContent");
        int nullFlag;
        if(monthContent.length()==0){
            if(monthFile.length()==0){
                nullFlag = 0;
            }else{
                nullFlag = 1;
            }
        }else{
            nullFlag = 1;
        }
        if("请选择".equals(monthMessageStr)==false){
            Integer monthMessage = Integer.valueOf(monthMessageStr);
            if(nullFlag==1) {
                if ("无法提交".equals(monthBan)) {
                    System.out.println("该报告已超过提交时间，无法提交！");
                } else {
                    String monthFlag = "";
                    if ("补交".equals(monthBan)) {
                        monthFlag = "是";
                    } else if ("正常提交".equals(monthBan)) {
                        monthFlag = "否";
                    }

                    Date monthDate = new Date();
                    SimpleDateFormat monthDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String monthTime = monthDf.format(monthDate).toString();

                    MonthReport monthReport = new MonthReport();
                    monthReport.setMonthSno(monthSno);
                    monthReport.setContent_monthReport(monthContent);
                    monthReport.setMonthMessage(monthMessage);
                    monthReport.setTime_submit(monthTime);
                    monthReport.setIsAfter(monthFlag);
                    monthReport.setScore(0);
                    monthReport.setMonthReview("");
                    if (monthFile.length() == 0) {
                        monthReport.setMonthReportFilePath("未提交文件");
                    } else {
                        String monthReportFilePath = upload(monthSno, monthMessage, uploadMonthFile).toString();
                        monthReport.setMonthReportFilePath(monthReportFilePath);
                    }
                    insertMonthReport(monthReport);
                    //创建获取实训成绩的对象
                    CountScore countScore = new CountScore();
                    //获取平时成绩、中期成绩、实训报告成绩、实训成绩
                     Map<String,Object> scoreMap = countScore.countAllScore(monthSno);
                    int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
                    int middleScore = Integer.valueOf(scoreMap.get("middleScore").toString());
                    int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
                    int score = Integer.valueOf(scoreMap.get("score").toString());
                    //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
                    if(selectStuScoreInSubmitMonthReport(monthSno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                        insertStuScoreInfoInSubmitMonthReport(monthSno,dailyScore,middleScore,finalScore,score);
                    }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                        updateStuDailyScoreInSubmitMonthReport(monthSno,dailyScore,score);
                    }
                }
            }else{
                System.out.println("月报内容与附件不能同时为空！");
            }

        }
        getMonthPageInfo(monthSno,httpSession,model);

        return "/stu/stu_monthReport";
    }

    @RequestMapping(value="/monthFileReportDownload",method = RequestMethod.POST)
//    @ResponseBody
    public String monthFileReportDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                          HttpSession httpSession,Model model){
        System.out.println("进入到下载月报的方法中");
        String monthFilePath = httpServletRequest.getParameter("monthReportFilePath");
        System.out.println("monthFilePath 为"+monthFilePath);
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        DownloadFile downloadFile = new DownloadFile();
        if("未提交文件".equals(monthFilePath)==false){
            downloadFile.download(monthFilePath,httpServletResponse);
        }else{
            System.out.println("未提交报告");
        }
        getMonthPageInfo(sno,httpSession,model);
        return "/stu/stu_monthReport";
    }

    //得到月报页面信息
    public void getMonthPageInfo(String sno, HttpSession httpSession, Model model){
        List<MonthReport> list = selectMonthReportBySno(sno);
        model.addAttribute("resultList",list);
        httpSession.setAttribute("monthNum",list.size());
        TimeMessage timeMessage = findMonthTime(1);
        List<Month> month = getMonth(list,timeMessage);

        int startMonthBJ = findMonthTime(1).getStartBuJiao();
        getAllTime(httpSession,timeMessage);

        httpSession.setAttribute("startMonthBJ",startMonthBJ);
        model.addAttribute("monthList",month);
    }

    //上传报告
    public Object upload(String sno,int monthMessage,MultipartFile monthFile){
        System.out.print("进入上传方法！");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        //保存时的文件名dataName
        String dateName = "month"+monthMessage+"_"+sno+"_"+df.format(calendar.getTime())+monthFile.getOriginalFilename();
        String realPath = "F:/uploadMonthReportTest/";
        try{
            //调用上传文件的方法
            //上传周报
            UploadFile.uploadFile(monthFile.getBytes(),realPath,dateName);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return realPath+dateName;
    }

    //得到月信息
    public List<Month> getMonth(List<MonthReport> list,TimeMessage timeMessage){
        List<Month> month = new ArrayList<>();
        //获取应提交月报的总次数
        int monthAll1 = timeMessage.getMonthNums();
        int flag = 0;
        for(int i = 1;i<=monthAll1;i++){
            Month month1 = new Month();
            for(int j = 0;j<list.size();j++){
                if(i == list.get(j).getMonthMessage()){
                    flag = 1;
                }
            }
            if(flag == 0){
                month1.setId(i);
                month1.setMonthMessage(i);
                month1.setMonthValue(i);
                month.add(month1);
            }
            flag = 0;
        }
        return month;
    }

    //得到月报提交时间信息
    public void getAllTime(HttpSession httpSession,TimeMessage timeMessage){
        Date firstMonthBeginTime = timeMessage.getFirstMonthBeginTime();
        Date firstMonthEndTime = timeMessage.getFirstMonthEndTime();

        Date monthEndTime2 = new Date();
        monthEndTime2.setTime(firstMonthEndTime.getTime()+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7);
        Date monthEndTime3 = new Date();
        monthEndTime3.setTime(monthEndTime2.getTime()+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7);
        Date monthEndTime4 = new Date();
        monthEndTime4.setTime(monthEndTime3.getTime()+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7+1000*60*60*24*7);

        //设置每次月报补交截止时间
        Date monthBan1 = new Date();
        monthBan1.setTime(firstMonthEndTime.getTime()+1000*60*60*24*7);
        Date monthBan2 = new Date();
        monthBan2.setTime(monthEndTime2.getTime()+1000*60*60*24*7);
        Date monthBan3 = new Date();
        monthBan3.setTime(monthEndTime3.getTime()+1000*60*60*24*7);
        Date monthBan4 = new Date();
        monthBan4.setTime(monthEndTime4.getTime()+1000*60*60*24*7);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstMonthBegin = sdf1.format(firstMonthBeginTime);
        String firstMonthEnd = sdf1.format(firstMonthEndTime);
        String MonthEndTime2 = sdf1.format(monthEndTime2);
        String MonthEndTime3 = sdf1.format(monthEndTime3);
        String MonthEndTime4 = sdf1.format(monthEndTime4);
        String MonthBan1 = sdf1.format(monthBan1);
        String MonthBan2 = sdf1.format(monthBan2);
        String MonthBan3 = sdf1.format(monthBan3);
        String MonthBan4 = sdf1.format(monthBan4);

        httpSession.setAttribute("firstMonthBeginTime",firstMonthBegin);
        httpSession.setAttribute("firstMonthEndTime",firstMonthEnd);
        httpSession.setAttribute("MonthEndTime2",MonthEndTime2);
        httpSession.setAttribute("MonthEndTime3",MonthEndTime3);
        httpSession.setAttribute("MonthEndTime4",MonthEndTime4);
        httpSession.setAttribute("MonthBan1",MonthBan1);
        httpSession.setAttribute("MonthBan2",MonthBan2);
        httpSession.setAttribute("MonthBan3",MonthBan3);
        httpSession.setAttribute("MonthBan4",MonthBan4);
    }



    @Autowired
    MonthReportService monthReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuScoreService stuScoreService;

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

    @GetMapping("/findMonthTime/{id}")
    public TimeMessage findMonthTime(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }

    @GetMapping("/selectStuScoreInSubmitMonthReport/{sno}")
    public StudentScore selectStuScoreInSubmitMonthReport(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitMonthReport/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitMonthReport(@PathVariable("sno") String sno,
                                                    @PathVariable("dailyScore") int dailyScore,
                                                    @PathVariable("middleScore") int middleScore,
                                                    @PathVariable("ptScore") int ptScore,
                                                    @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @GetMapping("/updateStuDailyScoreInSubmitMonthReport/{sno},{dailyScore},{score}")
    public int updateStuDailyScoreInSubmitMonthReport(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.updateStuDailyScore(sno,dailyScore,score);

    }

}
