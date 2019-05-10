package edu.zut.pt.controller.stuController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.UploadFile;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.pojo.TimeMessage;
import edu.zut.pt.service.FinalReportService;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class StuFinalReportController {

    DownloadFile downloadFile = new DownloadFile();

    @GetMapping("stu_final")
    public String stu_final( HttpSession httpSession, Model model){
        String sno = httpSession.getAttribute("login").toString();
        getFinalPageInfo(sno,httpSession,model);
        return "stu/stu_final";
    }

    @RequestMapping(value = "/fileUpload",method = RequestMethod.POST)
    public String addProjectFile(@RequestParam("file-1") MultipartFile file1,
                                 @RequestParam("file-2") MultipartFile file2,
                                 HttpServletRequest httpServletRequest,
                                 HttpSession httpSession,Model model){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String finalBan = httpServletRequest.getParameter("finalBan");
        String add_finalReport = file1.getOriginalFilename();
        String add_finalProject = file2.getOriginalFilename();
        //判断实训报告和项目路径是否为空
        int nullFlag;
        if(add_finalReport.length()==0){
           if(add_finalProject.length()==0){
               nullFlag = 0;
           }else{
               nullFlag = 0;
           }
        }else{
            if(add_finalProject.length()==0){
                nullFlag = 0;
            }else{
                nullFlag = 1;
            }

        }
        if("无法提交".equals(finalBan)){
            //未到提交时间或者已经超过提交时间，无法提交！
            System.out.println("不在操作时间内，无法提交！");
        }else{
            if(nullFlag==1){
                //获取是否补交信息
                String finalFlag = "";
                if ("补交".equals(finalBan)) {
                    finalFlag = "是";
                } else if("正常提交".equals(finalBan)){
                    finalFlag = "否";
                }
                //获取上传时间信息
                Date finalDate = new Date();
                SimpleDateFormat finalDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String finalTime = finalDf.format(finalDate).toString();

               //实现上传，并获取两个文件的路径
                List<Object> objPath = upload(file1,file2);
                String reportPath = objPath.get(0).toString();
                String projectPath = objPath.get(1).toString();

                FinalReport finalReport = new FinalReport();
                finalReport.setFinalSno(sno);
                finalReport.setTime_submit(finalTime);
                finalReport.setScore(0);
                finalReport.setAdd_finReport(reportPath);
                finalReport.setAdd_finalProject(projectPath);
                finalReport.setIsAfter(finalFlag);
                finalReport.setReportReview("");
                finalReport.setIsUpdate("刚刚提交");

                if(findFinalReport(sno)!=null){
                    String isUpdate = "更新";
                    updateFinalInfo(sno,reportPath,projectPath,finalTime,finalFlag,isUpdate);
                    stuScoreInsertOrUpdateInSubmitFinalReport(sno);
                }else{
                    insertFinalInfo(finalReport);
                    stuScoreInsertOrUpdateInSubmitFinalReport(sno);
                }
            }else{
                System.out.println("附件不能为空！");
            }
        }

        FinalReport finalReport = findFinalReport(sno);

        List<FinalReport> finalReportList = new ArrayList<>();
        if(finalReport!=null){
            finalReportList.add(finalReport);
        }

        httpSession.setAttribute("finalReportNum",finalReportList.size());
        model.addAttribute("finalReportList",finalReportList);
        return "stu/stu_final";
    }

    @RequestMapping(value = "/fileReportDownload",method = RequestMethod.POST)
    public void fileReportDownload(HttpServletRequest httpServletRequest,HttpSession httpSession,
                                   HttpServletResponse httpServletResponse,Model model){

        String fileReportPath = httpServletRequest.getParameter("finReportFilePath");
        downloadFile.download(fileReportPath,httpServletResponse);

    }

    @RequestMapping(value = "/fileProjectDownload",method = RequestMethod.POST)
    public void fileProjectDownload(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse){
        String fileProjectPath = httpServletRequest.getParameter("finProjectFilePath");
        downloadFile.download(fileProjectPath,httpServletResponse);
    }


    //实现文件上传
    public List<Object> upload(MultipartFile file1,MultipartFile file2){

        System.out.print("进入上传方法！");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        System.out.println(file1.getOriginalFilename());
        //保存时的文件名dataName
        String dateName1 = df.format(calendar.getTime())+file1.getOriginalFilename();
        String dateName2 = df.format(calendar.getTime())+file2.getOriginalFilename();
        String realPath1 = "F:/uploadReportTest/";
        String realPath2 = "F:/uploadProjectTest/";
        try{
            //调用上传文件的方法
            //上传报告
            UploadFile.uploadFile(file1.getBytes(),realPath1,dateName1);
            //上传项目
            UploadFile.uploadFile(file2.getBytes(),realPath2,dateName2);
        }catch(Exception e){
            e.printStackTrace();
        }
        List<Object> filePath = new ArrayList<>();
        filePath.add(realPath1+dateName1);
        filePath.add(realPath2+dateName2);

        return filePath;
    }

    //获取实训报告和项目的页面信息数据
    public void getFinalPageInfo(String sno,HttpSession httpSession,Model model){
        FinalReport finalReport = findFinalReport(sno);

        List<FinalReport> finalReportList = new ArrayList<>();
        if(finalReport!=null){
            finalReportList.add(finalReport);
            httpSession.setAttribute("finalReportScore",finalReport.getScore());
        }else{
            httpSession.setAttribute("finalReportScore","未提交");
        }

        TimeMessage timeMessage = findFinalTime(1);
        int week = timeMessage.getWeekNums();
        Date firstWeekEndTime = timeMessage.getFirstWeekEndTime();
        Date finalBeginTime = new Date();
        finalBeginTime.setTime(firstWeekEndTime.getTime());
        //计算开始提交实训报告和项目的时间
        for(int i = 0;i<week-2;i++){
            finalBeginTime.setTime(finalBeginTime.getTime()+1000*60*60*24*7);
            System.out.println("当前计算的时间为："+finalBeginTime);
        }
        Date finalEndTime = new Date();
        finalEndTime.setTime(finalBeginTime.getTime()+1000*60*60*24*7+1000*60*60*24*7);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finalBegin = sdf1.format(finalBeginTime);
        String finalEnd = sdf1.format(finalEndTime);

        System.out.println("实训报告开始提交时间"+finalBegin+"实训报告提交截止时间"+finalEnd);

        int finalStartBJ = findFinalTime(1).getStartBuJiao();
        System.out.println("是否可以提交"+finalStartBJ);

        httpSession.setAttribute("finalStartBJ",finalStartBJ);
        httpSession.setAttribute("finalBeginTime",finalBegin);
        httpSession.setAttribute("finalEndTime",finalEnd);
        httpSession.setAttribute("finalReportNum",finalReportList.size());
        model.addAttribute("finalReportList",finalReportList);
    }

    public void stuScoreInsertOrUpdateInSubmitFinalReport(String sno){

        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitFinalReport(sno).getTypeName();
        //创建获取实训成绩的对象
        CountScore countScore = new CountScore();
        //获取平时成绩、中期成绩、实训报告成绩、实训成绩
        Map<String,Object> scoreMap = new HashMap<>();
        if("校外自行联系".equals(type)){
            scoreMap = countScore.countAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitFinalReport(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalReport(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuFinalScoreInSubmitFinalReport(sno,finalScore,score1);
            }
        }else if("校企合作".equals(type)){
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitFinalReport(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalReport(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuFinalScoreInSubmitFinalReport(sno,finalScore,score1);
            }
        }

    }

    @Autowired
    FinalReportService finalReportService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    StuScoreService stuScoreService;

    @GetMapping("/insertFinalInfo/{finalReport}")
    public int insertFinalInfo(@PathVariable("finalReport") FinalReport finalReport){
        return finalReportService.insertFinal(finalReport);
    }

    @GetMapping("/findFinalTime/{id}")
    public TimeMessage findFinalTime(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }

    @GetMapping("/findFinalReport/{finalSno}")
    public FinalReport findFinalReport(@PathVariable("finalSno") String finalSno){
        return finalReportService.findFinalReportBySno(finalSno);
    }

    @GetMapping("/updateFinalInfo/{finalSno},{add_finReport},{add_finalProject},{time_submit},{isAfter},{isUpdate}")
    public int updateFinalInfo(@PathVariable("finalSno") String finalSno,
                               @PathVariable("add_finReport") String add_finReport,
                               @PathVariable("add_finalProject") String add_finalProject,
                               @PathVariable("time_submit") String time_submit,
                               @PathVariable("isAfter") String isAfter,
                               @PathVariable("isUpdate") String isUpdate){
        return this.finalReportService.updateFinal(finalSno,add_finReport,add_finalProject,time_submit,isAfter,isUpdate);
    }

    @RequestMapping("/getStuPTInfoInSubmitFinalReport/{sno}")
    public StudentInfo getStuPTInfoInSubmitFinalReport(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitFinalReport/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitFinalReport(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @RequestMapping("/updateStuFinalScoreInSubmitFinalReport/{sno},{finalScore},{score}")
    public int updateStuFinalScoreInSubmitFinalReport(@PathVariable("sno") String sno,
                                                       @PathVariable("finalScore") int finalScore,
                                                       @PathVariable("score") int score){
        return stuScoreService.updateStuPTScore(sno,finalScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitFinalReport/{sno}")
    public StudentScore selectStuScoreInSubmitFinalReport(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
