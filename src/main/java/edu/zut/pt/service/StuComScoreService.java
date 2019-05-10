package edu.zut.pt.service;

import edu.zut.pt.pojo.StuComScore;
import java.util.List;

public interface StuComScoreService {

    /**
     * 根据学生学号插入实训成绩信息(校内合作实训公司实训的学生)
     * @param sno
     * @param dailyScore
     * @param middleScore
     * @param finalScore
     * @return
     */
    public int insertStuComScore(String sno,int dailyScore,int middleScore,int finalScore,String middleFilePath,String finalFilePath,String finalProjectPath);

    /**
     * 根据学生学号修改学生平时成绩
     * @param sno
     * @param dailyScore
     * @return
     */
    public int updateStuComDailyScore(String sno,int dailyScore);

    /**
     * 根据学生学号修改学生中期成绩
     * @param sno
     * @param middleSno
     * @return
     */
    public int updateStuComMiddleScore(String sno,int middleSno);


    /**
     * 根据学生学号修改学生实训报告成绩
     * @param sno
     * @param finalScore
     * @return
     */
    public int updateStuComFinalScore(String sno,int finalScore);

    /**
     * 根据学生学号查找学生成绩信息
     * @param sno
     * @return
     */
    public StuComScore selectStuComScore(String sno);


}
