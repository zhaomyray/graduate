package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaComStuScoreMapper;
import edu.zut.pt.pojo.StudentComScore;
import edu.zut.pt.service.TeaComStuScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TeaComStuScoreServiceImpl implements TeaComStuScoreService {

    @Autowired
    TeaComStuScoreMapper teaComStuScoreMapper;

    @Override
    public StudentComScore getComStuScore(String sno) {
        return this.teaComStuScoreMapper.getComStuScore(sno);
    }

    @Override
    public int insertStuDailyScore(String sno, int dailyScore) {
        return this.insertStuDailyScore(sno,dailyScore);
    }

    @Override
    public int updateStuDailyScore(String sno, int dailyScore) {
        return this.updateStuMiddleScore(sno,dailyScore);
    }

    @Override
    public int insertStuMiddleScore(String sno, int middleScore) {
        return this.insertStuMiddleScore(sno,middleScore);
    }

    @Override
    public int updateStuMiddleScore(String sno, int middleScore) {
        return this.updateStuMiddleScore(sno,middleScore);
    }
}
