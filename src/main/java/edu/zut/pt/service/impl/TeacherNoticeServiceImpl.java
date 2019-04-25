package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TeacherNoticeMapper;
import edu.zut.pt.pojo.TeacherNotice;
import edu.zut.pt.service.TeacherNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherNoticeServiceImpl implements TeacherNoticeService {

    @Autowired
    TeacherNoticeMapper teacherNoticeMapper;

    @Override
    public List<TeacherNotice> getTeacherNoticeByTeaId(String teaId) {
        return this.teacherNoticeMapper.getTeacherNoticeByTeaId(teaId);
    }

    @Override
    public int insertTeacherNotice(TeacherNotice teacherNotice) {
        return this.insertTeacherNotice(teacherNotice);
    }

    @Override
    public int deleteTeacherNotice(Integer id) {
        return this.deleteTeacherNotice(id);
    }
}
