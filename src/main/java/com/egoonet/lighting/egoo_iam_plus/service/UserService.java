package com.egoonet.lighting.egoo_iam_plus.service;

import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.genesyslab.wfm8.API.service.config850.*;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Log4j
@Service
public class UserService {
    /**
     * opType选择1的时候
     * user增加数据
     *
     * @param userPojo
     * @return
     */
    @PostMapping("/insertUser")
    public Map<String, Object> insertUser(WFMConfigService850Soap cfgService, UserPojo userPojo) {
        Map<String, Object> insertResult = new HashMap<>();

        CfgUser cfgUser = new CfgUser();
        cfgUser.setGswUserId(userPojo.getId());
        cfgUser.setWmUser(userPojo.getUserName());

        CfgValidationHolder res = cfgService.insertUser(cfgUser, true);
        if (res.isSuccess()) {
            insertResult.put("userPojo", res);
            log.info("添加数据成功!");
            return insertResult;
        }
        return insertResult;
    }

    /**
     * opType选择2的时候
     * 修改数据
     *
     * @param userPojo
     * @return
     */
    @PutMapping("/updateUser")
    public Map<String, Object> updateUser(WFMConfigService850Soap cfgService, UserPojo userPojo) {
        Map<String, Object> updateResult = new HashMap<>();

        int userId = userPojo.getId();
        String userName = userPojo.getUserName();
        CfgUser cfgUser = new CfgUser();
        cfgUser.setGswUserId(userId);
        cfgUser.setWmUser(userName);
        CfgValidationHolder res = cfgService.updateUser(cfgUser, true, true);
        if (res.isSuccess()) {
            updateResult.put("userPojo", res);
            log.info("更新数据成功!");
            return updateResult;
        }
        return updateResult;
    }

    /**
     * opType选择3的时候
     * 删除数据
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteUser")
    public Map<String, Object> deleteById(WFMConfigService850Soap cfgService, int id) {
        Map<String, Object> deleteResult = new HashMap<>();

        CfgValidationHolder res = cfgService.deleteCfgObject(ECfgObjectType.CFG_USER_OBJECT,
                0, id, true);
        if (res.isSuccess()) {
            deleteResult.put("成功", 0);
            log.info("删除数据成功！");
            return deleteResult;
        }
        return deleteResult;
    }
}
