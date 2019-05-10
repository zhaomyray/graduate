package edu.zut.pt.pojo;

public class StuComScore {

    private int id;
    private String sno;
    private int dailyScore;
    private int middleScore;
    private int finalScore;
    private String middleFilePath;
    private String finalFilePath;
    private String finalProjectPath;


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

    public int getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(int dailyScore) {
        this.dailyScore = dailyScore;
    }

    public int getMiddleScore() {
        return middleScore;
    }

    public void setMiddleScore(int middleScore) {
        this.middleScore = middleScore;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getMiddleFilePath() {
        return middleFilePath;
    }

    public void setMiddleFilePath(String middleFilePath) {
        this.middleFilePath = middleFilePath;
    }

    public String getFinalFilePath() {
        return finalFilePath;
    }

    public void setFinalFilePath(String finalFilePath) {
        this.finalFilePath = finalFilePath;
    }

    public String getFinalProjectPath() {
        return finalProjectPath;
    }

    public void setFinalProjectPath(String finalProjectPath) {
        this.finalProjectPath = finalProjectPath;
    }
}
