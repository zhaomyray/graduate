package edu.zut.pt.mapper;

import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对中期报告表的操作
 */
@Mapper
public interface MiddleReportMapper {

    /**
     * 添加中期报告信息
     * @param middleReport
     * @return
     */
    @Insert("insert into tb_info_middlereport(middleSno,content_midReport,time_submit,isAfter) value(#{middleSno},#{content_midReport},#{time_submit},#{isAfter})")
    public int insertMiddleReport(MiddleReport middleReport);


//    /**
//     * 添加周报批阅信息
//     * @param id
//     * @return
//     */
//    @Insert("insert into tb_info_weekreport(score,weekReview) values(#{score},#{weekReview})")
//    public int insertWeekReportById(Integer id);
//
    /**
     * 查找某学生已提交的中期报告信息
     * @param middleSno
     * @param middleSno
     * @return
     */
    @Select("select * From tb_info_middlereport where middleSno=#{middleSno}")
    public List<MiddleReport> findMiddleReportBySno(String middleSno);

    /**
     * 查找某些学生的中期报告信息
     * @param middleSno
     * @return
     */
    @Select("select * From tb_info_middlereport where middleSno=#{middleSno}")
    public MiddleReport findMiddleReportByMidSWM(String middleSno);

}
