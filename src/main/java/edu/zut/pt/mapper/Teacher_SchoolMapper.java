package edu.zut.pt.mapper;

import edu.zut.pt.pojo.Teacher_School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 对校内指导教师登录信息表的操作
 */
@Mapper
public interface Teacher_SchoolMapper {

    /**
     * 查询校内指导老师登录信息
     * 根据教工号和密码确定该登录信息是否存在
     * @param tno
     * @param password
     * @return
     */
    @Select("select * from tb_login_schtea where tno=#{tno} and password=#{password}")
    public Teacher_School findTeaSchByTnoPsd(String tno,String password);


    /**
     * 根据老师学工号查询老师姓名
     * @param tno
     * @return
     */
    @Select("select tname from tb_login_schtea where tno=#{tno}")
    public String findTeaSchByTno(String tno);

    /**
     *修改密码
     * 根据教工号修改登录密码
     * @param teacher_school
     * @return
     */
    @Update("update tb_login_schtea set password=#{password} where tno=#{tno}")
    public Teacher_School updateTeaSchPsd(Teacher_School teacher_school);


}
