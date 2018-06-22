package com.sf.arch.udata.privilege.common;

public enum ErrorMsg {
    VALID_ERROR(1000, "参数验证错误！"),
    PRIVILEGE_UNIQUE_ERROR(1001, "权限名已经存在，请使用其他名字！"),
    ID_NULL_ERROR(1002, "主键不能为空！"),
    FIELD_NULL_ERROR(1003, "排序字段不能为空！"),
    CONDITION_NULL_ERROR(1004, "查询条件不能为空！"),
    ROLE_UNIQUE_ERROR(1005, "角色名字已经存在，请使用其他名字！"),
    STATUS_NULL_ERROR(1006, "id和status字段不能为空！"),
    UID_UNIQUE_ERROR(1007, "工号已经存在！"),
    NO_PRIVILEGE_ERROR(1008, "您没有权限！"),
    NOT_LOGIN_ERROR(1009, "您还没有登录，请登录！"),
    LESS_THAN_ERROR(1010, "权限必须小于等于当前用户的权限！"),
    NULL_POINTER_ERROR(1011, "空指针错误");
    private ErrorMsg(int no, String msg){
        this.err_msg = msg;
        this.err_no = no;
    }

    private int err_no;
    private String err_msg;

    public int getErr_no() {
        return err_no;
    }

    public String getErr_msg() {
        return err_msg;
    }

    @Override
    public String toString(){
        return err_no + ":" + err_msg;
    }
}
