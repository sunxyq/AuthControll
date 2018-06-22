package com.sf.arch.udata.privilege.dao;

import com.sf.arch.udata.privilege.pojo.RolePrivilegeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePrivilegeDAO extends JpaRepository<RolePrivilegeDO, Long>{
    void removeRolePrivilegeDOByRoleId(Long roleId);
    void removeRolePrivilegeDOByPrivilegeId(Long privilegeId);
    List<RolePrivilegeDO> findAllByRoleId(Long roleId);

    @Query(value = "select * from role_privilege where role_id in :roleIds", nativeQuery = true)
    List<RolePrivilegeDO> findAllByRoleIds(@Param("roleIds") List<Long> roleIds);

    @Query(value = "select * from role_privilege left join role on role_privilege.role_id = role.id " +
            " left join privilege on privilege_id = role_privilege.privilege_id where role.status = 1 and privilege.status = 1", nativeQuery = true)
    List<RolePrivilegeDO> findOpenItems();
}
