package edu.zut.pt.controller.teaComController;

import edu.zut.pt.pojo.Teacher_Company;
import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.service.Teacher_CompanyService;
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
public class Teacher_CompanyUpdatePSDController {

    @GetMapping("tea_company_updatePassword")
    public String updatePSD(HttpServletRequest httpServletRequest, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String findTeaComOldPsd = findTeaComPsdByTno(tno);

        httpSession.setAttribute("findTeaComOldPsd",findTeaComOldPsd);
        return "/tea_company/tea_company_updatePassword";
    }

    @RequestMapping(value="/updateTeaCompanySubmit",method = RequestMethod.POST)
    public String updateSubmit(HttpServletRequest httpServletRequest, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        String findTeaComOldPsd = findTeaComPsdByTno(tno);
        String oldPsd = httpServletRequest.getParameter("oldTeaComPassword");
        String newPsd = httpServletRequest.getParameter("newTeaComPassword");
        String newPsdSure = httpServletRequest.getParameter("newTeaComPassword2");
        System.out.println("数据库的旧密码："+findTeaComOldPsd+"用户输入的旧密码"+oldPsd+"用户输入的新密码："+newPsd+"用户输入的确认密码"+newPsdSure);
        if(findTeaComOldPsd.equals(oldPsd)==true){
            if(newPsd.equals(newPsdSure)==true){
                Teacher_Company teacher_company = new Teacher_Company();
                teacher_company.setTno(tno);
                teacher_company.setPassword(newPsdSure);
                updateTeaComPsdByTno(teacher_company);
            }
        }else{
            System.out.println("密码输入错误！无法修改密码！");
        }
        httpSession.setAttribute("findTeaComOldPsd",findTeaComOldPsd);
        return "/tea_company/tea_company_updatePassword";
    }

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @GetMapping("findTeaComPsdByTno/{tno}")
    public String findTeaComPsdByTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaComPsdByTno(tno);
    }

    @GetMapping("updateTeaComPsdByTno/{teacher_company}")
    public int updateTeaComPsdByTno(@PathVariable("teacher_company") Teacher_Company teacher_company){
        return teacher_companyService.updateTeaComPsd(teacher_company);
    }

}
