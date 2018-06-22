package com.sf.arch.udata.privilege.dao;

import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.RoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountDAO extends JpaRepository<AccountDO, Long>, JpaSpecificationExecutor<AccountDO>{

    @Modifying
    @Transactional
    @Query("update AccountDO a set a.uname=?2, a.status=?3, a.manageScope=?4, a.operatorUid=?5, a.utime=?6 where a.id=?1")
    int updateAccountDOById(Long id, String uname, Integer status, String scope, String op_uid, Integer utime);

    List<AccountDO> findAllByOrderByCtimeDesc();
    List<AccountDO> findByUid(String uid);
    List<AccountDO> findByUidAndStatus(String uid, int status);
}
