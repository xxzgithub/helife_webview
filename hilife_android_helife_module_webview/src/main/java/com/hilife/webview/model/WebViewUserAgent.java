package com.hilife.webview.model;

import android.content.Context;
import android.os.Build;

import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.tools.NetworkUtil;
import com.hilife.mobile.android.tools.PhoneUtil;
import com.hilife.view.BuildConfig;
import com.hilife.view.weight.Configuration;
import com.hilife.view.other.cache.DJCacheUtil;

import java.io.Serializable;

/**
 * 主app中webview的useragent 添加项
 *
 * @author adminiatrator
 */
public class WebViewUserAgent implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2277901926132525475L;
    private String version = "1.0";
    private String system = "android";
    private String app = "dajia";
    private String pID;
    private String customID;//定制ID
    private String cID;//{"version":"version","system":"android","app":"dajia","pID":"xxx","cID":"xxx"}
    private String buildVersion;
    private String deviceID;
    private String deviceType;
    private String deviceBrand;
    private String appCode;
    private String appname;
    private String appVersion;

    public WebViewUserAgent(Context context) {
        version = PhoneUtil.getAppVersion(context);
        if (!StringUtil.isEmpty(DJCacheUtil.readPersonID())) {
            pID = DJCacheUtil.readPersonID();
        } else {
            pID = "guest";

        }
        cID = DJCacheUtil.readCommunityID();
        customID = Configuration.getCustomID(context);
        buildVersion = Configuration.getBuildVersion(context);
        deviceID = NetworkUtil.getDeviceId(context);
        deviceType = Build.MODEL;
        deviceBrand = Build.MANUFACTURER;
        appCode = BuildConfig.APP_CODE;
        if (appCode.equals("128") || appCode.equals("256")) {
            appname = "manshenghuo";
        } else {
            appname = "heshenghuo";
        }
        appVersion = BuildConfig.VERSION_NAME;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
//		String uagentStr = DJCacheUtil.read("WebViewUserAgent"+cID+pID+version);
//		if(uagentStr == null){
        String uagentStr = " DajiaWebView/" + version + "(Build " + buildVersion + ") dajia/" + JSONUtil.toJSON(this);
        DJCacheUtil.keep("WebViewUserAgent" + cID + pID + version, uagentStr);
//		}
        return uagentStr;
    }
}
