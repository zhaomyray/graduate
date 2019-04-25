package edu.zut.pt.controller;

import edu.zut.pt.pojo.StudentComScore;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.TeaComStuScoreService;
import edu.zut.pt.service.Teacher_CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaCom_StuScoreController {

    @GetMapping("tea_company_stuScore")
    public String tea_company_stuScore(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String tname = findComMyNameByTno(tno);
        List<StudentInfo> list = findComMyStuByTname(tname);
        List<StudentComScore> stuComScoreList = new ArrayList<>();

        for(int i = 0;i<list.size();i++){
            StudentComScore studentComScore = new StudentComScore();
            if(getComStuScoreBySno(list.get(i).getSno())==null){
                studentComScore.setId(i);
                studentComScore.setComSno(list.get(i).getSno());
                studentComScore.setDailyScore(0);
                studentComScore.setMiddleScore(0);
                studentComScore.setPtScore(0);
            }else{
                studentComScore = getComStuScoreBySno(list.get(i).getSno());
            }
            stuComScoreList.add(studentComScore);
        }
        httpSession.setAttribute("stuComScoreNum",stuComScoreList.size());
        model.addAttribute("stuComScoreList",stuComScoreList);
        return "/tea_company/tea_company_stuScore";
    }

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    TeaComStuScoreService teaComStuScoreService;


    @GetMapping("/selectComMyNameByTno/{tno}")
    public String findComMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComMyStuByTname/{tName}")
    public List<StudentInfo> findComMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectComStuScoreBySno/{sno}")
    public StudentComScore getComStuScoreBySno(@PathVariable("sno") String sno){
        return teaComStuScoreService.getComStuScore(sno);
    }


}
