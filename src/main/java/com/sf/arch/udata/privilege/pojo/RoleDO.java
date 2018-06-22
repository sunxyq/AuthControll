package com.sf.arch.udata.privilege.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="role")
public class RoleDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name="role_no")
  private String roleNO;

  @NotBlank
  @Column(name="role_name")
  private String roleName;

  @NotNull
  @Column(name="description")
  private String description;

  @NotNull
  @Column(name="status")
  private Integer status;

  @NotNull
  @Column(name="ctime")
  private Integer ctime;

  @NotNull
  @Column(name="utime")
  private Integer utime;

  public RoleDO(){
  }

  public RoleDO(Long id, String roleNO, String roleName, String description, Integer status, Integer ctime, Integer utime) {
    this.id = id;
    this.roleNO = roleNO;
    this.roleName = roleName;
    this.description = description;
    this.status = status;
    this.ctime = ctime;
    this.utime = utime;
  }

  public String getRoleNO() {
    return roleNO;
  }

  public void setRoleNO(String roleNO) {
    this.roleNO = roleNO;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
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
