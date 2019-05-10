package edu.zut.pt.mapper;

import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaSchFinalReportMapper {

    /**
     * 根据学生学号修改学生成绩
     * @param sno
     * @param score
     * @return
     */
    @Update("update tb_info_final set score=#{score} where finalSno=#{sno}")
    public int updateFinalScoreBySno(String sno, int score);

    /**
     * 根据学生学号修改报告和项目的状态
     * @param isUpdate
     * @param finalSno
     * @return
     */
    @Update("update tb_info_final set isUpdate=#{isUpdate} where finalSno=#{finalSno}")
    public int updateIsUpdate(String isUpdate,String finalSno);

    /**
     * 根据老师学工号查找学生信息
     * @param tno
     * @return
     */
    @Select("select * from tb_info_student where schTeaId=#{tno}")
    public List<StudentInfo> findStuByTno(String tno);

    /**
     * 根据学生学号查找实训报告和项目信息
     * @param sno
     * @return
     */
    @Select("select * from tb_info_final where finalSno=#{sno}")
    public FinalReport findBySno(String sno);

}
