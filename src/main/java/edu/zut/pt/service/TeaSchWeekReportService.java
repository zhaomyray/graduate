package edu.zut.pt.service;

import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;

import java.util.List;

public interface TeaSchWeekReportService {

    /**
     * 插入成绩和评语
     * @param sno
     * @param weekMessage
     * @param score
     * @param weekReview
     * @return
     */
    public int updateScoreBySno(String sno,int weekMessage,int score,String weekReview);

    /**
     * 根据教工号和实训类型查找学生实训信息
     * @param tno
     * @param type
     * @return
     */
    public List<StudentInfo> findStuByTno(String tno, String type);

    /**
     * 根据学号和周信息查找周报
     * @param sno
     * @param weekMessage
     * @return
     */
    public WeekReport findBySno(String sno, int weekMessage);

}
