package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import com.sf.arch.udata.privilege.pojo.RoleDO;
import com.sf.arch.udata.privilege.pojo.RoleJson;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/10
 * \* Time: 上午6:17
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface RoleService extends Service<RoleDO> {
    ResponseData saveRole(RoleDO role);
    ResponseData saveRole(RoleDO role, Long[] privilegeIds);
    ResponseData bindPrivilege(Long roleId, Long[] privilegeIds);
    ResponseData updateStatus(Long id, Integer status);
    ResponseData updateRole(RoleDO role);
    ResponseData updateRole(RoleDO role, Long[] privilegeIds);
    ResponseData removeRole(Long id);
    ResponseData findAllByUid(String uid);
    ResponseData findAll();
    List<PrivilegeDO> findPrivileges(String uid);
}