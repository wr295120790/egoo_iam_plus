package com.egoonet.lighting.egoo_iam_plus;

import com.egoonet.lighting.egoo_iam_plus.entity.RolePojo;
import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.egoonet.lighting.egoo_iam_plus.service.RoleGetService;
import com.egoonet.lighting.egoo_iam_plus.until.ServiceUntil;
import com.genesyslab.wfm8.API.service.config850.CfgSecurityRole;
import com.genesyslab.wfm8.API.service.config850.WFMConfigService850Soap;
import com.genesyslab.wfm8.API.service.session800.ServiceInfo;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Log4j
@RestController
@RequestMapping(value = "/controller", produces = "application/json;charset=UTF-8")
public class CfgTestConnect {

    @PostMapping("/allRole")
    public Map<String,Object> getRole() {
        log.info("准备进入连接！");
        WFMConfigService850Soap cfgService = null;
        Map<String, ServiceInfo> serviceInfoMap = new ServiceUntil().getConnect();
        cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(),
                        "222.73.213.174", "21007"));
        log.info("已经建立连接");

        List<Integer> roleIds = null;

        RoleGetService roleGetService = new RoleGetService();

        List<CfgSecurityRole> securityRoles = roleGetService.getRoles(cfgService, roleIds);

        List roleList = new ArrayList();

        for (CfgSecurityRole cfgSecurityRole : securityRoles) {
            String roleId = Integer.toString(cfgSecurityRole.getWmSecurityRoleId());
            String roleName = cfgSecurityRole.getWmName();

            RolePojo rolePojo = new RolePojo();
            rolePojo.setId(roleId);
            rolePojo.setName(roleName);
            roleList.add(rolePojo);
        }
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> resultHead = new HashMap<>();
        resultHead.put("tenantld", "boc");
        resultHead.put("vers", "1.0");
        if (!roleList.isEmpty() || roleList.equals(0)) {
            int errorCode = 0;
            String errorMessage = "成功";
            resultHead.put("errorCode", errorCode);
            resultHead.put("errorMessage", errorMessage);

        } else {
            int errorCode = 1;
            String errorMessage = "获取数据失败";
            resultHead.put("errorCode", errorCode);
            resultHead.put("errorMessage", errorMessage);
        }

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("data", roleList);

        result.put("head", resultHead);
        result.put("info", resultInfo);
        return result;
    }
}
