package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.WeekReportMapper;
import edu.zut.pt.pojo.WeekReport;
import edu.zut.pt.service.WeekReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekReportServiceImpl implements WeekReportService {

    @Autowired
    WeekReportMapper weekReportMapper;

    @Override
    public int insertWeekReport(WeekReport weekReport) {
        return this.weekReportMapper.insertWeekReport(weekReport);
    }

    @Override
    public List<WeekReport> findWeekReportBySno(String weekSno) {
        return this.weekReportMapper.findWeekReportBySno(weekSno);
    }

    @Override
    public WeekReport findWeekReportByweekSWM(String weekSno, String weekMessage) {
        return this.weekReportMapper.findWeekReportByweekSWM(weekSno,weekMessage);
    }
}
