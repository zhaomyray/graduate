package edu.zut.pt.service;

import edu.zut.pt.pojo.TeacherNotice;

import java.util.List;

public interface TeacherNoticeService {

    /**
     * 根据老师id查询通知消息
     * @param teaId
     * @return
     */
    public List<TeacherNotice> getTeacherNoticeByTeaId(String teaId);

    /**
     * 添加通知
     * @param teacherNotice
     * @return
     */
    public int insertTeacherNotice(TeacherNotice teacherNotice);

    /**
     * 删除通知
     * @param id
     * @return
     */
    public int deleteTeacherNotice(Integer id);

}


