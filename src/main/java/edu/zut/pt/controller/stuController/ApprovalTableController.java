package edu.zut.pt.controller.stuController;

import edu.zut.pt.DownloadFile;
import edu.zut.pt.UploadFile;
import edu.zut.pt.pojo.ApprovalTable;
import edu.zut.pt.pojo.TimeMessage;
import edu.zut.pt.service.ApprovalTableService;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ApprovalTableController {

    @GetMapping("stu_approvalTable")
    public String approvalTable(HttpServletRequest httpServletRequest,Model model,HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        getApprovalPageInfo(sno,model,httpSession);
        return "stu/stu_approvalTable";
    }

    //提交审批表
    @RequestMapping(value="/submitApprovalTable",method = RequestMethod.POST)
    public String submitApprovalTable(@RequestParam("uploadApprovalTable") MultipartFile tableFilePath,
                                      Model model,HttpServletRequest httpServletRequest,HttpSession httpSession){
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        String tablePath = tableFilePath.getOriginalFilename();
        Date tableTime = new Date();
        SimpleDateFormat tableDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String submitTime = tableDf.format(tableTime).toString();
        String approvalTableBan = httpServletRequest.getParameter("approvalTableBan");

        if("无法提交".equals(approvalTableBan)){
            System.out.println("无法提交文件");
        }else{
            // 上传审批表文件
            String filePath = uploadApprovalTable(sno,tableFilePath).toString();
            //如果该生已经提交过审批表，那么再次提交，会更新审批表的路径和时间
            if(selectApprovalTableInfo(sno)!=null){
                //更新该生在数据库中的审批表信息
                updateApprovalTableInfo(sno,submitTime,filePath);
            }else{
                //否则，会直接插入新的审批表信息
                //将审批表信息保存到数据库
                insertApprovalTableInfo(sno,submitTime,filePath);
            }

        }
        System.out.println("实训审批表路径："+tablePath);
        getApprovalPageInfo(sno,model,httpSession);
        return "stu/stu_approvalTable";
    }

    //下载审批表
    @RequestMapping(value="/tableFileDownload",method = RequestMethod.POST)
    public String tableFileDownload(HttpServletRequest httpServletRequest,HttpSession httpSession,
                                    HttpServletResponse httpServletResponse, Model model){
        //获取审批表路径
        String tableFilePath = httpServletRequest.getParameter("tableFilePath");
        DownloadFile downloadFile = new DownloadFile();
        //获取学生学号
        String sno = httpServletRequest.getSession().getAttribute("login").toString();
        //下载审批表文件
        downloadFile.download(tableFilePath,httpServletResponse);
        getApprovalPageInfo(sno,model,httpSession);
        return "stu/stu_weekReport";
    }

    //获取提交审批表页面的数据
    public void getApprovalPageInfo(String sno, Model model, HttpSession httpSession){
        List<ApprovalTable> approvalTableList = new ArrayList<>();
        if(selectApprovalTableInfo(sno)!=null){

            ApprovalTable approvalTable = selectApprovalTableInfo(sno);
            System.out.println("提交时间为："+approvalTable.getTime_submit());
            approvalTableList.add(approvalTable);
        }

        TimeMessage timeMessage = getApprovalTableTimeMessage(1);
        Date beginTime = timeMessage.getMessageBeginTime();
        Date endTime = timeMessage.getMessageEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tableBeginTime = sdf.format(beginTime);
        String tableEndTime = sdf.format(endTime);

        httpSession.setAttribute("tableBeginTime",tableBeginTime);
        httpSession.setAttribute("tableEndTime",tableEndTime);
        model.addAttribute("approvalTableList",approvalTableList);
    }

    //上传审批表
    public Object uploadApprovalTable(String sno,MultipartFile weekFile){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        //保存时的文件名dataName
        String dateName = "审批表_"+sno+"_"+df.format(calendar.getTime())+weekFile.getOriginalFilename();
        String realPath = "F:/uploadApprovalTableTest/";
        try{
            //调用上传文件的方法
            //上传周报
            UploadFile.uploadFile(weekFile.getBytes(),realPath,dateName);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return realPath+dateName;
    }

    @Autowired
    ApprovalTableService approvalTableService;

    @Autowired
    TimeMessageService timeMessageService;

    @RequestMapping("insertApprovalTableInfo/{sno},{time_submit},{tableFilePath}")
    public int insertApprovalTableInfo(@PathVariable("sno") String sno,
                                       @PathVariable("time_submit")String time_submit,
                                       @PathVariable("tableFilePath") String tableFilePath){
        return approvalTableService.insertApprovalTableInfo(sno, time_submit, tableFilePath);
    }

    @RequestMapping("updateApprovalTableInfo/{sno},{time_submit},{tableFilePath}")
    public int updateApprovalTableInfo(@PathVariable("sno") String sno,
                                       @PathVariable("time_submit") String time_submit,
                                       @PathVariable("tableFilePath") String tableFilePath){
        return approvalTableService.updateApprovalTableInfo(sno, time_submit, tableFilePath);
    }

    @RequestMapping("selectApprovalTableInfo/{sno}")
    public ApprovalTable selectApprovalTableInfo(@PathVariable("sno") String sno){
        return approvalTableService.selectApprovalTableInfoBySno(sno);
    }

    @RequestMapping("getApprovalTableTimeMessage/{id}")
    public TimeMessage getApprovalTableTimeMessage(@PathVariable("id") int id){
        return timeMessageService.getTimeMessage(id);
    }


}
