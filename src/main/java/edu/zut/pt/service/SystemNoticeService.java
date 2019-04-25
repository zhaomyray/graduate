package edu.zut.pt.service;

import edu.zut.pt.pojo.SystemNotice;

import java.util.List;

public interface SystemNoticeService {

    /**
     * 查询系统公告
     * @return
     */
    public List<SystemNotice> findAllSysArticle();


}
