package edu.zut.pt.controller;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeaSch_StuPTInfoController {

    @GetMapping("tea_school_stuPTInfo")
    public String tea_stuPTInfo(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession){

        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<StudentInfo> list = getSchAllStudent(tno);
        httpSession.setAttribute("schStuListNum",list.size());
        model.addAttribute("schStuList",list);

        return "tea_school/tea_school_stuPTInfo";
    }

    @Autowired
    PTInformationService ptInformationService;

    @GetMapping("/selectSchAllStudent/{tno}")
    public List<StudentInfo> getSchAllStudent(@PathVariable("tno") String tno){
        return ptInformationService.findBySchTeaId(tno);
    }



}
