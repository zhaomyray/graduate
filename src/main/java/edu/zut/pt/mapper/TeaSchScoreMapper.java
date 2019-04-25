package edu.zut.pt.mapper;


import edu.zut.pt.pojo.FinalReport;
import edu.zut.pt.pojo.MiddleReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeaSchScoreMapper {

    @Select("select * from tb_info_middlereport where middleSno=#{sno}")
    public MiddleReport findMidScoreBySno(String sno);


    @Select("select * from tb_info_final where finalSno=#{sno}")
    public FinalReport findFinalScoreBySno(String sno);

}
