package com.sf.arch.udata.privilege.controller;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.annotation.ControllerLog;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.AccountIDs;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import com.sf.arch.udata.privilege.pojo.RoleDO;
import com.sf.arch.udata.privilege.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @ControllerLog("添加用户")
    @RequestMapping("/save")
    public ResponseData save(@RequestBody AccountDO account){
        return service.saveAccount(account);
    }

    @ControllerLog("添加用户以及关联角色")
    @RequestMapping("/saveAccount")
    public ResponseData saveAccount(@RequestBody AccountIDs account){
        return service.saveAccount(account.getAccount(), account.getIds());
    }

    @ControllerLog("修改用户")
    @RequestMapping("/update")
    public ResponseData update(@RequestBody AccountDO account){
        return service.updateAccount(account.getId(), account.getUname(),
                account.getStatus(), account.getManageScope().toString(), account.getOperatorUid());
    }

    @ControllerLog("修改用户及关联角色")
    @RequestMapping("/updateAccount")
    @Transactional
    public ResponseData updateAccount(@RequestBody AccountIDs account, HttpServletRequest request){
        if(account == null || request==null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        AccountDO accountDO = account.getAccount();

        String uid = AppUtil.getUidFromCookie(request);
        if(uid == null || uid.isEmpty()){
            return AppUtil.responseData(ErrorMsg.NOT_LOGIN_ERROR);
        }

        AccountDO operator = service.findByUid(uid);
        //if operator uid equals current user, the user can update account.
        if( accountDO.getOperatorUid().equals(uid) || service.isSuperAccount(operator)){
            return service.updateAccount( accountDO, account.getIds());
        }
        return AppUtil.responseData(ErrorMsg.NO_PRIVILEGE_ERROR);
    }

    @ControllerLog("获取所有用户")
    @RequestMapping("/findAll")
    public ResponseData findAll(){
        return service.findAll();
    }

    @ControllerLog("获得分页")
    @RequestMapping("/getPage")
    public ResponseData getPage(String orderBy, String order, int page, int size){
        return service.pageAccountOrderBy(orderBy, order, page, size);
    }

    @ControllerLog("按顺序获取所有用户")
    @RequestMapping("/getAllOrderBy")
    public ResponseData getAllOrderByTime(String col){
        return service.findAccountOrderBy(col);
    }

    @ControllerLog("删除用户")
    @RequestMapping("/delete")
    public ResponseData delete(@RequestBody AccountDO account){
        return service.removeAccount(account.getId());
    }

    @ControllerLog("查找用户")
    @RequestMapping("/get")
    public ResponseData get(@RequestBody AccountDO account){
        if(account != null)
            return service.getAccountById(account.getId());
        else
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
    }

    @ControllerLog("按条件查找用户")
    @RequestMapping("/findBy")
    public ResponseData findBy(@RequestBody AccountDO obj){
        return service.findBy(obj);
    }
}
