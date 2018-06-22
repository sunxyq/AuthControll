package com.sf.arch.udata.privilege.common.util;

import com.sf.arch.udata.privilege.common.Constant;
import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import com.sf.arch.udata.privilege.pojo.RoleDO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/* 
 * @author Eddy Xiang
 * @date 2018/4/2 下午3:02 
 * @class AppUtil
 * @description 
 */
public class AppUtil {
    public static ResponseData responseData(){
        return new ResponseData();
    }

    public static ResponseData responseData(Object obj){
        return new ResponseData(obj);
    }

    public static ResponseData responseData(ErrorMsg msg){
        return new ResponseData(msg, "");
    }

    public static ResponseData responseData(ErrorMsg msg, Object obj){
        return new ResponseData(msg, obj);
    }

    public static ResponseData responseData(int no, String msg, Object obj){
        return new ResponseData(no, msg, obj);
    }

    public static String generateNO(String product, Long id){
        Long pid = (null == id) ? 1 : id + 1;
        if("鹰眼".equals(product))
            return String.valueOf(pid + 10000);
        else if("哨兵".equals(product))
            return String.valueOf(pid + 20000);
        else if("万花筒".equals(product))
            return String.valueOf(pid + 30000);
        else if("系统管理".equals(product))
            return String.valueOf(pid + 40000);
        else if("role".equals(product))
            return String.valueOf(pid + 10000);
        else
            return String.valueOf(pid + 90000);
    }

    public static String getUidFromCookie(HttpServletRequest request){
        String oper = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("UUI")) {
                    String information = Encryptor.decode(cookies[i].getValue());
                    oper = information.split(",")[0];
                    break;
                }
            }
        }
        return oper;
    }
}