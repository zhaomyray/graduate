package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.Teacher_CompanyMapper;
import edu.zut.pt.pojo.Teacher_Company;
import edu.zut.pt.service.Teacher_CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Teacher_CompanyServiceImpl implements Teacher_CompanyService {

    @Autowired
    Teacher_CompanyMapper teacher_companyMapper;

    @Override
    public Teacher_Company findTeaComByTnoPsd(String tno, String password) {
        return this.teacher_companyMapper.findTeaComByTnoPsd(tno,password);
    }

    @Override
    public String findTeaNameByTno(String tno) {
        return this.teacher_companyMapper.findTeaNameByTno(tno);
    }

    @Override
    public Teacher_Company updateTeaComPsd(Teacher_Company teacher_company) {
        return this.teacher_companyMapper.updateTeaComPsd(teacher_company);
    }
}
