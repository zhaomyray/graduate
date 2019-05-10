package edu.zut.pt.mapper;

import edu.zut.pt.pojo.StudentScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StuScoreMapper {

    /**
     * 根据学生学号查找学生成绩
     *
     * @param sno
     * @return
     */
    @Select("select * from tb_info_stugrade where sno=#{sno}")
    public StudentScore getStuScore(String sno);

    /**
     * 根据学生学号插入学生成绩信息
     *
     * @param sno
     * @param dailyScore
     * @return
     */
    @Insert("insert into tb_info_stugrade(sno,dailyScore,middleScore,ptScore,score) value(#{sno},#{dailyScore},#{middleScore},#{ptScore},#{score})")
    public int insertStuDailyScore(String sno, int dailyScore,int middleScore,int ptScore,int score);

    /**
     * 根据学号修改学生平时成绩和实训成绩
     *
     * @param sno
     * @param dailyScore
     * @return
     */
    @Update("update tb_info_stugrade set dailyScore=#{dailyScore},score=#{score} where sno=#{sno}")
    public int updateStuDailyScore(String sno, int dailyScore,int score);

    /**
     * 根据学生学号修改学生中期成绩和实训成绩
     *
     * @param sno
     * @param middleScore
     * @return
     */
    @Update("update tb_info_stugrade set middleScore=#{middleScore},score=#{score} where sno=#{sno}")
    public int updateStuMiddleScore(String sno, int middleScore,int score);


    /**
     * 根据学生学号修改学生的实训报告成绩和实训成绩
     * @param sno
     * @param ptScore
     * @param score
     * @return
     */
    @Update("update tb_info_stugrade set ptScore=#{ptScore},score=#{score} where sno=#{sno}")
    public int updateStuPTScore(String sno,int ptScore,int score);


}


