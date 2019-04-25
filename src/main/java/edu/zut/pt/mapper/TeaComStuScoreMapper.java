package edu.zut.pt.mapper;

import edu.zut.pt.pojo.StudentComScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface TeaComStuScoreMapper {

    /**
     * 根据学生学号查询校外指导老师给学生评的成绩
     * @param sno
     * @return
     */
    @Select("select * from tb_info_stucompany where comSno=#{sno}")
    public StudentComScore getComStuScore(String sno);

    /**
     * 根据学生学号插入学生平时成绩信息
     * @param sno
     * @param dailyScore
     * @return
     */
    @Insert("insert into tb_info_stucompany(comSno,dailyScore) value(#{sno},#{dailyScore})")
    public int insertStuDailyScore(String sno,int dailyScore);

    /**
     * 根据学号修改学生成绩
     * @param sno
     * @param dailyScore
     * @return
     */
    @Update("update tb_info_stucompany set dailyScore=#{dailyScore} where comSno=#{sno}")
    public int updateStuDailyScore(String sno,int dailyScore);

    /**
     * 根据学生学号插入学生成绩
     * @param sno
     * @param middleScore
     * @return
     */
    @Insert("insert into tb_info_stucompany(comSno,middleScore) value(#{sno},#{middleScore})")
    public int insertStuMiddleScore(String sno,int middleScore);

    /**
     * 根据学生学号修改学生成绩
     * @param sno
     * @param middleScore
     * @return
     */
    @Update("update tb_info_stucompany set middleScore=#{middleScore} where comSno=#{sno}")
    public int updateStuMiddleScore(String sno,int middleScore);

}
