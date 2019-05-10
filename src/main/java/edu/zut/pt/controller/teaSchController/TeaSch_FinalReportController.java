package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TeaSchFinalReportService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TeaSch_FinalReportController {

    DownloadFile downloadFile = new DownloadFile();

    @GetMapping("tea_school_finalReportReview")
    public String tea_school_finalReview(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getTeaSchFinalPageInfo(tno,httpSession,model);
        return "tea_school/tea_school_finalReportReview";
    }

    //下载报告
    @RequestMapping(value = "/fileReportTeaSchDownload",method = RequestMethod.POST)
    public String fileReportDownload(HttpServletRequest httpServletRequest,HttpSession httpSession,
                                   HttpServletResponse httpServletResponse,Model model) {
        //获取学号
        String sno = httpServletRequest.getParameter("downloadReportSno");
        //获取报告路径
        String reportFilePath = httpServletRequest.getParameter("downloadReportAddress");
        System.out.println("---"+reportFilePath);

        if("未提交文件".equals(reportFilePath)){
            System.out.println("该生未提交文件");
        }else{
            downloadFile.download(reportFilePath,httpServletResponse);
            String isUpdate = "已下载";
            updateIsUpdate(isUpdate,sno);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getTeaSchFinalPageInfo(tno,httpSession,model);
        return "tea_school/tea_school_finalReportReview";
    }

    //下载项目
    @RequestMapping(value = "/fileProjectTeaSchDownload",method = RequestMethod.POST)
    public String fileProjectDownload(HttpServletRequest httpServletRequest, HttpSession httpSession,
                                      HttpServletResponse httpServletResponse, Model model){
        //获取学号
        String sno = httpServletRequest.getParameter("downloadProjectSno");
        //获取报告路径
        String projectFilePath = httpServletRequest.getParameter("downloadProjectAddress");
        System.out.println("---"+projectFilePath);
        if ("未提交文件".equals(projectFilePath)){
            System.out.println("该生未提交文件");
        }else{
            downloadFile.download(projectFilePath,httpServletResponse);
            String isUpdate="已下载";
            updateIsUpdate(isUpdate,sno);
        }
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getTeaSchFinalPageInfo(tno,httpSession,model);
        return "tea_school/tea_school_finalReportReview";
    }

    //提交分数
    @RequestMapping(value="/submitFinalScore/{sno},{isSubmit},{score}",method = RequestMethod.POST)
    public String insertSubmitFinalScore(@PathVariable("sno") String sno,
                                         @PathVariable("isSubmit") String isSubmit,
                                         @PathVariable("score") String finalScore,
                                         HttpServletRequest httpServletRequest,HttpSession httpSession,
                                         Model model){

        System.out.println(sno+isSubmit+finalScore);
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        if("提交".equals(isSubmit)){
            int score = Integer.valueOf(finalScore);
            System.out.println("成绩为"+score);
            updateFinalScoreBySno(sno,score);
            stuScoreUpdateInSubmitFinalScore(sno);
        }else{
            System.out.println("无法插入该生的实训成绩！");
        }
        getTeaSchFinalPageInfo(tno,httpSession,model);
        return "tea_school/tea_school_finalReportReview";
    }

    //获取页面信息
    public void getTeaSchFinalPageInfo(String tno,HttpSession httpSession,Model model){
        List<StudentInfo> stuInfoList = findStuByTno(tno);
        List<FinalReport> finalReportList = getStuFinalReport(stuInfoList);

        httpSession.setAttribute("teaSchStuFinalNum",stuInfoList.size());
        model.addAttribute("finalReportList",finalReportList);
    }

    //获取学生实训报告和项目信息
    public List<FinalReport> getStuFinalReport(List<StudentInfo> list){
        List<FinalReport> finalList = new ArrayList<FinalReport>();
        for(int i = 0;i<list.size();i++){
            FinalReport finalReport = new FinalReport();
            if(findStuBySno(list.get(i).getSno())==null){
                finalReport.setFinalSno(list.get(i).getSno());
                finalReport.setTime_submit("---");
                finalReport.setIsAfter("--");
                finalReport.setIsUpdate("未提交");
                finalReport.setScore(0);
                finalReport.setAdd_finReport("未提交文件");
                finalReport.setAdd_finalProject("未提交文件");
            }else{
                finalReport = findStuBySno(list.get(i).getSno());
            }
            finalList.add(finalReport);
        }
        return finalList;
    }

    public void stuScoreUpdateInSubmitFinalScore(String sno){

        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitFinalScore(sno).getTypeName();
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
            if(selectStuScoreInSubmitFinalScore(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalScore(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuFinalScoreInSubmitFinalScore(sno,finalScore,score1);
            }
        }else if("校企合作".equals(type)){
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitFinalScore(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalScore(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuFinalScoreInSubmitFinalScore(sno,finalScore,score1);
            }
        }

    }

    @Autowired
    TeaSchFinalReportService teaSchFinalReportService;

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    StuScoreService stuScoreService;

    @RequestMapping("/findStuByTno/{tno}")
    public List<StudentInfo> findStuByTno(@PathVariable("tno") String tno){
       return teaSchFinalReportService.findStuByTno(tno);
    }

    @RequestMapping("/findStuBySno/{sno}")
    public FinalReport findStuBySno(@PathVariable("sno") String sno){

        return teaSchFinalReportService.findBySno(sno);
    }

    @RequestMapping("/updateFinalScoreBySno/{sno},{score}")
    public int updateFinalScoreBySno(@PathVariable("sno") String sno,
                                     @PathVariable("score") int score){
        System.out.println("进入到提交分数的方法");
        return teaSchFinalReportService.updateFinalScoreBySno(sno,score);
    }

    @RequestMapping("/updateIsUpdate/{isUpdate},{finalSno}")
    public int updateIsUpdate(@PathVariable("isUpdate") String isUpdate,
                              @PathVariable("finalSno") String finalSno){
        return teaSchFinalReportService.updateIsUpdate(isUpdate,finalSno);
    }

    @RequestMapping("/getStuPTInfoInSubmitFinalScore/{sno}")
    public StudentInfo getStuPTInfoInSubmitFinalScore(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitFinalScore/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitFinalScore(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @RequestMapping("/updateStuFinalScoreInSubmitFinalScore/{sno},{finalScore},{score}")
    public int updateStuFinalScoreInSubmitFinalScore(@PathVariable("sno") String sno,
                                                      @PathVariable("finalScore") int finalScore,
                                                      @PathVariable("score") int score){
        return stuScoreService.updateStuPTScore(sno,finalScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitFinalScore/{sno}")
    public StudentScore selectStuScoreInSubmitFinalScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
