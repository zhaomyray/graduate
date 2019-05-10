package edu.zut.pt.service;


import edu.zut.pt.pojo.StudentScore;

public interface StuScoreService {

    /**
     * 根据学生学号查找学生成绩
     *
     * @param sno
     * @return
     */
    public StudentScore getStuScore(String sno);

    /**
     * 根据学生学号插入学生成绩信息
     *
     * @param sno
     * @param dailyScore
     * @return
     */
    public int insertStuDailyScore(String sno, int dailyScore,int middleScore,int ptScore,int score);

    /**
     * 根据学号修改学生平时成绩和实训成绩
     *
     * @param sno
     * @param dailyScore
     * @return
     */
    public int updateStuDailyScore(String sno, int dailyScore,int score);

    /**
     * 根据学生学号修改学生中期成绩和实训成绩
     *
     * @param sno
     * @param middleScore
     * @return
     */
    public int updateStuMiddleScore(String sno, int middleScore,int score);


    /**
     * 根据学生学号修改学生的实训报告成绩和实训成绩
     * @param sno
     * @param ptScore
     * @param score
     * @return
     */
    public int updateStuPTScore(String sno,int ptScore,int score);

}
