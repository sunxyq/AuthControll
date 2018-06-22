package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.dao.LogDAO;
import com.sf.arch.udata.privilege.pojo.LogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDAO logDAO;

    @Override
    public void saveLog(LogDO log){
        logDAO.save(log);
    }
}
