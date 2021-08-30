package com.hilife.webview.settings;

import android.content.Context;
import android.os.Build;


import com.hilife.webview.R;
import com.hilife.webview.bowser_client.DefaultWebViewClient;
import com.hilife.webview.config.WebModuleAppLifecycleImpl;
import com.tencent.smtt.sdk.WebView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.net.cyberwy.hopson.lib_util.JSONUtil;
import cn.net.cyberwy.hopson.lib_util.PhoneUtil;
import cn.net.cyberwy.hopson.lib_util.log.LogHelper;
import cn.net.cyberwy.hopson.sdk_public_base.constant.Constants;

/**
 * 全局js注入
 */
public class JsInjector {

    private static String dajiaJSStr;

    public static void injectGlobalJs(WebView webView) {
        if (webView == null) {
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("msg", "ok");
        try {
            webView.evaluateJavascript("javascript:" + readDajiaJJS(WebModuleAppLifecycleImpl.getContext()), null);
            int inputswitch = CacheAppData.readInt(GlobalApplication.getContext(), Constants.SWITCH_INPUT, 0);
            if (inputswitch == 1) {
                webView.evaluateJavascript("javascript:" + DefaultWebViewClient.readInputJS(WebModuleAppLifecycleImpl.getContext()), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "error");
        }
        webView.evaluateJavascript("javascript:if(da){da.check('" + JSONUtil.toJSON(map) + "')}", null);
        if (Configuration.isSupport(GlobalApplication.getContext(), R.string.perfStatSwitch)) {
            // 加载监控timing.js
            String getTimes = "(function(window){'use strict';window.timing=window.timing||{getTimes:function(opts){" +
                    "var performance=window.performance||window.webkitPerformance||window.msPerformance||window.mozPerformance;" +
                    "if(performance===undefined){return false}var timing=performance.timing;var api={};opts=opts||{};" +
                    "if(timing){if(opts&&!opts.simple){for(var k in timing){if(isNumeric(timing[k])){api[k]=parseFloat(timing[k])}}}" +
                    "if(api.firstPaint===undefined){var firstPaint=0;if(window.chrome&&window.chrome.loadTimes)" +
                    "{firstPaint=window.chrome.loadTimes().firstPaintTime*1e3;api.firstPaintTime=firstPaint-window.chrome.loadTimes().startLoadTime*1e3}" +
                    "else if(typeof window.performance.timing.msFirstPaint===\"number\"){firstPaint=window.performance.timing.msFirstPaint;" +
                    "api.firstPaintTime=firstPaint-window.performance.timing.navigationStart}if(opts&&!opts.simple){api.firstPaint=firstPaint}}" +
                    "api.loadTime=timing.loadEventEnd-timing.fetchStart;api.domReadyTime=timing.domComplete-timing.domInteractive;" +
                    "api.readyStart=timing.fetchStart-timing.navigationStart;api.redirectTime=timing.redirectEnd-timing.redirectStart;" +
                    "api.appcacheTime=timing.domainLookupStart-timing.fetchStart;api.unloadEventTime=timing.unloadEventEnd-timing.unloadEventStart;" +
                    "api.lookupDomainTime=timing.domainLookupEnd-timing.domainLookupStart;api.connectTime=timing.connectEnd-timing.connectStart;" +
                    "api.requestTime=timing.responseEnd-timing.requestStart;api.initDomTreeTime=timing.domInteractive-timing.responseEnd;api.loadEventTime=timing.loadEventEnd-timing.loadEventStart}return api}," +
                    "printTable:function(opts){var table={};var data=this.getTimes(opts)||{};Object.keys(data).sort().forEach(function(k){table[k]={ms:data[k],s:+(data[k]/1e3).toFixed(2)}});console.table(table)}," +
                    "printSimpleTable:function(){this.printTable({simple:true})}};function isNumeric(n){return!isNaN(parseFloat(n))&&isFinite(n)}if(typeof module!=='undefined'&&module.exports){module.exports=window.timing}})" +
                    "(typeof window!=='undefined'?window:{});window.timing.getTimes()";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(getTimes, new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        commitPerformanceInfo(value, webView.getUrl());
                        LogHelper.e("js----15>" + value);
                    }
                });

            }
        }
    }

    /**
     * 从本地读取js
     *
     * @param context
     * @return
     * @throws IOException
     */

    private static String readDajiaJJS(Context context) throws IOException {
        if (dajiaJSStr == null) {
            //注入android和html交互的基础js
            InputStream in = context.getResources().getAssets().open("js/jdajiaAndroid.js");
            byte buff[] = new byte[1024];
            ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
            do {
                int numread = in.read(buff);
                if (numread <= 0) break;
                fromFile.write(buff, 0, numread);
            } while (true);
            String js = "var newscript = document.createElement(\"script\");";
            js += fromFile.toString();
            js += "document.body.appendChild(newscript);";
            dajiaJSStr = js;
        }
        return dajiaJSStr;
    }

    /**
     * 客户端提交性能分析数据到服务端
     *
     * @param timingValue
     */
    private static void commitPerformanceInfo(String timingValue, String webUrl) {
        Map performanceParam = buildPerformanceParam(timingValue, webUrl);
        PerformanceService performanceService = ServiceFactory.getPerformanceService(GlobalApplication.getContext());
        performanceService.commitPerformanceData(performanceParam, new DataCallbackHandler<Void, Void, Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
            }

            @Override
            public void onError(AppException e) {
                super.onError(e);
            }

            @Override
            public void onCanceled() {
                super.onCanceled();
            }
        });
    }

    /**
     * 构造性能分析json数据
     *
     * @param timingValue
     * @return Map
     */
    private static Map buildPerformanceParam(String timingValue, String webUrl) {
        Map<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("deviceVersion", Build.VERSION.RELEASE);
        deviceInfo.put("appVersionString", PhoneUtil.getAppVersion(GlobalApplication.getContext()));
        deviceInfo.put("deviceType", Build.MODEL);
        deviceInfo.put("appBundleId", GlobalApplication.getContext().getPackageName());
        deviceInfo.put("deviceName", Build.BRAND);
        Map<String, String> extendedInfo = new HashMap<>();
        extendedInfo.put("communityId", DJCacheUtil.readCommunityID());
        extendedInfo.put("personId", DJCacheUtil.readPersonID());
        extendedInfo.put("currentView", "WebFragment");
        extendedInfo.put("requestUrl", webUrl);
        extendedInfo.put("personName", DJCacheUtil.read(CacheUserData.PERSON_NAME));
        Map timing = JSONUtil.parseJSON(timingValue, Map.class);
        Map<String, String> memoryInfo = new HashMap<>();
        memoryInfo.put("currentMemoryInfo", PhoneUtil.getCurrentMenmoryInfo(GlobalApplication.getContext()) + "");
        Map timingMap = new HashMap();
        timingMap.put("deviceInfo", deviceInfo);
        timingMap.put("extendedInfo", extendedInfo);
        timingMap.put("timing", timing);
        timingMap.put("memoryInfo", memoryInfo);
        return timingMap;
    }

}
