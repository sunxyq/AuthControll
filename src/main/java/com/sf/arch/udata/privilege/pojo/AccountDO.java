package com.sf.arch.udata.privilege.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="account")
public class AccountDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name="uid")
  private String uid;

  @NotBlank
  @Column(name="uname")
  private String uname;

  @NotNull
  @Column(name="status")
  private Integer status;

  @NotNull
  @Column(name="operator_uid")
  private String operatorUid;

  @NotBlank
  @Column(name="manage_scope")
  private String manageScope;

  @NotNull
  @Column(name="ctime")
  private Integer ctime;

  @NotNull
  @Column(name="utime")
  private Integer utime;

  public AccountDO(){
  }

  public AccountDO(Long id, String uid, String uname, Integer status, String manageScope,String operatorUid, Integer ctime, Integer utime) {
    this.id = id;
    this.uid = uid;
    this.uname = uname;
    this.status = status;
    this.operatorUid = operatorUid;
    this.manageScope = manageScope;
    this.ctime = ctime;
    this.utime = utime;
  }

  public String getOperatorUid() {
    return operatorUid;
  }

  public void setOperatorUid(String operatorUid) {
    this.operatorUid = operatorUid;
  }

  public String getManageScope() {
    return manageScope;
  }

  public void setManageScope(String manageScope) {
    this.manageScope = manageScope;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getUname() {
    return uname;
  }

  public void setUname(String uname) {
    this.uname = uname;
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
