package edu.zut.pt.mapper;

import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaSchMonthReportMapper {

    @Update("update tb_info_monthreport set score=#{score},monthReview=#{monthReview} where monthSno=#{sno} and monthMessage=#{monthMessage}")
    public int updateMonthScoreBySno(String sno,int monthMessage,int score,String monthReview);

    @Select("select * from tb_info_student where schTeaId=#{tno} and typeName=#{type}")
    public List<StudentInfo> findStuByTno(String tno,String type);


    @Select("select * from tb_info_monthreport where monthSno=#{sno} and monthMessage=#{monthMessage}")
    public MonthReport findBySno(String sno, int monthMessage);



}
