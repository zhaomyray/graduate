package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.MiddleReportMapper;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.service.MiddleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiddleReportServiceImpl implements MiddleReportService {

    @Autowired
    MiddleReportMapper middleReportMapper;

    @Override
    public int insertMiddleReport(MiddleReport middleReport) {
        return this.middleReportMapper.insertMiddleReport(middleReport);
    }

    @Override
    public List<MiddleReport> findMiddleReportBySno(String middleSno) {
        return this.middleReportMapper.findMiddleReportBySno(middleSno);
    }

    @Override
    public MiddleReport findMiddleReportByMidSWM(String middleSno) {
        return this.middleReportMapper.findMiddleReportByMidSWM(middleSno);
    }
}
