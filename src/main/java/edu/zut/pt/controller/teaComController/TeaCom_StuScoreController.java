package edu.zut.pt.controller.teaComController;

import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.StuScoreService;
import edu.zut.pt.service.Teacher_CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaCom_StuScoreController {

    @GetMapping("tea_company_stuScore")
    public String tea_company_stuScore(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String name = findMyNameByTno(tno);
        //存储学生的实训信息
        List<StudentInfo> infoList = new ArrayList<>();
        if(findMyStuByTname(name).size()>0){//如果有我的学生,获取我的学生的实训信息
            infoList = findMyStuByTname(name);
        }

        //存储学生的成绩信息
        List<StudentScore> studentScoreList = new ArrayList<>();
        if(infoList.size()>0){//如果我的学生的实训信息列表不为空,就去获取学生的成绩信息
            for(int i = 0;i<infoList.size();i++){
                StudentScore studentScore = new StudentScore();
                if(getMyStuScoreTeaCom(infoList.get(i).getSno())==null){//如果该学生的成绩信息不存在
                    studentScore.setSno(infoList.get(i).getSno());
                    studentScore.setPtScore(0);
                    studentScore.setScore(0);
                    studentScore.setDailyScore(0);
                    studentScore.setMiddleScore(0);
                }else{//如果该学生的成绩信息存在
                    studentScore = getMyStuScoreTeaCom(infoList.get(i).getSno());
                }
                studentScoreList.add(studentScore);
            }
        }
        httpSession.setAttribute("stuTeaComNum",infoList.size());
        model.addAttribute("stuScoreTeaComList",studentScoreList);
        return "tea_company/tea_company_stuScore";
    }

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    StuScoreService stuScoreService;




    @GetMapping("/selectComMyNameByTno/{tno}")
    public String findMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComMyStuByTname/{tName}")
    public List<StudentInfo> findMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }


    @RequestMapping("/getMyStuScoreTeaCom/{sno}")
    public StudentScore getMyStuScoreTeaCom(@PathVariable("sno") String sno){
        return stuScoreService.getStuScore(sno);
    }


}
