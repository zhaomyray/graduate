package edu.zut.pt.mapper;

import edu.zut.pt.pojo.TimeMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TimeMessageMapper {

    @Select("select * from sys_setting where id=#{id}")
    public TimeMessage getTimeMessage(int id);

}
