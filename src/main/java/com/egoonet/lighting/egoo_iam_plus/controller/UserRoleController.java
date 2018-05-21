package com.egoonet.lighting.egoo_iam_plus.controller;

import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.egoonet.lighting.egoo_iam_plus.service.UserRoleService;
import com.egoonet.lighting.egoo_iam_plus.until.ServiceUntil;
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
 * 接口说明：根据用户和角色的关系，来进行解析，将获得的数据进行增删改操作
 * 将操作后的数据以json形式发送给用户
 */
@RestController
@RequestMapping(value = "../controller", produces = "application/json;charset=UTF-8")//映射前台的地址
@Api(value = "排版统一权限和参数配置管理", tags = "UserController", description = "排版统一权限和参数配置管理")
@Log4j
public class UserRoleController {
   /* @Autowired
    private UserRoleService userRoleService;*/

    @PostMapping("/userRolecompose")//给别人提供的地址
    @ApiOperation("用户组/角色/用户数据推送接口")
    public Map<String, Object> userRoleJson(@RequestBody Map<String, Object> parameter) {

        Map<String, String> head = (Map<String, String>) parameter.get("head");

        String reqId = head.get("reqId");

        String layerId = head.get("layerId");

        String seqId = head.get("seqId");

        String times = head.get("times");

        String compId = head.get("compId");

        Map<String, Object> info = (Map<String, Object>) parameter.get("info");

        String dataType = (String) info.get("dataType");

        String opType = (String) info.get("opType");

        JSONArray jsonArrayCfgUser = (JSONArray) info.get("data");
        List<UserPojo> listUser = JSONArray.toList(jsonArrayCfgUser, new UserPojo(), new JsonConfig());
        Map<String, Object> getUserAndRole = new HashMap<>();
        try {

            ServiceUntil serviceUntil = new ServiceUntil();
            WFMConfigService850Soap cfgService = null;
            Map<String, ServiceInfo> serviceInfoMap = serviceUntil.getConnect();
            log.info("准备建立连接");
            cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                    "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                    ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(), "222.73.213.174", "21007"));
            log.info("已经建立连接");

            UserRoleService userRoleService = new UserRoleService();
            for (UserPojo userPojo : listUser) {
                if ("1".equals(opType) && opType != null) {
                    getUserAndRole = userRoleService.addUserAndRole(cfgService, userPojo, 1);
                } else if ("2".equals(opType) && opType != null) {
                    getUserAndRole = userRoleService.updateUserAndRole(cfgService, userPojo);
                } else if ("3".equals(opType) && opType != null) {
                    getUserAndRole = userRoleService.deleteUserAndRole(cfgService, userPojo.getId());
                } else {
                    log.debug("没有从opType中获取到数据");
                }
            }
        } catch (Exception e) {
            log.error("没有从json中获取到data任何数据", e.getCause());
        }

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> errorHead = new HashMap<String, Object>();

        if (getUserAndRole != null) {
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
        resultInfo.put("opType", opType);//1  新增   2  修改  3  删除
        resultInfo.put("dataType", getUserAndRole);

        result.put("head", resultHead);
        result.put("info", resultInfo);
        return result;
    }
}
