package com.hilife.webview.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import com.hilife.webview.model.WebViewUserAgent;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * 全局设定
 */
public class GlobalWebManager {

    public static void settingWebview(WebView webView, String webUrl) {
        if (webView.getX5WebViewExtension() != null) {
            webView.getX5WebViewExtension().setHorizontalScrollBarEnabled(false);//水平不显示滚动按钮
            webView.getX5WebViewExtension().setVerticalScrollBarEnabled(false); //垂直不显示滚动按钮
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            hiddenZoomControls(webSettings);
        }
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setBlockNetworkImage(false);
        webSettings.setUseWideViewPort(true);
        /**
         * 设置浏览器支持JavaScript
         */
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 通知javascript自动打开窗口
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // 设置可以访问文件
        webSettings.setAppCacheEnabled(true);
        // 加载时优先在缓存中寻找资源
        if (18 < Build.VERSION.SDK_INT) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        webSettings.setSaveFormData(false);// 存储的WebView是否保存表单数据。
        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setDatabaseEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);//设置允许视频自动播放
//        webSettings.setUserAgentString(webSettings.getUserAgentString() + new WebViewUserAgent(GlobalApplication.getContext()));
        //下面是 其它的
//        WebSettings webSetting = webView.getSettings();
//        webSetting.setSupportMultipleWindows(false);
//        webSetting.setSupportZoom(false);
//        webSetting.setBuiltInZoomControls(false);
//        webSetting.setLoadWithOverviewMode(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            hiddenZoomControls(webSetting);
//        }
//        webSetting.setAllowFileAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            webSetting.setAllowFileAccessFromFileURLs(true);
//        }
//        webSetting.setAllowContentAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            webSetting.setAllowUniversalAccessFromFileURLs(true);
//        }
//        webSetting.setBlockNetworkImage(false);
//        webSetting.setUseWideViewPort(true);
        webSettings.setUserAgent(webSettings.getUserAgentString() + new WebViewUserAgent(GlobalApplication.getContext()).toString());
        if (webUrl != null && webUrl.contains("lcg")) {
            webSettings.setUserAgent(null);
        }
        // TODO 下面是测试用的
//        webSetting.setJavaScriptEnabled(true);
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setSupportZoom(true);
//        webSetting.setBuiltInZoomControls(true);
//        webSetting.setSaveFormData(false);// 存储的WebView是否保存表单数据
//        webSetting.setSupportMultipleWindows(true);
//        webSetting.setLoadWithOverviewMode(true);
//        webSetting.setAppCacheEnabled(true);
//        // webSetting.setDatabaseEnabled(true);
//        webSetting.setDefaultTextEncodingName("utf-8");
//        webSetting.setDomStorageEnabled(true);
//        webSetting.setGeolocationEnabled(true);
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
//         webSettings.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//        webSettings.setAppCachePath(GlobalApplication.getContext().getApplicationContext().getDir("appcache", 0).getPath());
//        webSettings.setDatabasePath(GlobalApplication.getContext().getApplicationContext().getDir("databases", 0).getPath());
//        webSettings.setGeolocationDatabasePath(GlobalApplication.getContext().getApplicationContext().getDir("geolocation", 0).getPath());
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setRenderPriority(WebSettings.RenderPriority.LOW);
//        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        disableX5FullscreenFunc(webView);
    }

    private static void disableX5FullscreenFunc(WebView webView) {
        if (webView.getX5WebViewExtension() != null) {
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        } else {
            Toast.makeText(webView.getContext(), "恢复webkit初始状态", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void hiddenZoomControls(WebSettings webSettings) {
        webSettings.setDisplayZoomControls(false);
    }

    /**
     * 是否根据龙存管设置独立的UserAgent
     *
     * @param view
     * @param url
     * @return
     */

    public static boolean processLcgUserAgent(WebView view, String url) {
        if (view == null) {
            return false;
        }
        WebSettings settings = view.getSettings();
//
        if (url != null && url.contains("lcg")) {
            com.orhanobut.logger.Logger.i("包含龙存管 " + url);
            settings.setUserAgent("");
            com.orhanobut.logger.Logger.i(view.getSettings().getUserAgentString());
            view.loadUrl(url);
            return true;
        } else {
            com.orhanobut.logger.Logger.i("不包含龙存管 " + url);
            String normalUserAgent = new WebViewUserAgent(view.getContext()).toString();
            String nowUserAgent = settings.getUserAgentString();
            if (!nowUserAgent.contains(normalUserAgent)) {
                com.orhanobut.logger.Logger.i("不包含龙存管  设置新的userAgent " + url);
                settings.setUserAgent(settings.getUserAgentString() + normalUserAgent);
                com.orhanobut.logger.Logger.i(settings.getUserAgentString());
            }
        }
        return false;
    }

    public static void destroyWebView(WebView wv) {
        destroyWebView(wv, false);
    }

    public static void destroyWebView(WebView wv, boolean needLoad) {
        try {
            wv.stopLoading();
            if (needLoad) {
//                wv.evaluateJavascript("about:blank", null);
                wv.loadUrl("about:blank");
            } else {
//                wv.reload(XWalkView.RELOAD_NORMAL);
            }
            wv.clearFormData();
            wv.clearAnimation();
            wv.clearDisappearingChildren();
            wv.clearHistory();
            wv.destroyDrawingCache();
            wv.removeAllViews();
            wv.destroy();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearWebviewCache(WebView webView) {
        if (webView == null) {
            return;
        }
//        webView.clearCache(true);
    }
}
