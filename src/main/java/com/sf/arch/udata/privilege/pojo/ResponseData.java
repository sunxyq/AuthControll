package com.sf.arch.udata.privilege.pojo;

import com.sf.arch.udata.privilege.common.ErrorMsg;

public class ResponseData {
    public ResponseData(int err_no, String err_msg, Object data) {
        this.err_no = err_no;
        this.err_msg = err_msg;
        this.data = data;
    }

    public ResponseData(Object data){
        this.data = data;
    }

    public ResponseData(ErrorMsg msg, Object data){
        this.err_msg = msg.getErr_msg();
        this.err_no = msg.getErr_no();
        this.data = data;
    }

    public ResponseData(){
        this.data = "";
    }

    public int getErr_no() {
        return err_no;
    }

    public void setErr_no(int err_no) {
        this.err_no = err_no;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    private int err_no = 0;
    private String err_msg = "success";
    private Object data;
}
