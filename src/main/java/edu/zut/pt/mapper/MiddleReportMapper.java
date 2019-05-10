package edu.zut.pt.mapper;

import edu.zut.pt.pojo.MiddleReport;
import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Insert("insert into tb_info_middlereport(middleSno,content_midReport,score,time_submit,isAfter,middleReview,middleReportFilePath,isUpdate) value(#{middleSno},#{content_midReport},#{score},#{time_submit},#{isAfter},#{middleReview},#{middleReportFilePath},#{isUpdate})")
    public int insertMiddleReport(MiddleReport middleReport);

    /**
     * 根据学号更新中期报告的内容
     * @param middleSno
     * @return
     */
    @Update("update tb_info_middlereport set content_midReport=#{content_midReport},time_submit=#{time_submit},isAfter=#{isAfter},middleReportFilePath=#{middleReportFilePath},isUpdate=#{middleReportIsUpdate} where middleSno=#{middleSno}")
    public int updateMiddleReport(String middleSno,String content_midReport,String time_submit,String isAfter,String middleReportFilePath,String middleReportIsUpdate);

    /**
     * 根据学号更新中期报告状态
     * @param middleReportIsUpdate
     * @param middleSno
     * @return
     */
    @Update("update tb_info_middlereport set isUpdate=#{middleReportIsUpdate} where middleSno=#{middleSno}")
    public int updateMiddleReportIsUpdate(String middleReportIsUpdate,String middleSno);

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
