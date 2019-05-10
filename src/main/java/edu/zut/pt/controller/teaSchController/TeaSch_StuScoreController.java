package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.pojo.*;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.TeaSchScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaSch_StuScoreController {

    @GetMapping("tea_school_stuScore")
    public String tea_stuScore(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getStuScorePageInfo(tno,httpSession,model);
        return "/tea_school/tea_school_stuScore";
    }

    @RequestMapping(value = "/importScoreBySelect",method = RequestMethod.POST)
    public String importScoreBySelect(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){
        String option = httpServletRequest.getParameter("importScore");

        System.out.println("准备导出成绩啦！"+option);
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        getStuScorePageInfo(tno,httpSession,model);
        return "/tea_school/tea_school_stuScore";
    }


    public void getStuScorePageInfo(String tno,HttpSession httpSession,Model model){
        List<StudentInfo> infoList = new ArrayList<>();

        List<StudentScore> studentScoreList = new ArrayList<>();

        if(getSchAllStudent(tno).size()>0){
            infoList = getSchAllStudent(tno);
            for(int i = 0;i<infoList.size();i++){
                StudentScore studentScore = new StudentScore();
                if(getMyStuScoreTeaSch(infoList.get(i).getSno())==null){
                    studentScore.setSno(infoList.get(i).getSno());
                    studentScore.setDailyScore(0);
                    studentScore.setMiddleScore(0);
                    studentScore.setPtScore(0);
                    studentScore.setScore(0);
                }else{
                    studentScore = getMyStuScoreTeaSch(infoList.get(i).getSno());
                }
                studentScoreList.add(studentScore);
            }
        }

        httpSession.setAttribute("stuTeaSchNum",infoList.size());
        model.addAttribute("stuScoreTeaSchList",studentScoreList);
    }

    @Autowired
    PTInformationService ptInformationService;

//    @Autowired
//    TeaSchScoreService teaSchScoreService;

    @Autowired
    StuScoreService stuScoreService;

    @GetMapping("/selectSchAllStu/{tno}")
    public List<StudentInfo> getSchAllStudent(@PathVariable("tno") String tno){
        return ptInformationService.findBySchTeaId(tno);
    }

//    @GetMapping("/selectStuMidScore/{sno}")
//    public MiddleReport getStuMidScore(@PathVariable("sno") String sno){
//        return teaSchScoreService.findMidScoreBySno(sno);
//    }
//
//    @GetMapping("/selectStuFinalScore/{sno}")
//    public FinalReport getStuFinalScore(@PathVariable("sno") String sno){
//        return teaSchScoreService.findFinalScoreBySno(sno);
//    }

    @RequestMapping("/getMyStuScoreTeaSch/{sno}")
    public StudentScore getMyStuScoreTeaSch(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }

}
