package edu.zut.pt.controller;

import edu.zut.pt.pojo.Student;
import edu.zut.pt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UpdatePsdController {

    @GetMapping("updatePSD")
    public String updatePSD(HttpServletRequest httpServletRequest,HttpSession httpSession){

        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String findOldPsd = findStuPsdBySno(sno);

        httpSession.setAttribute("findOldPsd",findOldPsd);
        return "updatePsd";
    }

    @RequestMapping(value="/updateSubmit",method = RequestMethod.POST)
    public String updateSubmit(HttpServletRequest httpServletRequest, HttpSession httpSession){

        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String findOldPsd = findStuPsdBySno(sno);
        String oldPsd = httpServletRequest.getParameter("oldpassword");
        String newPsd = httpServletRequest.getParameter("newpassword");
        String newPsdSure = httpServletRequest.getParameter("newpassword2");
        if(findOldPsd.equals(oldPsd)==true){
            if(newPsd.equals(newPsdSure)==true){
                Student student = new Student();
                student.setSno(sno);
                student.setPassword(newPsdSure);
                updatePsdBySno(student);
            }
        }else{
            System.out.println("密码输入错误！无法修改密码！");
        }


        httpSession.setAttribute("findOldPsd",findOldPsd);

        return "updatePsd";
    }

    @Autowired
    StudentService studentService;

    @GetMapping("findStuPsdBySno/{sno}")
    public String findStuPsdBySno(@PathVariable("sno") String sno){
        return studentService.findStudentPsdBySno(sno);
    }

    @GetMapping("updatePsdBySno/{student}")
    public int updatePsdBySno(@PathVariable("student") Student student){
        return studentService.updateStudentPsd(student);
    }

}
