package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaSchWeekReportMapper;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;
import edu.zut.pt.service.TeaSchWeekReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeaSchWeekReportServiceImpl implements TeaSchWeekReportService {

    @Autowired
    TeaSchWeekReportMapper teaSchWeekReportMapper;

    @Override
    public int updateScoreBySno(String sno, int weekMessage, int score, String weekReview) {
        return this.teaSchWeekReportMapper.updateScoreBySno(sno,weekMessage,score,weekReview);
    }

    @Override
    public List<StudentInfo> findStuByTno(String tno, String type) {
        return this.teaSchWeekReportMapper.findStuByTno(tno,type);
    }

    @Override
    public WeekReport findBySno(String sno, int weekMessage) {
        return this.teaSchWeekReportMapper.findBySno(sno,weekMessage);
    }
}
