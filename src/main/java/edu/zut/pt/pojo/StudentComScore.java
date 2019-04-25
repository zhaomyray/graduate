package edu.zut.pt.pojo;

/**
 * 校外自行联系实训学生成绩实体类
 */
public class StudentComScore {

    //成绩id
    private int id;
    //学号
    private String comSno;
    //平时成绩
    private int dailyScore;
    //中期成绩
    private int middleScore;
    //实训成绩
    private int ptScore;

    public int getId() {
        return id;
    }

    public String getComSno() {
        return comSno;
    }

    public int getDailyScore() {
        return dailyScore;
    }

    public int getMiddleScore() {
        return middleScore;
    }

    public int getPtScore() {
        return ptScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComSno(String comSno) {
        this.comSno = comSno;
    }

    public void setDailyScore(int dailyScore) {
        this.dailyScore = dailyScore;
    }

    public void setMiddleScore(int middleScore) {
        this.middleScore = middleScore;
    }

    public void setPtScore(int ptScore) {
        this.ptScore = ptScore;
    }
}
