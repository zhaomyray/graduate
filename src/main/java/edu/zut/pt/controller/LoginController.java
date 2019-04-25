package edu.zut.pt.controller;

import edu.zut.pt.mapper.Teacher_CompanyMapper;
import edu.zut.pt.mapper.Teacher_SchoolMapper;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.Teacher_Company;
import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.service.Teacher_CompanyService;
import edu.zut.pt.service.Teacher_SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录验证控制类
 */

@Controller
public class LoginController {

    @Autowired
    StudentController studentController;
    @Autowired
    PTInformationController ptInformationController;
    @Autowired
    StuWeekReportController stuWeekReportController;
    @Autowired
    StuMonthReportController stuMonthReportController;
    @Autowired
    StuMiddleReportController stuMiddleReportController;


    /**
     * 登录页面的映射
     * @return
     */
    @GetMapping("login")
//    @RequestMapping("login")
    public String login(){
        return "login";
    }


//    @GetMapping("perfect")
//    public String perfectInformation(){
//        return "perfectInformation";
//    }

    /**
     * 学生登录页面验证
     * @param username
     * @param password
     * @param stuMap
     * @param session
     * @return
     */
    @PostMapping(value="/student/login")
    public String stu_loginVal(@RequestParam("stu_username") String username,
                           @RequestParam("stu_psd") String password,
                           Map<String ,Object> stuMap, HttpSession session) {


        Student student = studentController.findStudentBySnoPsd(username, password);
        String type = ptInformationController.findBySno(username).getTypeName();
        int weekReport = stuWeekReportController.selectWeekReportBySno(username).size();
        int monthReport = stuMonthReportController.selectMonthReportBySno(username).size();
        int middleReport = stuMiddleReportController.selectMiddleReportBySno(username).size();
        int weekIsAfter = 0;
        for(int i = 0;i<weekReport;i++){
            if("true".equals(stuWeekReportController.selectWeekReportBySno(username).get(i).getIsAfter())){
                weekIsAfter = weekIsAfter + 1;
            }
        }
//        String weekRecentTime="---";
//        if(stuWeekReportController.selectWeekReportBySno(username).get(weekReport-1)!=null){
//            weekRecentTime = stuWeekReportController.selectWeekReportBySno(username).get(weekReport-1).getTime_submit();
//        }
//        String weekRecentTime = stuWeekReportController.selectWeekReportBySno(username).get(weekReport-1).getTime_submit();
        int monthIsAfter = 0;
        for(int i = 0;i<monthReport;i++){
            if("true".equals(stuMonthReportController.selectMonthReportBySno(username).get(i).getIsAfter())){
                monthIsAfter = monthIsAfter + 1;
            }
        }
//        String monthRecentTime="---";
//        if(stuMonthReportController.selectMonthReportBySno(username).get(monthReport-1)!=null){
//            monthRecentTime = stuMonthReportController.selectMonthReportBySno(username).get(monthReport-1).getTime_submit();
//        }
//        String monthRecentTime = stuMonthReportController.selectMonthReportBySno(username).get(monthReport-1).getTime_submit();


        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            stuMap.put("stuMsg", "用户名或密码不能为空");
            return "login";
        } else if (student == null) {
            stuMap.put("stuMsg", "用户名或密码错误");
            return "login";
        } else {
            session.setAttribute("login", username);
            session.setAttribute("shouldSubmitMiddle",1);
            session.setAttribute("shouldSubmitReport",1);
            session.setAttribute("shouldSubmitProject",1);
            session.setAttribute("hadSubmitMiddle",middleReport);
            session.setAttribute("hadSubmitReport",0);
            session.setAttribute("hadSubmitProject",0);
            session.setAttribute("isAfterMiddle",0);
            session.setAttribute("isAfterReport",0);
            session.setAttribute("isAfterProject",0);
            session.setAttribute("middleRecent","---");
            session.setAttribute("reportRecent","---");
            session.setAttribute("projectRecent","---");
            if("校外自行联系".equals(type)){
                session.setAttribute("shouldSubmitWeek",12);
                session.setAttribute("shouldSubmitMonth",2);
                session.setAttribute("hadSubmitWeek",weekReport);
                session.setAttribute("hadSubmitMonth",monthReport);
                session.setAttribute("isAfterWeek",weekIsAfter);
                session.setAttribute("isAfterMonth",monthIsAfter);
//                session.setAttribute("weekRecent",weekRecentTime);
//                session.setAttribute("monthRecent",monthRecentTime);
                return "redirect:/stuMain.html";
            }else{
                session.setAttribute("shouldSubmitWeek",0);
                session.setAttribute("shouldSubmitMonth",0);
                session.setAttribute("hadSubmitWeek",0);
                session.setAttribute("hadSubmitMonth",0);
                session.setAttribute("isAfterWeek",0);
                session.setAttribute("isAfterMonth",0);
//                session.setAttribute("weekRecent","--");
//                session.setAttribute("monthRecent","--");
                return "redirect:/stu_Main.html";
            }

        }
    }


    @Autowired
    Teacher_SchoolService teacher_schoolService;
    @Autowired
    Teacher_CompanyService teacher_companyService;

    /**
     * 指导老师的登录验证
     * @param username
     * @param password
     * @param request
     * @param teaMap
     * @param session
     * @return
     */
    @PostMapping(value = "/teacher/login")
    public String tea_loginVal(@RequestParam("tea_username") String username,
                               @RequestParam("tea_psd") String password,
                               HttpServletRequest request,
                               Map<String,Object> teaMap,HttpSession session){

        String flag = request.getParameter("tea_role");

       if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
           teaMap.put("teaMsg","用户名或密码不能为空") ;
           return "login";
       }else if("校外".equals(flag)){
           Teacher_Company teacher_company = teacher_companyService.findTeaComByTnoPsd(username,password);
           if(teacher_company==null){
               teaMap.put("teaMsg","用户名或密码错误");
               return "login";
           }else{
               session.setAttribute("login",username);
               return "redirect:/teaCompanyMain.html";
//           return "redirect:/tea_school_homePage";
           }
       }else{
           Teacher_School teacher_school = teacher_schoolService.findTeaSchByTnoPsd(username,password);
           if(teacher_school==null){
               teaMap.put("teaMsg","用户名或密码错误");
               return "login";
           }else{
               session.setAttribute("login",username);
               return "redirect:/teaSchoolMain.html";
           }
       }
    }


    @GetMapping("editPSD")
    public String updatePSD(){
        return "updatePsd";
    }

    @GetMapping("stu_homePage")
    public String stuHome(){
        return "stu_homePage";
    }

    @GetMapping("tea_school_homePage")
    public String teaSchHome(){
        return "tea_school_homePage";
    }

    @GetMapping("tea_company_homePage")
    public String teaComHome(){
        return "tea_company_homePage";
    }

    @GetMapping("stu_mainPage")
    public String stuMainPage(){
        return "/stu/stu_mainPage";
    }

    @GetMapping("tea_school_mainPage")
    public String teaSchMainPage(){
        return "/tea_school/tea_school_mainPage";
    }

    @GetMapping("tea_company_mainPage")
    public String teaComMainPage(){
        return "/tea_company/tea_company_mainPage";
    }


    @GetMapping("can")
    public String can(){
        return "can";
    }



    @GetMapping("stu_approvalTable")
    public String approvalTable(){
        return "/stu/stu_approvalTable";
    }

    @GetMapping("welcome")
    public String welcome(){
        return "welcome";
    }


    @GetMapping("changePassword")
    public String change(){
        return "changePassword";
    }

    @GetMapping("sorryCoding")
    public String sorry(){
        return "sorryCoding";
    }

//    @GetMapping("stu_weekReport")
//    public String week(){
//        return "/stu/stu_weekReport";
//    }



    @GetMapping("stu_final")
    public String stu_final(){
        return "/stu/stu_final";
    }




















    @GetMapping("tea_school_finalReportReview")
    public String tea_school_finalReview(){
        return "/tea_school/tea_school_finalReportReview";
    }










    @GetMapping("tea_company_PTReview")
    public String tea_company_pt(){
        return "/tea_company/tea_company_ptReview";
    }



}
