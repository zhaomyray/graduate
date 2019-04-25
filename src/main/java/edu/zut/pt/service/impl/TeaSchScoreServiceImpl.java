package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeaSchScoreMapper;
import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.service.TeaSchScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeaSchScoreServiceImpl implements TeaSchScoreService {

    @Autowired
    TeaSchScoreMapper teaSchScoreMapper;


    @Override
    public MiddleReport findMidScoreBySno(String sno) {
        return this.teaSchScoreMapper.findMidScoreBySno(sno);
    }

    @Override
    public FinalReport findFinalScoreBySno(String sno) {
        return this.teaSchScoreMapper.findFinalScoreBySno(sno);
    }
}
