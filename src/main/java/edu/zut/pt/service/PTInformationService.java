package edu.zut.pt.service;

import edu.zut.pt.pojo.StudentInfo;

import java.util.List;

public interface PTInformationService {

    /**
     * 修改学生实训信息
     * @param studentInfo
     * @return
     */
    public int updatePTInformation(StudentInfo studentInfo);

    /**
     * 根据学号查找学生实训信息
     * @param sno
     * @return
     */
    public StudentInfo findBySno(String sno);

    /**
     * 根据校内老师id查找学生实训信息
     * @param schTeaId
     * @return
     */
    public List<StudentInfo> findBySchTeaId(String schTeaId);

    /**
     * 根据校外老师姓名查找学生实训信息
     * @param comTeaName
     * @return
     */
    public List<StudentInfo> findBySchTeaName(String comTeaName);

}
