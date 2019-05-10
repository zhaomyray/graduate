package edu.zut.pt.mapper;

import edu.zut.pt.pojo.ApprovalTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface ApprovalTableMapper {

    /**
     * 添加审批表信息
     * @param sno
     * @param time_submit
     * @param tableFilePath
     * @return
     */
    @Insert("insert into tb_info_approvaltable(sno,time_submit,tableFilePath) values(#{sno},#{time_submit},#{tableFilePath})")
    public int insertApprovalTableInfo(String sno, String time_submit, String tableFilePath);


    /**
     * 更新审批表信息
     * @param sno
     * @param time_submit
     * @param tableFilePath
     * @return
     */
    @Update("update tb_info_approvaltable set time_submit=#{time_submit},tableFilePath=#{tableFilePath} where sno=#{sno}")
    public int updateApprovalTableInfo(String sno,String time_submit,String tableFilePath);


    /**
     * 查找某学生的审批表信息
     * @param sno
     * @return
     */
    @Select("select * from tb_info_approvaltable where sno=#{sno}")
    public ApprovalTable selectApprovalTableInfoBySno(String sno);
}
