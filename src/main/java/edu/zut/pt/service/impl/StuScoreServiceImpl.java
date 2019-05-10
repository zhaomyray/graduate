package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.StuScoreMapper;
import edu.zut.pt.pojo.StudentScore;
import edu.zut.pt.service.StuScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StuScoreServiceImpl implements StuScoreService {

    @Autowired
    StuScoreMapper stuScoreMapper;

    @Override
    public StudentScore getStuScore(String sno) {
        return this.stuScoreMapper.getStuScore(sno);
    }

    @Override
    public int insertStuDailyScore(String sno, int dailyScore, int middleScore, int ptScore, int score) {
        return this.stuScoreMapper.insertStuDailyScore(sno,dailyScore,middleScore,ptScore,score);
    }

    @Override
    public int updateStuDailyScore(String sno, int dailyScore, int score) {
        return this.stuScoreMapper.updateStuDailyScore(sno,dailyScore,score);
    }

    @Override
    public int updateStuMiddleScore(String sno, int middleScore, int score) {
        return this.stuScoreMapper.updateStuMiddleScore(sno,middleScore,score);
    }

    @Override
    public int updateStuPTScore(String sno, int ptScore, int score) {
        return this.stuScoreMapper.updateStuPTScore(sno,ptScore,score);
    }
}
