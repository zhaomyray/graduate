package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.ApprovalTableMapper;
import edu.zut.pt.pojo.ApprovalTable;
import edu.zut.pt.service.ApprovalTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApprovalTableServiceImpl implements ApprovalTableService {

    @Autowired
    ApprovalTableMapper approvalTableMapper;

    @Override
    public int insertApprovalTableInfo(String sno, String time_submit, String tableFilePath) {
        return this.approvalTableMapper.insertApprovalTableInfo(sno,time_submit,tableFilePath);
    }

    @Override
    public int updateApprovalTableInfo(String sno, String time_submit, String tableFilePath) {
        return this.approvalTableMapper.updateApprovalTableInfo(sno,time_submit,tableFilePath);
    }

    @Override
    public ApprovalTable selectApprovalTableInfoBySno(String sno) {
        return this.approvalTableMapper.selectApprovalTableInfoBySno(sno);
    }
}
