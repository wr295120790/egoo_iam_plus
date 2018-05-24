package com.egoonet.wang;

import com.egoonet.lighting.egoo_iam_plus.until.ServiceUntil;
import com.genesyslab.wfm8.API.service.config850.CfgSite;
import com.genesyslab.wfm8.API.service.config850.CfgSiteFilter;
import com.genesyslab.wfm8.API.service.config850.WFMConfigService850Soap;
import com.genesyslab.wfm8.API.service.session800.ServiceInfo;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j
public class SiteController {

    public static void main(String[] args) {

        log.info("准备进入连接！");
        WFMConfigService850Soap cfgService = null;
        Map<String, ServiceInfo> serviceInfoMap = new ServiceUntil().getConnect();
        cfgService = ServiceUntil.getCurrentService(WFMConfigService850Soap.class,
                "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
                ServiceUntil.getCurrentUrl(serviceInfoMap.get("ConfigService850").getServiceURL(),
                        "222.73.213.174", "21007"));
        log.info("已经建立连接");

        CfgSiteFilter cfgSiteFilter = new CfgSiteFilter();
        int buId = 2;
        List<Integer> listBuId = new ArrayList<>();
        listBuId.add(buId);
        cfgSiteFilter.setWmBUId(listBuId);
        cfgSiteFilter.setWmBUIdNSizeIs(1);
        int siteId = 2;
        List<Integer> listSiteId = new ArrayList<>();
        listSiteId.add(siteId);
        cfgSiteFilter.setWmSiteId(listSiteId);
        cfgSiteFilter.setWmSiteIdNSizeIs(1);
        cfgSiteFilter.setCfgSnapshotID("Tom");

        SiteService siteService = new SiteService();
        List<CfgSite> siteList;
        siteList = siteService.getSites(cfgService, cfgSiteFilter);

        System.out.println("这个是什么东西呢！！" + siteList);

    }
}
