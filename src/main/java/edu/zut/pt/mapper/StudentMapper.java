package edu.zut.pt.mapper;


import edu.zut.pt.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 查找学生的登录信息
 */
@Mapper
public interface StudentMapper {

    /**
     * 查找登录信息
     * 根据学号密码查找学生登录信息是否存在
     * @param sno
     * @param password
     * @return
     */
    @Select("select * from tb_login_student where sno=#{sno} and password=#{password}")
    public Student findStudentBySnoPsd(String sno,String password);

    /**
     * 修改密码
     * 根据学号修改对应的密码
     * @param student
     * @return
     */
    @Update("update tb_login_student set password=#{password} where sno=#{sno}")
    public Student updateStudentPsd(Student student);




//    @Select("select * from tb_login_student where sno=#{sno} AND password=#{password}")
//    public int findStudentBySnoPsd(String sno,String password);

//    @Select("select * from tb_login_student where sno=#{sno}")
//    public Student findStudentBySno(String sno);
//
//    @Select("select * from tb_login_student")
//    public int finStudentAll();
//
//    @Insert("insert into tb_login_student(sno,password) values(#{sno},#{password})")
//    public int insertStudent(Student student);



}
