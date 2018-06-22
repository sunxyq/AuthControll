package com.sf.arch.udata.privilege.pojo;



/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/22
 * \* Time: 下午7:25
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class RoleIDs {
    private Long id;
    private String roleNO;
    private String roleName;
    private String description;
    private Integer status;
    private Integer ctime;
    private Integer utime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoleNO(String roleNO) {
        this.roleNO = roleNO;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    public void setUtime(Integer utime) {
        this.utime = utime;
    }

    public RoleIDs() {
    }

    public RoleDO getRole() {
        return new RoleDO(id, roleNO, roleName, description,status, ctime,utime );
    }

    public void setRole(){
    }

    public Long[] getIds() {
        return ids;
    }
    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    private Long[] ids;
}