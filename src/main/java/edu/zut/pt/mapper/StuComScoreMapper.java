package edu.zut.pt.mapper;

import edu.zut.pt.pojo.StuComScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StuComScoreMapper {

    /**
     * 根据学生学号插入实训成绩信息(校内合作实训公司实训的学生)
     * @param sno
     * @param dailyScore
     * @param middleScore
     * @param finalScore
     * @return
     */
    @Insert("insert into tb_info_comstuscore(sno,dailyScore,middleScore,finalScore,middleFilePath,finalFilePath,finalProjectPath) values(#{sno},#{dailyScore},#{middleScore},#{finalScore},#{middleFilePath},#{finalFilePath},#{finalProjectPath})")
    public int insertStuComScore(String sno,int dailyScore,int middleScore,int finalScore,String middleFilePath,String finalFilePath,String finalProjectPath);

    /**
     * 根据学生学号修改学生平时成绩
     * @param sno
     * @param dailyScore
     * @return
     */
    @Update("update tb_info_comstuscore set dailyScore=#{dailyScore} where sno=#{sno}")
    public int updateStuComDailyScore(String sno,int dailyScore);

    /**
     * 根据学生学号修改学生中期成绩
     * @param sno
     * @param middleSno
     * @return
     */
    @Update("update tb_info_comstuscore set middleScore=#{middleScore} where sno=#{sno}")
    public int updateStuComMiddleScore(String sno,int middleSno);


    /**
     * 根据学生学号修改学生实训报告成绩
     * @param sno
     * @param finalScore
     * @return
     */
    @Update("update tb_info_comstuscore set finalScore=#{finalScore} where sno=#{sno}")
    public int updateStuComFinalScore(String sno,int finalScore);

    /**
     * 根据学生学号查找学生成绩信息
     * @param sno
     * @return
     */
    @Select("select * from tb_info_comstuscore where sno=#{sno}")
    public StuComScore selectStuComScore(String sno);

}
