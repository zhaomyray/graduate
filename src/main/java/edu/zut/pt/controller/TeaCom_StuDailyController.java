package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.mapper.TeaComStuScoreMapper;
import edu.zut.pt.mapper.Teacher_CompanyMapper;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.StudentComScore;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.TeaComStuScoreService;
import edu.zut.pt.service.Teacher_CompanyService;
import org.apache.ibatis.annotations.Mapper;
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
import java.util.List;

@Controller
public class TeaCom_StuDailyController {

    @GetMapping("tea_company_dailyReview")
    public String tea_company_daily(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentComScore> studentComScoreList = getStuComScore(tno);
        httpSession.setAttribute("studentComDailyScoreNum",studentComScoreList.size());
        model.addAttribute("studentComDailyScoreList",studentComScoreList);

        return "tea_company/tea_company_dailyReview";
    }

    @RequestMapping(value="/comTeaInsertDailyScore",method = RequestMethod.POST)
    public String comTeaInsertDailyScore(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){


        String sno = httpServletRequest.getParameter("dailyScoreSno").replace("\"","");
        int dailyScore = Integer.valueOf(httpServletRequest.getParameter("dailyScore"));
        if(selectStuExit(sno)==null){
            insertComDailyStuScore(sno,dailyScore);
        }else{
            updateStuDailyScore(sno,dailyScore);
        }

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentComScore> studentComScoreList = getStuComScore(tno);
        httpSession.setAttribute("studentComDailyScoreNum",studentComScoreList.size());
        model.addAttribute("studentComDailyScoreList",studentComScoreList);

        return "tea_company/tea_company_dailyReview";
    }


    public List<StudentComScore> getStuComScore(String tno){
        String tName = findComDailyMyNameByTno(tno);
        List<StudentInfo> list = findComDailyMyStuByTname(tName);
        List<StudentComScore> studentComScoreList = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            StudentComScore studentComScore = new StudentComScore();
            if(getComDailyStuScoreBySno(list.get(i).getSno())==null||"".equals(getComDailyStuScoreBySno(list.get(i).getSno()).getDailyScore())){
                studentComScore.setComSno(list.get(i).getSno());
                studentComScore.setDailyScore(0);
            }else{
                studentComScore = getComDailyStuScoreBySno(list.get(i).getSno());
            }
            studentComScoreList.add(studentComScore);
        }
        return studentComScoreList;
    }

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    TeaComStuScoreService teaComStuScoreService;


    @GetMapping("/selectComDailyMyNameByTno/{tno}")
    public String findComDailyMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComDailyMyStuByTname/{tName}")
    public List<StudentInfo> findComDailyMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectComDailyStuScoreBySno/{sno}")
    public StudentComScore getComDailyStuScoreBySno(@PathVariable("sno") String sno){
        return teaComStuScoreService.getComStuScore(sno);
    }

    @GetMapping("/insertComDailyStuScore/{sno},{dailyScore}")
    public int insertComDailyStuScore(@PathVariable("sno") String sno,
                                      @PathVariable("dailyScore") int dailyScore){
        return teaComStuScoreService.insertStuDailyScore(sno,dailyScore);
    }

    @GetMapping("/selectStuScoreIS/{sno}")
    public StudentComScore selectStuExit(@PathVariable("sno") String sno){
        return teaComStuScoreService.getComStuScore(sno);
    }

    @GetMapping("/updateStuDailyScore/{sno},{dailyScore}")
    public int updateStuDailyScore(@PathVariable("sno") String sno,
                                   @PathVariable("dailyScore") int dailyScore){
        return teaComStuScoreService.updateStuDailyScore(sno,dailyScore);

    }

}
