package com.sf.arch.udata.privilege.pojo;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/11
 * \* Time: 下午8:32
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class RoleJson {
    private Long id;
    private String roleNO;
    private String roleName;
    private String description;
    private Integer status;
    private Integer ctime;
    private Integer utime;
    private List<Privileges> privileges;

    public String getRoleNO() {
        return roleNO;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getUtime() {
        return utime;
    }

    public Long getId() {
        return id;
    }

    public void setRole(RoleDO role) {
        this.id = role.getId();
        this.roleNO = role.getRoleNO();
        this.roleName = role.getRoleName();
        this.description = role.getDescription();
        this.status = role.getStatus();
        this.ctime = role.getCtime();
        this.utime = role.getUtime();
    }

    public List<Privileges> getPrivileges() {
        return privileges;
    }
    public void setPrivileges(List<Privileges> privileges) {
        this.privileges = privileges;
    }

    public RoleJson(){
    }

    public class Privileges{
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public List<PrivilegeDO> getPrivileges() {
            return privileges;
        }

        public void setPrivileges(List<PrivilegeDO> privileges) {
            this.privileges = privileges;
        }

        public Privileges(String productName, List<PrivilegeDO> privileges) {
            this.productName = productName;
            this.privileges = privileges;
        }

        public Privileges(){
        }

        private String productName;
        private List<PrivilegeDO> privileges;
    }
}