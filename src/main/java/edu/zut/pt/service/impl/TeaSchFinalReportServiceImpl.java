package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaSchFinalReportMapper;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.TeaSchFinalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeaSchFinalReportServiceImpl implements TeaSchFinalReportService {

    @Autowired
    TeaSchFinalReportMapper teaSchFinalReportMapper;

    @Override
    public int updateFinalScoreBySno(String sno, int score) {
        System.out.println("进入到更新分数的实现类中");
        return this.teaSchFinalReportMapper.updateFinalScoreBySno(sno,score);
    }

    @Override
    public int updateIsUpdate(String isUpdate, String finalSno) {
        return this.teaSchFinalReportMapper.updateIsUpdate(isUpdate,finalSno);
    }

    @Override
    public List<StudentInfo> findStuByTno(String tno) {
        return this.teaSchFinalReportMapper.findStuByTno(tno);
    }

    @Override
    public FinalReport findBySno(String sno) {
        return this.teaSchFinalReportMapper.findBySno(sno);
    }
}
