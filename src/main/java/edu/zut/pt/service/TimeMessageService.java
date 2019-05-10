package edu.zut.pt.service;

import edu.zut.pt.pojo.TimeMessage;

public interface TimeMessageService {

    /**
     * 从数据库获取时间信息
     * @param id
     * @return
     */
    public TimeMessage getTimeMessage(int id);




}
