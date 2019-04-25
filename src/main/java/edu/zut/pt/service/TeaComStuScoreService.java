package edu.zut.pt.service;

import edu.zut.pt.pojo.StudentComScore;

public interface TeaComStuScoreService {

    /**
     * 根据学生学号查找学生成绩
     * @param sno
     * @return
     */
    public StudentComScore getComStuScore(String sno);

    /**
     * 根据学生学号插入学生平时成绩信息
     * @param sno
     * @param dailyScore
     * @return
     */
    public int insertStuDailyScore(String sno,int dailyScore);

    /**
     * 根据学号修改学生成绩
     * @param sno
     * @param dailyScore
     * @return
     */
    public int updateStuDailyScore(String sno,int dailyScore);

    /**
     * 根据学生学号插入学生成绩
     * @param sno
     * @param middleScore
     * @return
     */
    public int insertStuMiddleScore(String sno,int middleScore);

    /**
     * 根据学生学号修改学生成绩
     * @param sno
     * @param middleScore
     * @return
     */
    public int updateStuMiddleScore(String sno,int middleScore);


}
