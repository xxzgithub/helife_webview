package com.hilife.webview.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.framework.component.onActivityForResult.OnActivityForResultUtils;
import com.hilife.mobile.android.framework.component.onActivityForResult.SimpleOnActivityForResultCallback;
import com.hilife.mobile.android.tools.file.FileUtil;
import com.hilife.view.main.base.BaseFragment;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.other.component.webview.settings.JsInjector;
import com.hilife.view.other.component.webview.model.WebViewUserAgent;
import com.hilife.view.other.component.webview.bowser_client.DefaultWebChromeClient;
import com.hilife.view.other.component.webview.bowser_client.DefaultWebViewClient;
import com.hilife.view.other.component.webview.settings.GlobalWebManager;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.R;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseWebFragment extends BaseFragment {

    protected boolean isPageFinished = false;

    protected WebView webView;
    protected ProgressBar webProgressView;
    protected View webErrorView;
    protected String mTitle;
    protected String webUrl;
    protected Map<String, String> titleMap;

    protected String webTitle;

    protected ValueCallback<Uri> mUploadMessage;
    protected File fileChoose;
    protected String tagName;
    private WebChromeClient wcc;
    private View mCustomView;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

    protected String windowState;

    protected AttachDelegate attachDelegate;
    private ViewGroup rootView;

    protected ValueCallback<Uri[]> fileCallback;

    private boolean isLoadFinished = false; // 页面是否load完成，用于处理重定向的问题
    private String startUrl;

    public BaseWebFragment() {
    }

    @SuppressLint("ValidFragment")
    public BaseWebFragment(AttachDelegate delegate) {
        this.attachDelegate = delegate;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        titleMap = new HashMap<String, String>();
    }

    @Override
    protected void findView() {
        rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        webView = (WebView) findViewById(R.id.webView);
        webErrorView = findViewById(R.id.webErrorView);
        webProgressView = (ProgressBar) findViewById(R.id.webProgressView);
        webView.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hiddenZoomControls(WebSettings webSettings) {
        webSettings.setDisplayZoomControls(false);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void settingWebView() {
        GlobalWebManager.settingWebview(webView, null);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                webView.goBack();
            }
        });
        boolean showOtherWindows = false;
        if (attachDelegate != null && attachDelegate instanceof Fragment) {
            showOtherWindows = true;
        }
        DefaultWebViewClient webViewClient = new DefaultWebViewClient(webView, webUrl, showOtherWindows,
                new DefaultWebViewClient.IWebClientListener() {
                    @Override
                    public void windowGoBack() {
                        goBack();
                    }

                    @Override
                    public void onOpenUrlWithNewTab(String url) {
                        Intent regIntent = new Intent(mContext, WebActivity.class);
                        regIntent.putExtra("category", Constants.WEB_PORTAL);
                        regIntent.putExtra("title", tagName);
                        regIntent.putExtra("tagName", tagName);
                        regIntent.putExtra("web_url", url);
                        startActivity(regIntent);
                    }

                    @Override
                    public boolean isLoadFinished() {
                        return isLoadFinished;
                    }
                }, webErrorView, mContext) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                BaseWebFragment.this.onPageStarted(url);
                webProgressView.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
                startUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        };
        webView.setWebViewClient(webViewClient);
        if (!webViewClient.addOpenID(webView, webUrl, mContext)) {
            webView.loadUrl(webUrl);
        }
        wcc = new DefaultWebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("um-----webConsole", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (tagName == null) {
                    if (attachDelegate != null) {
                        attachDelegate.setTitle(title);
                    }
                }
                String url = view.getUrl();
                titleMap.put(url, webTitle);
                if (!url.equals(title)) {
                    webTitle = title;
                }
                super.onReceivedTitle(view, title);
            }

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

            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (fileCallback != null) {
                    fileCallback.onReceiveValue(null);
                    fileCallback = null;
                }
                fileCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, Constants.REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    fileCallback = null;
                    Toast.makeText(getActivity().getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            private View mVideoProgressView;
            private Bitmap mDefaultVideoPoster;

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
                // if a view already exists then immediately terminate the new one
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                //Log.i(LOGTAG, "here in on ShowCustomView");
                webView.setVisibility(View.GONE);
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                rootView.addView(view);
                mCustomView = view;
                mCustomView.setBackgroundResource(android.R.color.background_dark);
                mCustomViewCallback = callback;
            }

            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onHideCustomView() {
                if (mCustomView == null)
                    return;
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                // Remove the custom view from its container.
                rootView.removeView(mCustomView);
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();
                webView.setVisibility(View.VISIBLE);

            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory.decodeResource(
                            getResources(), R.drawable.default_video_poster);
                }
                return mDefaultVideoPoster;
            }

            @Override
            public View getVideoLoadingProgressView() {
                if (mVideoProgressView == null) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
                }
                return mVideoProgressView;
            }

            @Override
            public void onCloseWindow(WebView window) {
                mActivity.finish();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog,
                                          boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //todo  需要测试一下
                if (newProgress >= 100) { //这里有可能会执行2次
                    if (!isPageFinished) { // 确保只执行一次
                        doOnPageFinished(view);
                    }
                    isPageFinished = true;

                } else {
                    isPageFinished = false;
                }
            }
        };
        webView.setWebChromeClient(wcc);
        settingJavascript();

    }

    private void doOnPageFinished(WebView view) {
        Log.d("xxz","1111"+"BaseWeb");
        JsInjector.injectGlobalJs(view);
        isLoadFinished = true;
        if (attachDelegate != null) {
            if (webView.canGoBack()) {
                attachDelegate.setTitle(webTitle);
            } else {
                if (StringUtil.isNotEmpty(webTitle)) {
                    attachDelegate.setTitle(webTitle);
                } else {
                    attachDelegate.setTitle(mTitle);
                    webTitle = mTitle;
                }
            }
        }
        BaseWebFragment.this.onPageFinished();
        webView.setVisibility(View.VISIBLE);
        webProgressView.setVisibility(View.GONE);
        if (startUrl != null) {
            String js = getJs(startUrl);
            view.evaluateJavascript(js, null);
        }

    }

    public class JsObject {
        @JavascriptInterface
        public void fullscreen() {
            //监听到用户点击全屏按钮
            fullScreen();
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private String getTagByUrl(String url) {
        if (url.contains("qq")) {
            return "tvp_fullscreen_button"; // http://m.v.qq.com
        } else if (url.contains("youku")) {
            return "x-zoomin";              // http://www.youku.com
        } else if (url.contains("bilibili")) {
            return "icon-widescreen";       // http://www.bilibili.com/mobile/index.html
        } else if (url.contains("acfun")) {
            return "controller-btn-fullscreen"; //http://m.acfun.tv   无效
        } else if (url.contains("le")) {
            return "hv_ico_screen";         // http://m.le.com  无效
        }
        return "";
    }

    //  "javascript:document.getElementsByClassName('" + referParser(url) + "')[0].addEventListener('click',function(){local_obj.playing();return false;});"
    private String getJs(String url) {
        String tag = getTagByUrl(url);
        if (StringUtil.isEmpty(tag)) {
            return "javascript:";
        } else {
            return "javascript:document.getElementsByClassName('" + tag + "')[0].addEventListener('click',function(){onClick.fullscreen();return false;});";
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

    public void reload() {
        webView.reload();
    }

    @Override
    public boolean goBack() {
        if ("onProgress".equals(windowState)) {
            webView.loadUrl("javascript:web.historyBack()");
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
        }
        settingWebView();
        if (attachDelegate != null) {
            attachDelegate.setTitle(mTitle);
        }
    }

    @Override
    public void onResume() {
        resumeWebviewTimer(webView);
        super.onResume();
    }

    @Override
    public void onPause() {
        pauseWebviewTimer(webView);
        super.onPause();
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == Constants.REQUEST_SELECT_FILE) {
                if (fileCallback == null)
                    return;
                Uri[] res = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                if (res == null && fileChoose != null) {
                    res = new Uri[]{Uri.fromFile(fileChoose)};
                }
                fileCallback.onReceiveValue(res);
                fileCallback = null;
            }
        } else if (requestCode == Constants.CHOOSER_FILE) {
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
        }
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            destroyWebView(webView);
        }
        super.onDestroy();
    }

    private void destroyWebView(WebView wv) {
        GlobalWebManager.clearWebviewCache(wv);
        GlobalWebManager.destroyWebView(wv);

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

    public void refresh() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(new WebViewUserAgent(mContext) + "");
    }

}
