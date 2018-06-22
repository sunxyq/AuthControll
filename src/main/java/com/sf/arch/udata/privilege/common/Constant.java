package com.sf.arch.udata.privilege.common;

public class Constant {
    /*
        Define pages in system
     */
    public static final String[] UDATA_PAGES ={
            "/manage",
            "/manage/business-target",
            "/manage/quality-control",
            "/manage/auth-manage",
            "/manage/role-manage",
            "/manage/account-manage",
            "/hawkeye",
            "/hawkeye/business-overview",
            "/hawkeye/manage-theme",
            "/hawkeye/customer-theme",
            "/hawkeye/operate-theme",
            "/guard",
            "/guard/state-monitor",
            "/guard/city-monitor"
    };

    /*
        Define privileges everybody has.
     */
    public static final String[] COMMON_PRIVILEGES = {
            "/privilege/findPagePrivileges",
            "/privilege/findBy",
            "/privilege/findAll",
            "/role/findAll",
            "/role/findBy",
            "/account/findAll",
            "/account/findBy"
    };

    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSE = 0;
}
