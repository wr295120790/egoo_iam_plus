package com.egoonet.lighting.egoo_iam_plus.service;

import com.egoonet.lighting.egoo_iam_plus.entity.RolePojo;
import com.genesyslab.wfm8.API.service.config850.*;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Log4j
@Service
public class RoleService {
    /**
     * 根据getRole方法通过传参数id，进行封装，将获取到的子系统的角色信息返回给用户这边
     */
    @GetMapping("/getRole")
    public Map<String,Object> getRoleList(WFMConfigService850Soap cfgService, CfgUser cfgUser) {
        Map<String,Object> roleMap = new HashMap<>();

        int roleId = cfgUser.getWmSecurityRoleId();
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(roleId);

        RoleGetService roleGetService = new RoleGetService();
        List<CfgSecurityRole> cfgRole = roleGetService.getRoles(cfgService, roleIds);
        for (CfgSecurityRole role : cfgRole) {
            String wfmRoleId = Integer.toString(role.getWmSecurityRoleId());
            String wfmName = role.getWmName();

            RolePojo rolePojo = new RolePojo();
            rolePojo.setId(wfmRoleId);
            rolePojo.setName(wfmName);

            roleMap.put("Role",rolePojo);
            return roleMap;
        }
        return roleMap;
    }
}
