package edu.zut.pt.controller;

import edu.zut.pt.mapper.Teacher_SchoolMapper;
import edu.zut.pt.pojo.Teacher_School;
import edu.zut.pt.service.Teacher_SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Teacher_SchoolController{

    @Autowired
    Teacher_SchoolService teacher_schoolService;

    @GetMapping("/Teacher_School/{tno},{password}")
    public Teacher_School findTeaSchByTnoPsd(@PathVariable("tno") String tno,
                                             @PathVariable("password") String password){
        return teacher_schoolService.findTeaSchByTnoPsd(tno,password);
    }

}
