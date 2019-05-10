package edu.zut.pt.service.impl;


import edu.zut.pt.mapper.StuComScoreMapper;
import edu.zut.pt.pojo.StuComScore;
import edu.zut.pt.service.StuComScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StuComScoreServiceImpl implements StuComScoreService {

    @Autowired
    StuComScoreMapper stuComScoreMapper;

    @Override
    public int insertStuComScore(String sno, int dailyScore, int middleScore, int finalScore,String middleFilePath,String finalFilePath,String finalProjectPath) {
        return this.stuComScoreMapper.insertStuComScore(sno,dailyScore,middleScore,finalScore,middleFilePath,finalFilePath,finalProjectPath);
    }

    @Override
    public int updateStuComDailyScore(String sno, int dailyScore) {
        return this.stuComScoreMapper.updateStuComDailyScore(sno,dailyScore);
    }

    @Override
    public int updateStuComMiddleScore(String sno, int middleSno) {
        return this.stuComScoreMapper.updateStuComMiddleScore(sno,middleSno);
    }

    @Override
    public int updateStuComFinalScore(String sno, int finalScore) {
        return this.stuComScoreMapper.updateStuComFinalScore(sno,finalScore);
    }

    @Override
    public StuComScore selectStuComScore(String sno) {
        return this.stuComScoreMapper.selectStuComScore(sno);
    }
}
