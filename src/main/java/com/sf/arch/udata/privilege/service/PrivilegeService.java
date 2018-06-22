package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;

import java.util.List;


public interface PrivilegeService extends Service<PrivilegeDO> {
    ResponseData savePrivilege(PrivilegeDO privilegeDO);
    ResponseData updateStatus(Long id, Integer status);
    ResponseData updatePrivilege(PrivilegeDO privilegeDO);
    ResponseData removePrivilege(Long id);
    ResponseData findPrivilege(PrivilegeDO privilegeDO);
    ResponseData findPrivilegeByRoleIds(Long[] roleIds);
    List<PrivilegeDO> findByUid(String uid);
}
