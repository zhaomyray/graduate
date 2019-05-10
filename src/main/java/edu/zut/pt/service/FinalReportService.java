package edu.zut.pt.service;

import edu.zut.pt.pojo.FinalReport;

public interface FinalReportService {

    /**
     * 插入实训报告及项目信息
     * @param finalReport
     * @return
     */
    public int insertFinal(FinalReport finalReport);

    /**
     * 根据学生学号修改实训报告和项目信息
     * @param finalSno
     * @param add_finReport
     * @param add_finalProject
     * @param time_submit
     * @return
     */
    public int updateFinal(String finalSno,String add_finReport,String add_finalProject,String time_submit,String isAfter,String isUpdate);

    /**
     * 根据学号查找某学生的报告信息
     * @param finalSno
     * @return
     */
    public FinalReport findFinalReportBySno(String finalSno);



}
