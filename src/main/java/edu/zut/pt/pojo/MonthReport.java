package edu.zut.pt.pojo;

/**
 * 月报信息实体类
 */
public class MonthReport {

    //月报id
    private int id;
    //学号
    private String monthSno;
    //月报内容
    private String content_monthReport;
    //成绩
    private int score;
    //月期信息
    private int monthMessage;
    //提交时间
    private String time_submit;
    //是否补交
    private String isAfter;
    //月报评语
    private String monthReview;
    //月报文件路径
    private String monthReportFilePath;

    public int getId() {
        return id;
    }

    public String getMonthSno() {
        return monthSno;
    }

    public String getContent_monthReport() {
        return content_monthReport;
    }

    public int getScore() {
        return score;
    }

    public int getMonthMessage() {
        return monthMessage;
    }

    public String getTime_submit() {
        return time_submit;
    }

    public String getIsAfter() {
        return isAfter;
    }

    public String getMonthReview() {
        return monthReview;
    }

    public String getMonthReportFilePath() {
        return monthReportFilePath;
    }

    public void setMonthReportFilePath(String monthReportFilePath) {
        this.monthReportFilePath = monthReportFilePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonthSno(String monthSno) {
        this.monthSno = monthSno;
    }

    public void setContent_monthReport(String content_monthReport) {
        this.content_monthReport = content_monthReport;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMonthMessage(int monthMessage) {
        this.monthMessage = monthMessage;
    }

    public void setTime_submit(String time_submit) {
        this.time_submit = time_submit;
    }

    public void setIsAfter(String isAfter) {
        this.isAfter = isAfter;
    }

    public void setMonthReview(String monthReview) {
        this.monthReview = monthReview;
    }
}
