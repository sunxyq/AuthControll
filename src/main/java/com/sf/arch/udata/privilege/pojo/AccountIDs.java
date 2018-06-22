package com.sf.arch.udata.privilege.pojo;

import net.sf.json.JSONObject;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/12
 * \* Time: 下午5:55
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class AccountIDs {
    private Long id;
    private String uid;
    private String uname;
    private Integer status;
    private String operatorUid;
    private Object manageScope;
    private Integer ctime;
    private Integer utime;
    private Long[] ids;

    public AccountIDs() {
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

    public void setManageScope(Object manageScope) {
        this.manageScope = manageScope;
    }

    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    public void setUtime(Integer utime) {
        this.utime = utime;
    }
    public AccountDO getAccount() {
        return new AccountDO(id,uid, uname,status, JSONObject.fromObject(manageScope).toString(), operatorUid, ctime,utime );
    }


    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
}