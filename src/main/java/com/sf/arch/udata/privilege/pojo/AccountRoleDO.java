package com.sf.arch.udata.privilege.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="account_role")
public class AccountRoleDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "account_id")
  private Long accountId;

  @NotNull
  @Column(name = "role_id")
  private Long roleId;

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

  public AccountRoleDO(Long id, Long accountId, Long roleId, String description, Integer status, Integer ctime, Integer utime) {
    this.id = id;
    this.accountId = accountId;
    this.roleId = roleId;
    this.description = description;
    this.status = status;
    this.ctime = ctime;
    this.utime = utime;
  }

  public AccountRoleDO() {
  }

  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
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
