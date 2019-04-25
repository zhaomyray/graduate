package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.SystemNoticeMapper;
import edu.zut.pt.pojo.SystemNotice;
import edu.zut.pt.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemNoticeServiceImpl implements SystemNoticeService {

    @Autowired
    SystemNoticeMapper systemNoticeMapper;

    @Override
    public List<SystemNotice> findAllSysArticle() {
        return this.systemNoticeMapper.findAllSysArticle();
    }
}
