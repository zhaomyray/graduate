package edu.zut.pt.service;

import edu.zut.pt.pojo.ClassInformation;

public interface ClassInformationService {

    /**
     * 根据班级id获取班级信息
     * @param classId
     * @return
     */
    public ClassInformation getClassInforById(int classId);


}
