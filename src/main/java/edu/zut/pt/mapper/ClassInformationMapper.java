package edu.zut.pt.mapper;

import edu.zut.pt.pojo.ClassInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassInformationMapper {

    @Select("select * from tb_info_class where classId=#{calssId}")
    public ClassInformation getClassInforById(int classId);



}
