package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.ScorePercentMapper;
import edu.zut.pt.pojo.ScorePercent;
import edu.zut.pt.service.ScorePercentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScorePercentServiceImpl  implements ScorePercentService {

    @Autowired
    ScorePercentMapper scorePercentMapper;

    @Override
    public ScorePercent getScorePercentInfo(int id) {
        return scorePercentMapper.getScorePercentInfo(id);
    }
}
