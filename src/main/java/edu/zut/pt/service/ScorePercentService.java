package edu.zut.pt.service;

import edu.zut.pt.pojo.ScorePercent;

public interface ScorePercentService {

    /**
     * 获取成绩比例信息
     * @param id
     * @return
     */
    public ScorePercent getScorePercentInfo(int id);
}
