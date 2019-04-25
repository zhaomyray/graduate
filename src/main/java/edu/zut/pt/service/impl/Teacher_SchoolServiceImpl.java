package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.Teacher_SchoolMapper;
import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.service.Teacher_SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Teacher_SchoolServiceImpl implements Teacher_SchoolService {

    @Autowired
    Teacher_SchoolMapper teacher_schoolMapper;

    @Override
    public Teacher_School findTeaSchByTnoPsd(String tno, String password) {
        return this.teacher_schoolMapper.findTeaSchByTnoPsd(tno,password);
    }

    @Override
    public String findTeaSchByTno(String tno) {
        return this.teacher_schoolMapper.findTeaSchByTno(tno);
    }

    @Override
    public Teacher_School updateTeaSchPsd(Teacher_School teacher_school) {
        return this.teacher_schoolMapper.updateTeaSchPsd(teacher_school);
    }
}
