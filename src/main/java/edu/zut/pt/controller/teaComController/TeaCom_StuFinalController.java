package edu.zut.pt.controller.teaComController;

import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StuComScore;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.*;
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
public class TeaCom_StuFinalController {

    @GetMapping("tea_company_PTReview")
    public String tea_company_pt(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StuComScore> stuComScoreList = getComStuFinalScorePageInfo(tno);

        httpSession.setAttribute("stuComFinalScoreNum",stuComScoreList.size());
        model.addAttribute("stuComFinalScore",stuComScoreList);

        return "tea_company/tea_company_ptReview";
    }


    @RequestMapping(value="/submitStuComFinalScore/{sno},{isSubmit},{score}" ,method = RequestMethod.POST)
    public String submitStuComFinalScore(@PathVariable("sno") String sno,
                                         @PathVariable("isSubmit") String isSubmit,
                                         @PathVariable("score") String score,
                                         HttpSession httpSession,
                                         HttpServletRequest httpServletRequest,Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();

        if("无法提交".equals(isSubmit)){
            System.out.println("该生未提交报告，无法插入成绩信息！");
        }else{
            int finalScore = Integer.valueOf(score);
            if(selectStuComFinalScore(sno)==null){//成绩信息表没有此学生，要插入整条成绩信息
                System.out.print("确定学生是否存在在成绩表！");
                insertStuComFinalScore(sno,0,0,finalScore,"","","");
                System.out.print("插入成绩！");
            }else{//成绩信息表中有此学生，更新成绩信息
                updateStuComFinalScore(sno,finalScore);
                System.out.print("更新成绩！");
            }
            stuScoreUpdateInSubmitFinalScoreInCom(sno);
        }

        List<StuComScore> stuComScoreList = getComStuFinalScorePageInfo(tno);

        httpSession.setAttribute("stuComFinalScoreNum",stuComScoreList.size());
        model.addAttribute("stuComFinalScore",stuComScoreList);

        return "tea_company/tea_company_ptReview";

    }

    DownloadFile downloadFile = new DownloadFile();

    //报告下载
    @RequestMapping(value = "/stuComFinalFileDownload",method = RequestMethod.POST)
    public String comTeaMiddleReportDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                             HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        //获取文件路径
        String reportFilePath = httpServletRequest.getParameter("stuComDownloadReportAddress");

        if("未提交文件".equals(reportFilePath)==false){
            downloadFile.download(reportFilePath,httpServletResponse);
        }else{
            System.out.println("该生未提交文件，无法下载！");
        }

        List<StuComScore> stuComScoreList = getComStuFinalScorePageInfo(tno);

        httpSession.setAttribute("stuComFinalScoreNum",stuComScoreList.size());
        model.addAttribute("stuComFinalScore",stuComScoreList);

        return "tea_company/tea_company_ptReview";
    }

    //项目下载
    @RequestMapping(value = "/stuComFinalProjectFileDownload",method = RequestMethod.POST)
    public String stuComFinalProjectFileDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                             HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        //获取文件路径
        String projectFilePath = httpServletRequest.getParameter("stuComDownloadProjectAddress");

        if("未提交文件".equals(projectFilePath)==false){
            downloadFile.download(projectFilePath,httpServletResponse);
        }else{
            System.out.println("该生未提交文件，无法下载！");
        }

        List<StuComScore> stuComScoreList = getComStuFinalScorePageInfo(tno);

        httpSession.setAttribute("stuComFinalScoreNum",stuComScoreList.size());
        model.addAttribute("stuComFinalScore",stuComScoreList);

        return "tea_company/tea_company_ptReview";
    }

    //获取实训报告页面信息
    public List<StuComScore> getComStuFinalScorePageInfo(String tno){
        //获取老师姓名
        String name = findComFinalMyNameByTno(tno);
        //获取老师的学生信息
        List<StudentInfo> studentInfoList = findComFinalMyStuByTname(name);
        //用来保存学生的成绩信息
        List<StuComScore> stuComScoreList = new ArrayList<>();
        //进入for循环
        for(int i = 0;i<studentInfoList.size();i++){
            StuComScore stuComScore = new StuComScore();

            if(selectComStuFinalFilePath(studentInfoList.get(i).getSno())==null){//学生的报告信息不存在
                stuComScore.setSno(studentInfoList.get(i).getSno());
                if(selectStuComFinalScore(studentInfoList.get(i).getSno())==null){//学生的成绩信息不存在
                    //默认路径为“未提交文件”   成绩为0
                    stuComScore.setFinalScore(0);
                }else{//学生的成绩信息存在
                    //获取学生的成绩信息
                    stuComScore.setFinalScore(selectStuComFinalScore(studentInfoList.get(i).getSno()).getFinalScore());
                }
                stuComScore.setFinalFilePath("未提交文件");
                stuComScore.setFinalProjectPath("未提交文件");
            }else{//如果学生报告信息存在
                FinalReport finalReport = selectComStuFinalFilePath(studentInfoList.get(i).getSno());
                stuComScore.setSno(studentInfoList.get(i).getSno());
                stuComScore.setFinalFilePath(finalReport.getAdd_finReport());
                stuComScore.setFinalProjectPath(finalReport.getAdd_finalProject());
                if(selectStuComFinalScore(studentInfoList.get(i).getSno())==null){//学生成绩信息不存在
                    stuComScore.setFinalScore(0);
                }else{//学生成绩信息存在
                    stuComScore.setFinalScore(selectStuComFinalScore(studentInfoList.get(i).getSno()).getFinalScore());
                }

            }
            stuComScoreList.add(stuComScore);
        }
        return stuComScoreList;
    }

    public void stuScoreUpdateInSubmitFinalScoreInCom(String sno){
        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitFinalScoreInCom(sno).getTypeName();
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
            if(selectStuScoreInSubmitFinalScoreInCom(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitFinalScoreInCom(sno,finalScore,score1);
            }
        }else if("校企合作".equals(type)){
            //获取平时成绩、中期成绩、实训报告成绩、实训成绩
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitFinalScoreInCom(sno)==null){//实训成绩表中没有该学号是呢过的成绩信息，需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitFinalScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitFinalScoreInCom(sno,finalScore,score1);
            }
        }
    }


    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    FinalReportService finalReportService;

    @Autowired
    StuComScoreService stuComScoreService;

    @Autowired
    StuScoreService stuScoreService;



    @GetMapping("/selectComFinalMyNameByTno/{tno}")
    public String findComFinalMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComFinalMyStuByTname/{tName}")
    public List<StudentInfo> findComFinalMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectComStuFileFilePath/{sno}")
    public FinalReport selectComStuFinalFilePath(@PathVariable("sno") String sno){
        return finalReportService.findFinalReportBySno(sno);
    }

    @GetMapping("/selectStuComFinalScore/{sno}")
    public StuComScore selectStuComFinalScore(@PathVariable("sno") String sno){
        return stuComScoreService.selectStuComScore(sno);
    }


    @GetMapping("/insertStuComFinalScore/{sno},{dailyScore},{middleScore},{ptScore},{middleFilePath},{finalFilePath},{finalProjectPath}")
    public int insertStuComFinalScore(@PathVariable("sno") String sno,
                                       @PathVariable("dailyScore") int dailyScore,
                                       @PathVariable("middleScore") int middleScore,
                                       @PathVariable("ptScore") int ptScore,
                                       @PathVariable("middleFilePath") String middleFilePath,
                                       @PathVariable("finalFilePath") String finalFilePath,
                                       @PathVariable("finalProjectPath") String finalProjectPath){
        return stuComScoreService.insertStuComScore(sno,dailyScore,middleScore,ptScore,middleFilePath,finalFilePath,finalProjectPath);

    }

    @GetMapping("/updateStuComFinalScore/{sno},{finalScore}")
    public int updateStuComFinalScore(@PathVariable("sno") String sno,
                                       @PathVariable("finalScore") int finalScore){
        return stuComScoreService.updateStuComFinalScore(sno,finalScore);
    }


    @GetMapping("/insertStuScoreInfoInSubmitFinalScoreInCom/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitFinalScoreInCom(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @RequestMapping("/updateStuMiddleScoreInSubmitFinalScoreInCom/{sno},{finalScore},{score}")
    public int updateStuMiddleScoreInSubmitFinalScoreInCom(@PathVariable("sno") String sno,
                                                       @PathVariable("finalScore") int finalScore,
                                                       @PathVariable("score") int score){
        return stuScoreService.updateStuPTScore(sno,finalScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitFinalScoreInCom/{sno}")
    public StudentScore selectStuScoreInSubmitFinalScoreInCom(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }


    @RequestMapping("/getStuPTInfoInSubmitFinalScoreInCom/{sno}")
    public StudentInfo getStuPTInfoInSubmitFinalScoreInCom(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

}
