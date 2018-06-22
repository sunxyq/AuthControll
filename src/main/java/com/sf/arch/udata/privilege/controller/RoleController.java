package com.sf.arch.udata.privilege.controller;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.annotation.ControllerLog;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.pojo.*;
import com.sf.arch.udata.privilege.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService service;

    @RequestMapping("/save")
    @ControllerLog("添加角色")
    public ResponseData save(@RequestBody RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        role.setCtime(DateUtil.getDateInt());
        role.setUtime(DateUtil.getDateInt());
        role.setStatus(1);
        role.setRoleNO(AppUtil.generateNO("role", service.getMaxId()));
        return service.saveRole(role);
    }

    @RequestMapping("/saveRole")
    @ControllerLog("创建角色")
    public ResponseData saveRole(@RequestBody RoleIDs role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.saveRole(role.getRole(), role.getIds());
    }

    @RequestMapping("/updateRole")
    @ControllerLog("修改角色")
    public ResponseData updateRole(@RequestBody RoleIDs role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        RoleDO roleDO = role.getRole();
        roleDO.setUtime(DateUtil.getDateInt());
        return service.updateRole(roleDO, role.getIds());
    }

    @RequestMapping("/update")
    @ControllerLog("修改角色")
    public ResponseData update(@RequestBody RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        role.setUtime(DateUtil.getDateInt());
        return service.updateRole(role);
    }


    @RequestMapping("/updateStatus")
    @ControllerLog("修改角色状态")
    public ResponseData updateStatus(@RequestBody RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.updateStatus(role.getId(), role.getStatus());
    }

    @RequestMapping("/findAll")
    @ControllerLog("获取有效的角色列表")
    public ResponseData findAll(HttpServletRequest request){
        if(request == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        String uid = AppUtil.getUidFromCookie(request);
        if(uid == null || uid.isEmpty())
            return AppUtil.responseData(ErrorMsg.NOT_LOGIN_ERROR);
        return service.findAllByUid(uid);
    }

    @RequestMapping("/remove")
    @ControllerLog("删除角色")
    public ResponseData remove(@RequestBody RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.removeRole(role.getId());
    }

    @RequestMapping("/page")
    @ControllerLog("获取分页")
    public ResponseData page(int page, int size){
        return service.getPage(page, size);
    }

    @RequestMapping("/findBy")
    @ControllerLog("多条件联合查询")
    public ResponseData findBy(@RequestBody RoleDO roleDO){
        if(roleDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return service.findBy(roleDO);
    }
}
