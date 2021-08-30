package com.hilife.webview.bowser_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hilife.webview.R;
import com.hilife.webview.settings.GlobalWebManager;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import cn.net.cyberwy.hopson.lib_custom_views.toast.ToastUtil;
import cn.net.cyberwy.hopson.lib_util.IntentUtil;
import cn.net.cyberwy.hopson.lib_util.StringUtil;
import cn.net.cyberwy.hopson.lib_util.UrlUtil;
import cn.net.cyberwy.hopson.lib_util.log.Logger;
import cn.net.cyberwy.hopson.lib_util.net.NetUtil;
import cn.net.cyberwy.hopson.sdk_public_base.constant.Constants;

/**
 * 对应WebView的WebViewClient
 * Created by huteng on 16/5/27.
 */
public class DefaultXwalkViewClient extends WebViewClient implements View.OnClickListener {

    private String TAG = "DefaultXwalkViewClient";
    private static String dajiaJSStr = null;
    private Context context;
    private String webUrl;
    private IWebClientListener listener;
    private View webError;
    private Button retry;
    private WebView xWalkView;
    private boolean needNewTab;
    private IRetryClickListener retryClickListener;
    private boolean isNotOverride = true;

    public DefaultXwalkViewClient(WebView xWalkView, String webUrl, boolean needNewTab, IWebClientListener listener, View webError, Context context) {
        this.xWalkView = xWalkView;
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

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i(TAG, "shouldOverrideUrlLoading: ");
        if (GlobalWebManager.processLcgUserAgent(view, url)) {
            return true;
        }
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
        } else if (url.toLowerCase().startsWith("weixin://")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
            return true;
        } else if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://") && !url.toLowerCase().startsWith("ftp://") && !url.toLowerCase().startsWith("dajia") && !url.toLowerCase().startsWith("djapp") && !url.toLowerCase().startsWith("file:///")) {
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
            if (!listener.isLoadFinished()) {
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
            if (needNewTab) {
                if (url.startsWith("file:///")) {
                    url = url.replaceFirst("file://", Configuration.getWebHost(context));
                }
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
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        if (NetUtil.hasNetwork(context) == false && webError != null) {
            webError.setVisibility(View.VISIBLE);
        }
        // 防止当重新加载的时候，将webview隐藏掉，用户无法再将webview调出来
        if (xWalkView != null) {
            xWalkView.setVisibility(View.VISIBLE);
        }
        super.onReceivedError(webView, errorCode, description, failingUrl);
    }

    @Override
    public void onClick(View v) {
        if (xWalkView != null) {
            if (webError != null)
                webError.setVisibility(View.GONE);
            xWalkView.setVisibility(View.VISIBLE);
            xWalkView.reload();
        }
        if (retryClickListener != null) {
            retryClickListener.onRetryClicked();
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (isNotOverride && Build.VERSION.SDK_INT == 10) {
            //有些机型的浏览器 会在url之后自动加分隔符
            if (webUrl != null && !webUrl.equals(url) && !(webUrl + "/").equals(url)) {
//                if(xWalkView.getHitTestResult()!= null ){//重定向
//                    if (needNewTab) {
//                        listener.onOpenUrlWithNewTab(url);
//                        view.stopLoading();
//                        return;
//                    }
//                }
                if (view.getHitTestResult() != null) {
                    if (needNewTab) {
                        listener.onOpenUrlWithNewTab(url);
                        view.stopLoading();
                        return;
                    }
                }
            }
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
//        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        sslErrorHandler.proceed();

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

    }

    /**
     * URL 解析 发短信
     *
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
     *
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
                    //openID = DJCacheUtil.read("openID_" + clientID + "_" + personID);
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
                                            //DJCacheUtil.keep("openID_" + cID + "_" + personID, url);  //不使用缓存了 实时取
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
        webUrl = webLoc;  //加载url和urlencode后的保持一致
        view.loadUrl(webLoc);
    }

    public void setRetryClickListener(IRetryClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

    public interface IWebClientListener {
        void onOpenUrlWithNewTab(String url);

        void windowGoBack();

        boolean isLoadFinished();
    }

    public interface IRetryClickListener {
        void onRetryClicked();
    }
}
