package edu.zut.pt;


import edu.zut.pt.pojo.*;
import edu.zut.pt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计成绩
 */
//将工具类声明为spring组件
@Component
public class CountScore {

    @Autowired
    WeekReportService weekReportService;

    @Autowired
    MonthReportService monthReportService;

    @Autowired
    MiddleReportService middleReportService;

    @Autowired
    FinalReportService finalReportService;

    @Autowired
    ScorePercentService scorePercentService;

    @Autowired
    TimeMessageService timeMessageService;

    @Autowired
    StuComScoreService stuComScoreService;

    @Autowired
    StuScoreService stuScoreService;

    //静态初始化当前类
    public static CountScore countScore;

    //在方法上加上此注解，这样方法就会在Bean初始化时候被Spring容器执行
    @PostConstruct
    public void init(){
        countScore = this;
        countScore.timeMessageService = this.timeMessageService;
        countScore.weekReportService = this.weekReportService;
        countScore.monthReportService = this.monthReportService;
        countScore.middleReportService = this.middleReportService;
        countScore.finalReportService = this.finalReportService;
        countScore.scorePercentService = this.scorePercentService;
    }

    //校外自行联系成绩统计
    public  Map<String,Object> countAllScore(String sno){

        //平时成绩 = 【（周报成绩之和/提交周报次数）+（月报成绩之和/提交月报次数）】/2
        //中期成绩 = 中期成绩  实训报告成绩 = 实训报告成绩
        // 实训成绩 = 平时成绩*平时比例 + 中期成绩*中期比例 + 实训报告成绩*实训成绩比例
        //先计算平时成绩
        TimeMessage timeMessage = countScore.timeMessageService.getTimeMessage(1);
        int weekNum = timeMessage.getWeekNums();//周报提交总次数
        int monthNum = timeMessage.getMonthNums();//月报提交总次数
        int allWeekScore = 0;
        //计算周报总成绩
        for(int i = 0;i<weekNum;i++){//拿到每次的周报成绩
            String weekMessage1 = String.valueOf(i+1);
            int weekScore ;
            if(countScore.weekReportService.findWeekReportByweekSWM(sno,weekMessage1)==null){//周报信息表没有该生该周的周报信息，说明此周报未提交，默认成绩为0
                weekScore = 0;
            }else{//周报信息表中有该周报的信息，获取此周报的成绩
                weekScore = countScore.weekReportService.findWeekReportByweekSWM(sno,weekMessage1).getScore();
            }
            allWeekScore = allWeekScore+weekScore;//计算出所有周报的成绩之和
        }
        System.out.println("周报总成绩为："+allWeekScore);
        //计算月报总成绩
        int allMonthScore = 0;
        for(int i = 0; i<monthNum; i++){//拿到每次的月报成绩
            String monthMessage = String.valueOf(i+1);
            int monthScore;
            if(countScore.monthReportService.findMonthReportBymonthSWM(sno,monthMessage)==null){//月报信息表中没有该生该月的报告信息，说明未提交此月报，默认成绩为0
                monthScore = 0;
                System.out.println("第"+monthMessage+"月月报成绩为"+monthScore);
            }else{//月报信息表中有此月报信息，获取月报成绩
                monthScore = countScore.monthReportService.findMonthReportBymonthSWM(sno,monthMessage).getScore();
            }
            System.out.println("第"+monthMessage+"月月报成绩为"+monthScore);
            allMonthScore = allMonthScore+monthScore;
        }
        System.out.println("月报总成绩为："+allMonthScore);
        //平时成绩
        float floatDaily= (float)((double)(((double)(allWeekScore/weekNum)+(double)(allMonthScore/monthNum))/2));
        int dailyScore = Math.round(floatDaily);
        System.out.println("平时成绩的float类型："+floatDaily+"平时成绩的int类型："+dailyScore);
        //得到中期成绩
        int middleScore ;
        if(countScore.middleReportService.findMiddleReportByMidSWM(sno)==null){
            middleScore = 0;
        }else{
            middleScore = countScore.middleReportService.findMiddleReportByMidSWM(sno).getScore();
        }
        //得到实训成绩
        int finalScore;
        if(countScore.finalReportService.findFinalReportBySno(sno)==null){
            finalScore = 0;
        }else{
            finalScore = countScore.finalReportService.findFinalReportBySno(sno).getScore();
        }

        //获取成绩比例：
        ScorePercent scorePercent= countScore.scorePercentService.getScorePercentInfo(1);
        float dailyPercent = (float)((double)scorePercent.getUsualGrade()/10);
        float middlePercent = (float)((double)scorePercent.getMiddleGrade()/10);
        float finalPercent = (float)((double)scorePercent.getFinalGrade()/10);
        System.out.println(scorePercent.getUsualGrade()+"--"+scorePercent.getMiddleGrade()+"--"+scorePercent.getFinalGrade()+"--"+dailyPercent+"--"+middlePercent+":"+finalPercent);
        float floatScore = (float)((double)(dailyScore * dailyPercent)) + (float)((double)middleScore * middlePercent) + (float)((double)finalScore * finalPercent);
        System.out.println("实训成绩的float类型为："+floatScore);
        //计算实训成绩
        int score = Math.round(floatScore);
        System.out.println("实训成绩为："+score);

        Map<String,Object> scoreMap = new HashMap<>();
        scoreMap.put("dailyScore",dailyScore);
        scoreMap.put("middleScore",middleScore);
        scoreMap.put("finalScore",finalScore);
        scoreMap.put("score",score);


        return scoreMap;
    }

    //校内合作实训公司成绩统计
    public Map<String,Object> countComAllScore(String sno){
        Map<String,Object> comScoreMap = new HashMap<>();

        //对于校内合作实训公司的学生
        //平时成绩：先去实训成绩表中，查看是否有自己的成绩信息：如果有，直接获取该平时成绩；如果没有，再去校外实训成绩表中看是否有自己的成绩信息：如果有，就获取校外实训成绩表中的实训成绩；如果没有就默认为0
        //平时成绩：1、平时成绩=实训成绩表中平时成绩   2、平时成绩=校外实训成绩表中平时成绩    3、平时成绩=0
        //中期成绩：获取校外老师给的中期成绩middle1，获取校内老师给的中期成绩middle2---中期成绩=（middle1 + middle2）/2
        //实训报告成绩：获取校外老师给的实训报告成绩final1，获取校内老师给的实训报告成绩final2---实训报告成绩=（final1 + final2）/2
        //实训成绩：实训成绩 = 平时成绩 * 平时比例 + 中期成绩 * 中期比例 + 实训报告成绩 * 实训成绩比例

       //获取平时成绩
        float floatDailyScore;
        //判断实训成绩表中是否存在该生的成绩
        System.out.println("校企合作：马上进入判断实训成绩表中有没有该生的成绩信息的判断");
        if(countScore.stuScoreService.getStuScore(sno)==null){//实训成绩表中没有该生的成绩信息
            System.out.println("进入到判断中1");
            //判断校外实训成绩表中有没有该生的成绩信息
            if(countScore.stuComScoreService.selectStuComScore(sno)==null){//校外实训成绩表中没有该生的成绩信息
                //默认平时成绩为0
                floatDailyScore = 0;
            }else{//校外实训成绩表中有该生的成绩信息
                floatDailyScore = countScore.stuComScoreService.selectStuComScore(sno).getDailyScore();
            }
        }else{//实训成绩表中有该生的成绩信息
            //获取实训成绩表中的平时成绩
            floatDailyScore = countScore.stuScoreService.getStuScore(sno).getDailyScore();
        }
        int dailyScore = Math.round(floatDailyScore);//平时成绩转为int类型

        //获取中期成绩
        float floatMiddle;    //中期成绩
        float floatMiddle1;    //校外指导教师给的中期成绩
        float floatMiddle2;    //校内指导教师给的中期成绩
        //判断校外实训成绩表是否有该生的成绩信息
        if(countScore.stuComScoreService.selectStuComScore(sno)==null){//校外实训成绩表中没有该生的实训成绩信息
            //默认中期成绩为0
            floatMiddle1 = 0;
        }else{//校外实训成绩表中有该生的实训成绩信息
            //获取实训成绩表中的中期成绩
            floatMiddle1 = countScore.stuComScoreService.selectStuComScore(sno).getMiddleScore();
        }
        //判断中期报告信息表中是否有该生的成绩信息
        if(countScore.middleReportService.findMiddleReportByMidSWM(sno)==null){//中期报告信息表中没有该生的报告信息
            //默认成绩为0
            floatMiddle2 = 0;
        }else{//中期报告信息表中有该生的报告信息
            floatMiddle2 = countScore.middleReportService.findMiddleReportByMidSWM(sno).getScore();
        }
        //计算中期成绩
        floatMiddle = Math.round((float)((double)((floatMiddle1 + floatMiddle2)/2)));
        int middleScore = Math.round(floatMiddle);//中期成绩转化为int类型

        //获取实训报告成绩
        float floatFinalScore;    //实训报告成绩
        float floatFinal1;        //校外老师给的实训报告成绩
        int floatFinal2;        //校内老师给的实训报告成绩
        //判断校外实训成绩表中是否有该生的成绩信息
        if(countScore.stuComScoreService.selectStuComScore(sno)==null){//校外实训成绩表中没有该生的成绩信息
            //默认成绩为0
            floatFinal1 = 0;
        }else{//校外实训成绩表中有该生的成绩信息
            //获取校外实训成绩表中的实训报告成绩
            floatFinal1 = countScore.stuComScoreService.selectStuComScore(sno).getFinalScore();
        }
        //判断实训报告信息表中是否有该生的成绩信息
        if(countScore.finalReportService.findFinalReportBySno(sno)==null){//实训报告信息表中没有该生的报告信息
            //默认成绩为0
            floatFinal2 = 0;
        }else{//实训报告信息表中有该生的报告信息
            //获取实训报告信息表中的报告成绩
            floatFinal2 = countScore.finalReportService.findFinalReportBySno(sno).getScore();
        }
        //计算实训报告成绩
        floatFinalScore = Math.round((float)((double)((floatFinal1+floatFinal2)/2)));
        int finalScore = Math.round(floatFinalScore);//实训报告成绩转化为int类型

        //获取成绩比例：
        ScorePercent scorePercent= countScore.scorePercentService.getScorePercentInfo(1);
        float dailyPercent = (float)((double)scorePercent.getUsualGrade()/10);
        float middlePercent = (float)((double)scorePercent.getMiddleGrade()/10);
        float finalPercent = (float)((double)scorePercent.getFinalGrade()/10);

        //计算实训成绩
        float floatScore = (float)((double)(floatDailyScore * dailyPercent)) + (float)((double)floatMiddle * middlePercent) + (float)((double)floatFinalScore * finalPercent);
        int score = Math.round(floatScore);

        comScoreMap.put("dailyScore",dailyScore);
        comScoreMap.put("middleScore",middleScore);
        comScoreMap.put("finalScore",finalScore);
        comScoreMap.put("score",score);

        return comScoreMap;
    }


}
