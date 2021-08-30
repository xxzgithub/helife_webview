package com.hilife.webview.ui.xwalk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.framework.activity.BaseActivity;
import com.hilife.mobile.android.framework.component.onActivityForResult.OnActivityForResultUtils;
import com.hilife.mobile.android.framework.component.onActivityForResult.SimpleOnActivityForResultCallback;
import com.hilife.mobile.android.tools.file.FileUtil;
import com.hilife.view.R;
import com.hilife.view.main.base.BaseFragment;
import com.hilife.view.main.base.DajiaBaseActivity;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.view.other.component.webview.bowser_client.DefaultWebViewClient;
import com.hilife.view.other.component.webview.bowser_client.DefaultXwalkChromeClient;
import com.hilife.view.other.component.webview.bowser_client.DefaultXwalkViewClient;
import com.hilife.view.other.component.webview.model.WebViewUserAgent;
import com.hilife.view.other.component.webview.settings.GlobalWebManager;
import com.hilife.view.other.component.webview.settings.JsInjector;
import com.hilife.view.other.component.webview.ui.WebActivity;
import com.hilife.view.other.context.GlobalApplication;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.share.model.ShareBean;
import com.hilife.view.share.tools.ShareMessegeEvent;
import com.hilife.view.weight.Configuration;
import com.hopson.hilife.commonbase.util.LogHelper;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 对应BaseWebFragment
 * Created by huteng on 16/5/27.
 */
public abstract class BaseXwalkFragment extends BaseFragment {

    private boolean isPageFinished = false;
    protected WebView webView;
    protected ProgressBar webProgressView;
    protected ProgressBar webProgressView2;
    protected View webErrorView;
    protected String mTitle;
    protected String webUrl;
    protected Map<String, String> titleMap;

    protected String webTitle;

    protected ValueCallback<Uri> mUploadMessage;
    protected File fileChoose;
    protected String tagName;
    protected String windowState;
    protected AttachDelegate attachDelegate;
    private WebChromeClient wcc;
    private DefaultXwalkChromeClient xWalkUIClient;
    private View mCustomView;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;
    private ViewGroup rootView;

    private boolean isFullScreen = false;
    /**
     * 页面是否load完成，用于处理重定向的问题
     */
    private boolean isLoadFinished = false;
    /**
     * 在fragment中是否需要调用xwalkview的load方法，如果不是加载的模板，则需要调用load
     */
    protected boolean needLoad = true;
    private LinearLayout ll_webview;
    // 是否是首次加载页面
    private boolean isFirstLoad = true;
    private String rightTag;

    public BaseXwalkFragment() {
    }

    @SuppressLint("ValidFragment")
    public BaseXwalkFragment(AttachDelegate delegate) {
        this.attachDelegate = delegate;
    }

    @SuppressLint("ValidFragment")
    public BaseXwalkFragment(AttachDelegate delegate, String rightTag) {
        this.attachDelegate = delegate;
        this.rightTag = rightTag;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        titleMap = new HashMap<String, String>();
    }

    @Override
    protected void findView() {
        rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        // 判断是否是模板加载，构造显示界面 start
        if (needLoad) {
            webView = (WebView) findViewById(R.id.webView);
            enableXWalkViewCookie();
        } else {
            ll_webview = (LinearLayout) findViewById(R.id.ll_webview);
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if (null != viewGroup) {
                viewGroup.removeAllViews();
            }
            ll_webview.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        // 判断是否是模板加载，构造显示界面 end
        webErrorView = findViewById(R.id.webErrorView);
//        webProgressView = (ProgressBar) findViewById(R.id.webProgressView);
        webProgressView2 = (ProgressBar) findViewById(R.id.webProgressView2);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void settingWebView() {
        GlobalWebManager.settingWebview(webView, webUrl);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long l) {
                if (BaseXwalkFragment.this.isAdded()) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });
        boolean showOtherWindows = false;
        if (attachDelegate != null && attachDelegate instanceof Fragment) {
            showOtherWindows = true;
        }
        if (!needLoad && !showOtherWindows) {
            // 如果走模板加载，页面上的所有<a>标签都新打开页面来显示
            showOtherWindows = true;
        }
        DefaultXwalkViewClient.IWebClientListener webClientListener = new DefaultXwalkViewClient.IWebClientListener() {
            @Override
            public void onOpenUrlWithNewTab(String url) {
                Intent regIntent = new Intent(mContext, WebActivity.class);
                if (null != url && url.contains("ccm/recharge")) {
                    regIntent.putExtra("category", Constants.WEB_SHOP);
                } else {
                    regIntent.putExtra("category", Constants.WEB_PORTAL);
                }
                regIntent.putExtra("title", tagName);
                regIntent.putExtra("tagName", tagName);
                regIntent.putExtra("web_url", url);
                startActivity(regIntent);
            }

            @Override
            public void windowGoBack() {
                goBack();
            }

            @Override
            public boolean isLoadFinished() {
                return isLoadFinished;
            }
        };
        DefaultXwalkViewClient webViewClient = new DefaultXwalkViewClient(webView, webUrl, showOtherWindows, webClientListener, webErrorView, mContext) {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                EventBus.getDefault().post(new ShareMessegeEvent(new ShareBean(), "cancle", rightTag));
//                if(null != webProgressView) {
//                    webProgressView.setVisibility(View.VISIBLE);
//                }
                isPageFinished = false;
                Logger.i("onPageStarted  " + url);
                BaseXwalkFragment.this.onPageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.i("onPageFinished  " + url);
                //JsInjector.injectGlobalJs(webView);
            }
        };
        webView.setWebViewClient(webViewClient);
        if (needLoad && !webViewClient.addOpenID(webView, webUrl, mContext)) {
            webView.loadUrl(webUrl, null);
        }
        xWalkUIClient = new DefaultXwalkChromeClient() {
            private View mVideoProgressView;
            private Bitmap mDefaultVideoPoster;

            @Override
            public void onProgressChanged(WebView webView, int progressInPercent) {
                super.onProgressChanged(webView, progressInPercent);
                adjustPageFinished(webView, progressInPercent);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d("web----------", "----onReceivedTitle");
                if (tagName == null) {
                    if (attachDelegate != null && null != title && StringUtil.isEmpty(mTitle)) {
                        attachDelegate.setTitle(title);
                    }
                }
                titleMap.put(view.getUrl(), webTitle);
                webTitle = title;
                setWebTitle(view.getUrl());
                super.onReceivedTitle(view, title);
            }

            // TODO: 2020/5/18 需要测试下视频全屏播放
            // 全屏播放
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
                View topBar = ((WebActivity) mActivity).getTopBar();
                if (null != mActivity && mActivity instanceof WebActivity) {
                    if (null != topBar) {
                        topBar.setVisibility(View.GONE);
                    }
                }
                isFullScreen = true;
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            // 非全屏播放
            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                View topBar = ((WebActivity) mActivity).getTopBar();
                if (null != mActivity && mActivity instanceof WebActivity) {
                    topBar.setVisibility(View.VISIBLE);
                }
                isFullScreen = false;
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            //            add by kevin 20160924 支持视频横向播放
//            @SuppressLint("SourceLockedOrientationActivity")
//            @Override
//            public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
//                super.onFullscreenToggled(view, enterFullscreen);
//                Log.d("web----------", "----onFullscreenToggled");
//                Log.v("onFullscreenToggled", enterFullscreen + "");
//                View topBar = ((WebActivity) mActivity).getTopBar();
//                if (enterFullscreen) {
//                    if (null != mActivity && mActivity instanceof WebActivity) {
//                        if (null != topBar) {
//                            topBar.setVisibility(View.GONE);
//                        }
//                    }
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                } else {
//                    if (null != mActivity && mActivity instanceof WebActivity) {
//                        topBar.setVisibility(View.VISIBLE);
//                    }
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//            }

            @Override
            public Intent createCameraIntent() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileChoose = FileUtil.findOpenFile(FileUtil.createPictureName());
                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", fileChoose);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    imageUri = Uri.fromFile(fileChoose);
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                return cameraIntent;
            }

            @Override
            public void chooseFile(ValueCallback<Uri> uploadMsg, Intent i) {
                mUploadMessage = uploadMsg;
                OnActivityForResultUtils.startActivityForResult(mActivity, Constants.CHOOSER_FILE, Intent.createChooser(i, "File Chooser"), new SimpleOnActivityForResultCallback() {

                    @Override
                    public void success(Integer resultCode, Intent data) {
                        if (null == mUploadMessage)
                            return;
                        Uri res = null;
                        if (resultCode == Activity.RESULT_OK) {
                            res = data == null ? null : data.getData();
                            if (res == null && fileChoose != null) {
                                res = Uri.fromFile(fileChoose);
                            }
                        }
                        mUploadMessage.onReceiveValue(res);
                        mUploadMessage = null;
                        fileChoose = null;
                    }
                });
            }
        };
        webView.setWebChromeClient(xWalkUIClient);
        settingJavascript();
    }

    private void doOnPageFinished(WebView webView, String url) {
        Logger.i("doOnPageFinished");
        // TODO: 2020/6/22 测试
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("xxz","1111"+"BaseXwalk");
                JsInjector.injectGlobalJs(webView);
            }
        }, 1000);
        isLoadFinished = true;
        setWebTitle(url);
        // BaseXwalkFragment.this.onPageFinished();
        // 奇葩的方案 会议中从详情返回到列表时，通过这种方式来刷新列表
        if (url.contains("pbmeeting/mobile/index.html#/activity")
                || url.contains("pbmeeting/mobile/index.html#/threeMeeting")) {
            webView.evaluateJavascript("javascript:appCtx.customizedAppCallback()", null);
        }
        if (webUrl.equals("https://www.dajiashequ.com")) {
            stopJsReady();
        }
        BaseXwalkFragment.this.onPageFinished();
        if (getActivity()==null){
            mActivity.getIntent().putExtra("currentUrl", url);
        }else{
            getActivity().getIntent().putExtra("currentUrl", url);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hiddenZoomControls(WebSettings webSettings) {
        webSettings.setDisplayZoomControls(false);
    }

    public void setWebTitle(String url) {
        if (getActivity() instanceof WebActivity) {
            ((WebActivity) getActivity()).adjustTheme(url);
        }
        if (attachDelegate != null) {
            String currentTitle = webView.getTitle();
            if (webView.canGoBack()) {
                if (!TextUtils.isEmpty(currentTitle)) {
                    attachDelegate.setTitle(currentTitle);
                } else {
                    attachDelegate.setTitle(webTitle);
                }
                return;
            }
            if (!TextUtils.isEmpty(mTitle)) {
                attachDelegate.setTitle(mTitle);
                return;
            }
            attachDelegate.setTitle(webTitle);
        }
    }

    protected int getCacheMode() {
        return WebSettings.LOAD_DEFAULT;
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClickEvent(View view) {
    }

    public void clickRight(boolean isDispatch) {
    }

    //todo 需要测试忽略缓存
    public void reload() {
        webView.reload();
    }

    @Override
    public boolean goBack() {
        if ("onProgress".equals(windowState)) {
            webView.evaluateJavascript("javascript:web.historyBack()", null);
        } else {
            mActivity.finish();
        }
        return super.goBack();
    }

    @Override
    protected void processLogic() {
        if (attachDelegate instanceof Fragment) {
            if (getArguments() != null) {
                webUrl = getArguments().getString("web_url");
                mTitle = getArguments().getString("title");
                /**
                 * 从PortalFragment过来的才会有此参数
                 */
                tagName = getArguments().getString("tagName");
            }
        } else {
            Intent mIntent = mActivity.getIntent();
            webUrl = mIntent.getStringExtra("web_url");
            mTitle = mIntent.getStringExtra("title");
            /**
             * 从PortalFragment过来的才会有此参数
             */
            tagName = mIntent.getStringExtra("tagName");
        }
        if (StringUtil.isEmpty(webUrl)) {
            DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_app_disabled));
        } else {
            if (StringUtil.isNotBlank(tagName) && !webUrl.contains("tagName")) {
                if (webUrl.contains("?")) {
                    webUrl = webUrl + "&tagName=" + tagName;
                } else {
                    webUrl = webUrl + "?tagName=" + tagName;
                }
            }
        }
        if (!needLoad) {
            invokeJsMethod();
        } else {
            if (attachDelegate != null) {
                attachDelegate.setTitle(mTitle);
            }
        }
        settingWebView();
    }

    public void invokeJsMethod() {
        LogHelper.d("webview---->", "invokeJsMethod2");
        if (webUrl.startsWith("http") || webUrl.startsWith("https")) {
            try {
                URL url1 = new URL(webUrl);
                LogHelper.e("url----->" + url1 + "");
                if (null != url1) {
                    String query = url1.getQuery(); // shopId=8733643000632135059&shopType=0&tagName=云内品牌
                    if (StringUtil.isNotEmpty(query)) {
                        Map queryMap = new HashMap();
                        String[] queryArray = query.split("&");
                        for (int i = 0; i < queryArray.length; i++) {
                            String[] queryArrayChild = queryArray[i].split("=");
                            if (queryArrayChild.length == 2) {
                                queryMap.put(queryArrayChild[0], queryArrayChild[1]);
                            }
                        }
                        if (null != queryMap && !queryMap.isEmpty()) {
//                            webProgressView.setVisibility(View.VISIBLE);
                            webView.evaluateJavascript("javascript:loadData(" + JSONUtil.toJSON(queryMap) + ")", value -> {
                                LogHelper.e("js----10>" + value);
                            });
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != webView) {
//            xWalkView.onDestroy();
//            xWalkView.reload(XWalkView.RELOAD_NORMAL);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (null != webView) {
            // 这里不做处理，直接destroy会导致显示异常
//            xWalkView.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeWebviewTimer(webView);
        if (null != webView) {
//            xWalkView.onShow();
            if (StringUtil.isNotEmpty(webUrl) && !isFirstLoad) {
                if (webUrl.contains("worksshow/worksShowIndex.action")) {
                    // 修改worktile上问题：作品秀在作品详情中点赞，返回作品列表，被点赞的作品在列表中点赞数未增加
                    webView.evaluateJavascript("javascript:refreshLikeCount()", null);
                }
            }
            if (isFirstLoad) {
                isFirstLoad = !isFirstLoad;
            }
            webView.evaluateJavascript("javascript:appCtx.customizedAppCallback()", null);
        }
    }

    @Override
    public void onPause() {
        pauseWebviewTimer(webView);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mCustomView != null && wcc != null) {
                    wcc.onHideCustomView();
                } else {
                    goBack();
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private void confirmBack() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 返回文件选择
        if (requestCode == Constants.CHOOSER_FILE) {
            if (null == mUploadMessage)
                return;
            Uri res = null;
            if (resultCode == Activity.RESULT_OK) {
                res = data == null ? null : data.getData();
                if (res == null && fileChoose != null) {
                    res = Uri.fromFile(fileChoose);
                }
                res = Utils.contentUriToFileUri(mContext, res);
            }
            mUploadMessage.onReceiveValue(res);
            mUploadMessage = null;
            fileChoose = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (webView != null && needLoad) {
            destroyWebView(webView);
        }
        super.onDestroy();
    }

    private void destroyWebView(WebView wv) {
        GlobalWebManager.destroyWebView(wv, needLoad);
    }

    @Override
    public String getPageID() {
        return Constants.MONITOR_PAGE_WEB;
    }

    @Override
    public String getTagName() {
        return super.getTagName();
    }

    public void onPageStarted(String url) {
    }

    public void onPageFinished() {
    }

    public void settingJavascript() {
    }

    /**
     * 是否是全屏播放
     *
     * @return
     */
    public boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * 退出全屏
     */
    public void quitFullScreen() {
        isFullScreen = false;
    }

    @Override
    public void setAttachDelegate(AttachDelegate attachDelegate) {
        super.setAttachDelegate(attachDelegate);
        this.attachDelegate = attachDelegate;
        needLoad = false;
    }

    public void setWebUrl(final String templateName, final String version, String webUrl, BaseActivity context) {
        Logger.i("setWebUrl");
        this.webUrl = webUrl;
        mContext = context;
        if (null == mActivity) {
            mActivity = (DajiaBaseActivity) context;
        }
        //检测xWalkView 的sActivityInfo是否为空解决bugly上崩溃问题
        //xWalkView监听没有注册成功 不能走下面方法 否则会报错
        // 产生原因是这个方法是网络请求回来后调用可能页面已经关闭xWalkView监听已经注销或者xWalkView还没初始化成功
        boolean pass = checkXWalkViewObserver(context);
        if (pass) {
            return;
        }
        webView = new WebView(context);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setUserAgentString(webSettings.getUserAgentString() + new WebViewUserAgent(mContext));
        enableXWalkViewCookie();
        webView.loadUrl(webUrl, null);
        DJCacheUtil.keepBoolean(mContext, templateName + "_" + version + "_load", false);
//        WebViewClient webViewClient = new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
//                super.onPageStarted(webView, s, bitmap);
//                // 页面加载时注入NativeEvnParamsInject.webHost
//                String injectJs = "NativeEvnParamsInject=function(){};\n" +
//                        "NativeEvnParamsInject.webHost='" + Configuration.getWebHost(mContext) + "';";
//                BaseXwalkFragment.this.webView.evaluateJavascript(injectJs, value -> {
//                    LogHelper.e("js----11>" + value);
//                });
//                isPageFinished = false;
//            }
//
//            @Override
//            public void onPageFinished(WebView webView, String url) {
//                super.onPageFinished(webView, url);
//
//            }
//
//        };
        WebViewClient webViewClient = new DefaultWebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                // 页面加载时注入NativeEvnParamsInject.webHost
                String injectJs = "NativeEvnParamsInject=function(){};\n" +
                        "NativeEvnParamsInject.webHost='" + Configuration.getWebHost(mContext) + "';";
                BaseXwalkFragment.this.webView.evaluateJavascript(injectJs, value -> {
                    LogHelper.e("js----11>" + value);
                });
                Logger.i("isPageFinished = false;");
                isPageFinished = false;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                setWebTitle(url);
                //JsInjector.injectGlobalJs(webView);

            }
        };
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView webView, int progressInPercent) {
                super.onProgressChanged(webView, progressInPercent);
                adjustPageFinished(webView, progressInPercent);
            }
        });
        webView.setWebViewClient(webViewClient);
        settingJavascript();
        Map xwalkViewMap = ((GlobalApplication) context.getApplication()).getXWalkViewMap();
        xwalkViewMap.put(templateName + "_" + version, webView);
    }


    private void adjustPageFinished(WebView webView, int progressInPercent) {
        if (progressInPercent >= 100 && !isPageFinished) { //+sPageFinished 防止执行2次
            onPageFinished();
            doOnPageFinished(webView, webView.getUrl());
            Logger.i("isPageFinished = true;");
            isPageFinished = true;
        }
        if (isPageFinished) { //这里有可能会执行2次
            webProgressView2.setVisibility(View.GONE);
        } else {
            webProgressView2.setVisibility(View.VISIBLE);
            webProgressView2.setProgress(progressInPercent);
        }
    }

    private String mapKey, xwalkview_version;

    public void setxWalkView(WebView xWalkView, String mapKey, String xwalkview_version) {
        Logger.i("setxWalkView");
        this.webView = xWalkView;
        needLoad = false;
        this.mapKey = mapKey;
        this.xwalkview_version = xwalkview_version;
        Log.v("ajax", "setxWalkView, url = " + this.webView.getUrl());
        isLoadFinished = true;
    }

    private boolean isSendBroadcast = false;

    /**
     * 模板加载时，ajax方法执行结束，设置标题并关闭等待框
     */
    public void setTitleAjaxUse(String result) {
        if (result.equals("fail")) {
            webErrorView.setVisibility(View.VISIBLE);
            Button retry = (Button) webErrorView.findViewById(R.id.button_try_again);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    webProgressView.setVisibility(View.VISIBLE);
                    webErrorView.setVisibility(View.GONE);
                    invokeJsMethod();
                }
            });
        } else {
            if (null != webProgressView2 && webProgressView2.isShown()) {
                Log.v("ajax", "xwalkview.onshow()");
                webView.resumeTimers();
//                webProgressView.setVisibility(View.GONE);
            }
            if (null != ll_webview && ll_webview.getVisibility() != View.VISIBLE) {
                ll_webview.setVisibility(View.VISIBLE);
            }
            if (null != attachDelegate) { //  模板加载的时候统一设置成网页的标题
                String titleFromXWalkView = webView.getTitle();
                if (StringUtil.isNotEmpty(titleFromXWalkView)
                        && !titleFromXWalkView.contains("mobileproduct_findProductIndex")
                        && !titleFromXWalkView.contains("mobileshop_findProductByShopId")) {
                    attachDelegate.setTitle(webView.getTitle());
                }
            }
            Log.v("ajax", "setTitleAjaxUse, isSendBroadcast = " + isSendBroadcast);
            if (!isSendBroadcast && StringUtil.isNotEmpty(mapKey) && StringUtil.isNotEmpty(xwalkview_version)) {
                isSendBroadcast = true;
                Intent intent = new Intent();
                intent.setAction(Constants.BROADCAST_TYPE_XWALKVIEW);
                intent.putExtra("mapKey", mapKey + "_" + xwalkview_version);
                intent.putExtra(mapKey + "_version", xwalkview_version);
                mActivity.sendBroadcast(intent);
            }
        }
    }

    /**
     * enable xwalkview accept cookie
     */
    private void enableXWalkViewCookie() {
        // TODO: 2020/5/19 允许cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        XWalkCookieManager xWalkCookieManager = new XWalkCookieManager();
//        xWalkCookieManager.setAcceptCookie(true);
//        xWalkCookieManager.setAcceptFileSchemeCookies(true);
    }

    private void stopJsReady() {
        String inject = "da.djJsReady";
        webView.evaluateJavascript(inject, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
               /* if(StringUtil.isNotEmpty(value) && value.equals("true")) {
                }*/
                webView.evaluateJavascript("da.djJsReady=false;web.inDajia=false;", value1 -> {
                    LogHelper.e("js----16>" + value1);
                });
            }
        });
    }

    private boolean checkXWalkViewObserver(Activity context) {
        try {
            Class clazz = Class.forName("org.chromium.base.ApplicationStatus");
            if (clazz != null) {
                Field fe = clazz.getDeclaredField("sActivityInfo");
                fe.setAccessible(true);
                Map<Activity, Object> actiityInfo = (Map<Activity, Object>) fe.get(null);
                if (null == actiityInfo.get(context)) {
                    return true;   //xWalkView监听没有注册成功 不能走下面方法 否则会报错
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void refresh() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(new WebViewUserAgent(mContext) + "");
    }

    public void clearCache(boolean b) {
        GlobalWebManager.clearWebviewCache(webView);
    }
}
