package edu.zut.pt.controller.teaComController;

import com.sun.org.apache.xpath.internal.operations.Mod;
import edu.zut.pt.CountScore;
import edu.zut.pt.DownloadFile;
import edu.zut.pt.pojo.MiddleReport;
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
public class TeaCom_StuMiddleController {

    @GetMapping("tea_company_middleReview")
    public String tea_company_middle(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StuComScore> stuComScoreList = new ArrayList<>();
        if(getStuMiddleScore(tno).size()>0){
            stuComScoreList = getStuMiddleScore(tno);
        }

        httpSession.setAttribute("stuComMiddleScoreNum",stuComScoreList.size());
        model.addAttribute("stuComMiddleScore",stuComScoreList);
        return "tea_company/tea_company_middleReview";
    }

    //插入学生成绩或者修改学生成绩
    @RequestMapping(value="/comTeaInsertMiddleScore/{sno},{isSubmit},{score}",method = RequestMethod.POST)
    public String comTeaInsertMiddleScore(@PathVariable("sno") String sno,
                                          @PathVariable("isSubmit") String isSubmit,
                                          @PathVariable("score") String score,
                                          HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){

        System.out.print("跳入该函数！");
        if("无法提交".equals(isSubmit)){
            System.out.println("无法添加该生的成绩信息！");
        }else{
            int middleScore = Integer.valueOf(score);
            //判断在成绩表中是否存在该生成绩信息
            if(selectStuComMiddleScore(sno)==null){
                //如果不存在，就插入整条成绩信息
                System.out.print("确定学生是否存在在成绩表！");
                insertStuComMiddleScore(sno,0,middleScore,0,"","","");
                System.out.print("插入成绩！");
            }else{
                updateStuComMiddleScore(sno,middleScore);
                System.out.print("更新成绩！");
            }
            stuScoreUpdateInSubmitMiddleScoreInCom(sno);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StuComScore> stuComScoreList = getStuMiddleScore(tno);
        httpSession.setAttribute("stuComMiddleScoreNum",stuComScoreList.size());
        model.addAttribute("stuComMiddleScore",stuComScoreList);

        return "tea_company/tea_company_middleReview";
    }

    DownloadFile downloadFile;

    @RequestMapping(value = "/comTeaMiddleReportDownload",method = RequestMethod.POST)
    public String comTeaMiddleReportDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                             HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        //获取文件路径
        String comTeaMiddleFilePath = httpServletRequest.getParameter("comTeaMiddleReportFilePath");

        if("未提交文件".equals(comTeaMiddleFilePath)==false){
            downloadFile.download(comTeaMiddleFilePath,httpServletResponse);
        }else{
            System.out.println("该生未提交文件，无法下载！");
        }


        List<StuComScore> stuComScoreList = getStuMiddleScore(tno);
        httpSession.setAttribute("stuComMiddleScoreNum",stuComScoreList.size());
        model.addAttribute("stuComMiddleScore",stuComScoreList);
        return "tea_company/tea_company_middleReview";
    }



    //获取中期页面信息
    public List<StuComScore> getStuMiddleScore(String tno){
        //根据校外指导老师学工号查找老师姓名
        String tname = findComMiddleMyNameByTno(tno);
        //根据教师姓名查找学生信息
        List<StudentInfo> list = findComMiddleMyStuByTname(tname);
        //用来保存学生的成绩信息
        List<StuComScore> stuComScoreList = new ArrayList<>();
        //for循环
        for(int i = 0;i<list.size();i++){
            StuComScore stuComScore = new StuComScore();
            //如果学生成绩信息不存在
            if(selectStuComMiddleScore(list.get(i).getSno())==null){
                stuComScore.setSno(list.get(i).getSno());
                stuComScore.setDailyScore(0);
                stuComScore.setMiddleScore(0);
                stuComScore.setFinalScore(0);
                stuComScore.setMiddleFilePath("未提交文件");
                stuComScore.setFinalFilePath("未提交文件");
                stuComScore.setFinalProjectPath("未提交文件");
            }else{
                stuComScore = selectStuComMiddleScore(list.get(i).getSno());
                //如果该生在中期信息表中存在，直接从表中获取中期报告文件路径
                if(selectComStuMiddleFilePath(list.get(i).getSno())!=null){
                    String middleFilePath = selectComStuMiddleFilePath(list.get(i).getSno()).getMiddleReportFilePath();
                    stuComScore.setMiddleFilePath(middleFilePath);
                }else{
                    stuComScore.setMiddleFilePath("未提交文件");
                }
            }
            stuComScoreList.add(stuComScore);
        }
        return stuComScoreList;

    }

    public void stuScoreUpdateInSubmitMiddleScoreInCom(String sno){
        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitMiddleScoreInCom(sno).getTypeName();
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
            if(selectStuScoreInSubmitMiddleScoreInCom(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitMiddleScoreInCom(sno,middleScore1,score1);
            }
        }else if("校企合作".equals(type)){
            //获取平时成绩、中期成绩、实训报告成绩、实训成绩
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitMiddleScoreInCom(sno)==null){//实训成绩表中没有该学号是呢过的成绩信息，需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitMiddleScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitMiddleScoreInCom(sno,middleScore1,score1);
            }
        }
    }


    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    MiddleReportService middleReportService;

    @Autowired
    StuComScoreService stuComScoreService;

    @Autowired
    StuScoreService stuScoreService;



    @GetMapping("/selectComMiddleMyNameByTno/{tno}")
    public String findComMiddleMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComMiddleMyStuByTname/{tName}")
    public List<StudentInfo> findComMiddleMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectComStuMiddleFilePath/{sno}")
    public MiddleReport selectComStuMiddleFilePath(@PathVariable("sno") String sno){
        return middleReportService.findMiddleReportByMidSWM(sno);
    }

    @GetMapping("/selectStuComMiddleScore/{sno}")
    public StuComScore selectStuComMiddleScore(@PathVariable("sno") String sno){
        return stuComScoreService.selectStuComScore(sno);
    }


    @GetMapping("/insertStuComMiddleScore/{sno},{dailyScore},{middleScore},{ptScore},{middleFilePath},{finalFilePath},{finalProjectPath}")
    public int insertStuComMiddleScore(@PathVariable("sno") String sno,
                                      @PathVariable("dailyScore") int dailyScore,
                                      @PathVariable("middleScore") int middleScore,
                                      @PathVariable("ptScore") int ptScore,
                                       @PathVariable("middleFilePath") String middleFilePath,
                                       @PathVariable("finalFilePath") String finalFilePath,
                                       @PathVariable("finalProjectPath") String finalProjectPath){
        return stuComScoreService.insertStuComScore(sno,dailyScore,middleScore,ptScore,middleFilePath,finalFilePath,finalProjectPath);

    }

    @GetMapping("/updateStuComMiddleScore/{sno},{middleScore}")
    public int updateStuComMiddleScore(@PathVariable("sno") String sno,
                                      @PathVariable("middleScore") int middleScore){
        return stuComScoreService.updateStuComMiddleScore(sno,middleScore);
    }

    @RequestMapping("/getStuPTInfoInSubmitMiddleScoreInCom/{sno}")
    public StudentInfo getStuPTInfoInSubmitMiddleScoreInCom(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }


    @GetMapping("/insertStuScoreInfoInSubmitMiddleScoreInCom/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitMiddleScoreInCom(@PathVariable("sno") String sno,
                                                     @PathVariable("dailyScore") int dailyScore,
                                                     @PathVariable("middleScore") int middleScore,
                                                     @PathVariable("ptScore") int ptScore,
                                                     @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

    @RequestMapping("/updateStuMiddleScoreInSubmitMiddleScoreInCom/{sno},{middleScore},{score}")
    public int updateStuMiddleScoreInSubmitMiddleScoreInCom(@PathVariable("sno") String sno,
                                                       @PathVariable("middleScore") int middleScore,
                                                       @PathVariable("score") int score){
        return stuScoreService.updateStuMiddleScore(sno,middleScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitMiddleScoreInCom/{sno}")
    public StudentScore selectStuScoreInSubmitMiddleScoreInCom(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
