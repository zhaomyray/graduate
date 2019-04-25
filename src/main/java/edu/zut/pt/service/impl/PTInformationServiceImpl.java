package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.PTInformationMapper;
import edu.zut.pt.pojo.StudentInfo;
import edu.zut.pt.service.PTInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PTInformationServiceImpl implements PTInformationService {

    @Autowired
    PTInformationMapper ptInformationMapper;

    @Override
    public int updatePTInformation(StudentInfo studentInfo) {
        return this.ptInformationMapper.updatePTInformation(studentInfo);
    }

    @Override
    public StudentInfo findBySno(String sno) {
        return this.ptInformationMapper.findBySno(sno);
    }

    @Override
    public List<StudentInfo> findBySchTeaId(String schTeaId) {
        return this.ptInformationMapper.findBySchTeaId(schTeaId);
    }

    @Override
    public List<StudentInfo> findBySchTeaName(String comTeaName) {
        return this.ptInformationMapper.findBySchTeaName(comTeaName);
    }
}
