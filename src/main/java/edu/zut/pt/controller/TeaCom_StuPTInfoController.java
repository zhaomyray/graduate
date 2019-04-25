package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.mapper.Teacher_CompanyMapper;
import edu.zut.pt.mapper.Teacher_SchoolMapper;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.Teacher_Company;
import edu.zut.pt.service.PTInformationService;
import edu.zut.pt.service.Teacher_CompanyService;
import edu.zut.pt.service.Teacher_SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeaCom_StuPTInfoController {

    @GetMapping("tea_company_stuPTInfo")
    public String tea_company_stuPTInfo(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String comTno = httpServletRequest.getSession().getAttribute("login").toString();
        String comTname = findNameByComTno(comTno);
        List<StudentInfo> list = findStuInfoByComTeaName(comTname);
        for(int i = 0;i<list.size();i++){
            String schName = findNameBySchTno(list.get(i).getSchTeaId());
            list.get(i).setSchTeaId(schName);
        }

        httpSession.setAttribute("schStuListNum",list.size());
        model.addAttribute("stuInfoList",list);

        return "tea_company/tea_company_stuPTInfo";
    }



    @Autowired
    PTInformationService ptInformationService;

    @Autowired
    Teacher_CompanyService teacher_companyService;

    @Autowired
    Teacher_SchoolService teacher_schoolService;

    @GetMapping("/selectStuInfoByComTeaName/{comTeaName}")
    public List<StudentInfo> findStuInfoByComTeaName(@PathVariable("comTeaName") String comTeaName){
        return ptInformationService.findBySchTeaName(comTeaName);
    }

    @GetMapping("/finNameByComTno/{tno}")
    public String findNameByComTno(@PathVariable("tno") String tno){
        return teacher_companyService.findTeaNameByTno(tno);
    }

    @GetMapping("/findNameBySchTno/{tno}")
    public String findNameBySchTno(@PathVariable("tno") String tno){
        return teacher_schoolService.findTeaSchByTno(tno);
    }

}
