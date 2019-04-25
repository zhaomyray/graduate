package edu.zut.pt.mapper;

import edu.zut.pt.pojo.MonthReport;
import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *对月报信息表的操作
 */
@Mapper
public interface MonthReportMapper {
    /**
     * 添加月报信息
     * @param monthReport
     * @return
     */
    @Insert("insert into tb_info_monthreport(monthSno,content_monthReport,monthMessage,time_submit,isAfter) value(#{monthSno},#{content_monthReport},#{monthMessage},#{time_submit},#{isAfter})")
    public int insertMonthReport(MonthReport monthReport);


//    /**
//     * 添加批阅信息
//     * @param id
//     * @return
//     */
//    @Insert("insert into tb_info_weekreport(score,weekReview) values(#{score},#{weekReview})")
//    public int insertWeekReportById(Integer id);

    /**
     * 查找某学生已提交的月报信息
     * @param monthSno
     * @param monthSno
     * @return
     */
    @Select("select * From tb_info_monthreport where monthSno=#{monthSno}")
    public List<MonthReport> findMonthReportBySno(String monthSno);

    /**
     * 查找某周某些学生的所有周报信息
     * @param monthSno
     * @return
     */
    @Select("select * From tb_info_monthreport where monthSno=#{monthSno} and monthMessage=#{monthMessage}")
    public MonthReport findMonthReportBymonthSWM(String monthSno,String monthMessage);


}
