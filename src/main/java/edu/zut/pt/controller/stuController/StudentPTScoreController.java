package edu.zut.pt.controller.stuController;

import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.StuScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentPTScoreController {

    @GetMapping("stu_ptScore")
    public String welcome(HttpServletRequest httpServletRequest, Model model){

        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentScore> studentScoreList = new ArrayList<>();
        StudentScore studentScore = new StudentScore();
        if(getMyScore(sno)==null){
            studentScore.setId(1);
            studentScore.setDailyScore(0);
            studentScore.setMiddleScore(0);
            studentScore.setPtScore(0);
            studentScore.setScore(0);
            studentScore.setSno(sno);
        }else{
            studentScore = getMyScore(sno);
        }

        studentScoreList.add(studentScore);

        model.addAttribute("studentScoreList",studentScoreList);
        return "stu/stu_ptScore";
    }

    @Autowired
    StuScoreService stuScoreService;

    @RequestMapping("/getMyScore/{sno}")
    public StudentScore getMyScore(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }


}
