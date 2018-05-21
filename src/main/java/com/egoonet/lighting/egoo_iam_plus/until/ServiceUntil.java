package com.egoonet.lighting.egoo_iam_plus.until;

import com.genesyslab.wfm8.API.service.locator800.LoginInfo;
import com.genesyslab.wfm8.API.service.locator800.WFMLocatorService800Soap;
import com.genesyslab.wfm8.API.service.session800.ServiceInfo;
import com.genesyslab.wfm8.API.service.session800.SessionInfo;
import com.genesyslab.wfm8.API.service.session800.WFMSessionService800Soap;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.*;


/**
 * WFMservice获取工具类，
 * Created by leiyou on 18/04/25.
 */
@Log4j
@Component(value = "ServiceUntil")
public class ServiceUntil {

    @Value("${wfm.joint.appName}")
    private String appName = "appName";
    @Value("${wfm.joint.wfmServerHost}")
    private String wfmServerHost = "222.73.213.174";
    @Value("${wfm.joint.wfmServerPort}")
    private String wfmServerPort = "21007";
    @Value("${wfm.joint.userName}")
    private String userName = "default";
    @Value("${wfm.joint.password}")
    private String password = "password";

    public static String getCurrentUrl(String url, String wfmServerHost, String wfmServerPort) {
        url = url.replaceAll("hffgpdna", wfmServerHost);
        url = url.replaceAll("6030", wfmServerPort);
        return url;
    }

    public Map<String, ServiceInfo> getConnect() {
        LoginInfo loginInfo = getLoginInfo(wfmServerHost, wfmServerPort, appName, userName, password);

        WFMSessionService800Soap sessionService = getCurrentService(WFMSessionService800Soap.class,
                "/SOAPServer/WFMSessionService800.wsdl", "WFMSessionService800", getCurrentUrl(loginInfo.getSessionServiceURL(), wfmServerHost, wfmServerPort));

        SessionInfo sessionInfo = sessionService.openSession(appName, loginInfo.getUserID());

        Map<String, ServiceInfo> serviceInfoMap = new HashedMap();
        for (
                int i = 0; i < sessionInfo.getServicesNSizeIs(); i++)

        {
            ServiceInfo serviceInfo = sessionInfo.getServices().get(i);
            serviceInfoMap.put(serviceInfo.getServiceName(), serviceInfo);
        }

//        if (serviceInfoMap.containsKey("SessionService800"))
//
//        {
//            sessionService = getCurrentService(WFMSessionService800Soap.class,
//                    "/SOAPServer/WFMSessionService800.wsdl", "WFMSessionService800",
//                    serviceInfoMap.get("SessionService800").getServiceURL());
//        }
//
//        cfgService = getCurrentService(WFMConfigService850Soap.class,
//                "/ConfigService/WFMConfigService850.wsdl", "WFMConfigService850",
//                serviceInfoMap.get("ConfigService850").getServiceURL());

        return serviceInfoMap;
    }

    public LoginInfo getLoginInfo(String wfmServerHost, String wfmServerPort, String appName, String userName, String password) {

        WFMLocatorService800Soap locator = getCurrentService(
                WFMLocatorService800Soap.class,
                "/LocatorService/WFMLocatorService800.wsdl",
                "WFMLocatorService800",
                "http://" + wfmServerHost + ":" + wfmServerPort
                        + "/?Handler=WFMLocatorService800");

        LoginInfo loginInfo = locator.locateServerLogin("SessionService800", appName, userName, password);

        return loginInfo;
    }

    public static <T> T getCurrentService(Class<T> klass, String wsdlLocation, String serviceName, String serverUrl) {
        URL tmpUrl = klass.getResource(wsdlLocation);

        Service srvc = Service.create(tmpUrl, new QName("urn:" + serviceName,
                serviceName));

        T rez = srvc.getPort(new QName("urn:" + serviceName, serviceName + "Soap"),
                klass);

        ((BindingProvider) rez).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverUrl);
        return rez;
    }
}
