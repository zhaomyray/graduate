package edu.zut.pt.service.impl;

import edu.zut.pt.mapper.TimeMessageMapper;
import edu.zut.pt.pojo.TimeMessage;
import edu.zut.pt.service.TimeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeMessageServiceImpl implements TimeMessageService {

    @Autowired
    TimeMessageMapper timeMessageMapper;

    @Override
    public TimeMessage getTimeMessage(int id) {
        return this.timeMessageMapper.getTimeMessage(id);
    }
}
