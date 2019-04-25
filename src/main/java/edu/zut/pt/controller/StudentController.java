package edu.zut.pt.controller;

import edu.zut.pt.mapper.StudentMapper;
import edu.zut.pt.pojo.Student;
import edu.zut.pt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/Student/{sno},{password}")
    public Student findStudentBySnoPsd(@PathVariable("sno") String sno,
                                    @PathVariable("password") String password){
        return studentService.findStudentBySnoPsd(sno,password);
    }

    @GetMapping("/update/{sno},{password}")
    public Student updateStudentPsd( String sno,
                                    @PathVariable("password") String password){
        Student student = new Student();
        student.setSno(sno);
        student.setPassword(password);
        return studentService.updateStudentPsd(student);
    }

}
