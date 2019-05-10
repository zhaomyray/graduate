package edu.zut.pt.pojo;

/**
 * 学生成绩实体类
 */
public class StudentScore {

        //成绩id
        private int id;
        //学号
        private String sno;
        //平时成绩
        private int dailyScore;
        //中期成绩
        private int middleScore;
        //实训报告成绩
        private int ptScore;
        //实训成绩
        private int score;

        public int getId() {
            return id;
        }

        public String getSno() {
            return sno;
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

        public void setSno(String sno) {
            this.sno = sno;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

