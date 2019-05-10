package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.mapper.MiddleReportMapper;
import edu.zut.pt.mapper.TeaSchMiddleReportMapper;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.MiddleReportService;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TeaSch_MiddleReportController {

    @GetMapping("tea_school_midReportReview")
    public String tea_school_middleReview(HttpServletRequest httpServletRequest, Model model,HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getSchTeaMiddlePageInfo(tno,httpSession,model);
        return "tea_school/tea_school_midReportReview";
    }


    @RequestMapping(value="/teaSchStuMiddleInsertBySno/{sno},{review},{isSubmit},{score}",method = RequestMethod.POST)
    public String teaSchStuMiddleInsertBySno(@PathVariable("sno") String sno,
                                             @PathVariable("review") String review,
                                             @PathVariable("isSubmit") String isSubmit,
                                             @PathVariable("score") String middleScore,
                                             HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        if("提交".equals(isSubmit)){
            int score = Integer.valueOf(middleScore);
            String middleReview = review;
            System.out.println(sno+score+middleReview);
            updateMiddleScoreBySno(sno,score,middleReview);
            stuScoreUpdateInSubmitMiddleScore(sno);
        }else{
            System.out.println("无法插入该生的中期报告成绩");
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getSchTeaMiddlePageInfo(tno,httpSession,model);
        return "tea_school/tea_school_midReportReview";
    }

    //根据学生学号查找学生中期报告信息
    public List<MiddleReport> getStuMiddleReport(List<StudentInfo> list){
        List<MiddleReport> middleList = new ArrayList<MiddleReport>();
        for(int i = 0;i<list.size();i++){
            MiddleReport middleReport = new MiddleReport();
            if(selectMiddleBySno(list.get(i).getSno())==null){
                middleReport.setMiddleSno(list.get(i).getSno());
                middleReport.setTime_submit("---");
                middleReport.setIsAfter("--");
                middleReport.setMiddleReportFilePath("未提交报告");
                middleReport.setContent_midReport("");
                middleReport.setScore(0);
                middleReport.setIsUpdate("未提交");
            }else{
                middleReport = selectMiddleBySno(list.get(i).getSno());
            }
            middleList.add(middleReport);
        }
        return middleList;
    }

    DownloadFile downloadFile = new DownloadFile();

    @RequestMapping(value="/schTeaMiddleReportDownload",method = RequestMethod.POST)
    public String schTeaMiddleReportDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                             HttpSession httpSession,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String schTeaMiddleFilePath = httpServletRequest.getParameter("schTeaMiddleReportFilePath");
        String schTeaMiddleFileSno = httpServletRequest.getParameter("schTeaMiddleReportSno");
        System.out.println("中期报告路径："+schTeaMiddleFilePath);

        if("未提交报告".equals(schTeaMiddleFilePath)==false){
            String isUpdate = "已下载";
            updateSchTeaMiddleIsUpdate(isUpdate,schTeaMiddleFileSno);
            downloadFile.download(schTeaMiddleFilePath,httpServletResponse);
        }else{
            System.out.println("未提交报告");
        }
        getSchTeaMiddlePageInfo(tno,httpSession,model);
        return "tea_school/tea_school_midReportReview";
    }

    //获取中期报告页面信息
    public void getSchTeaMiddlePageInfo(String tno,HttpSession httpSession,Model model){
        List<StudentInfo> stuInfoList = selectMiddleMyStu(tno);//根据教工号查找学生信息
        List<MiddleReport> middleReportList = getStuMiddleReport(stuInfoList);

        httpSession.setAttribute("teaSchStuMiddleNum",stuInfoList.size());
        model.addAttribute("middleReportList",middleReportList);
    }

    public void stuScoreUpdateInSubmitMiddleScore(String sno){
        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitMiddleScore(sno).getTypeName();
        //创建获取实训成绩的对象
        CountScore countScore = new CountScore();
        Map<String,Object> scoreMap = new HashMap<>();
        if("校外自行联系".equals(type)){
            //获取平时成绩、中期成绩、实训报告成绩、实训成绩
            scoreMap = countScore.countAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitMiddleScore(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleScore(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitMiddleScore(sno,middleScore1,score1);
            }
        }else if("校企合作".equals(type)){
            //获取平时成绩、中期成绩、实训报告成绩、实训成绩
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitMiddleScore(sno)==null){//实训成绩表中没有该学号是呢过的成绩信息，需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleScore(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitMiddleScore(sno,middleScore1,score1);
            }
        }
    }

    @Autowired
    TeaSchMiddleReportService teaSchMiddleReportService;

    @Autowired
    MiddleReportService middleReportService;

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    StuScoreService stuScoreService;

    @RequestMapping("/getStuPTInfoInSubmitMiddleScore/{sno}")
    public StudentInfo getStuPTInfoInSubmitMiddleScore(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

    @RequestMapping("/selectMiddleMyStu/{tno}")
    public List<StudentInfo> selectMiddleMyStu(@PathVariable("tno") String tno){
        return teaSchMiddleReportService.findStuByTno(tno);
    }

    @RequestMapping("/selectMiddleBySno/{sno}")
    public MiddleReport selectMiddleBySno(@PathVariable("sno") String sno){
        return teaSchMiddleReportService.findBySno(sno);
    }

    @RequestMapping("/insertMiddleScore/{sno},{score},{middleReview}")
    public int updateMiddleScoreBySno(@PathVariable("sno") String sno,
                                      @PathVariable("score") int score,
                                      @PathVariable("middleReview") String middleReview){
        return teaSchMiddleReportService.updateMiddleScoreBySno(sno,score,middleReview);
    }

    @RequestMapping("/selectMiddleIsUpdate/{sno}")
    public MiddleReport selectMiddleIsUpdate(@PathVariable("sno") String sno){
        return middleReportService.findMiddleReportByMidSWM(sno);
    }

    @RequestMapping("/updateSchTeaMiddleIsUpdate/{isUpdate},{middleSno}")
    public int updateSchTeaMiddleIsUpdate(@PathVariable("isUpdate") String isUpdate,
                                          @PathVariable("middleSno") String middleSno){
        return middleReportService.updateMiddleReportIsUpdate(isUpdate,middleSno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitMiddleScore/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitMiddleScore(@PathVariable("sno") String sno,
                                                      @PathVariable("dailyScore") int dailyScore,
                                                      @PathVariable("middleScore") int middleScore,
                                                      @PathVariable("ptScore") int ptScore,
                                                      @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @RequestMapping("/updateStuMiddleScoreInSubmitMiddleScore/{sno},{middleScore},{score}")
    public int updateStuMiddleScoreInSubmitMiddleScore(@PathVariable("sno") String sno,
                                                        @PathVariable("middleScore") int middleScore,
                                                        @PathVariable("score") int score){
        return stuScoreService.updateStuMiddleScore(sno,middleScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitMiddleScore/{sno}")
    public StudentScore selectStuScoreInSubmitMiddleScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
