package edu.zut.pt.service;

import edu.zut.pt.pojo.MonthReport;

import java.util.List;

public interface MonthReportService {

    /**
     * 提交月报
     * @param monthReport
     * @return
     */
    public int insertMonthReport(MonthReport monthReport);

    /**
     * 根据学号查找月报
     * @param monthSno
     * @return
     */
    public List<MonthReport> findMonthReportBySno(String monthSno);

    /**
     * 根据学号和月期信息查找月报
     * @param monthSno
     * @param monthMessage
     * @return
     */
    public MonthReport findMonthReportBymonthSWM(String monthSno,String monthMessage);

}
