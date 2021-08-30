package com.hilife.webview.ui.xwalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.framework.handler.def.DefaultDataCallbackHandler;
import com.dajia.mobile.esn.model.common.MReturnData;
import com.dajia.mobile.esn.model.community.MCommunity;
import com.hilife.view.feed.model.TransformCard;
import com.hilife.view.feed.ui.NewActivity;
import com.hilife.view.main.service.ServiceFactory;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.other.component.webview.ui.InputActivity;
import com.hilife.view.other.component.webview.ui.WebActivity;
import com.hilife.view.weight.Configuration;
import com.hilife.view.main.util.ShareManager;
import com.hilife.view.me.ui.PersonFragment;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.mobile.android.tools.ToastUtil;
import com.hilife.view.other.component.net.UrlUtil;
import com.hilife.view.other.component.webview.base.impl.BasicDJJavascript;
import com.hilife.view.other.component.webview.base.impl.BasicJavaScript;
import com.hilife.view.other.component.webview.model.TipoffParam;
import com.hilife.view.other.component.webview.model.js.JSPaymentParam;
import com.hilife.view.other.component.webview.model.js.JSReturnParam;
import com.hilife.view.other.component.webview.model.js.JSShowOptMenuParam;
import com.hilife.view.other.component.webview.model.js.JSWindowStateParam;
import com.hilife.view.other.component.webview.model.js.PromptParam;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.IntentUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.pay.model.PayResult;
import com.hilife.view.share.model.ShareMessage;
import com.hilife.view.share.ui.ShareUtils;
import com.hilife.view.R;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

import java.util.HashSet;
import java.util.Set;

/*
 * NOTE:
 * WebView中包含一个ZoomButtonsController，当使用web.getSettings().setBuiltInZoomControls(true);
 * 启用该设置后，用户一旦触摸屏幕，就会出现缩放控制图标。这个图标过上几秒会自动消失，
 * 但在3.0系统以上上，如果图标自动消失前退出当前Activity的话，就会发生ZoomButton找不到依附的Window而造成程序崩溃，
 * 解决办法很简单就是在Activity的ondestory方法中调用web.setVisibility(View.GONE);
 * 方法，手动将其隐藏，就不会崩溃了。
 * 在3.0一下系统上不会出现该崩溃问题，真是各种崩溃，防不胜防啊！
 */

/**
 * 浏览器界面、接受其他界面需要打开浏览器的请求
 *
 * @author hewx
 * title 浏览器标题
 * category 入口
 * Constants.WEB_REGISTER 注册新账号
 * Constants.WEB_FORGET 忘记密码
 * Constants.WEB_ALTER 修改密码
 * Constants.WEB_PROTOCOL 大家协议
 * Constants.WEB_NOSUBMIT 免登陆试用
 * Constants.WEB_FEEDBACK 咨询与反馈
 * Constants.WEB_PERSON 编辑资料
 * Constants.WEB_PORTAL 门户
 * Constants.WEB_FORM 表单
 * Constants.WEB_TIPOFF 举报
 * Constants.WEB_BIND_PHONE 手机绑定
 * web_url 需要打开的链接
 */
public class PayWebFragment extends BaseXwalkFragment implements ShareUtils.IShare {
    private int category;
    /**
     * 举报需要拼装的字符串
     */
    private String personJS;
    private String formOrderID;
    private String paymentType;
    private String jsCallbackId;
    private String companyInfoID;
    private boolean isSupportShare = false;
    private boolean needSendBroadcast;
    private BasicJavaScript injsJavascript;
    private String sourceID;
    private String sourceType;

    public PayWebFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public PayWebFragment(AttachDelegate attachDelegate) {
        super(attachDelegate);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 9: {
                    //js交互回调
                    JSReturnParam param = (JSReturnParam) msg.obj;
                    if (param != null && !param.isEmpty()) {
                        webView.evaluateJavascript(param.toString(), null);
                    }
                }
                break;
                case 11: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ServiceFactory.getPaymentService(mContext).confirmPayment(DJCacheUtil.readCommunityID(),
                                formOrderID, paymentType, new DefaultDataCallbackHandler<Void, Void, MReturnData<String>>(errorHandler) {
                                    @Override
                                    public void onSuccess(MReturnData<String> result) {
                                        if (result.isSuccess()) {
                                            DJToastUtil.showMessage(mContext, "支付成功");
                                        } else {
                                            DJToastUtil.showMessage(mContext, "支付确认失败");
                                        }
                                        super.onSuccess(result);
                                    }

                                    @Override
                                    public void onHandleFinal() {
                                        JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                                        webView.evaluateJavascript(param.toString(), null);
                                        super.onHandleFinal();
                                    }
                                });
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DJToastUtil.showMessage(mContext, "支付结果确认中");
                            JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            DJToastUtil.showMessage(mContext, "您已取消支付");
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            DJToastUtil.showMessage(mContext, "网络出错，请稍后再试");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            DJToastUtil.showMessage(mContext, "支付失败");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        }
                    }
                }
                break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    private ShareUtils shareUtils;

    @Override
    protected int getContentView() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        shareUtils = new ShareUtils(mContext);
        shareUtils.setShareListenr(this);
        if (StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
            shareUtils.isSupportForward = false;
        } else {
            shareUtils.isSupportForward = true;
        }
        shareUtils.isSupportBrowser = true;
        shareUtils.isSuppertRefresh = true;
    }

    @Override
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void settingJavascript() {
        injsJavascript = new BasicJavaScript(mActivity) {
            @JavascriptInterface
            public void onInputFoucs(String inputID, String type, String inputValue) {
                jsCallbackId = inputID;
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("inputValue", inputValue);
                attachDelegate.startActivityForResult(intent, Constants.REQUEST_WEBVIEW_INPUT);
            }
        };
        webView.addJavascriptInterface(injsJavascript, "injs");
        webView.addJavascriptInterface(new BasicDJJavascript(mActivity, webView) {

            @Override
            public void historyBackIn() {
                goBack();
            }

            @Override
            public void showPromptIn(PromptParam paramObj) {
                needSendBroadcast = false;
                if (Constants.CALLBACK_PROPMT_SERVICEFORM == paramObj.getType()) {
                    needSendBroadcast = true;
                } else if (Constants.CALLBACK_PROPMT_STARTPAGE == paramObj.getType()) {
                    if (Constants.WEB_PERSON == category) {
                        DJToastUtil.showImageToast(mContext, mContext.getResources().getString(R.string.web_save_success), R.drawable.prompt_success, Toast.LENGTH_SHORT);
                        mActivity.setResult(PersonFragment.PERSON_EDIT_SUCCESS);
                        mActivity.finish();
                    } else if (Constants.WEB_BIND_PHONE == category) {
                        DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_bind_success));
                        mActivity.setResult(PersonFragment.PERSON_BIND_PHONE);
                        mActivity.finish();
                    } else if (Constants.WEB_COLLECT == category) {
                        MCommunity community = (MCommunity) mActivity.getIntent().getSerializableExtra("community");
                        Intent data = new Intent();
                        if (null != community) {
                            DJCacheUtil.keepInt(mContext, community.getcID() + "filledcol", 1);
                            data.putExtra("community", community);
                        }
                        mActivity.setResult(Constants.RESULT_JOIN_COMMUNITY, data);
                        mActivity.finish();
                    }
                }
                super.showPromptIn(paramObj);
            }

            @Override
            public void statusWindowIn(JSWindowStateParam paramObj) {
                if (paramObj != null) {
                    windowState = paramObj.getType();
                }
            }

            @Override
            public void payCheckIn(JSPaymentParam param) {
                if (param != null) {
                    formOrderID = param.getOrderID();
                    paymentType = param.getPaymentType();
                    jsCallbackId = param.getCallbackId();
                    companyInfoID = param.getCompanyID();
                    ServiceFactory.getPaymentService(mContext).checkPayment(DJCacheUtil.readCommunityID(),
                            param.getSubject(), param.getDesc(), formOrderID, param.getMerchantId(),
                            param.getOrderAmount(), param.getPaymentType(), companyInfoID, new DefaultDataCallbackHandler<Void, Void, MReturnData<String>>(errorHandler) {
                                @Override
                                public void onSuccess(MReturnData<String> result) {
                                    //开始支付
                                    if (result != null) {
                                        if (result.isSuccess()) {
                                            final String content = result.getContent();
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(mActivity);
                                                    // 调用支付接口，获取支付结果
                                                    String result = alipay.pay(content, true);
                                                    Message msg = new Message();
                                                    msg.what = 11;
                                                    msg.obj = result;
                                                    handler.sendMessage(msg);
                                                }
                                            };
                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                            super.onSuccess(result);
                                        } else {
                                            if (result.getFailType() == -1001) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_inexistence));
                                            } else if (result.getFailType() == -1002) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_go_pay, DJCacheUtil.read(DJCacheUtil.COMMUNITY_NAME)));
                                            } else if (result.getFailType() == -1003) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_pay_closed));
                                            } else if (result.getFailType() == -1004) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_community_closed));
                                            } else if (result.getFailType() == -1010 || result.getFailType() == 1004 || result.getFailType() == 1005 || result.getFailType() == 2001) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_repetition));
                                            } else if (result.getFailType() == -1011) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_amount_differ));
                                            } else if (result.getFailType() == 1002) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_pay_overtime));
                                            } else if (result.getFailType() == 1003) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_deobligation));
                                            } else if (result.getFailType() == 2002) {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_failed_waite));
                                            } else {
                                                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_failed) + "(" + result.getFailType() + ")：" + StringUtil.filterNull(result.getContent()));
                                            }
                                        }
                                    } else {
                                        DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_payment_failed_waite));
                                    }
                                    super.onSuccess(result);
                                }
                            });
                }
            }

            @Override
            public void setupSupportMethod() {
                super.setupSupportMethod();
                Set<String> set = new HashSet<String>();
                set.add("showPrompt");
                set.add("closeWindow");
                set.add("historyBack");
                set.add("statusWindow");
                set.add("payCheck");
                addSupportMethods(set);
            }
        }, "dj");
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClickEvent(View view) {
    }

    public void clickRight() {
        if (Configuration.ExpState.ExpStateNo != Configuration.getMISEXP(mContext)) {
            DJToastUtil.showHintToast(mContext, mContext.getResources().getString(R.string.prompt_nologin));
            return;
        }
        String url = webView.getUrl();
        if (!StringUtil.isBlank(url)) {
            shareUtils.showShare(attachDelegate, null);
        } else {
            ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_no_share));
        }
    }

    public void reload() {
        webView.reload(); // XWalkView.RELOAD_IGNORE_CACHE
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        if (attachDelegate instanceof Fragment) {
            category = getArguments().getInt("category", -1);
            sourceID = getArguments().getString("sourceID");
            sourceType = getArguments().getString("sourceType");
        } else {
            Intent mIntent = mActivity.getIntent();
            sourceID = mIntent.getStringExtra("sourceID");
            sourceType = mIntent.getStringExtra("sourceType");
            category = mIntent.getIntExtra("category", -1);
        }
        if (StringUtil.isEmpty(sourceID) || StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
            shareUtils.isSupportTipoff = false;
        } else {
            shareUtils.isSupportTipoff = true;
        }
        switch (category) {
            case -1:
            case Constants.WEB_PORTAL:
            case Constants.WEB_FORM:
            case Constants.WEB_THEME_FORM:
                if (attachDelegate != null) {
                    attachDelegate.setAttachRightVisiable(View.VISIBLE);
                }
                isSupportShare = true;
                break;
            case Constants.WEB_SHOP:
            case Constants.WEB_COLLECT:
                if (mActivity.getIntent().getBooleanExtra("canSkip", false) && attachDelegate != null) {
                    attachDelegate.setAttachRightVisiable(View.VISIBLE);
                }
                break;
            case Constants.WEB_REGISTER:
            case Constants.WEB_FORGET:
            case Constants.WEB_ALTER:
            case Constants.WEB_PROTOCOL:
            case Constants.WEB_FEEDBACK:
            case Constants.WEB_PERSON:
            case Constants.WEB_SERVICEFORM:
                break;
            case Constants.WEB_TIPOFF:
                TipoffParam tipoff = (TipoffParam) mActivity.getIntent().getSerializableExtra("tipoff");
                if (tipoff != null) {
                    personJS = new Gson().toJson(tipoff).replaceAll("\"", "'");
                }
                break;
            case Constants.WEB_COMMUNITY_TEMPLATE:
            case Constants.WEB_COMMUNITY_TEMPLATE_NO_SUBMIT:
                break;
            default:
                break;
        }
        if (StringUtil.isEmpty(webUrl)) {
            shareUtils.isSuppertRefresh = false;
        }
    }

    @Override
    public void onResume() {
//		webView.onResume();
        resumeWebviewTimer(webView);
        super.onResume();
    }

    @Override
    public void onPause() {
//		webView.onPause();
        pauseWebviewTimer(webView);
        super.onPause();
    }

    private void confirmBack() {
        showConfirmPrompt(mContext.getResources().getString(R.string.alert_title_propmpt), mContext.getResources().getString(R.string.prompt_give_up_editoria), mContext.getResources().getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                    }
                }, mContext.getResources().getString(R.string.btn_submit), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        mActivity.finish();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        shareUtils.onActivityResult(requestCode, resultCode, data);
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
        } else if (requestCode == Constants.REQUEST_WEBVIEW_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                String text = data.getStringExtra("text");
                if (StringUtil.isNotEmpty(text)) {
                    webView.evaluateJavascript("javascript:_backfill('" + jsCallbackId + "','" + text.replace("'", "\\'") + "')", null);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            destroyWebView(webView);
        }
        super.onDestroy();
        if (category == Constants.WEB_SERVICEFORM && needSendBroadcast) {
            Intent intent = new Intent();
            intent.setAction(Constants.BROADCAST_TYPE_SERVICEFORM);
            mContext.sendBroadcast(intent);
        }

    }

    private void destroyWebView(WebView wv) {
        try {
            wv.stopLoading();
//			wv.pauseTimers();
            wv.clearFormData();
            wv.clearAnimation();
            wv.clearDisappearingChildren();
            wv.loadUrl("about:blank");
//			wv.evaluateJavascript("about:blank",null);
//			wv.clearCache(true);
//			wv.clearHistory();
            wv.clearHistory();
//			wv.clearMatches();
//			wv.clearSslPreferences();
            wv.destroyDrawingCache();
            wv.removeAllViews();
//			wv.destroy();
            wv.destroy();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getShareMessageForWeb(ShareUtils shareUtils, int platformType, JSShowOptMenuParam menuParam) {
    }

    @Override
    public void getShareMessage(final ShareUtils shareUtils, final int platformType) {
        final String title = tagName != null ? tagName : (StringUtil.isBlank(webTitle) ? mContext.getResources().getString(R.string.btn_share) : webTitle);
        final String desc = StringUtil.isBlank(injsJavascript.getWebDesc()) ? Configuration.getWebHost(mContext) : injsJavascript.getWebDesc();
        String href = (shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getAddress())) ? Configuration.getWebShortChain(mContext) + shareUtils.webParam.getAddress() : webView.getUrl();
        if (StringUtil.isBlank(href)) {
            DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_url_error));
            return;
        }
        if (href.indexOf("?") != -1) {
            href += "&openShareSource=dajia";
        } else {
            href += "?openShareSource=dajia";
        }
        final String url = href;
        String accessToken = DJCacheUtil.readToken();
        String communityID = DJCacheUtil.readCommunityID();
        String logo = null;
        if (shareUtils.webParam != null && !StringUtil.isBlank(shareUtils.webParam.getPicID())) {
            logo = shareUtils.webParam.getPicID();
        }
        if (StringUtil.isBlank(logo) && shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getLogoID())) {
            logo = shareUtils.webParam.getLogoID();
        }
        progressShow(mContext.getResources().getString(R.string.processing_waiting), true);
        ShareManager shareManager = new ShareManager();
        shareManager.sharePortalWeixin(mContext, accessToken, communityID, UrlUtil.getPictureDownloadUrl(communityID, logo, 1), new ShareManager.OnShareListener() {

            @Override
            public void onPrepare(String wxURL, String wxTitle, String wxDescription,
                                  Bitmap wxBitmap, String wxImageUrl) {
                progressHide();
                ShareMessage shareMessage = new ShareMessage();
                shareMessage.setTitle(title);
                //朋友圈中，表单、官网、外部链接取描述内容
                if (platformType == 2
                        && category == Constants.WEB_PORTAL) {//朋友圈
                    shareMessage.setTitle(desc);
                } else {
                    shareMessage.setTitle(title);
                }
                shareMessage.setDescription(desc);
                shareMessage.setUrl(url);
                shareMessage.setImageUrl(wxImageUrl);
                shareMessage.setWxType("webpage");
                shareUtils.shareMessage(wxBitmap, shareMessage);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                progressHide();
                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_share_error));
            }
        });

    }

    @Override
    public void shareOperate(int type) {
        switch (type) {
            case ShareUtils.RESULT_TIPOFF:
                Intent tipoffIntent = new Intent(mContext, WebActivity.class);
                tipoffIntent.putExtra("category", Constants.WEB_TIPOFF);
                tipoffIntent.putExtra("web_url", Configuration.getWebUrl(mContext, R.string.url_gototipoff));
                tipoffIntent.putExtra("title", getResources().getString(R.string.btn_tipoff));
                tipoffIntent.putExtra("activityType", "web");
                TipoffParam tipoffParam = new TipoffParam();
                tipoffParam.setTipoffedMsgID(StringUtil.isEmpty(injsJavascript.getSourceID()) ? sourceID : injsJavascript.getSourceID());
                tipoffParam.setTipoffedMsgType(StringUtil.isEmpty(injsJavascript.getSourceType()) ? sourceType : injsJavascript.getSourceType());
                tipoffIntent.putExtra("tipoff", tipoffParam);
                startActivity(tipoffIntent);
                break;
            case ShareUtils.RESULT_TRANFORM:
                Intent newIntent = new Intent(mContext, NewActivity.class);
                TransformCard transformCard = new TransformCard();
                transformCard.setType(TransformCard.TRANSFORM_WEB);
                String logo = null;
                if (shareUtils.webParam != null && !StringUtil.isBlank(shareUtils.webParam.getPicID())) {
                    logo = shareUtils.webParam.getPicID();
                }
                if (StringUtil.isBlank(logo) && shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getLogoID())) {
                    logo = shareUtils.webParam.getLogoID();
                }
                transformCard.setPic(logo);
                transformCard.setTitle(webTitle);
                final String desc = StringUtil.isEmpty(injsJavascript.getWebDesc()) ? Configuration.getWebHost(mContext) : injsJavascript.getWebDesc();
                transformCard.setDesc(desc);
                transformCard.setData(webView.getUrl());
                newIntent.putExtra("from", Constants.NEW_FROM_SHARE);
                newIntent.putExtra("transformCard", transformCard);
                startActivity(newIntent);
                break;
            case ShareUtils.RESULT_REFRESH:
                String url = webView.getUrl();
                if (!StringUtil.isBlank(url)) {
//					webView.loadDataWithBaseURL(null, "","text/html", "utf-8",null);
                    webView.loadUrl(url, null);
                }
                break;
            case ShareUtils.RESULT_BROWSER:
                String currentUrl = webView.getUrl();
                if (!StringUtil.isBlank(currentUrl)) {
                    IntentUtil.openWeb(mContext, currentUrl);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public String getPageID() {
        return Constants.MONITOR_PAGE_WEB;
    }

    @Override
    public String getTagName() {
        switch (category) {
            case Constants.WEB_REGISTER:
            case Constants.WEB_FORGET:
            case Constants.WEB_ALTER:
            case Constants.WEB_PROTOCOL:
            case Constants.WEB_FEEDBACK:
            case Constants.WEB_PERSON:
                return Constants.MONITOR_TAG_WEB_REG;
            case Constants.WEB_FORM:
                return Constants.MONITOR_TAG_WEB_FORM;
            case Constants.WEB_PORTAL:
            case Constants.WEB_TIPOFF:// 举报
            default:
                break;
        }
        return super.getTagName();
    }

    @Override
    public void onPageStarted(String url) {
        if (isSupportShare && attachDelegate != null) {
            attachDelegate.setAttachRightEnable(false);
        }
        shareUtils.webParam = null;
        super.onPageStarted(url);
    }

    @Override
    public void onPageFinished() {
        if (isSupportShare && attachDelegate != null) {
            attachDelegate.setAttachRightEnable(true);
        }
        //普通网页 和 表单页面 和  门户中网页链接 的分享
        if (category == Constants.WEB_PORTAL || category == Constants.WEB_FORM || category == Constants.WEB_THEME_FORM || category == -1) {
            //注入js获取网页内容
            webView.evaluateJavascript("javascript:try{window.injs.getWebDesc(document.getElementsByName('description')[0].content);}catch(e){window.injs.getWebDesc('');}", null);
            webView.evaluateJavascript("javascript:try{window.injs.getWebParam(document.getElementsByName('dajiaAppParamJson')[0].text);}catch(e){window.injs.getWebParam('');}", null);
        }
        webView.evaluateJavascript("javascript:try{window.injs.setTipoffID(document.getElementsByName('sourceID')[0].text);}catch(e){window.injs.setTipoffID('');}", null);
        webView.evaluateJavascript("javascript:try{window.injs.setTipoffType(document.getElementsByName('sourceType')[0].text);}catch(e){window.injs.setTipoffType('');}", null);
        if (category == Constants.WEB_TIPOFF) {
            webView.evaluateJavascript("javascript:initData(" + personJS + ")", null);
        }
        super.onPageFinished();
    }
}
