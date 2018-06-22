package com.sf.arch.udata.privilege.controller;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.annotation.ControllerLog;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.common.util.PrivilegeUtil;
import com.sf.arch.udata.privilege.pojo.IdJson;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import com.sf.arch.udata.privilege.service.AccountService;
import com.sf.arch.udata.privilege.service.PrivilegeService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/privilege")
public class PrivilegeController {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    @Autowired
    private PrivilegeService service;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/save")
    @ControllerLog("添加权限")
    public ResponseData save(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        privilegeDO.setCtime(DateUtil.getDateInt());
        privilegeDO.setUtime(DateUtil.getDateInt());
        privilegeDO.setStatus(1);   //set open default
        privilegeDO.setPrivilegeNO(AppUtil.generateNO(privilegeDO.getProductName(), service.getMaxId()));
        return service.savePrivilege(privilegeDO);
    }

    @RequestMapping("/update")
    @ControllerLog("修改权限")
    public ResponseData update(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        privilegeDO.setUtime(DateUtil.getDateInt());
        return service.updatePrivilege(privilegeDO);
    }

    @RequestMapping("/updateStatus")
    @ControllerLog("修改权限状态")
    public ResponseData updateStatus(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.updateStatus(privilegeDO.getId(), privilegeDO.getStatus());
    }

    @RequestMapping("/remove")
    @ControllerLog("删除权限")
    public ResponseData remove(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.removePrivilege(privilegeDO.getId());
    }

    @RequestMapping("/page")
    @ControllerLog("获取分页")
    public ResponseData page(int page, int size){
        return service.getPage(page, size);
    }

    @RequestMapping("/findBy")
    @ControllerLog("多条件联合查询")
    public ResponseData findBy(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.findBy(privilegeDO);
    }

    @RequestMapping("/findAll")
    @ControllerLog("获取全部权限信息")
    public ResponseData findAll(){
        return service.findAll();
    }

    @RequestMapping("/findPrivilege")
    @ControllerLog("根据产品检索权限")
    public ResponseData findPrivilege(@RequestBody PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.findPrivilege(privilegeDO);
    }

    @RequestMapping("/findPrivileges")
    @ControllerLog("根据角色检索指标")
    public ResponseData findPrivileges(@RequestBody IdJson id){
        if(id == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.findPrivilegeByRoleIds(id.ids);
    }

    @RequestMapping("/findPagePrivileges")
    @ControllerLog("根据uid检索用户指标")
    public ResponseData findPagePrivileges(HttpServletRequest request){
        if(request == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        String uid = AppUtil.getUidFromCookie(request);
        if(uid == null || uid.isEmpty()){
            return AppUtil.responseData(ErrorMsg.NOT_LOGIN_ERROR);
        }

        logger.info("Welcome " + uid + " !");
        JSONObject obj = PrivilegeUtil.getPagePrivilege(uid, accountService, service);

        return AppUtil.responseData(obj);
    }
}
