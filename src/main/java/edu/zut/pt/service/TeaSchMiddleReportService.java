package edu.zut.pt.service;

import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.StudentInfo;

import java.util.List;

public interface TeaSchMiddleReportService {

    /**
     * 修改中期成绩
     * @param sno
     * @param score
     * @param middleReview
     * @return
     */
    public int updateMiddleScoreBySno(String sno, int score,String middleReview);


    /**
     * 根据学工号查找学生信息
     * @param tno
     * @return
     */
    public List<StudentInfo> findStuByTno(String tno);


    /**
     * 根据学号查找报告
     * @param sno
     * @return
     */
    public MiddleReport findBySno(String sno);



}
