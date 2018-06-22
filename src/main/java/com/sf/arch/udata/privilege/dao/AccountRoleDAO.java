package com.sf.arch.udata.privilege.dao;

import com.sf.arch.udata.privilege.pojo.AccountRoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRoleDAO extends JpaRepository<AccountRoleDO, Long> {
    void deleteAccountRoleDOByAccountId(Long accountId);
    List<AccountRoleDO> findAllByAccountId(Long id);

    @Query(value = "select * from account_role where account_id in :accountIds", nativeQuery = true)
    List<AccountRoleDO> findAllByAccountIds(@Param("accountIds") List<Long> accountIds);
}
