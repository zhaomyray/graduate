package edu.zut.pt.mapper;

import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaSchWeekReportMapper {

    @Update("update tb_info_weekreport set score=#{score},weekReview=#{weekReview} where weekSno=#{sno} and weekMessage=#{weekMessage}")
    public int updateScoreBySno(String sno,int weekMessage,int score,String weekReview);

    @Select("select * from tb_info_student where schTeaId=#{tno} and typeName=#{type}")
    public List<StudentInfo> findStuByTno(String tno,String type);


    @Select("select * from tb_info_weekreport where weekSno=#{sno} and weekMessage=#{weekMessage}")
    public WeekReport findBySno(String sno,int weekMessage);
}
