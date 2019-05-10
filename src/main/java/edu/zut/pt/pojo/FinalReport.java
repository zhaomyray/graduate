package edu.zut.pt.pojo;

/**
 * 实训报告项目信息实体类
 */
public class FinalReport {

    //实训id
    private int id;
    //学生学号
    private String finalSno;
    //实训报告地址
    private String add_finReport;
    //实训项目地址
    private String add_finalProject;
    //实训成绩
    private int score;
    //实训报告、项目提交时间
    private String time_submit;
    //是否补交
    private String isAfter;
    //实训评语
    private String reportReview;
    //报告状态
    private String isUpdate;


    public int getId() {
        return id;
    }

    public String getFinalSno() {
        return finalSno;
    }

    public String getAdd_finReport() {
        return add_finReport;
    }

    public String getAdd_finalProject() {
        return add_finalProject;
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

    public String getReportReview() {
        return reportReview;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFinalSno(String finalSno) {
        this.finalSno = finalSno;
    }

    public void setAdd_finReport(String add_finReport) {
        this.add_finReport = add_finReport;
    }

    public void setAdd_finalProject(String add_finalProject) {
        this.add_finalProject = add_finalProject;
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

    public void setReportReview(String reportReview) {
        this.reportReview = reportReview;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }
}
