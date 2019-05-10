package edu.zut.pt.controller.teaSchController;

import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.service.Teacher_SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class Teacher_SchoolUpdatePSDController {

    @GetMapping("tea_school_updatePassword")
    public String updatePSD(HttpServletRequest httpServletRequest, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String findTeaSchOldPsd = findTeaSchPsdByTno(tno);

        httpSession.setAttribute("findTeaSchOldPsd",findTeaSchOldPsd);
        return "/tea_school/tea_school_updatePassword";
    }

    @RequestMapping(value="/updateTeaSchoolSubmit",method = RequestMethod.POST)
    public String updateSubmit(HttpServletRequest httpServletRequest, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String findTeaSchOldPsd = findTeaSchPsdByTno(tno);
        String oldPsd = httpServletRequest.getParameter("oldTeaSchPassword");
        String newPsd = httpServletRequest.getParameter("newTeaSchPassword");
        String newPsdSure = httpServletRequest.getParameter("newTeaSchPassword2");
        System.out.println("数据库的旧密码："+findTeaSchOldPsd+"用户输入的旧密码"+oldPsd+"用户输入的新密码："+newPsd+"用户输入的确认密码"+newPsdSure);
        if(findTeaSchOldPsd.equals(oldPsd)==true){
            if(newPsd.equals(newPsdSure)==true){
                Teacher_School teacher_school = new Teacher_School();
                teacher_school.setTno(tno);
                teacher_school.setPassword(newPsdSure);
                updateTeaSchPsdByTno(teacher_school);
            }
        }else{
            System.out.println("密码输入错误！无法修改密码！");
        }
        httpSession.setAttribute("findTeaSchOldPsd",findTeaSchOldPsd);
        return "/tea_school/tea_school_updatePassword";
    }

    @Autowired
    Teacher_SchoolService teacher_schoolService;

    @GetMapping("findTeaSchPsdByTno/{tno}")
    public String findTeaSchPsdByTno(@PathVariable("tno") String tno){
        return teacher_schoolService.findTeaSchPsdByTno(tno);
    }

    @GetMapping("updateTeaSchPsdByTno/{teacher_school}")
    public int updateTeaSchPsdByTno(@PathVariable("teacher_school")Teacher_School teacher_school){
        return teacher_schoolService.updateTeaSchPsd(teacher_school);
    }

}
