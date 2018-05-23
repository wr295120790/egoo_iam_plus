package com.egoonet.lighting.egoo_iam_plus;

import com.egoonet.lighting.egoo_iam_plus.controller.UserRoleController;
import net.sf.json.JSONObject;
import org.junit.Test;

public class UserRoleTest {
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
                "\"opType\":\"3\"," +
                "\"data\":[{\"id\":\"001\"," +
                "\"name\":\"欧阳风华\"," +
                "\"userName\":\"机长\"," +
                "\"gender\":\"2\"," +
                "\"employeeId\":\"001\"," +
                "\"jobId\":\"550\"," +
                "\"jobName\":\"财务主管\"," +
                "\"phone\":\"741747741\"," +
                "\"email\":\"xiangxiang@163.com\"," +
                "\"parentId\":\"660\"," +
                "\"parentName\":\"欧阳的下属\"," +
                "\"hireDate\":\"2017-06-10\"," +
                "\"orgId\":\"701\"," +
                "\"employeeType\":\"110\"," +
                "\"status\":\"1\"," +
                "\"date\":\"2017-07-10 10:10:10\"," +
                "\"remark\":\"职员\"}]}}";
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);

        UserRoleController userRoleController = new UserRoleController();
        userRoleController.userRoleJson(jsonObject);
    }
}
