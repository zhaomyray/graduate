package edu.zut.pt.controller;

import edu.zut.pt.mapper.Teacher_CompanyMapper;
import edu.zut.pt.pojo.Teacher_Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Teacher_CompanyController {

    @Autowired
    Teacher_CompanyMapper teacher_companyMapper;

    @GetMapping("/Teacher_Company/{tno},{password}")
    public Teacher_Company findTeaComByTnoPsd(@PathVariable("tno") String tno,
                                             @PathVariable("password") String password){
        return teacher_companyMapper.findTeaComByTnoPsd(tno,password);
    }

}
