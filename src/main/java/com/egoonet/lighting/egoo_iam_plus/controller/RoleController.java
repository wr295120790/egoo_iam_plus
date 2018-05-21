package com.egoonet.lighting.egoo_iam_plus.controller;

import com.egoonet.lighting.egoo_iam_plus.entity.RolePojo;
import com.egoonet.lighting.egoo_iam_plus.service.RoleService;
import com.egoonet.lighting.egoo_iam_plus.until.ServiceUntil;
import com.genesyslab.wfm8.API.service.config850.CfgUser;
import com.genesyslab.wfm8.API.service.config850.WFMConfigService850Soap;
import com.genesyslab.wfm8.API.service.session800.ServiceInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 接口说明：从子系统那里拿到URL地址，进行解析，来进行查找所有的角色信息
 * 然后将查到的数据又以json返回给用户这边
 */

@RestController
@RequestMapping(value = "../controller", produces = "application/json;charset=UTF-8")//映射前台的地址
@Api(value = "排版统一权限和参数配置管理", tags = "UserController", description = "排版统一权限和参数配置管理")
@Log4j
public class RoleController {
   /* @Autowired
    private RoleService roleHandleService;*/

    @PostMapping("/rolecompose")//给别人提供的地址
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

        JSONArray jsonArrayCfg = (JSONArray) info.get("data");
        List<CfgUser> listCfgUser = JSONArray.toList(jsonArrayCfg, new CfgUser(), new JsonConfig());
        log.info("--------------此处是分隔符----------");

        log.info("准备进入连接！");
        ServiceUntil serviceUntil = new ServiceUntil();
        WFMConfigService850Soap cfgService = null;
        Map<String, ServiceInfo> serviceInfoMap = serviceUntil.getConnect();
        cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(), "222.73.213.174", "21007"));
        log.info("已经建立连接");

        RoleService roleService = new RoleService();
        List<RolePojo> getRoles = new ArrayList();
        try {
            for (CfgUser cfgUser : listCfgUser) {
                getRoles = roleService.getRoleList(cfgService,cfgUser);//角色id
            }
        } catch (Exception e) {
            log.error("没有从json解析出来data数据", e.getCause());
        }
        log.debug("获取到角色信息了"+getRoles);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> errorHead = new HashMap<String, Object>();

        if (getRoles != null) {
            int errorCode = 0;
            String errorMessage = "成功";
            errorHead.put("errorCode", errorCode);
            errorHead.put("errorMessage", errorMessage);
        } else {
            int errorCode = 1;
            String errorMessage = "获取数据失败";
            errorHead.put("errorCode", errorCode);
            errorHead.put("errorMessage", errorMessage);
        }
        log.debug("返回的结果" + errorHead);

        errorHead.put("tenantld", "boc");
        errorHead.put("vers", "1.0");
        errorHead.put("reqId", reqId);
        errorHead.put("layerId", layerId);
        errorHead.put("seqId", seqId);
        errorHead.put("times", times);
        errorHead.put("compId", compId);

        Map<String, Object> errorInfo = new HashMap<String, Object>();

        result.put("errorHead", errorHead);
        result.put("errorInfo", errorInfo);

        Map<String, Object> resultHead = new HashMap<>();
        resultHead.put("reqId", reqId);
        resultHead.put("times", times);
        resultHead.put("compId", compId);

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dataType", dataType);
        resultInfo.put("opType", opType);
        resultInfo.put("dataType", getRoles);

        result.put("head", resultHead);
        result.put("info", resultInfo);
        return result;
    }
}
