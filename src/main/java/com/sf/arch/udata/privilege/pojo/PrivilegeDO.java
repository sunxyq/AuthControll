package com.sf.arch.udata.privilege.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="privilege")
public class PrivilegeDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name="privilege_no")
  private String privilegeNO;

  @NotBlank
  @Column(name="privilege_name")
  private String privilegeName;

  @NotBlank
  @Column(name="product_name")
  private String productName;

  @NotNull
  @Column(name="privilege_type")
  private Integer privilegeType;

  @NotBlank
  @Column(name="privilege_action")
  private String privilegeAction;

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

  public String getPrivilegeNO() {
    return privilegeNO;
  }

  public void setPrivilegeNO(String privilegeNO) {
    this.privilegeNO = privilegeNO;
  }

  public String getPrivilegeName() {
    return privilegeName;
  }

  public void setPrivilegeName(String privilegeName) {
    this.privilegeName = privilegeName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getPrivilegeType() {
    return privilegeType;
  }

  public void setPrivilegeType(Integer privilegeType) {
    this.privilegeType = privilegeType;
  }

  public String getPrivilegeAction() {
    return privilegeAction;
  }

  public void setPrivilegeAction(String privilegeAction) {
    this.privilegeAction = privilegeAction;
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
