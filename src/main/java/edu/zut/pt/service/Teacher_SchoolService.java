package edu.zut.pt.service;

import edu.zut.pt.pojo.Teacher_School;

public interface Teacher_SchoolService {

    /**
     * 验证校内指导老师的身份
     * @param tno
     * @param password
     * @return
     */
    public Teacher_School findTeaSchByTnoPsd(String tno, String password);

    /**
     * 根据学工号查询老师姓名
     * @param tno
     * @return
     */
    public String findTeaSchByTno(String tno);

    /**
     * 修改密码
     * @param teacher_school
     * @return
     */
    public Teacher_School updateTeaSchPsd(Teacher_School teacher_school);


}
