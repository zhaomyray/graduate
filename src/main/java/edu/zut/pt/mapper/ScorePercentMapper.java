package edu.zut.pt.mapper;

import edu.zut.pt.pojo.ScorePercent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScorePercentMapper {

    /***
     * 获取成绩比例信息
     * @param id
     * @return
     */
    @Select("select * from sys_gradesetting where id=#{id}")
    public ScorePercent getScorePercentInfo(int id);

}
