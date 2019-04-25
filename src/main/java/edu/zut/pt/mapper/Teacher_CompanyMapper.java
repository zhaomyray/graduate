package edu.zut.pt.mapper;

import edu.zut.pt.pojo.TeacherNotice;
import edu.zut.pt.pojo.Teacher_Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 对校外指导教师登录表的操作
 */
@Mapper
public interface Teacher_CompanyMapper {

    /**
     * 查询校内指导老师登录信息
     * 根据教工号和密码确定该登录信息是否存在
     * @param tno
     * @param password
     * @return
             */
    @Select("select * from tb_login_comtea where tno=#{tno} and password=#{password}")
    public Teacher_Company findTeaComByTnoPsd(String tno, String password);

    /**
     * 根据学工号查询指导教师的姓名
     * @param tno
     * @return
     */
    @Select("select cname from tb_login_comtea where tno=#{tno}")
    public String findTeaNameByTno(String tno);

    /**
     *修改密码
     * 根据教工号修改登录密码
     * @param teacher_company
     * @return
     */
    @Update("update tb_login_comtea set password=#{password} where tno=#{tno}")
    public Teacher_Company updateTeaComPsd(Teacher_Company teacher_company);

}
