package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.MonthReportMapper;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.service.MonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthReportServiceImpl implements MonthReportService {

    @Autowired
    MonthReportMapper monthReportMapper;

    @Override
    public int insertMonthReport(MonthReport monthReport) {
        return this.monthReportMapper.insertMonthReport(monthReport);
    }

    @Override
    public List<MonthReport> findMonthReportBySno(String monthSno) {
        return this.monthReportMapper.findMonthReportBySno(monthSno);
    }

    @Override
    public MonthReport findMonthReportBymonthSWM(String monthSno, String monthMessage) {
        return this.monthReportMapper.findMonthReportBymonthSWM(monthSno,monthMessage);
    }
}
