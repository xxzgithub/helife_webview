/**
 *
 */
package com.hilife.webview.bowser_client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;


import com.hilife.webview.R;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.net.cyberwy.hopson.lib_custom_views.toast.ToastUtil;
import cn.net.cyberwy.hopson.lib_util.IntentUtil;
import cn.net.cyberwy.hopson.lib_util.StringUtil;
import cn.net.cyberwy.hopson.lib_util.UrlUtil;
import cn.net.cyberwy.hopson.lib_util.log.Logger;
import cn.net.cyberwy.hopson.lib_util.net.NetUtil;
import cn.net.cyberwy.hopson.sdk_public_base.constant.Constants;

/**
 * @author hewx
 * @version 1.0
 * @since SDK1.6
 */
public class DefaultWebViewClient extends WebViewClient implements OnClickListener {
    private String TAG = "DefaultWebViewClient";

    private Context context;

    private String webUrl;

    private IWebClientListener listener;

    private View webError;

    private Button retry;

    private WebView webview;

    private boolean needNewTab;

    private IRetryClickListener retryClickListener;

    public DefaultWebViewClient() {
    }

    public DefaultWebViewClient(WebView webview, String webUrl, View webError, Context context) {
        this(webview, webUrl, false, null, webError, context);
    }

    public DefaultWebViewClient(WebView webview, IWebClientListener listener, View webError, Context context) {
        this(webview, null, false, listener, webError, context);
    }

    public DefaultWebViewClient(WebView webview, String webUrl, boolean needNewTab, IWebClientListener listener, View webError, Context context) {
        this.webview = webview;
        this.webUrl = webUrl;
        this.needNewTab = needNewTab;
        this.listener = listener;
        this.webError = webError;
        this.context = context;
        if (this.webError != null) {
            retry = (Button) webError.findViewById(R.id.button_try_again);
            retry.setOnClickListener(this);
        }
    }

    private boolean isNotOverride = true;

    @SuppressLint("DefaultLocale")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //打电话
        if (url.toLowerCase().startsWith("tel://")) {
            IntentUtil.openDialIntent(context, url.substring(6));
            return true;
        } else if (url.toLowerCase().startsWith("scan://")) {
            Intent scanIntent = new Intent(context, QrcodeScanActivity.class);
            scanIntent.putExtra("needReturn", "directReturn");//直接返回
            scanIntent.putExtra("no_invite", true);
            scanIntent.putExtra("scanUrlHost", url);
            ((Activity) context).startActivityForResult(scanIntent, Constants.REQUEST_SCAN_WEB);
            return true;
            //扫描二维码打开连接
        } else if (url.toLowerCase().startsWith("tel:")) {
            IntentUtil.openDialIntent(context, url.substring(4));
            return true;
            //发短信
        } else if (url.toLowerCase().startsWith("smsto://")) {
            smsto(url.substring(8));
            return true;
        } else if (url.toLowerCase().startsWith("sms://") || url.toLowerCase().startsWith("smsto:")) {
            smsto(url.substring(6));
            return true;
        } else if (url.toLowerCase().startsWith("sms:")) {
            smsto(url.substring(4));
            return true;
            //发邮件
        } else if (url.toLowerCase().startsWith("mailto://")) {
            mailto(url.substring(9));
            return true;
        } else if (url.toLowerCase().startsWith("mailto:")) {
            mailto(url.substring(7));
            return true;
        } else if (url.toLowerCase().startsWith("djapp")) {
            addOpenID(view, url, context);
            return true;
        } else if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")
                && !url.toLowerCase().startsWith("ftp://") && !url.toLowerCase().startsWith("dajia")
                && !url.toLowerCase().startsWith("djapp") && !url.toLowerCase().startsWith("file://")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        isNotOverride = false;
        //有些机型的浏览器 会在url之后自动加分隔符
        if (webUrl != null && !webUrl.equals(url) && !(webUrl + "/").equals(url)) {
            if (null != listener && !listener.isLoadFinished()) {
                String query = UrlUtil.getQuery(url);
                if (StringUtil.isNotBlank(query) && query.indexOf("openID") == -1
                        && query.indexOf("clientID") != -1) {
                    //有clientID且没有openID就走添加逻辑  否则走正常逻辑
                    addOpenID(view, url, context);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
            if (needNewTab && null != listener) {
                listener.onOpenUrlWithNewTab(url);
                return true;
            }
        }
        if (!webUrl.equals(url) && addOpenID(view, url, context)) {
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (NetUtil.hasNetwork(context) == false && webError != null) {
            webError.setVisibility(View.VISIBLE);
        }
        // 防止当重新加载的时候，将webview隐藏掉，用户无法再将webview调出来
        if (webview != null) {
            webview.setVisibility(View.VISIBLE);
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }


    public interface IWebClientListener {
        void onOpenUrlWithNewTab(String url);

        void windowGoBack();

        boolean isLoadFinished();
    }

    @Override
    public void onClick(View v) {
        if (webview != null) {
            if (webError != null)
                webError.setVisibility(View.GONE);
            webview.setVisibility(View.GONE);
            webview.reload();
        }
        if (retryClickListener != null) {
            retryClickListener.onRetryClicked();
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        //如果你买的是android 2.3.x 并且不幸你的webview的JsEngine的内核是JSC的话，我只能告诉你，你中彩了。。。。。。
        if (isNotOverride && Build.VERSION.SDK_INT == 10) {
            //有些机型的浏览器 会在url之后自动加分隔符
            if (webUrl != null && !webUrl.equals(url) && !(webUrl + "/").equals(url)) {
                if (webview.getHitTestResult() != null) {//重定向
                    if (needNewTab) {
                        listener.onOpenUrlWithNewTab(url);
                        view.stopLoading();
                        return;
                    }
                }
            }
        }
        webUrl = url;
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedSslError(WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
//		super.onReceivedSslError(webView, sslErrorHandler, sslError);
        LogComponent.i(TAG, "---onReceivedSslError: ");
        sslErrorHandler.proceed();
    }
//    @Override
//    public void onPageFinished(WebView webView, String s) {
//        super.onPageFinished(webView, s);
//
//    }

    private static String dajiaJSStr = null;

    public static String readDajiaJJS(Context context) throws IOException {
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

    private static String inputJSStr = null;

    public static String readInputJS(Context context) throws IOException {
        if (inputJSStr == null) {
            //注入android和html交互的基础js
            InputStream in = context.getResources().getAssets().open("js/jdajiaInput.js");
            byte buff[] = new byte[1024];
            ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
            do {
                int numread = in.read(buff);
                if (numread <= 0) break;
                fromFile.write(buff, 0, numread);
            } while (true);
            String js = "var inputscript = document.createElement(\"script\");";
            js += fromFile.toString();
            js += "document.body.appendChild(inputscript);";
            inputJSStr = js;
        }
        return inputJSStr;
    }

    /**
     * URL 解析 发短信
     * @param url
     */
    private void smsto(String url) {
        if (StringUtil.isBlank(url)) {
            ToastUtil.showMessage(context, context.getResources().getString(R.string.default_web_url_error));
            return;
        }
        int size = url.indexOf(":");
        if (size != -1) {
            IntentUtil.smsToSomeOne(context, url.substring(0, size), url.substring(size + 1));
        }
        IntentUtil.smsToSomeOne(context, url, null);
    }

    /**
     * URL 解析 发邮件
     * @param url
     */
    private void mailto(String url) {
        if (StringUtil.isBlank(url)) {
            ToastUtil.showMessage(context, context.getResources().getString(R.string.default_web_email_error));
            return;
        }
        //Mailto:aa1@xx.com;aa2@xx.com?cc=bb@yy.com&bcc=cc@zz.com&subject=主题&body=邮件内容
        String[] mailto = null;    //接受者
        String cc = null;    //抄送
        String bcc = null;    //秘密抄送
        String subject = null;    //主题
        String content = null;    //正文
        if (url.indexOf("?") != -1) {
            String[] urls = url.split("\\?");
            mailto = urls[0].split(";");
            for (String s : urls[1].split("&")) {
                String[] ts = s.split("=");
                if ("cc".equals(ts[0])) {
                    cc = ts[1];
                } else if ("bcc".equals(ts[0])) {
                    bcc = ts[1];
                } else if ("bcc".equals(ts[0])) {
                    bcc = ts[1];
                } else if ("subject".equals(ts[0])) {
                    subject = ts[1];
                } else if ("body".equals(ts[0])) {
                    content = ts[1];
                }
            }
        } else {
            mailto = url.split(";");
        }
        IntentUtil.mailToSomeOne(context, "", mailto, cc, bcc, subject, content);
    }

    public Boolean addOpenID(final WebView view, String webUrl, final Context context) {
        Logger.D(DefaultWebViewClient.class.getSimpleName(), "" + webUrl);
        boolean isPerform = false;
        String query = UrlUtil.getQuery(webUrl);
        if ((StringUtil.isNotBlank(query) && query.indexOf("clientID") != -1)) {
            try {
                String openID = null;
                String clientID = null;
                for (String param : query.split("&")) {
                    if (param.indexOf("clientID") != -1 && param.split("=").length == 2) {
                        clientID = param.split("=")[1];
                    }
                    if (param.indexOf("openID") != -1 && param.split("=").length == 2) {
                        openID = param.split("=")[1];
                    }
                }
                if (StringUtil.isBlank(openID)) {
                    final String personID = DJCacheUtil.readPersonID();
                    //openID = DJCacheUtil.read("openID_"+clientID+"_"+personID);
                    if (StringUtil.isBlank(openID)) {
                        final String webLoc = webUrl;
                        final String cID = clientID;
                        ServiceFactory.getOauthService(context).oauth(
                                DJCacheUtil.readToken(), personID,
                                cID, new DefaultDataCallbackHandler<Void, Void, OpenToken>() {
                                    @Override
                                    public void onSuccess(OpenToken open) {
                                        String url = webLoc;
                                        if (open != null) {
                                            url = "&openID=" + open.getOpenID() + "&dajia_rand=" + open.getDajia_rand() + "&dajia_signature=" + open.getDajia_signature() + "&dajia_timestamp=" + open.getDajia_timestamp() + "&companyID=" + DJCacheUtil.readCommunityID();
                                            //DJCacheUtil.keep("openID_"+cID+"_"+personID, url);
                                            url = webLoc + url;
                                        }
                                        afterAddOpenID(context, view, url);
                                    }

                                    @Override
                                    public void onError(AppException e) {
                                        afterAddOpenID(context, view, webLoc);
                                    }
                                });
                    } else {
                        webUrl += openID;
                        afterAddOpenID(context, view, webUrl);
                    }
                    isPerform = true;
                } else if (webUrl != null && webUrl.startsWith("djapp") && listener != null) {
                    afterAddOpenID(context, view, webUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isPerform;
    }

    private void afterAddOpenID(Context context, WebView view, String webLoc) {
        if (webLoc.startsWith("djapp") && listener != null) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(webLoc);
                intent.setData(content_url);
                context.startActivity(intent);
            } catch (Exception e3) {
                ToastUtil.showMessage(context, "跳转失败，请联系管理员");
            }
            listener.windowGoBack();
            return;
        }
        view.loadUrl(webLoc);
    }

    public interface IRetryClickListener {
        void onRetryClicked();
    }

    public void setRetryClickListener(IRetryClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

}
