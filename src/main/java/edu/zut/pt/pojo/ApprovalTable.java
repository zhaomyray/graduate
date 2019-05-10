package edu.zut.pt.pojo;

import java.util.Date;

public class ApprovalTable {

    int id;
    String sno;
    String time_submit;
    String tableFilePath;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getTime_submit() {
        return time_submit;
    }

    public void setTime_submit(String time_submit) {
        this.time_submit = time_submit;
    }

    public String getTableFilePath() {
        return tableFilePath;
    }

    public void setTableFilePath(String tableFilePath) {
        this.tableFilePath = tableFilePath;
    }
}
