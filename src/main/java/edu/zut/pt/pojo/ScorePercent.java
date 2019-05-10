package edu.zut.pt.pojo;

//成绩比例实体类
public class ScorePercent {

    private int id;
    private int usualGrade;
    private int middleGrade;
    private int finalGrade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsualGrade() {
        return usualGrade;
    }

    public void setUsualGrade(int usualGrade) {
        this.usualGrade = usualGrade;
    }

    public int getMiddleGrade() {
        return middleGrade;
    }

    public void setMiddleGrade(int middleGrade) {
        this.middleGrade = middleGrade;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }
}
