package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaSchMonthReportMapper;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.TeaSchMonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeaSchMonthReportServiceImpl implements TeaSchMonthReportService {

    @Autowired
    TeaSchMonthReportMapper teaSchMonthReportMapper;

    @Override
    public int updateMonthScoreBySno(String sno, int monthMessage, int score, String monthReview) {
        return this.teaSchMonthReportMapper.updateMonthScoreBySno(sno,monthMessage,score,monthReview);
    }

    @Override
    public List<StudentInfo> findStuByTno(String tno, String type) {
        return this.teaSchMonthReportMapper.findStuByTno(tno,type);
    }

    @Override
    public MonthReport findBySno(String sno, int monthMessage) {
        return this.teaSchMonthReportMapper.findBySno(sno,monthMessage);
    }
}
