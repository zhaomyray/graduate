package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaSchMiddleReportMapper;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.TeaSchMiddleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeaSchMiddleReportServiceImpl implements TeaSchMiddleReportService {

    @Autowired
    TeaSchMiddleReportMapper teaSchMiddleReportMapper;

    @Override
    public int updateMiddleScoreBySno(String sno, int score, String middleReview) {
        return this.teaSchMiddleReportMapper.updateMiddleScoreBySno(sno,score,middleReview);
    }

    @Override
    public List<StudentInfo> findStuByTno(String tno) {
        return this.teaSchMiddleReportMapper.findStuByTno(tno);
    }

    @Override
    public MiddleReport findBySno(String sno) {
        return this.teaSchMiddleReportMapper.findBySno(sno);
    }
}
