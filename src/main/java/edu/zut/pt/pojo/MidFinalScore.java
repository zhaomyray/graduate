package edu.zut.pt.pojo;

public class MidFinalScore {

    private String id;
    private String sno;
    private String sname;
    private int midScore;
    private int finalScore;

    public String getId() {
        return id;
    }

    public String getSno() {
        return sno;
    }

    public String getSname() {
        return sname;
    }

    public int getMidScore() {
        return midScore;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setMidScore(int midScore) {
        this.midScore = midScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
