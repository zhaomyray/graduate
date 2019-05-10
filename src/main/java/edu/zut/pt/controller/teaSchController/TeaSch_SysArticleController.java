package edu.zut.pt.controller.teaSchController;


import edu.zut.pt.mapper.SystemNoticeMapper;
import edu.zut.pt.mapper.TeacherNoticeMapper;
import edu.zut.pt.pojo.SystemNotice;
import edu.zut.pt.pojo.TeacherNotice;
import edu.zut.pt.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeaSch_SysArticleController {

    @GetMapping("tea_school_receiveNotice")
    public String tea_school_receive(HttpSession session, Model model){



        List<SystemNotice> list = getAllArticle();
        session.setAttribute("receiveSysArticleCount",list.size());
        model.addAttribute("allArticle",list);
        return "tea_school/tea_school_receiveNotice";
    }

    @Autowired
    SystemNoticeService systemNoticeService;

    @GetMapping("/selectAllSysArticle")
    public List<SystemNotice> getAllArticle(){
        return systemNoticeService.findAllSysArticle();
    }
}
