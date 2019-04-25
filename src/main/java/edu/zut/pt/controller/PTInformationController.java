package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.impl.PTInformationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String  updateInformation(WebRequest webRequest, HttpServletRequest httpServletRequest, Model model){


        HttpSession session = httpServletRequest.getSession(true);
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setSno(sno);
//        String type = httpServletRequest.getParameter("ptType");
//        System.out.println(type);
        studentInfo.setTypeName(httpServletRequest.getParameter("ptType"));
        studentInfo.setParPhone(httpServletRequest.getParameter("myParentPhone"));
        studentInfo.setProjectName(httpServletRequest.getParameter("ptTitle"));
        studentInfo.setPhone(httpServletRequest.getParameter("myPhone"));
        studentInfo.setQqemail(httpServletRequest.getParameter("myEmail"));
        studentInfo.setComTeaName(httpServletRequest.getParameter("comTeacherName"));
        studentInfo.setComTeaPhone(httpServletRequest.getParameter("comTeacherPhone"));
        studentInfo.setComName(httpServletRequest.getParameter("ptCompany"));
        studentInfo.setCity(httpServletRequest.getParameter("ptCity"));

        if(httpServletRequest.getParameter("ptType")==null){

        }else {
            updateBySno(studentInfo);
        }
        tranValue(httpServletRequest);
        return "stu/stu_myPTInfo";
    }


    public void tranValue(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(true);
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        StudentInfo studentInfo = findBySno(sno);
        session.setAttribute("myName",studentInfo.getSname());
        session.setAttribute("myGender",studentInfo.getGender());
        session.setAttribute("myClass",studentInfo.getClassId());
        session.setAttribute("mySchTeacher",studentInfo.getSchTeaId());
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
    }

    @Autowired
    PTInformationService ptInformationService;

    @GetMapping("/update")
    public int updateBySno(StudentInfo studentInfo){
        return this.ptInformationService.updatePTInformation(studentInfo);
    }

    @GetMapping("/findBySno/{sno}")
    public StudentInfo findBySno(@PathVariable("sno") String sno){
        return this.ptInformationService.findBySno(sno);
    }
}
