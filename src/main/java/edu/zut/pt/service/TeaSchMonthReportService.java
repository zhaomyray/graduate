package edu.zut.pt.service;

import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;

import java.util.List;

public interface TeaSchMonthReportService {

    /**
     * 根据学号修改成绩
     * @param sno
     * @param monthMessage
     * @param score
     * @param monthReview
     * @return
     */
    public int updateMonthScoreBySno(String sno,int monthMessage,int score,String monthReview);

    /**
     * 根据教工号和实训类型查找学生信息
     * @param tno
     * @param type
     * @return
     */
    public List<StudentInfo> findStuByTno(String tno, String type);

    /**
     * 根据学号和月起信息查找学生月报
     * @param sno
     * @param monthMessage
     * @return
     */
    public MonthReport findBySno(String sno, int monthMessage);

}
