package edu.zut.pt.pojo;

/**
 * 中期报告信息实体类
 */
public class MiddleReport {

    //中期报告id
    private int id;
    //学号
    private String middleSno;
    //报告内容
    private String content_midReport;
    //分数
    private int score;
    //提交时间
    private String time_submit;
    //是否补交
    private String isAfter;
    //评语
    private String middleReview;

    public int getId() {
        return id;
    }

    public String getMiddleSno() {
        return middleSno;
    }

    public String getContent_midReport() {
        return content_midReport;
    }

    public int getScore() {
        return score;
    }

    public String getTime_submit() {
        return time_submit;
    }

    public String getIsAfter() {
        return isAfter;
    }

    public String getMiddleReview() {
        return middleReview;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMiddleSno(String middleSno) {
        this.middleSno = middleSno;
    }

    public void setContent_midReport(String content_midReport) {
        this.content_midReport = content_midReport;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTime_submit(String time_submit) {
        this.time_submit = time_submit;
    }

    public void setIsAfter(String isAfter) {
        this.isAfter = isAfter;
    }

    public void setMiddleReview(String middleReview) {
        this.middleReview = middleReview;
    }
}
