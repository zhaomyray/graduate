package edu.zut.pt.mapper;

import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MiddleReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 对实训报告和项目源码的操作
 */
@Mapper
public interface FinalReportMapper {

    /**
     * 插入实训报告和项目信息
     * @param middleReport
     * @return
     */
    @Insert("insert into tb_info_final(finalSno,add_finReport,add_finalProject,time_submit,isAfter) value(#{finalSno},#{add_finReport},#{add_finalProject},#{time_submit},#{isAfter})")
    public int insertFinal(MiddleReport middleReport);

    /**
     * 插入实训报告批阅信息
     * @param finalSno
     * @return
     */
    @Insert("insert into tb_info_final(score,reportReview) values(#{score},#{reportReview})")
    public int insertFinalReportBySno(String finalSno);

    /**
     * 查找某学生的实训报告和项目信息
     * @param finalSno
     * @return
     */
    @Select("select * From tb_info_final where finalSno=#{finalSno}")
    public FinalReport findFinalReportBySno(String finalSno);

}
