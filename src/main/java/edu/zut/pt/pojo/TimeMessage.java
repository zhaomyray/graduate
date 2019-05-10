package edu.zut.pt.pojo;

import java.util.Date;

public class TimeMessage {

    private int id;
    private Date messageBeginTime;
    private Date messageEndTime;
    private int weekNums;
    private int monthNums;
    private Date firstWeekBeginTime;
    private Date firstWeekEndTime;
    private Date firstMonthBeginTime;
    private Date firstMonthEndTime;
    private int startBuJiao;

    public int getId() {
        return id;
    }

    public Date getMessageBeginTime() {
        return messageBeginTime;
    }

    public Date getMessageEndTime() {
        return messageEndTime;
    }

    public int getWeekNums() {
        return weekNums;
    }

    public int getMonthNums() {
        return monthNums;
    }

    public Date getFirstWeekBeginTime() {
        return firstWeekBeginTime;
    }

    public Date getFirstWeekEndTime() {
        return firstWeekEndTime;
    }

    public Date getFirstMonthBeginTime() {
        return firstMonthBeginTime;
    }

    public Date getFirstMonthEndTime() {
        return firstMonthEndTime;
    }

    public int getStartBuJiao() {
        return startBuJiao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessageBeginTime(Date messageBeginTime) {
        this.messageBeginTime = messageBeginTime;
    }

    public void setMessageEndTime(Date messageEndTime) {
        this.messageEndTime = messageEndTime;
    }

    public void setWeekNums(int weekNums) {
        this.weekNums = weekNums;
    }

    public void setMonthNums(int monthNums) {
        this.monthNums = monthNums;
    }

    public void setFirstWeekBeginTime(Date firstWeekBeginTime) {
        this.firstWeekBeginTime = firstWeekBeginTime;
    }

    public void setFirstWeekEndTime(Date firstWeekEndTime) {
        this.firstWeekEndTime = firstWeekEndTime;
    }

    public void setFirstMonthBeginTime(Date firstMonthBeginTime) {
        this.firstMonthBeginTime = firstMonthBeginTime;
    }

    public void setFirstMonthEndTime(Date firstMonthEndTime) {
        this.firstMonthEndTime = firstMonthEndTime;
    }

    public void setStartBuJiao(int startBuJiao) {
        this.startBuJiao = startBuJiao;
    }
}
