package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.StudentMapper;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public Student findStudentBySnoPsd(String sno, String password) {
        return this.studentMapper.findStudentBySnoPsd(sno,password);
    }

    @Override
    public Student updateStudentPsd(Student student) {
        return this.studentMapper.updateStudentPsd(student);
    }
}
