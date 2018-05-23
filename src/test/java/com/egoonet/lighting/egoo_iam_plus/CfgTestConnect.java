package com.egoonet.lighting.egoo_iam_plus;

import com.egoonet.lighting.egoo_iam_plus.controller.RoleController;
import net.sf.json.JSONObject;
import org.junit.Test;

public class CfgTestConnect {
    @Test
    public void json() {
        String jsonStr = "{\"head\":" +
                "{\"reqId\":\"001\"," +
                "\"layerId\":\"101\"," +
                "\"seqId\":\"201\"," +
                "\"times\":\"2017-06-19 11:11:11\"," +
                "\"compId\":\"301\"}," +
                "\"info\":{" +
                "\"dataType\":\"1\"," +
                "\"data\":[{\"gswUserId  \":\"301 \"," +
                "\"wmUser\":\"乘客\"," +
                "\"gswFirstName\":\"欧阳风华\"," +
                "\"gswTenantId \":\"乘客\"," +
                "\"gswEmail\":\"1655615615616@163.com\"," +
                "\"wmSecurityRoleId\":\"3\"}]}}";

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        RoleController roleController = new RoleController();
        roleController.roleBackJson(jsonObject);

    }

}
