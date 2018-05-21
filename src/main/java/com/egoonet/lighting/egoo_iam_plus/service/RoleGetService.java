package com.egoonet.lighting.egoo_iam_plus.service;


import com.genesyslab.wfm8.API.service.config850.*;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Service
@Log4j
public class RoleGetService {
    /*@Autowired
    private ServiceUntil serviceUntil;*/

    /**
     * @param roleIds -- 根据roleId获取role信息，为null或空则获取所有
     * @return
     */
    @GetMapping("../getRole")
    public List<CfgSecurityRole> getRoles(WFMConfigService850Soap cfgService, List<Integer> roleIds) {
        List<CfgSecurityRole> roleList = null;

        // role排序
        CfgSortMode sortMode = new CfgSortMode().withSortMode(ECfgSortMode.SecurityRole.CFG_SECURITY_ROLE_SORT_NONE);
        // 获取role详细信息
        CfgSecurityRoleDetails roleDetails = new CfgSecurityRoleDetails().withInfoType(ECfgInfoType.CFG_INFO_OBJECT)
                .withProvideSubsystemInfo(true);

        CfgSecurityRoleHolder roleHolder = cfgService.getSecurityRole(roleIds, sortMode, roleDetails);

        roleList = roleHolder.getObjectArray();

        return roleList;
    }
}
