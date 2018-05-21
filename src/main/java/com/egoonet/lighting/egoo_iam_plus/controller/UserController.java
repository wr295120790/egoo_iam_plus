package com.egoonet.lighting.egoo_iam_plus.controller;


import com.alibaba.fastjson.annotation.JSONField;
import com.egoonet.lighting.egoo_iam_plus.entity.UserPojo;
import com.egoonet.lighting.egoo_iam_plus.service.UserService;
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
 * 接口说明：从用户那获取到数据，进行解析，解析后对相应数据进行增删改操作，
 * 将增删改后的数据在以json的形式发送给子系统
 */
@RestController
@Log4j
@RequestMapping(value = "../controller", produces = "application/json;charset=UTF-8")//映射前台的地址
@Api(value = "排版统一权限和参数配置管理", tags = "UserController", description = "排版统一权限和参数配置管理")
public class UserController {
    /*@Autowired
    private UserService userService;
    private ServiceUntil serviceUntil;
    private WFMConfigService850Soap cfgService;
*/
    @PostMapping("/userCompose")//给别人提供的地址
    @ApiOperation("用户组/角色/用户数据推送接口")
    @JSONField(format = "yyyy-MM-dd")
    public Map<String, Object> wfmUserJson(@RequestBody Map<String, Object> parameter) {

        Map<String, String> head = (Map<String, String>) parameter.get("head");

        String reqId = head.get("reqId");

        String layerId = head.get("layerId");

        String seqId = head.get("seqId");

        String times = head.get("times");

        String compId = head.get("compId");

        Map<String, Object> info = (Map<String, Object>) parameter.get("info");

        String dataType = (String) info.get("dataType");

        String opType = (String) info.get("opType");

        JSONArray jsonArrayUser = (JSONArray) info.get("data");
        List<UserPojo> listUser = JSONArray.toList(jsonArrayUser, new UserPojo(), new JsonConfig());
        Map<String, Object> getResult = new HashMap<>();
        WFMConfigService850Soap cfgService = null;
        try {
            ServiceUntil serviceUntil = new ServiceUntil();

            Map<String, ServiceInfo> serviceInfoMap = serviceUntil.getConnect();
            log.info("userController准备建立连接");
            cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                    "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                    ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(), "222.73.213.174", "21007"));
            log.info("userController已经建立连接");
            UserService userService = new UserService();
            for (UserPojo userPojo : listUser) {
                if ("1".equals(opType) && opType != null) {
                    getResult = userService.insertUser(cfgService, userPojo);
                } else if ("2".equals(opType) && opType != null) {
                    getResult = userService.updateUser(cfgService, userPojo);
                } else if ("3".equals(opType) && opType != null) {
                    getResult = userService.deleteById(cfgService, userPojo.getId());
                } else {
                    log.debug("没有从opType中获取到数据");
                }
            }
        } catch (Exception e) {
            log.error("在解析时没有从data中获取到数据", e.getCause());//e.getCause是屏蔽具体错误信息
        }

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> errorHead = new HashMap<String, Object>();

        if (getResult != null) {
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
        log.debug(errorHead);

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
        resultInfo.put("data", getResult);

        result.put("head", resultHead);
        result.put("info", resultInfo);
        return result;
    }
}

