package edu.zut.pt.mapper;

import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对周报信息表的操作
 */
@Mapper
public interface WeekReportMapper {

    /**
     * 添加周报信息
     * @param weekReport
     * @return
     */
    @Insert("insert into tb_info_weekreport(weekSno,content_weekReport,weekMessage,time_submit,isAfter) value(#{weekSno},#{content_weekReport},#{weekMessage},#{time_submit},#{isAfter})")
    public int insertWeekReport(WeekReport weekReport);


//    /**
//     * 添加周报批阅信息
//     * @param id
//     * @return
//     */
//    @Insert("insert into tb_info_weekreport(score,weekReview) values(#{score},#{weekReview})")
//    public int insertWeekReportById(Integer id);
//
    /**
     * 查找某学生已提交的周报信息
     * @param weekSno
     * @param weekSno
     * @return
     */
    @Select("select * From tb_info_weekreport where weekSno=#{weekSno}")
    public List<WeekReport> findWeekReportBySno(String weekSno);

    /**
     * 查找某周某些学生的所有周报信息
     * @param weekSno
     * @return
     */
    @Select("select * From tb_info_weekreport where weekSno=#{weekSno} and weekMessage=#{weekMessage}")
    public WeekReport findWeekReportByweekSWM(String weekSno,String weekMessage);



}
