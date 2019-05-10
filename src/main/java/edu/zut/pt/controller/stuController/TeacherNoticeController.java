package edu.zut.pt.controller.stuController;


import edu.zut.pt.controller.stuController.PTInformationController;
import edu.zut.pt.pojo.SystemNotice;
import edu.zut.pt.pojo.TeacherNotice;
import edu.zut.pt.service.SystemNoticeService;
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
        for(int i =0;i<list.size();i++){
            System.out.println("通知"+(i+1)+"的内容为："+list.get(i).getNoticeContent());
        }
        List<SystemNotice> list1 = getSysNoticeAll();

        if(list.size()>0 ){
            int m = list.size();
            if(list1.size()>0){
                for(int i = 0;i<list1.size();i++){
                    m = m+1;
                    TeacherNotice teacherNotice = new TeacherNotice();
                    teacherNotice.setId(m);
                    teacherNotice.setTeaId("系统管理员");
                    teacherNotice.setNoticeTitle(list1.get(i).getTitle());
                    teacherNotice.setNoticeContent(list1.get(i).getContent());
                    teacherNotice.setNoticeTime(list1.get(i).getCreatedate());
                    list.add(teacherNotice);
                }
            }
        }else{
            for(int i = 0;i<list1.size();i++){
                TeacherNotice teacherNotice = new TeacherNotice();
                teacherNotice.setId(i+1);
                teacherNotice.setTeaId("系统管理员");
                teacherNotice.setNoticeTitle(list1.get(i).getTitle());
                teacherNotice.setNoticeContent(list1.get(i).getContent());
                teacherNotice.setNoticeTime(list1.get(i).getCreatedate());
                list.add(teacherNotice);
            }
        }

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
        List<TeacherNotice> list = getTeacherNotice(tno);
        model.addAttribute("noticeList",list);


        return  "tea_school/tea_school_publishNotice";
    }




    @Autowired
    TeacherNoticeService teacherNoticeService;

    @Autowired
    SystemNoticeService systemNoticeService;

    @GetMapping("/teacherNotice/{teaId}")
    public List<TeacherNotice> getTeacherNotice(@PathVariable("teaId") String teaId){
        return teacherNoticeService.getTeacherNoticeByTeaId(teaId);
    }

    @GetMapping("/teacherNotice")
    public TeacherNotice insertTeacherNotice(TeacherNotice teacherNotice){
        teacherNoticeService.insertTeacherNotice(teacherNotice);
        return teacherNotice;
    }

    @GetMapping("/sysNotice")
    public List<SystemNotice> getSysNoticeAll(){
        return systemNoticeService.findAllSysArticle();
    }

}
