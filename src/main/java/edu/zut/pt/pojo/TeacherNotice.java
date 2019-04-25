package edu.zut.pt.pojo;

/**
 * 教师公告实体类
 */

public class TeacherNotice {

    //公告id
    private int  id;
    //发布老师id
    private String teaId;
    //公告标题
    private String noticeTitle;
    //公告内容
    private String noticeContent;
    //发布时间
    private String noticeTime;

    public int getId() {
        return id;
    }

    public String getTeaId() {
        return teaId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }
}
