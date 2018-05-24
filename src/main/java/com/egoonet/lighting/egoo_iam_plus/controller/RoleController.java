/*
package com.egoonet.lighting.egoo_iam_plus.controller;

import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.egoonet.lighting.egoo_iam_plus.service.RoleGetService;
import com.egoonet.lighting.egoo_iam_plus.until.ServiceUntil;
import com.genesyslab.wfm8.API.service.config850.CfgSecurityRole;
import com.genesyslab.wfm8.API.service.config850.CfgUser;
import com.genesyslab.wfm8.API.service.config850.WFMConfigService850Soap;
import com.genesyslab.wfm8.API.service.session800.ServiceInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/controller", produces = "application/json;charset=UTF-8")
@Api(value = "排版统一权限和参数配置管理", tags = "UserController", description = "排版统一权限和参数配置管理")
@Log4j
public class RoleController {
    @Autowired
    private RoleGetService roleGetService;
    private WFMConfigService850Soap cfgService;

    @PostMapping("/roleCompose")
    @ApiOperation("用户组/角色/用户数据推送接口")
    public Map<String, Object> roleBackJson(@RequestBody Map<String, Object> parameter) {

        Map<String, String> head = (Map<String, String>) parameter.get("head");

        String reqId = head.get("reqId");

        String layerId = head.get("layerId");

        String seqId = head.get("seqId");

        String times = head.get("times");

        String compId = head.get("compId");

        Map<String, Object> info = (Map<String, Object>) parameter.get("info");

        String dataType = (String) info.get("dataType");

        String opType = (String) info.get("opType");

        ArrayList arrayList = (ArrayList) info.get("data");

        Map<String, Object> getRoles = new HashMap<>();


        log.info("--------------此处是分隔符----------");
        log.info("准备进入连接！");

        Map<String, ServiceInfo> serviceInfoMap = new ServiceUntil().getConnect();
        cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(), "222.73.213.174", "21007"));
        log.info("已经建立连接");

        List<Integer> roleIds = null;

        List<CfgSecurityRole> securityRoles = roleGetService.getRoles(cfgService, roleIds);

        Map<String, Object> mapRoles = new HashMap<>();
        for (CfgSecurityRole cfgSecurityRole : securityRoles) {
            int roleId = cfgSecurityRole.getWmSecurityRoleId();
            String roleName = cfgSecurityRole.getWmName();

            UserPojo userPojo = new UserPojo();
            userPojo.setId(roleId);
            userPojo.setName(roleName);
            mapRoles.put("角色信息", userPojo);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            JSONObject jsonObject = JSONObject.fromObject(arrayList.get(i));
            CfgUser cfgUser = (CfgUser) JSONObject.toBean(jsonObject, CfgUser.class);
            try {
                getRoles = roleService.getRoleList(cfgService, cfgUser);
            } catch (Exception e) {
                log.error("没有从json解析出来data数据", e.getCause());
            }
        }


        Map<String, Object> result = new HashMap<>();

        Map<String, Object> resultHead = new HashMap<>();
        resultHead.put("tenantId", "boc");
        resultHead.put("vers", "1.0");
        resultHead.put("reqId", reqId);
        resultHead.put("layerId", layerId);
        resultHead.put("seqId", seqId);
        resultHead.put("times", times);
        resultHead.put("compId", compId);
        if (!mapRoles.isEmpty() || mapRoles.equals(0)) {
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
        resultInfo.put("dataType", dataType);
        resultInfo.put("opType", opType);
        resultInfo.put("data", mapRoles);

        result.put("head", resultHead);
        result.put("info", resultInfo);
        return result;
    }
}
*/
