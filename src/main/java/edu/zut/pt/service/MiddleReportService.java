package edu.zut.pt.service;

import edu.zut.pt.pojo.MiddleReport;

import java.util.List;

public interface MiddleReportService {

    /**
     * 提交中期报告
     * @param middleReport
     * @return
     */
    public int insertMiddleReport(MiddleReport middleReport);

    /**
     * 更新中期报告内容
     * @return
     */
    public int updateMiddleReport(String middleSno,String content_midReport,String time_submit,String isAfter,String middleReportFilePath,String middleReportIsUpdate);

    /**
     * 更新报告状态
     * @param middleReportIsUpdate
     * @param middleSno
     * @return
     */
    public int updateMiddleReportIsUpdate(String middleReportIsUpdate,String middleSno);

    /**
     * 根据学号查找中期报告，返回list
     * @param middleSno
     * @return
     */
    public List<MiddleReport> findMiddleReportBySno(String middleSno);

    /**
     * 根据学号查找中期报告
     * @param middleSno
     * @return
     */
    public MiddleReport findMiddleReportByMidSWM(String middleSno);

}


