package edu.zut.pt.mapper;

import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaSchMiddleReportMapper {


    @Update("update tb_info_middlereport set score=#{score},middleReview=#{middleReview} where middleSno=#{sno}")
    public int updateMiddleScoreBySno(String sno, int score,String middleReview);

    @Select("select * from tb_info_student where schTeaId=#{tno}")
    public List<StudentInfo> findStuByTno(String tno);


    @Select("select * from tb_info_middlereport where middleSno=#{sno}")
    public MiddleReport findBySno(String sno);

}
