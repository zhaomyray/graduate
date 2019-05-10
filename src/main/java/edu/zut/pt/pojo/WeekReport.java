package edu.zut.pt.pojo;

/**
 * 周报实体类
 */
public class WeekReport {

    //周报id
    private int id;
    //提交周报学生学号
    private String weekSno;
    //周报内容
    private String content_weekReport;
    //周报成绩
    private int score;
    //周期信息
    private int weekMessage;
    //周报提交时间
    private String time_submit;
    //是否补交
    private String isAfter;
    //周报评语
    private String weekReview;
    //周报文件路径
    private String weekReportFilePath;


    public int getId() {
        return id;
    }

    public String getWeekSno() {
        return weekSno;
    }

    public String getContent_weekReport() {
        return content_weekReport;
    }

    public int getScore() {
        return score;
    }

    public int getWeekMessage() {
        return weekMessage;
    }

    public String getTime_submit() {
        return time_submit;
    }

    public String getIsAfter() {
        return isAfter;
    }

    public String getWeekReview() {
        return weekReview;
    }

    public String getWeekReportFilePath() {
        return weekReportFilePath;
    }

    public void setWeekReportFilePath(String weekReportFilePath) {
        this.weekReportFilePath = weekReportFilePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeekSno(String weekSno) {
        this.weekSno = weekSno;
    }

    public void setContent_weekReport(String content_weekReport) {
        this.content_weekReport = content_weekReport;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setWeekMessage(int weekMessage) {
        this.weekMessage = weekMessage;
    }

    public void setTime_submit(String time_submit) {
        this.time_submit = time_submit;
    }

    public void setIsAfter(String isAfter) {
        this.isAfter = isAfter;
    }

    public void setWeekReview(String weekReview) {
        this.weekReview = weekReview;
    }
}
