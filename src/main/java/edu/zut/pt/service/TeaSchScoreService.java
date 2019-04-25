package edu.zut.pt.service;

import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MiddleReport;

public interface TeaSchScoreService {

    /**
     * 查找学生中期成绩
     * @param sno
     * @return
     */
    public MiddleReport findMidScoreBySno(String sno);

    /**
     * 查找学生实训成绩
     * @param sno
     * @return
     */
    public FinalReport findFinalScoreBySno(String sno);

}
