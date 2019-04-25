package edu.zut.pt.mapper;


//指定这是一个操作数据库的mapper

import edu.zut.pt.pojo.TeacherNotice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对教师通知信息表的操作
 */

@Mapper
public interface TeacherNoticeMapper {

    /**
     * 查找通知信息
     * @param teaId
     * @return
     */
    @Select("select * from tb_teanotice where teaId=#{teaId}")
    public List<TeacherNotice> getTeacherNoticeByTeaId(String teaId);


    /**
     * 添加通知信息
     * @param teacherNotice
     * @return
     */
    @Insert("insert into tb_teanotice(teaId,noticeTitle,noticeContent,noticeTime) values(#{teaId},#{noticeTitle},#{noticeContent},#{noticeTime})")
    public int insertTeacherNotice(TeacherNotice teacherNotice);

    /**
     * 删除通知信息
     * @param id
     * @return
     */
    @Delete("delete from tb_teanotice where id=#{id}")
    public int deleteTeacherNotice(Integer id);

}
