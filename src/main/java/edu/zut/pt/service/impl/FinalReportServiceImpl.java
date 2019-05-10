package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.FinalReportMapper;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.service.FinalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinalReportServiceImpl implements FinalReportService {

    @Autowired
    FinalReportMapper finalReportMapper;

    @Override
    public int insertFinal(FinalReport finalReport) {
        return this.finalReportMapper.insertFinal(finalReport);
    }

    @Override
    public int updateFinal(String finalSno, String add_finReport, String add_finalProject, String time_submit,String isAfter,String isUpdate) {
        return this.finalReportMapper.updateFinal(finalSno,add_finReport,add_finalProject,time_submit,isAfter,isUpdate);
    }

    @Override
    public FinalReport findFinalReportBySno(String finalSno) {
        return this.finalReportMapper.findFinalReportBySno(finalSno);
    }
}
