package edu.zut.pt.controller.teaComController;

import edu.zut.pt.mapper.SystemNoticeMapper;
import edu.zut.pt.pojo.SystemNotice;
import edu.zut.pt.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeaCom_SysArticleController {

    @GetMapping("tea_company_receiveNotice")
    public String tea_company_receive(HttpSession httpSession, Model model){

        List<SystemNotice> list = getAllArticle();
        httpSession.setAttribute("receiveSysArticleCount",list.size());
        model.addAttribute("allArticle",list);
        return "tea_company/tea_company_receiveNotice";
    }


    @Autowired
    SystemNoticeService systemNoticeService;

    @GetMapping("/findAllSysArticle")
    public List<SystemNotice> getAllArticle(){
        return systemNoticeService.findAllSysArticle();
    }
}
