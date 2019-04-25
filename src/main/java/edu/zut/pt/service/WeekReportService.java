package edu.zut.pt.service;

import edu.zut.pt.pojo.WeekReport;

import java.util.List;


public interface WeekReportService {

    /**
     * 插入周期报告
     * @param weekReport
     * @return
     */
    public int insertWeekReport(WeekReport weekReport);

    /**
     * 根据学号查找报告
     * @param weekSno
     * @return
     */
    public List<WeekReport> findWeekReportBySno(String weekSno);

    /**
     * 根据学号和周期信息查找报告
     * @param weekSno
     * @param weekMessage
     * @return
     */
    public WeekReport findWeekReportByweekSWM(String weekSno,String weekMessage);
}
