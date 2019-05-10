package edu.zut.pt.controller.teaComController;

import edu.zut.pt.CountScore;
import edu.zut.pt.pojo.StuComScore;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuComScoreService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.Teacher_CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TeaCom_StuDailyController {

    @GetMapping("tea_company_dailyReview")
    public String tea_company_daily(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StuComScore> stuComScoreList = new ArrayList<>();
        if(getStuComScore(tno).size()>0){
            stuComScoreList = getStuComScore(tno);
        }
        httpSession.setAttribute("studentComDailyScoreNum",stuComScoreList.size());
        model.addAttribute("studentComDailyScoreList",stuComScoreList);

        return "tea_company/tea_company_dailyReview";
    }


    //插入或者修改成绩
    @RequestMapping(value="/comTeaInsertDailyScore/{sno},{score},{isSubmit}",method = RequestMethod.POST)
    public String comTeaInsertDailyScore(@PathVariable("sno") String sno,
                                         @PathVariable("score") String score,
                                         @PathVariable("isSubmit") String isSubmit,
                                         HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){


        if("无法提交".equals(isSubmit)){
            System.out.println("成绩无法提交！");
        }else{
            int dailyScore = Integer.valueOf(score);
            if(selectStuComDailyScore(sno)==null){
                //如果该生在数据表中不存在，就插入一条完整的成绩信息
                insertStuComDailyScore(sno,dailyScore,0,0,"","","");
            }else{
                //如过该生在数据表中存在，就更新学生的平时成绩
                updateStuComDailyScore(sno,dailyScore);
            }
            stuScoreUpdateInSubmitDailyScoreInCom(sno);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StuComScore> studentComScoreList = getStuComScore(tno);
        httpSession.setAttribute("studentComDailyScoreNum",studentComScoreList.size());
        model.addAttribute("studentComDailyScoreList",studentComScoreList);

        return "tea_company/tea_company_dailyReview";
    }


    //得到平时成绩页面的数据
    public List<StuComScore> getStuComScore(String tno){
        //根据校外指导教师学工号找到老师姓名
        String tName = findComDailyMyNameByTno(tno);
        //根据老师姓名查找学生信息
        List<StudentInfo> list = findComDailyMyStuByTname(tName);
        //用来存储这些学生的成绩（校外指导教师给的成绩）
        List<StuComScore> studentComScoreList = new ArrayList<>();
        //for循环，来查询这些学生的成绩信息
        for(int i = 0;i<list.size();i++){
            StuComScore stuComScore = new StuComScore();
            System.out.println("进入循环...");
            //如果校外成绩表和实训成绩表中都没有该生的姓名,那么成绩默认为0
            if(selectStuComDailyScore(list.get(i).getSno())==null){//校外成绩表没有该生的姓名
                System.out.println("进入第一层if");
                if(getComStuDailyScore(list.get(i).getSno())== null) {//实训成绩表没有该生的姓名，成绩默认为0,校外老师可以打分
                    System.out.println("进入第二层if");
                    stuComScore.setSno(list.get(i).getSno());
                    stuComScore.setDailyScore(0);
                    stuComScore.setMiddleScore(0);
                    stuComScore.setFinalScore(0);
                    stuComScore.setMiddleFilePath("可以打分");
                }else{//校外成绩表没有姓名，但实训成绩表有姓名，获取实训成绩表中的成绩，校外老师不可以打分
                    stuComScore.setSno(list.get(i).getSno());
                    stuComScore.setDailyScore(getComStuDailyScore(list.get(i).getSno()).getDailyScore());
                    stuComScore.setMiddleFilePath("禁止打分");
                }
            }else{//校外成绩表有该生的姓名,校外老师可以打分
                stuComScore = selectStuComDailyScore(list.get(i).getSno());
                stuComScore.setMiddleFilePath("可以打分");
            }
            studentComScoreList.add(stuComScore);
        }
        return studentComScoreList;
    }

    public void stuScoreUpdateInSubmitDailyScoreInCom(String sno){
        //判断该学生的实训类型
        //如果是校内实训：成绩不受个人影响，也不受指导教师的影响，完全有系统管理员导入成绩
        //如果为校外自行联系：成绩只受校内指导老师的分数影响
        //如果是校内合作实训公司实训，成绩既受管理员影响、又受校内外指导教师影响
        String type = getStuPTInfoInSubmitDailyScoreInCom(sno).getTypeName();
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
            if(selectStuScoreInSubmitDailyScoreInCom(sno)==null){//实训成绩表中没有该学生的成绩信息,需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitDailyScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitDailyScoreInCom(sno,dailyScore,score1);
            }
        }else if("校企合作".equals(type)){
            //获取平时成绩、中期成绩、实训报告成绩、实训成绩
            scoreMap = countScore.countComAllScore(sno);
            int dailyScore = Integer.valueOf(scoreMap.get("dailyScore").toString());
            int middleScore1 = Integer.valueOf(scoreMap.get("middleScore").toString());
            int finalScore = Integer.valueOf(scoreMap.get("finalScore").toString());
            int score1 = Integer.valueOf(scoreMap.get("score").toString());
            //同时更新实训成绩表中的平时成绩、中期成绩、实训报告成绩、实训成绩
            if(selectStuScoreInSubmitDailyScoreInCom(sno)==null){//实训成绩表中没有该学号是呢过的成绩信息，需要插入一条完整的成绩信息
                insertStuScoreInfoInSubmitDailyScoreInCom(sno,dailyScore,middleScore1,finalScore,score1);
            }else{//实训成绩表中有该学生的成绩信息，只需要更新平时成绩和实训成绩
                updateStuMiddleScoreInSubmitDailyScoreInCom(sno,dailyScore,score1);
            }
        }
    }




    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    StuComScoreService stuComScoreService;

    @Autowired
    StuScoreService stuScoreService;


    @GetMapping("/selectComDailyMyNameByTno/{tno}")
    public String findComDailyMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComDailyMyStuByTname/{tName}")
    public List<StudentInfo> findComDailyMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectStuComDailyScore/{sno}")
    public StuComScore selectStuComDailyScore(@PathVariable("sno") String sno){
        return stuComScoreService.selectStuComScore(sno);
    }

   @GetMapping("/insertStuComDailyScore/{sno},{dailyScore},{middleScore},{ptScore},{middleFilePath},{finalFilePath},{finalProjectPath}")
    public int insertStuComDailyScore(@PathVariable("sno") String sno,
                                      @PathVariable("dailyScore") int dailyScore,
                                      @PathVariable("middleScore") int middleScore,
                                      @PathVariable("ptScore") int ptScore,
                                      @PathVariable("middleFilePath") String middleFilePath,
                                      @PathVariable("finalFilePath") String finalFilePath,
                                      @PathVariable("finalProjectPath") String finalProjectPath){
        return stuComScoreService.insertStuComScore(sno,dailyScore,middleScore,ptScore,middleFilePath,finalFilePath,finalProjectPath);

   }

   @GetMapping("/updateStuComDailyScore/{sno},{dailyScore}")
    public int updateStuComDailyScore(@PathVariable("sno") String sno,
                                      @PathVariable("dailyScore") int dailyScore){
        return stuComScoreService.updateStuComDailyScore(sno,dailyScore);
   }


   @GetMapping("/getComStuDailyScore/{sno}")
    public StudentScore getComStuDailyScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
   }

    @RequestMapping("/updateStuMiddleScoreInSubmitDailyScoreInCom/{sno},{dailyScore},{score}")
    public int updateStuMiddleScoreInSubmitDailyScoreInCom(@PathVariable("sno") String sno,
                                                           @PathVariable("dailyScore") int dailyScore,
                                                           @PathVariable("score") int score){
        return stuScoreService.updateStuDailyScore(sno,dailyScore,score);

    }

    @RequestMapping("/selectStuScoreInSubmitDailyScoreInCom/{sno}")
    public StudentScore selectStuScoreInSubmitDailyScoreInCom(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }


    @RequestMapping("/getStuPTInfoInSubmitDailyScoreInCom/{sno}")
    public StudentInfo getStuPTInfoInSubmitDailyScoreInCom(@PathVariable("sno") String sno){
        return ptInformationService.findBySno(sno);
    }

    @GetMapping("/insertStuScoreInfoInSubmitDailyScoreInCom/{sno},{dailyScore},{middleScore},{ptScore},{score}")
    public int insertStuScoreInfoInSubmitDailyScoreInCom(@PathVariable("sno") String sno,
                                                         @PathVariable("dailyScore") int dailyScore,
                                                         @PathVariable("middleScore") int middleScore,
                                                         @PathVariable("ptScore") int ptScore,
                                                         @PathVariable("score") int score){
        return stuScoreService.insertStuDailyScore(sno, dailyScore, middleScore, ptScore, score);
    }

}
