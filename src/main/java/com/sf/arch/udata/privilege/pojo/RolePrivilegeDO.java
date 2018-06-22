package com.sf.arch.udata.privilege.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="role_privilege")
public class RolePrivilegeDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "role_id")
  private Long roleId;

  @NotNull
  @Column(name = "privilege_id")
  private Long privilegeId;

  @NotNull
  @Column(name = "description")
  private String description;

  @NotNull
  @Column(name = "status")
  private Integer status;

  @NotNull
  @Column(name = "ctime")
  private Integer ctime;

  @NotNull
  @Column(name = "utime")
  private Integer utime;

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getPrivilegeId() {
    return privilegeId;
  }

  public void setPrivilegeId(Long privilegeId) {
    this.privilegeId = privilegeId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getCtime() {
    return ctime;
  }

  public void setCtime(Integer ctime) {
    this.ctime = ctime;
  }

  public Integer getUtime() {
    return utime;
  }

  public void setUtime(Integer utime) {
    this.utime = utime;
  }
}
