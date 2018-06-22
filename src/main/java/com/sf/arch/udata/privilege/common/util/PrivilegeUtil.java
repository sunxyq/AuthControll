/*
 * Copyright (c) 2018 by sf-express
 */

package com.sf.arch.udata.privilege.common.util;

import com.sf.arch.udata.privilege.common.Constant;
import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.service.AccountService;
import com.sf.arch.udata.privilege.service.PrivilegeService;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Author: Eddy Xiang
 * Mail: yongqingxiang@sf-express.com
 * Date: 2018/4/2
 * Time: 下午3:17
 * Class: PrivilegeUtil
 * Description:
 */
public class PrivilegeUtil {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PrivilegeUtil.class);
    /* 
     * @author Eddy Xiang
     * @date 2018/4/2 下午3:19
     * @param  list1, list2
     * @return  if list2 contains list1, return true, else false.
     */
    public static boolean lessThan(List<PrivilegeDO> list1, List<PrivilegeDO> list2){
        if(list1 == null)
            return true;
        if(list1 !=null && list2 == null)
            return false;
        if(list2.containsAll(list1))
            return true;
        return false;
    }

    //compare privilege ids.
    public static boolean lessThanIds(List<Long> list1, List<Long> list2){
        if(list1 == null)
            return true;
        if(list1 !=null && list2 == null)
            return false;
        if(list2.containsAll(list1))
            return true;
        return false;
    }

    public static boolean hasCommonPrivilege(String uri){
        if(uri == null)
            return false;
        for (String item: Constant.COMMON_PRIVILEGES) {
            if(uri.toLowerCase().indexOf(item.toLowerCase()) != -1) {
                logger.info(item + " has privilege in COMMON_PRIVILEGES.");
                return true;
            }
        }
        return false;
    }

    /* 
     * @author Eddy Xiang
     * @date 2018/4/2 下午3:28
     * @param  to judge uri has privilege or not
     * @return  if has, return true, else false.
     */
    public static boolean hasPrivilege(String uri, List<PrivilegeDO> list){
        if(uri == null || list == null) {
            logger.info("Your privilege list is null, please contact privilege manager!");
            return false;
        }

        for(PrivilegeDO item : list){
            //if privilege status is open
            if(item.getStatus() > 0) {
                uri = uri.toLowerCase();
                if (uri.indexOf(item.getPrivilegeAction().toLowerCase()) != -1) {
                    return true;
                }
            }
        }
        logger.info("Your have no privilege, please contact privilege manager!");
        return false;
    }

    //set default page privilege
    public static JSONObject getDefaultPrivilege(String uid){
        JSONObject obj = new JSONObject();
        obj.put("uid", uid);
        obj.put("manageScope", "{}");

        //set all pages can not be visited.
        for(String str : Constant.UDATA_PAGES){
            obj.put(str, 0);
        }

        JSONObject json = new JSONObject();

        //add role manage page privileges
        json.put("viewPage", 0);
        json.put("updateStatus", 0);
        obj.put("/manage/role-manage", json);

        //add privilege manage page privileges
        json.put("viewPage", 0);
        json.put("updateStatus", 0);
        obj.put("/manage/auth-manage", json);

        return obj;
    }

    public static JSONObject getPagePrivilege(String uid, AccountService service, PrivilegeService pService){
        JSONObject obj = getDefaultPrivilege(uid);
        if(service == null)
            return obj;

        AccountDO accountDO = service.findByUid(uid);
        if(accountDO == null || accountDO.getStatus() == 0)
            return obj;

        obj = new JSONObject();
        List<PrivilegeDO> list = pService.findByUid(uid);

        obj.put("uid", uid);
        obj.put("manageScope", accountDO.getManageScope());

        //add page privilege
        for(String str : Constant.UDATA_PAGES){
            obj.put(str, 0);
            if(list != null && list.size() > 0) {
                for (PrivilegeDO item : list) {
                    if (str.toLowerCase().equals(item.getPrivilegeAction().toLowerCase())) {
                        obj.put(str, 1);
                        break;
                    }
                }
            }
        }

        //add role manage page privileges
        int viewPage = obj.getInt("/manage/role-manage");
        JSONObject json = new JSONObject();
        json.put("viewPage", viewPage);
        if(PrivilegeUtil.hasPrivilege("/role/updateStatus", list))
            json.put("updateStatus", 1);
        else
            json.put("updateStatus", 0);
        obj.put("/manage/role-manage", json);

        //add privilege manage page privileges
        viewPage = obj.getInt("/manage/auth-manage");
        json.put("viewPage", viewPage);
        if(PrivilegeUtil.hasPrivilege("/privilege/updateStatus", list))
            json.put("updateStatus", 1);
        else
            json.put("updateStatus", 0);
        obj.put("/manage/auth-manage", json);

        return obj;
    }
}