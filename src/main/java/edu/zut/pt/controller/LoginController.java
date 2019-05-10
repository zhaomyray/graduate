package edu.zut.pt.controller;

import edu.zut.pt.controller.stuController.*;
import edu.zut.pt.pojo.*;
import edu.zut.pt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
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
    @Autowired
    StuFinalReportController stuFinalReportController;


    /**
     * 登录页面的映射
     * @return
     */
    @GetMapping("login")
//    @RequestMapping("login")
    public String login(){
        return "login";
    }


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
        int shouldSubmitWeek = getAllMessage(1).getWeekNums();
        int shouldSubmitMonth = getAllMessage(1).getMonthNums();

        int finalReport;
        int finalProject;
        if(stuFinalReportController.findFinalReport(username)!=null){
            FinalReport finalReport1 = stuFinalReportController.findFinalReport(username);
            if(finalReport1.getAdd_finReport()==""){
                finalReport = 0;
            }else{
                finalReport = 1;
            }
            if(finalReport1.getAdd_finalProject()==""){
                finalProject = 0;
            }else{
                finalProject = 1;
            }
        }else{
            finalReport = 0;
            finalProject = 0;
        }
        int weekIsAfter = 0;
        //计算补交的周报次数
        for(int i = 0;i<weekReport;i++){
            if("是".equals(stuWeekReportController.selectWeekReportBySno(username).get(i).getIsAfter())){
                weekIsAfter = weekIsAfter + 1;
            }
        }
        //计算补交的月报次数
        int monthIsAfter = 0;
        for(int i = 0;i<monthReport;i++){
            if("是".equals(stuMonthReportController.selectMonthReportBySno(username).get(i).getIsAfter())){
                monthIsAfter = monthIsAfter + 1;
            }
        }
        //判断中期报告是否补交
        int middleIsAfter ;
        if(stuMiddleReportController.selectMiddleReportBySno(username).size()==0){
            System.out.println("没有该生的中期报告信息");
            middleIsAfter = 0;
        }else{
            if("是".equals(stuMiddleReportController.selectMiddleReportBySno(username).get(0).getIsAfter())){
                middleIsAfter = 1;
            }else{
                middleIsAfter = 0;
            }
        }
        //判断实训报告和项目是否补交
        int finalIsAfter;
        if(stuFinalReportController.findFinalReport(username)!=null){
            if("是".equals(stuFinalReportController.findFinalReport(username).getIsAfter())){
                finalIsAfter = 1;
            }else{
                finalIsAfter = 0;
            }
        }else{
            finalIsAfter = 0;
        }


        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            stuMap.put("stuMsg", "用户名或密码不能为空");
            return "login";
        } else if (student == null) {
            stuMap.put("stuMsg", "用户名或密码错误");
            return "login";
        } else {
            //获取页面数据信息
            session.setAttribute("login", username);
            session.setAttribute("shouldSubmitMiddle",1);//应该提交的中期报告次数
            session.setAttribute("shouldSubmitReport",1);//应该提交的实训报告次数
            session.setAttribute("shouldSubmitProject",1);//应该提交的项目次数
            session.setAttribute("hadSubmitMiddle",middleReport);//已经提交的中期报告次数
            session.setAttribute("hadSubmitReport",finalReport);//已经提交的实训报告次数
            session.setAttribute("hadSubmitProject",finalProject);//已经提交的项目次数
            session.setAttribute("isAfterMiddle",middleIsAfter);//中期报告补交
            session.setAttribute("isAfterReport",finalIsAfter);//实训报告补交
            session.setAttribute("isAfterProject",finalIsAfter);//项目补交
            if("校外自行联系".equals(type)){
                session.setAttribute("shouldSubmitWeek",shouldSubmitWeek);//应该提交的周报次数
                session.setAttribute("shouldSubmitMonth",shouldSubmitMonth);//应该提交的月报次数
                session.setAttribute("hadSubmitWeek",weekReport);//已经提交的周报次数
                session.setAttribute("hadSubmitMonth",monthReport);//已经提交的月报次数
                session.setAttribute("isAfterWeek",weekIsAfter);//补交的周报次数
                session.setAttribute("isAfterMonth",monthIsAfter);//补交的月报次数
                return "redirect:/stuMain.html";
            }else{
                //如果实训类型不是校外自行联系，关于周月报的信息都为0
                session.setAttribute("shouldSubmitWeek",0);//应该提交的周报次数
                session.setAttribute("shouldSubmitMonth",0);//应该提交的月报次数
                session.setAttribute("hadSubmitWeek",0);//已经提交的周报次数
                session.setAttribute("hadSubmitMonth",0);//已经提交的周报次数
                session.setAttribute("isAfterWeek",0);//补交的周报次数
                session.setAttribute("isAfterMonth",0);//补交的周报次数
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
               findStuMiddleReportUpdate(username,session);
               findStuFinalReportUpdate(username,session);
               session.setAttribute("login",username);
               return "redirect:/teaSchoolMain.html";
           }
       }
    }

    @Autowired
    MiddleReportService middleReportService;

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    FinalReportService finalReportService;

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
    public String stuMainPage(HttpSession httpSession){
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


//    @GetMapping("can")
//    public String can(){
//        return "can";
//    }



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


    @Autowired
    TimeMessageService timeMessageService;

    @RequestMapping("getAllMessage/{id}")
    public TimeMessage getAllMessage(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }


    //获取中期报告的状态
    public void findStuMiddleReportUpdate(String username,HttpSession httpSession){
        //根据学工号查找自己的学生
        List<StudentInfo> myStuInfo = ptInformationService.findBySchTeaId(username);
        int updateMiddleFlag = 0;
        int submitMiddleFlag = 0;
        for(int i = 0;i<myStuInfo.size();i++){
            //如果发现某个学号的学生的中期报告已经提交，就获取他的报告的更新状态
            if(middleReportService.findMiddleReportBySno(myStuInfo.get(i).getSno()).size()>0){
                //获取该学生的中期报告的信息
                MiddleReport middleReport = middleReportService.findMiddleReportBySno(myStuInfo.get(i).getSno()).get(0);
                if("更新".equals(middleReport.getIsUpdate())){
                    updateMiddleFlag = 1;
                }else if("刚刚提交".equals(middleReport.getIsUpdate())){
                    submitMiddleFlag = 1;
                }else{
                    System.out.println("报告都已下载");
                }
            }
        }
        if(updateMiddleFlag==1){
            httpSession.setAttribute("middleFlag","更新");
        }else if(submitMiddleFlag==1){
            httpSession.setAttribute("middleFlag","刚刚提交");
        }else{
            httpSession.setAttribute("middleFlag","已下载");
        }
    }

    //获取实训报告的状态
    public void findStuFinalReportUpdate(String username,HttpSession httpSession){
        //根据学工号查找自己的学生
        List<StudentInfo> myStuInfo = ptInformationService.findBySchTeaId(username);
        int updateFinalFlag = 0;
        int submitFinalFlag = 0;
        for(int i = 0;i<myStuInfo.size();i++){
            //如果发现某个学号的学生的中期报告已经提交，就获取他的报告的更新状态
            if(finalReportService.findFinalReportBySno(myStuInfo.get(i).getSno())!=null){
                //获取该学生的实训报告和项目的状态信息
                FinalReport finalReport = finalReportService.findFinalReportBySno(myStuInfo.get(i).getSno());
                if("更新".equals(finalReport.getIsUpdate())){
                    updateFinalFlag = 1;
                }else if("刚刚提交".equals(finalReport.getIsUpdate())){
                    submitFinalFlag = 1;
                }else{
                    System.out.println("报告都已下载");
                }
            }
        }
        if(updateFinalFlag==1){
            httpSession.setAttribute("finalFlag","更新");
        }else if(submitFinalFlag==1){
            httpSession.setAttribute("finalFlag","刚刚提交");
        }else{
            httpSession.setAttribute("finalFlag","已下载");
        }
    }



}
