package com.egoonet.lighting.egoo_iam_plus.service;

import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.genesyslab.wfm8.API.service.config850.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.*;

@Log4j
@Service
public class UserRoleService {
    @Autowired
    private RoleGetService roleGetService;

    @PostMapping("/addUserAndRole")
    public Map<String, Object> addUserAndRole(WFMConfigService850Soap cfgService, UserPojo userPojo, int roleId) {
        Map<String, Object> addResult = new HashMap<>();

        int userId = userPojo.getId();
        String userName = userPojo.getUserName();

        roleId = Integer.valueOf(roleId);

        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(roleId);

        List<CfgSecurityRole> roleList = roleGetService.getRoles(cfgService, roleIds);

        for (CfgSecurityRole role : roleList) {
            List<Integer> subsystems = role.getWmSecurityRoleSubsystems();

            List<CfgUserSecurity> wmUserSecurity = new ArrayList<>();
            for (int sub : subsystems) {
                CfgUserSecurity userSecurity = new CfgUserSecurity();
                userSecurity.setWmSubsystemId(sub);
                wmUserSecurity.add(userSecurity);
            }
            CfgUser cfgUser = new CfgUser();
            cfgUser.setGswUserId(userId);
            cfgUser.setWmUser(userName);
            cfgUser.setWmSecurityRoleId(roleId);

            CfgValidationHolder res = cfgService.insertUser(cfgUser, true);
            if (res.isSuccess()) {
                addResult.put("CfgUser", res);
                log.info("添加数据成功");
                return addResult;
            }
        }
        return addResult;
    }

    @PutMapping("/updateUserAndRole")
    public Map<String, Object> updateUserAndRole(WFMConfigService850Soap cfgService, UserPojo userPojo) {
        Map<String, Object> updateResult = new HashMap<>();

        int userId = userPojo.getId();
        String userName = userPojo.getUserName();
        CfgUser cfgUser = new CfgUser();
        cfgUser.setGswUserId(userId);
        cfgUser.setWmUser(userName);
        CfgValidationHolder res = cfgService.updateUser(cfgUser, true, true);
        if (res.isSuccess()) {
            updateResult.put("success", cfgUser);
            log.info("更新数据成功");
            return updateResult;
        }
        return updateResult;
    }

    @DeleteMapping("/deleteUserAndRole")
    public Map<String, Object> deleteUserAndRole(WFMConfigService850Soap cfgService, @PathVariable("userId") Integer id) {
        Map<String, Object> getDelete = new HashMap<>();

        CfgValidationHolder res = cfgService.deleteCfgObject(ECfgObjectType.CFG_USER_OBJECT, 0, id,
                true);
        if (res.isSuccess()) {
            getDelete.put("delete", 0);
            log.info("删除成功");
            return getDelete;
        }
        return getDelete;
    }
}