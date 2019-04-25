package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.mapper.TeaComStuScoreMapper;
import edu.zut.pt.mapper.Teacher_CompanyMapper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeaCom_StuMiddleController {

    @GetMapping("tea_company_middleReview")
    public String tea_company_middle(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentComScore> studentComScoreList = getStuMiddleScore(tno);
        httpSession.setAttribute("stuComMiddleScoreNum",studentComScoreList.size());
        model.addAttribute("stuComMiddleScore",studentComScoreList);
        return "/tea_company/tea_company_middleReview";
    }

    @RequestMapping(value="/comTeaInsertMiddleScore",method = RequestMethod.POST)
    public String comTeaInsertMiddleScore(HttpServletRequest httpServletRequest,HttpSession httpSession,Model model){

        System.out.print("跳入该函数！");
        String sno = httpServletRequest.getParameter("middleScoreSno").replace("\"","");
        int middleScore;
        if(httpServletRequest.getParameter("middleScore")==null){
            System.out.print("确定成绩是否为空！");
            middleScore = 0;
        }else {
            middleScore = Integer.valueOf(httpServletRequest.getParameter("middleScore"));
        }

        if(selectStuExit(sno)==null){
            System.out.print("确定学生是否存在在成绩表！");
            insertComMiddleStuScore(sno,middleScore);
            System.out.print("插入成绩！");
        }else{
            updateStuMiddleScore(sno,middleScore);
            System.out.print("更新成绩！");
        }
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentComScore> studentComScoreList = getStuMiddleScore(tno);
        httpSession.setAttribute("stuComMiddleScoreNum",studentComScoreList.size());
        model.addAttribute("stuComMiddleScore",studentComScoreList);

        return "tea_company/tea_company_middleReview";
    }

    public List<StudentComScore> getStuMiddleScore(String tno){
        String tname = findComMiddleMyNameByTno(tno);
        List<StudentInfo> list = findComMiddleMyStuByTname(tname);
        List<StudentComScore> studentComScoreList = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            StudentComScore studentComScore = new StudentComScore();
            if(getComMiddleStuScoreBySno(list.get(i).getSno())==null||"".equals(getComMiddleStuScoreBySno(list.get(i).getSno()).getMiddleScore())){
                studentComScore.setComSno(list.get(i).getSno());
                studentComScore.setMiddleScore(0);
            }else{
                studentComScore = getComMiddleStuScoreBySno(list.get(i).getSno());
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


    @GetMapping("/selectComMiddleMyNameByTno/{tno}")
    public String findComMiddleMyNameByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/selectComMiddleMyStuByTname/{tName}")
    public List<StudentInfo> findComMiddleMyStuByTname(@PathVariable("tName") String tName){
        return ptInformationService.findBySchTeaName(tName);
    }

    @GetMapping("/selectComMiddleStuScoreBySno/{sno}")
    public StudentComScore getComMiddleStuScoreBySno(@PathVariable("sno") String sno){
        return teaComStuScoreService.getComStuScore(sno);
    }

    @GetMapping("/insertComMiddleStuScore/{sno},{middleScore}")
    public int insertComMiddleStuScore(@PathVariable("sno") String sno,
                                      @PathVariable("middleScore") int middleScore){
        return teaComStuScoreService.insertStuMiddleScore(sno,middleScore);
    }

    @GetMapping("/selectStuScoreExit/{sno}")
    public StudentComScore selectStuExit(@PathVariable("sno") String sno){
        return teaComStuScoreService.getComStuScore(sno);
    }

    @GetMapping("/updateMiddleStuScore/{sno},{middleScore}")
    public int updateStuMiddleScore(@PathVariable("sno") String sno,
                                   @PathVariable("middleScore") int middleScore){
        return teaComStuScoreService.updateStuMiddleScore(sno,middleScore);

    }



}
