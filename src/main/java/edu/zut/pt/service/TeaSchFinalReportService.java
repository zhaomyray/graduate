package edu.zut.pt.service;

import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StudentInfo;

import java.util.List;

public interface TeaSchFinalReportService {


    /**
     * 根据学生学号修改成绩
     * @param sno
     * @param score
     * @return
     */
    public int updateFinalScoreBySno(String sno, int score);

    /**
     * 根据学生学号修改报告状态
     * @param isUpdate
     * @param finalSno
     * @return
     */
    public int updateIsUpdate(String isUpdate,String finalSno);

    /**
     * 根据老师学工号查找学生信息
     * @param tno
     * @return
     */
    public List<StudentInfo> findStuByTno(String tno);

    /**
     * 根据学生学号查找实训报告和项目信息
     * @param sno
     * @return
     */
    public FinalReport findBySno(String sno);


}
