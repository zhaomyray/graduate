package edu.zut.pt.mapper;

import edu.zut.pt.pojo.SystemNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SystemNoticeMapper {

    /**
     * 查看系统公告
     * @param
     * @return
     */
    @Select("select * From sys_article")
    public List<SystemNotice> findAllSysArticle();

}
