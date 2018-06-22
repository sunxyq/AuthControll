package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/9
 * \* Time: 下午2:32
 * \* T
 * \* Description:
 * \
 */
public interface Service<T> {
    ResponseData getPage(int page, int size);
    ResponseData findBy(T obj, int page, int size);
    ResponseData findBy(T obj);
    ResponseData findAll();
    Long getMaxId();
    int findStatusById(Long id);
}