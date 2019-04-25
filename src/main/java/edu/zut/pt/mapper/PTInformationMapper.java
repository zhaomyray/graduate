package edu.zut.pt.mapper;

import edu.zut.pt.pojo.Student;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.pojo.WeekReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PTInformationMapper {

    /**
     * 添加学生实训信息
     * @param  studentInfo
     * @return int
     */
    @Update("update tb_info_student set typeName=#{typeName},comTeaName=#{comTeaName},projectName=#{projectName},phone=#{phone},qqemail=#{qqemail},parPhone=#{parPhone},comTeaPhone=#{comTeaPhone},comName=#{comName},city=#{city} where sno=#{sno}")
    public int updatePTInformation(StudentInfo studentInfo);

    /**
     * 根据学号查找实训信息
     * @param sno
     * @return
     */
    @Select("select * From tb_info_student where sno=#{sno}")
    public StudentInfo findBySno(String sno);

    /**
     * 根据校内指导老师的学工号查找学生的实训信息
     * @param schTeaId
     * @return
     */
    @Select("select * From tb_info_student where schTeaId=#{schTeaId}")
    public List<StudentInfo> findBySchTeaId(String schTeaId);

    /**
     * 根据校外指导老师姓名查找学生的实训信息
     * @param comTeaName
     * @return
     */
    @Select("select * from tb_info_student where comTeaName=#{comTeaName}")
    public List<StudentInfo> findBySchTeaName(String comTeaName);


}
