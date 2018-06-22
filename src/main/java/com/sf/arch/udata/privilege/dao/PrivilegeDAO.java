package com.sf.arch.udata.privilege.dao;

import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrivilegeDAO extends JpaRepository<PrivilegeDO, Long>,JpaSpecificationExecutor<PrivilegeDO> {
    String sql = "select privilege_action from privilege where id in (" +
                            "select privilege_id from role_privilege WHERE role_id in ( " +
                                    "select role_id from account_role where account_id in ( " +
                                        "select id from account where status=1 and uid=:uid" +
                                        ")"  +
                                    ")"  +
                            ") and status=1 and LOCATE(privilege_action,:uri)>0";

    @Modifying
    @Transactional
    @Query("update PrivilegeDO as a set a.privilegeName=?2, a.description=?3, a.productName=?4, a.privilegeType=?5, a.privilegeAction=?6, a.utime=?7 where a.id=?1")
    int updatePrivilegeDOById(Long id, String name, String description, String productName, Integer privilegeType, String privilegeAction, int utime);

    @Modifying
    @Transactional
    @Query("update PrivilegeDO as a set a.status=?2, a.utime=?3 where a.id=?1")
    int updateStatus(Long id, Integer status, Integer utime);

    List<PrivilegeDO> findAllByStatusOrderByCtimeDesc(int status);
    List<PrivilegeDO> findByPrivilegeNameAndProductName(String privilege, String product);
    List<PrivilegeDO> findByProductNameAndPrivilegeType(String product, Integer type);
    List<PrivilegeDO> findAllById(Long id);

    @Query(value = sql, nativeQuery = true)
    public List<String> findPrivilege(@Param("uid")String uid, @Param("uri") String uri);

    @Query(value = "select * from privilege where id in (" +
            "select privilege_id from role_privilege where role_id in :roleIds)", nativeQuery = true)
    List<PrivilegeDO> findByRoleIds(@Param("roleIds") List<Long> roleIds);

    @Query(value="select * from privilege where id in (select privilege_id from role_privilege " +
            " WHERE role_id in (select role_id from account_role left join role on account_role.role_id=role.id where account_id in (" +
            " select id from account where uid = :uid and status = 1) and role.status=1 ) and privilege.status=1)", nativeQuery = true)
    List<PrivilegeDO> findByUid(@Param("uid") String uid);
 }
