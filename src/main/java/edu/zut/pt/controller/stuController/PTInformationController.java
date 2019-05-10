package edu.zut.pt.controller.stuController;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import edu.zut.pt.pojo.ClassInformation;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.pojo.TimeMessage;
import edu.zut.pt.service.ClassInformationService;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.Teacher_SchoolService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PTInformationController {

//    @PostMapping(value="/student/perfectInformation")
    @GetMapping("stu_myPTInfo")
    public String myPTInfo(HttpServletRequest httpServletRequest){
        tranValue(httpServletRequest);
        return "stu/stu_myPTInfo";
    }

    @RequestMapping(value="/updateInformation",method = RequestMethod.POST)
//    @ResponseBody
    public String  updateInformation(HttpServletRequest httpServletRequest){

        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setSno(sno);

        String updatePTInfoBan = httpServletRequest.getParameter("updatePTInfoBan");
        if("不能修改".equals(updatePTInfoBan)){

        }else if("可以修改".equals(updatePTInfoBan)) {
            String myPhone = httpServletRequest.getParameter("myPhone");
            String myEmail = httpServletRequest.getParameter("myEmail");
            String myParentPhone = httpServletRequest.getParameter("myParentPhone");
            String ptType = httpServletRequest.getParameter("ptType");
            String ptCompany = httpServletRequest.getParameter("ptCompany");
            String comTeacherName = httpServletRequest.getParameter("comTeacherName");
            String comTeacherPhone = httpServletRequest.getParameter("comTeacherPhone");
            String ptTitle = httpServletRequest.getParameter("ptTitle");
            String ptCity = httpServletRequest.getParameter("ptCity");

            studentInfo.setTypeName(ptType);
            studentInfo.setParPhone(myParentPhone);
            studentInfo.setProjectName(ptTitle);
            studentInfo.setPhone(myPhone);
            studentInfo.setQqemail(myEmail);
            studentInfo.setComTeaName(comTeacherName);
            studentInfo.setComTeaPhone(comTeacherPhone);
            studentInfo.setComName(ptCompany);
            studentInfo.setCity(ptCity);

            String phoneFlag = "^1[34578]\\d{9}$";//联系方式
            String emailFlag = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";//邮箱
            Pattern phonePat = Pattern.compile(phoneFlag);
            Pattern emailPat = Pattern.compile(emailFlag);

            Matcher myPhoneMat = phonePat.matcher(myPhone);
            Matcher myEmailMat = emailPat.matcher(myEmail);
            Matcher myParentPhoneMat = phonePat.matcher(myParentPhone);
            Matcher comTeaPhoneMat = phonePat.matcher(comTeacherPhone);

            if (myPhoneMat.matches() == false) {
                System.out.println("联系方式格式错误！");
            } else if (myEmailMat.matches() == false) {
                System.out.println("邮箱格式错误！");
            } else if (myParentPhoneMat.matches() == false) {
                System.out.println("父母联系方式格式错误");
            } else if (ptType.length() == 0) {
                System.out.println("实训类型不能为空！");
            } else if (ptCompany.length() == 0) {
                System.out.println("实训公司不能为空！");
            } else if (comTeacherName.length() == 0) {
                System.out.println("校外指导老师姓名不能为空！");
            } else if (comTeaPhoneMat.matches() == false) {
                System.out.println("校外指导老师联系方式有误！");
            } else if (ptTitle.length() == 0) {
                System.out.println("实训题目不能为空！");
            } else if (ptCity.length() == 0) {
                System.out.println("实训城市不能为空！");
            } else {
                updateBySno(studentInfo);
            }
        }
//        if(httpServletRequest.getParameter("ptType")==null){
//
//        }else {
//            updateBySno(studentInfo);
//        }
        tranValue(httpServletRequest);
        return "stu/stu_myPTInfo";
    }


    public void tranValue(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(true);
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        StudentInfo studentInfo = findBySno(sno);
        String className = getClassInforById(studentInfo.getClassId()).getClassName();
        String teaSchName = getTeaSchName(studentInfo.getSchTeaId());
        session.setAttribute("myName",studentInfo.getSname());
        session.setAttribute("myGender",studentInfo.getGender());
        session.setAttribute("myClass",className);
        session.setAttribute("mySchTeacher",teaSchName);
        session.setAttribute("myPhone",studentInfo.getPhone());
        session.setAttribute("myEmail",studentInfo.getQqemail());
        session.setAttribute("myParentPhone",studentInfo.getParPhone());

        String type ;
        if("校内实训".equals(studentInfo.getTypeName())){
            type = "校内实训";
        }else if ("校企合作".equals(studentInfo.getTypeName())){
            type = "校企合作";
        }else{
            type = "校外自行联系";
        }
        session.setAttribute("typeValue",studentInfo.getTypeName());
//        @Value("${ptType:"+ type +"}")
        session.setAttribute("ptCompany",studentInfo.getComName());
        session.setAttribute("comTeacherName",studentInfo.getComTeaName());
        session.setAttribute("comTeacherPhone",studentInfo.getComTeaPhone());
        session.setAttribute("ptTitle",studentInfo.getProjectName());
        session.setAttribute("ptCity",studentInfo.getCity());
        TimeMessage timeMessage = getTimeMessage(1);
        Date messageBeginTime = timeMessage.getMessageBeginTime();
        Date messageEndTime = timeMessage.getMessageEndTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFormatBegin = sdf1.format(messageBeginTime);
        String timeFormatEnd = sdf1.format(messageEndTime);
        System.out.println("开始："+timeFormatBegin+",结束："+timeFormatEnd);
        session.setAttribute("messageBeginTime",timeFormatBegin);
        session.setAttribute("messageEndTime",timeFormatEnd);
    }

    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    ClassInformationService classInformationService;

    @Autowired
    Teacher_SchoolService teacher_schoolService;

    @GetMapping("/update")
    public int updateBySno(StudentInfo studentInfo){
        return this.ptInformationService.updatePTInformation(studentInfo);
    }

    @GetMapping("/findBySno/{sno}")
    public StudentInfo findBySno(@PathVariable("sno") String sno){
        return this.ptInformationService.findBySno(sno);
    }

    @GetMapping("/getTimeMessage/{id}")
    public TimeMessage getTimeMessage(@PathVariable("id") int id){
        return this.timeMessageService.getTimeMessage(id);
    }

    @GetMapping("/getClassInfor/{classId}")
    public ClassInformation getClassInforById(@PathVariable("classId") int classId){
        return classInformationService.getClassInforById(classId);
    }

    @GetMapping("/getTeaSchName/{teaSchId}")
    public String getTeaSchName(@PathVariable("teaSchId") String teaSchId){
        return teacher_schoolService.findTeaSchByTno(teaSchId);
    }



}
