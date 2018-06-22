package com.sf.arch.udata.privilege.pojo;

import net.sf.json.JSONObject;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/25
 * \* Time: 上午9:27
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class AccountJson {
    private Long id;
    private String uid;
    private String uname;
    private Integer status;
    private String operatorUid;
    private Object manageScope;
    private Integer ctime;
    private Integer utime;
    private List<RoleDO> roles;

    public Long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public Integer getStatus() {
        return status;
    }

    public String getOperatorUid() {
        return operatorUid;
    }

    public Object getManageScope() {
        return manageScope;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getUtime() {
        return utime;
    }

    public AccountJson() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setOperatorUid(String operatorUid) {
        this.operatorUid = operatorUid;
    }

    public void setManageScope(String manageScope) {
        this.manageScope = manageScope;
    }

    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    public void setUtime(Integer utime) {
        this.utime = utime;
    }

    public List<RoleDO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDO> roles) {
        this.roles = roles;
    }

    public void setAccount(AccountDO account){
        this.id = account.getId();
        this.uid = account.getUid();
        this.uname = account.getUname();
        this.status = account.getStatus();
        this.operatorUid = account.getOperatorUid();
        this.manageScope = JSONObject.fromObject(account.getManageScope());
        this.ctime = account.getCtime();
        this.utime = account.getUtime();
    }
}