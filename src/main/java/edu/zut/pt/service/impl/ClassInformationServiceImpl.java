package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.ClassInformationMapper;
import edu.zut.pt.pojo.ClassInformation;
import edu.zut.pt.service.ClassInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassInformationServiceImpl implements ClassInformationService {

    @Autowired
    ClassInformationMapper classInformationMapper;

    @Override
    public ClassInformation getClassInforById(int classId) {
        return this.classInformationMapper.getClassInforById(classId);
    }
}
