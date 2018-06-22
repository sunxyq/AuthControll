package com.sf.arch.udata.privilege.dao;

import com.sf.arch.udata.privilege.pojo.RoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleDAO extends JpaRepository<RoleDO, Long> ,JpaSpecificationExecutor<RoleDO> {
    List<RoleDO> findByRoleName(String name);

    @Modifying
    @Transactional
    @Query("update RoleDO as a set a.roleName=?2, a.description=?3, a.utime=?4 where a.id=?1")
    int updateRoleDOById(Long id, String name, String description, int utime);

    @Modifying
    @Transactional
    @Query("update RoleDO as a set a.status=?2, a.utime=?3 where a.id=?1")
    int updateStatus(Long id, Integer status, Integer utime);

    List<RoleDO> findAllByStatus(int status);
    List<RoleDO> findAllByStatusOrderByCtimeDesc(int status);

    String sql = "select * from role where id in (" +
            "select role_id from account_role where account_id in :accountIds)";
    @Query(value = sql, nativeQuery = true)
    List<RoleDO> findByAccountIds(@Param("accountIds") List<Long> accountIds);
}
