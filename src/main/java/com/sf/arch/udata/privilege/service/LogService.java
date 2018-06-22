package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.pojo.LogDO;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
    void saveLog(LogDO log);
}
