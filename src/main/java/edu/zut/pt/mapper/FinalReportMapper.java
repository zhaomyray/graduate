package edu.zut.pt.mapper;

import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MiddleReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 对实训报告和项目源码的操作
 */
@Mapper
public interface FinalReportMapper {

    /**
     * 插入实训报告和项目信息
     * @param finalReport
     * @return
     */
    @Insert("insert into tb_info_final(finalSno,add_finReport,add_finalProject,score,time_submit,isAfter,reportReview,isUpdate) value(#{finalSno},#{add_finReport},#{add_finalProject},#{score},#{time_submit},#{isAfter},#{reportReview},#{isUpdate})")
    public int insertFinal(FinalReport finalReport);


    /**
     * 根据学生学号修改实训报告和项目信息
     * @param finalSno
     * @param add_finReport
     * @param add_finalProject
     * @param time_submit
     * @return
     */
    @Update("update tb_info_final set add_finReport=#{add_finReport},add_finalProject=#{add_finalProject},time_submit=#{time_submit},isAfter=#{isAfter},isUpdate=#{isUpdate} where finalSno=#{finalSno}")
    public int updateFinal(String finalSno,String add_finReport,String add_finalProject,String time_submit,String isAfter,String isUpdate);
//    /**
//     * 插入实训报告批阅信息
//     * @param finalSno
//     * @return
//     */
//    @Insert("insert into tb_info_final(score,reportReview) values(#{score},#{reportReview})")
//    public int insertFinalReportBySno(String finalSno);

    /**
     * 查找某学生的实训报告和项目信息
     * @param finalSno
     * @return
     */
    @Select("select * From tb_info_final where finalSno=#{finalSno}")
    public FinalReport findFinalReportBySno(String finalSno);

}
