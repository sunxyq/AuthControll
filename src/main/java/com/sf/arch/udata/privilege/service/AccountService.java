package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;

import java.util.List;


public interface AccountService extends Service<AccountDO>{
    ResponseData saveAccount(AccountDO account);
    ResponseData saveAccount(AccountDO account, Long[] ids);
    ResponseData removeAccount(Long id);
    ResponseData getAccountById(Long id);
    ResponseData updateAccount(Long id, String uname, Integer status, String scope, String op_uid );
    ResponseData updateAccount(AccountDO account, Long[] roleIds);
    ResponseData findAll();
    ResponseData findAccountOrderBy(String col);
    ResponseData pageAccountOrderBy(String orderBy, String order, int page, int size);
    ResponseData bindRole(Long accountId, Long[] roleIds);
    List<PrivilegeDO> findPrivileges(String uid);
    AccountDO findByUid(String uid);
    boolean checkLessThanOperator(Long[]ids, Long[] operatorIds);
    Long[] findPrivilegeIds(String uid);
    boolean isSuperAccount(AccountDO accountDO);
    List<String> findPrivileges(String uid, String uri);
}
