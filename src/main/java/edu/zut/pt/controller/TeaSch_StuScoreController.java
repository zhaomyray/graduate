package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.mapper.TeaSchScoreMapper;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MidFinalScore;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.TeaSchScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaSch_StuScoreController {

    @GetMapping("tea_school_stuScore")
    public String tea_stuScore(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> infoList = getSchAllStudent(tno);

        List<MidFinalScore> scoreList = new ArrayList<MidFinalScore>();
        for(int i = 0;i<infoList.size();i++){
            String sno = infoList.get(i).getSno();
            String sname = infoList.get(i).getSname();
            int midScore;
            if(getStuMidScore(sno)==null){
                midScore = 0;
            }else{
                midScore = getStuMidScore(sno).getScore();
            }
            System.out.println("中期成绩"+midScore);
            int finalScore;
            if(getStuFinalScore(sno)==null){
                finalScore = 0;
            }else{
                finalScore = getStuFinalScore(sno).getScore();
            }

            System.out.println("实训成绩："+finalScore);
            MidFinalScore midFinalScore = new MidFinalScore();
            midFinalScore.setSno(sno);
            midFinalScore.setSname(sname);
            midFinalScore.setMidScore(midScore);
            midFinalScore.setFinalScore(finalScore);

            scoreList.add(midFinalScore);
        }

        httpSession.setAttribute("midFinalNum",infoList.size());
        model.addAttribute("midFinalScore",scoreList);

        return "/tea_school/tea_school_stuScore";
    }

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    TeaSchScoreService teaSchScoreService;

    @GetMapping("/selectSchAllStu/{tno}")
    public List<StudentInfo> getSchAllStudent(@PathVariable("tno") String tno){
        return ptInformationService.findBySchTeaId(tno);
    }

    @GetMapping("/selectStuMidScore/{sno}")
    public MiddleReport getStuMidScore(@PathVariable("sno") String sno){
        return teaSchScoreService.findMidScoreBySno(sno);
    }

    @GetMapping("/selectStuFinalScore/{sno}")
    public FinalReport getStuFinalScore(@PathVariable("sno") String sno){
        return teaSchScoreService.findFinalScoreBySno(sno);
    }
}
