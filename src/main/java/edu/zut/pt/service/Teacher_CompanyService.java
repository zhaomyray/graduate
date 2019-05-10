package edu.zut.pt.service;

import edu.zut.pt.pojo.Teacher_Company;

public interface Teacher_CompanyService {

    /**
     * 验证身份
     * @param tno
     * @param password
     * @return
     */
    public Teacher_Company findTeaComByTnoPsd(String tno, String password);

    /**
     * 根据学工号查找姓名
     * @param tno
     * @return
     */
    public String findTeaNameByTno(String tno);

    /**
     * 根据学工号查找密码
     * @param tno
     * @return
     */
    public String findTeaComPsdByTno(String tno);


    /**
     * 修改密码
     * @param teacher_company
     * @return
     */
    public int updateTeaComPsd(Teacher_Company teacher_company);

}
