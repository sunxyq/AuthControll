package com.sf.arch.udata.privilege.pojo;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: xiangyongqing
 * \* Date: 2018/3/12
 * \* Time: 下午7:20
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class IdJson {
    public Long[] ids;
    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
    public IdJson() {
    }

    public IdJson(Long[] ids) {
        this.ids = ids;
    }
}