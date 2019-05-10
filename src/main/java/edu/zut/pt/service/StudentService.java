package edu.zut.pt.service;

import edu.zut.pt.pojo.Student;

public interface StudentService {

    /**
     * 验证登录信息
     * @param sno
     * @param password
     * @return
     */
    public Student findStudentBySnoPsd(String sno, String password);

    /**
     * 根据学生的学号查找密码
     * @param sno
     * @return
     */
    public String findStudentPsdBySno(String sno);

    /**
     * 修改密码
     * @param student
     * @return
     */
    public int updateStudentPsd(Student student);
}
