package edu.zut.pt.controller;


import edu.zut.pt.mapper.TeacherNoticeMapper;
import edu.zut.pt.pojo.TeacherNotice;
import edu.zut.pt.service.TeacherNoticeService;
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
import java.util.List;

@Controller
public class TeacherNoticeController {

    @Autowired
    PTInformationController ptInformationController;

    @GetMapping("stu_notice")
    public String stu_notice(HttpServletRequest httpServletRequest, HttpSession httpSession,Model model){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String teaId = ptInformationController.findBySno(sno).getSchTeaId();
        List<TeacherNotice> list = getTeacherNotice(teaId);
        httpSession.setAttribute("noticeNum",list.size());
        model.addAttribute("noticeStuList",list);

        return "stu/stu_notice";
    }

    @GetMapping("tea_school_publishNotice")
    public String tea_notice(HttpServletRequest httpServletRequest,Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        List<TeacherNotice> list = getTeacherNotice(tno);

        model.addAttribute("noticeList",list);
        return "/tea_school/tea_school_publishNotice";
    }
    @RequestMapping(value="/addNotice",method = RequestMethod.POST)
    public String addNotice(WebRequest webRequest, HttpServletRequest httpServletRequest, Model model){
        String tno = httpServletRequest.getSession().getAttribute("login").toString();
        TeacherNotice teacherNotice = new TeacherNotice();

        List<TeacherNotice> list = getTeacherNotice(tno);

        model.addAttribute("noticeList",list);

        Date noticeDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String noticeTime = df.format(noticeDate).toString();
        teacherNotice.setTeaId(tno);
        if("".equals(webRequest.getParameter("noticeTitle"))||"".equals(webRequest.getParameter("noticeContent"))){

        }else{
            teacherNotice.setNoticeTitle(webRequest.getParameter("noticeTitle"));
            teacherNotice.setNoticeContent(webRequest.getParameter("noticeContent"));
            teacherNotice.setNoticeTime(noticeTime);
            insertTeacherNotice(teacherNotice);
        }
        return  "tea_school/tea_school_publishNotice";
    }




    @Autowired
    TeacherNoticeService teacherNoticeService;

    @GetMapping("/teacherNotice/{teaId}")
    public List<TeacherNotice> getTeacherNotice(@PathVariable("teaId") String teaId){
        return teacherNoticeService.getTeacherNoticeByTeaId(teaId);
    }

    @GetMapping("/teacherNotice")
    public TeacherNotice insertTeacherNotice(TeacherNotice teacherNotice){
        teacherNoticeService.insertTeacherNotice(teacherNotice);
        return teacherNotice;
    }

}
