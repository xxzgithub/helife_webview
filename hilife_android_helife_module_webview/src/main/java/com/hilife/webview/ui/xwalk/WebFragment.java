package com.hilife.webview.ui.xwalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.dajia.android.base.exception.AppException;
import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;
import com.dajia.mobile.esn.im.util.IMUserUtil;
import com.dajia.mobile.esn.model.command.MAction;
import com.dajia.mobile.esn.model.command.MCommand;
import com.dajia.mobile.esn.model.common.MReturnData;
import com.dajia.mobile.esn.model.community.MCommunity;
import com.dajia.mobile.esn.model.groupInfo.MGroup;
import com.dajia.mobile.esn.model.personInfo.MContactPerson;
import com.dajia.mobile.esn.model.personInfo.MPersonCard;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hilife.mobile.android.base.cache.CacheAppData;
import com.hilife.mobile.android.base.constant.BaseConstant;
import com.hilife.mobile.android.framework.activity.BaseActivity;
import com.hilife.mobile.android.framework.component.imageloader.ImageLoader;
import com.hilife.mobile.android.framework.component.onActivityForResult.OnActivityForResultUtils;
import com.hilife.mobile.android.framework.component.onActivityForResult.SimpleOnActivityForResultCallback;
import com.hilife.mobile.android.framework.handler.DataCallbackHandler;
import com.hilife.mobile.android.framework.handler.def.DefaultDataCallbackHandler;
import com.hilife.mobile.android.framework.model.oauth.OpenToken;
import com.hilife.mobile.android.tools.ToastUtil;
import com.hilife.mobile.android.tools.log.Logger;
import com.hilife.pay.alipay.Alipay;
import com.hilife.pay.weixin.WXPay;
import com.hilife.view.BuildConfig;
import com.hilife.view.R;
import com.hilife.view.app.model.PresetMenu;
import com.hilife.view.feed.model.TransformCard;
import com.hilife.view.feed.ui.DetailActivity;
import com.hilife.view.feed.ui.NewActivity;
import com.hilife.view.feed.ui.ScopeOptionActivity;
import com.hilife.view.login.ui.LoginActivity;
import com.hilife.view.main.model.RetureObject;
import com.hilife.view.main.service.ServiceFactory;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.main.ui.MainActivity;
import com.hilife.view.main.util.ShareManager;
import com.hilife.view.main.util.VisitorCommunityUtil;
import com.hilife.view.me.model.StewardListBean;
import com.hilife.view.me.ui.PersonFragment;
import com.hilife.view.other.bluetooth.BluetoothLeService;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.view.other.component.net.UrlUtil;
import com.hilife.view.other.component.qrcode.listener.IProcessScanListener;
import com.hilife.view.other.component.qrcode.ui.QrcodeScanActivity;
import com.hilife.view.other.component.webview.base.impl.BasicDJJavascript;
import com.hilife.view.other.component.webview.base.impl.BasicJavaScript;
import com.hilife.view.other.component.webview.model.TipoffParam;
import com.hilife.view.other.component.webview.model.js.BaseJSParam;
import com.hilife.view.other.component.webview.model.js.JSConnectDeviceParam;
import com.hilife.view.other.component.webview.model.js.JSEnterExperienceParam;
import com.hilife.view.other.component.webview.model.js.JSFeedParam;
import com.hilife.view.other.component.webview.model.js.JSHilifepayWxParam;
import com.hilife.view.other.component.webview.model.js.JSLoginParam;
import com.hilife.view.other.component.webview.model.js.JSNeedLoginForThirdParam;
import com.hilife.view.other.component.webview.model.js.JSNeedLoginParam;
import com.hilife.view.other.component.webview.model.js.JSPaymentParam;
import com.hilife.view.other.component.webview.model.js.JSPicDownloadParam;
import com.hilife.view.other.component.webview.model.js.JSPingpayParam;
import com.hilife.view.other.component.webview.model.js.JSProductParam;
import com.hilife.view.other.component.webview.model.js.JSReturnParam;
import com.hilife.view.other.component.webview.model.js.JSScanBluetoothDeviceParam;
import com.hilife.view.other.component.webview.model.js.JSShowOptMenuParam;
import com.hilife.view.other.component.webview.model.js.JSShowScanParam;
import com.hilife.view.other.component.webview.model.js.JSStartCustomServerParam;
import com.hilife.view.other.component.webview.model.js.JSStartIMConversationParam;
import com.hilife.view.other.component.webview.model.js.JSTopicParam;
import com.hilife.view.other.component.webview.model.js.JSWindowStateParam;
import com.hilife.view.other.component.webview.model.js.PromptParam;
import com.hilife.view.other.component.webview.model.js.ShareInfo;
import com.hilife.view.other.component.webview.settings.GlobalWebManager;
import com.hilife.view.other.component.webview.ui.InputActivity;
import com.hilife.view.other.component.webview.ui.WebActivity;
import com.hilife.view.other.libs.asset.AssetConstants;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.ImageUtil;
import com.hilife.view.other.util.IntentUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.pay.model.MProduct4Qy;
import com.hilife.view.pay.model.PayResult;
import com.hilife.view.pay.util.IWXPayListener;
import com.hilife.view.share.model.ShareBean;
import com.hilife.view.share.model.ShareMessage;
import com.hilife.view.share.tools.ShareMessegeEvent;
import com.hilife.view.share.ui.ShareUtils;
import com.hilife.view.utils.NormalUtils;
import com.hilife.view.weight.Configuration;
import com.hilife.view.weight.dialog.SharePosterDialog;
import com.hopson.hilife.commonbase.util.LogHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.pingplusplus.android.Pingpp;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.net.cyberway.hosponlife.main.wxapi.WXPayEntryActivity;
import cn.net.cyberwy.hopson.sdk_public_base_service.module_msg.ModuleMsgService;
import cn.net.cyberwy.hopson.sdk_public_base_service.module_msg.bean.ModuleMsgMsgDataBeean;
import cn.net.cyberwy.hopson.sdk_public_base_service.router.RouterHub;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import timber.log.Timber;
//import com.hilife.view.step.service.StepService;

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
public class WebFragment extends BaseXwalkFragment implements ShareUtils.IShare, IProcessScanListener {

    PayReq req;
    IWXAPI wxapi;
    private int category;
    /**
     * 举报需要拼装的字符串
     */
    private String personJS;
    private String formOrderID;
    private String paymentType;
    private String companyInfoID;
    private String jsCallbackId;
    private boolean isSupportShare = false;
    private boolean needSendBroadcast;
    private BasicJavaScript injsJavascript;
    private String sourceID;
    private String sourceType;
    private String clientID;
    private JSShowOptMenuParam optMenuParam;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private boolean mScanning;
    private boolean mConnected;
    private List<BluetoothDevice> scanDevices = new ArrayList<BluetoothDevice>();
    private String pid;
    private String sid;
    private String readCid;
    private String writeCid;
    private BluetoothGattCharacteristic gattCharacteristicRead;
    private BluetoothGattCharacteristic gattCharacteristicWrite;
    private Object mLeScanCallback;
    private int mScanCount = 0;
    private ShareUtils shareUtils;
    private boolean qiYuIsLoading = false;
    private MProduct4Qy mProduct4Qy;
    private BasicJavaScript opendoorJavascript;
    private ShareBean shareBean;
    private String rightTag;
    //判断登录者是否有认证房屋  1 有认证房间  2待认证  3管家
    private static String managerType;

    public WebFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public WebFragment(AttachDelegate attachDelegate) {
        super(attachDelegate);
    }

    @SuppressLint("ValidFragment")
    public WebFragment(AttachDelegate attachDelegate, String rightTag) {
        super(attachDelegate, rightTag);
        this.rightTag = rightTag;
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
                    LogHelper.e("js----webfragment>" + param);
                    if (null != webView && param != null && !param.isEmpty()) {
                        webView.evaluateJavascript(param.toString(), value -> {
                            LogHelper.e("js----14>" + value);
                        });
                    }
                }
                break;
                case 10: {
                    final JSEnterExperienceParam paramObj = (JSEnterExperienceParam) msg.obj;
                    if (paramObj != null && StringUtil.isNotEmpty(paramObj.getUsername()) &&
                            StringUtil.isNotEmpty(paramObj.getPassword())) {
                        final Configuration.ExpState misexp = Configuration.getMISEXP(mContext);
                        if (Configuration.ExpState.ExpStateNo == misexp) {
                            if (category == Constants.WEB_COMMUNITY_TEMPLATE_NO_SUBMIT) {
                                Configuration.setMISEXP(mContext, Configuration.ExpState.ExpStateNoLogin);
                            } else if (category == Constants.WEB_COMMUNITY_TEMPLATE) {
                                Configuration.setMISEXP(mContext, Configuration.ExpState.ExpStateFormal);
                            }
                        }
                        progressShow(mContext.getResources().getString(R.string.processing_waiting));
                        ServiceFactory.getLoginService(mContext).login(paramObj.getUsername(), paramObj.getPassword(), null, new DefaultDataCallbackHandler<Void, Void, MPersonCard>(errorHandler) {
                            @Override
                            public void onSuccess(MPersonCard result) {
                                progressHide();
                                if (result != null) {
                                    DJCacheUtil.keep(DJCacheUtil.ROLENAME, paramObj.getUsername());
                                    if (StringUtil.isNotEmpty(paramObj.getCommunityID())) {
                                        DJCacheUtil.keepCommunityID(paramObj.getCommunityID());
                                        DJCacheUtil.keep(DJCacheUtil.COMMUNITY_NAME, paramObj.getCommunityName());
                                    }
                                    CacheAppData.keep(mContext, "Exp_RoleName", paramObj.getUsername());
                                    CacheAppData.keep(mContext, "Exp_CommunityID", paramObj.getCommunityID());
                                    mApplication.exitToMainActivity(mContext);
                                } else if (Configuration.ExpState.ExpStateNo == misexp) {
                                    Configuration.setMISEXP(mContext, Configuration.ExpState.ExpStateNo);
                                }
                                super.onSuccess(result);
                            }

                            @Override
                            public void onError(AppException e) {
                                progressHide();
                                if (Configuration.ExpState.ExpStateNo == misexp) {
                                    Configuration.setMISEXP(mContext, Configuration.ExpState.ExpStateNo);
                                }
                                super.onError(e);
                            }
                        });
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
										/*if(result.isSuccess()){
											DJToastUtil.showMessage(mContext, "支付成功");
										}else{
                                            DJToastUtil.showMessage(mContext, "支付确认失败");
										}*/
                                        super.onSuccess(result);
                                    }

                                    @Override
                                    public void onHandleFinal() {
                                        super.onHandleFinal();
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                                                webView.evaluateJavascript(param.toString(), null);
                                            }
                                        });
                                    }
                                });
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            //DJToastUtil.showHintToast(mContext, "支付结果确认中");
                            JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
//							DJToastUtil.showHintToast(mContext, "您已取消支付");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6002")) {
//							DJToastUtil.showHintToast(mContext, "网络出错，请稍后再试");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        }
                    }
                }
                break;
                case 12: {//微信支付反馈回调页面
                    WeiXinPayMessage payResult = (WeiXinPayMessage) msg.obj;
                    // 判断payResult  0成功  -1错误  -2用户取消
                    if (payResult.errCode == 0) {
                        ServiceFactory.getPaymentService(mContext).confirmPayment(DJCacheUtil.readCommunityID(),
                                formOrderID, paymentType, new DefaultDataCallbackHandler<Void, Void, MReturnData<String>>(errorHandler) {
                                    @Override
                                    public void onSuccess(MReturnData<String> result) {
										/*if (result.isSuccess()) {
											DJToastUtil.showMessage(mContext, "支付成功");
										} else {
											DJToastUtil.showMessage(mContext, "支付确认失败");
										}*/
                                        super.onSuccess(result);
                                    }

                                    @Override
                                    public void onHandleFinal() {
                                        super.onHandleFinal();
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                                                webView.evaluateJavascript(param.toString(), null);
                                            }
                                        });
                                    }
                                });
                    } else if (payResult.errCode == -2) {
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							DJToastUtil.showHintToast(mContext, "您已取消支付");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (!StringUtil.isBlank(payResult.errMsg)) {
//							DJToastUtil.showHintToast(mContext, payResult.errMsg);
                        } else {
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                        }
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    }
                }
                break;
                case 13: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//						    DJToastUtil.showMessage(mContext, "支付成功");
                        JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
//							DJToastUtil.showHintToast(mContext, "支付结果确认中");
                            JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
//							DJToastUtil.showHintToast(mContext, "您已取消支付");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6002")) {
//							DJToastUtil.showHintToast(mContext, "网络出错，请稍后再试");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webView.evaluateJavascript(param.toString(), null);
                        }
                    }
                }
                break;
                case 14: {//微信支付反馈回调页面
                    WeiXinPayMessage payResult = (WeiXinPayMessage) msg.obj;
                    // 判断payResult  0成功  -1错误  -2用户取消
                    if (payResult.errCode == 0) {
//						DJToastUtil.showMessage(mContext, "支付成功");
                        JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    } else if (payResult.errCode == -2) {
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							DJToastUtil.showHintToast(mContext, "您已取消支付");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							//DJToastUtil.showHintToast(mContext, "支付失败");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webView.evaluateJavascript(param.toString(), null);
                    }
                }
                break;
                case 16: {//支付宝支付失败
//						DJToastUtil.showHintToast(mContext, "支付失败");
                    JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                    webView.evaluateJavascript(param.toString(), null);
                }
                break;
                case 15:
                    goBack();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentView() {
        if (needLoad) {
            return R.layout.fragment_web;
        } else {
            return R.layout.fragment_web_template;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        wxapi = WXAPIFactory.createWXAPI(this.getActivity(), Configuration.getWeChat(mContext));
        shareUtils = new ShareUtils(mContext);
        shareUtils.setShareListenr(this);
        if (StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
            shareUtils.isSupportForward = false;
        } else {
            shareUtils.isSupportForward = true;
        }
        shareUtils.isSupportBrowser = true;
        shareUtils.isSuppertRefresh = true;
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void settingJavascript() {
        injsJavascript = new BasicJavaScript(mActivity, mContext, shareUtils) {
            @JavascriptInterface
            public void onInputFoucs(String inputID, String type, String inputValue) {
                jsCallbackId = inputID;
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("inputValue", inputValue);
                attachDelegate.startActivityForResult(intent, Constants.REQUEST_WEBVIEW_INPUT);
            }
        };
        opendoorJavascript = new BasicJavaScript(mActivity, mContext, shareUtils) {
            @Override
            @JavascriptInterface
            public synchronized void openSuccess() {
                playDoorMus("success.mp3");
                vibrate(500);
            }

            @Override
            @JavascriptInterface
            public synchronized void openFailed() {
                playDoorMus("failed.mp3");
                vibrate(500);
            }

            @Override
            @JavascriptInterface
            public void Finish() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        } else {
                            mActivity.onBackPressed();
                        }

                    }
                });
            }
        };
//        xWalkView.addJavascriptInterface(this, "djInternal");
        webView.addJavascriptInterface(injsJavascript, "injs");
        webView.addJavascriptInterface(opendoorJavascript, "jsObj");
        webView.addJavascriptInterface(new BasicDJJavascript(mActivity, webView) {

            @Override
            @JavascriptInterface
            public void h5ToNativeJs(String param) {
                super.h5ToNativeJs(param);
                try {
                    JSONObject jsonObject = new JSONObject(param);
                    String type = jsonObject.getString("type");
                    if (!type.equals("PosterShare")) {
                        return;
                    }
                    String paramStr = jsonObject.getString("param");
                    Map<String, Object> map = JSONUtil.parseJSON(paramStr, Map.class);
                    String imgUrl = (String) map.get("url");
                    JSReturnParam jsReturnParam = JSReturnParam.success(jsonObject.getString("callbackId"), "");
                    Message msg = handler.obtainMessage(9);
                    msg.obj = jsReturnParam;
                    handler.sendMessage(msg);
                    handler.post(() -> new SharePosterDialog(mActivity).playDialog(imgUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            @JavascriptInterface
            public void openFlutterPage(String param) {
                super.h5ToNativeJs(param);
            }

            @Override
            @JavascriptInterface
            public void umBridge(String param) {
                super.umBridge(param);
                Log.i("um-----", "webview-" + param);
            }

            @Override
            @JavascriptInterface
            public void closeWindow() {
                Logger.D(WebFragment.class.getSimpleName(), "WebFragment---->closeWindow");
                if (!TextUtils.isEmpty(webUrl) && webUrl.contains("pay#/?payOrderId")) {
                    mContext.startActivity(new Intent(mActivity, MainActivity.class));
                    mContext.finish();
                } else {
                    super.closeWindow();
                }
            }

            /**
             * 页面加载交互分享数据
             * @param param
             */
            @JavascriptInterface
            @Override
            public void initShareOptMenu(String param) {
                Logger.D(TAG, "initShareOptMenu:---web1->" + param);
                if (!TextUtils.isEmpty(param)) {
                    shareBean = JSONUtil.parseJSON(param, ShareBean.class);
                    EventBus.getDefault().post(new ShareMessegeEvent(shareBean, "data", rightTag));
                }
            }

            /**
             * 页面点击交互分享（邀请页）
             * @param optMenuParam
             */
            @Override
            public void showOptMenuIn(JSShowOptMenuParam optMenuParam, String param) {
                Timber.i("showOptMenuIn: " + param);
                if (optMenuParam != null) {
                    shareBean = JSONUtil.parseJSON(param, ShareBean.class);
                    shareUtils.showShare(attachDelegate, optMenuParam);
                }
                super.showOptMenuIn(optMenuParam, param);
            }

            /**
             * 页面加载交互分享（店铺，商品等）
             * @param optMenuParam
             */
            @Override
            public void initShareOptMenuIn(JSShowOptMenuParam optMenuParam, String param) {
                Logger.D(TAG, "initShareOptMenuIn---->" + optMenuParam.toString());
                if (optMenuParam != null && optMenuParam.getShareInfo() != null) {
                    if (!optMenuParam.getShareInfo().isTouchShow()) {
                        shareUtils.showShare(attachDelegate, optMenuParam);
                    } else {
                        if (attachDelegate != null) {
                            attachDelegate.setAttachRightVisiable(View.VISIBLE);
                            attachDelegate.setAttachRightEnable(true);
                            WebFragment.this.optMenuParam = optMenuParam;
                        }
                    }
                }
                super.showOptMenuIn(optMenuParam, param);
            }

            @Override
            public void showProductIn(final JSProductParam productParam) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!VisitorCommunityUtil.isVisitorNeedLogin(mContext)) {
                            Intent webIntent = new Intent(mContext, WebActivity.class);
                            if (productParam.isRightRefresh()) {
                                webIntent.putExtra("category", Constants.WEB_SERVICEFORM);
                            }
                            String url = Configuration.getWebUrl(mContext, R.string.product_path) + "?productId=" + productParam.getProductID() + "&companyID=" + DJCacheUtil.readCommunityID() + "&access_token=" + DJCacheUtil.readToken();
                            webIntent.putExtra("web_url", url);
                            mContext.startActivity(webIntent);
                        }
                    }
                });
            }

            @Override
            public void showPortalDetail(String param) {
            }

            @Override
            public void hilifepayIn(String param) {
                JSNeedLoginParam loginParam = parse(param, JSNeedLoginParam.class);
                Map paraMap = JSONUtil.parseJSON(param, Map.class);
                if (null != paraMap && paraMap.containsKey("content")) {
                    String type = (String) paraMap.get("type");
                    Logger.E(TAG, "hilifepay" + paraMap.get("content"));
                    if ("wxpay".equals(type)) {
                        JSHilifepayWxParam paramObj = parse(param, JSHilifepayWxParam.class);
                        String wxPayinfo = JSONUtil.toJSON(paramObj.getPayinfo());
                        doWXPay(wxPayinfo, loginParam);
                    } else if ("alipay".equals(type)) {
                        String payInfo = (String) paraMap.get("content");
                        doAlipay(payInfo, loginParam);
                    }
                }
            }

            @Override
            public void requestPortalList(String param) {
            }

            @Override
            public void historyBackIn() {
                handler.sendEmptyMessage(15);
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
            public void appLoginIn(JSLoginParam paramObj) {
                if (paramObj != null) {
                    if (null != paramObj.getAction() && -1 == paramObj.getAction()) {
                        if (Constants.WEB_REGISTER == category) {
                            Intent data = new Intent();
                            data.putExtra("username", paramObj.getName());
                            data.putExtra("password", paramObj.getPwd());
                            mActivity.setResult(Constants.WEB_REGISTER, data);
                            mActivity.finish();
                            mActivity.overridePendingTransition(0, R.anim.slide_down_out);
                        }
                    }
                }
                super.appLoginIn(paramObj);
            }

            @Override
            public void enterExperienceIn(final JSEnterExperienceParam paramObj) {
                if (paramObj != null) {
                    Message message = Message.obtain();
                    message.what = 10;
                    message.obj = paramObj;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void statusWindowIn(JSWindowStateParam paramObj) {
                if (paramObj != null) {
                    windowState = paramObj.getType();
                }
            }

            @Override
            public void wxpayIn(String param) {
                callPaymentAppForOuter(Constants.PAYMENTTYPE_WX, param);
            }

            @Override
            public void alipayIn(String param) {
                callPaymentAppForOuter(Constants.PAYMENTTYPE_ZFB, param);
            }

            @Override
            public void pingpayIn(String param) {
                callPaymentAppForOuter(Constants.PAYMENTTYPE_PING, param);
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
                                            callPaymentApp(paymentType, result);
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
            public void touchPortalTopicIn(final JSTopicParam paramObj) {
                ServiceFactory.getTopicService(mContext).loadPresetMenuByID(DJCacheUtil.readCommunityID(), paramObj.getTagID(), new DefaultDataCallbackHandler<Void, Void, PresetMenu>() {
                    @Override
                    public void onSuccess(final PresetMenu presetMenu) {
                        if (presetMenu != null) {
                            NormalUtils.doEnterActivity(mContext, presetMenu, null, null);
                        } else {
                            DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.no_customer_exist));
                        }
                        super.onSuccess(presetMenu);
                    }
                });
                super.touchPortalTopicIn(paramObj);
            }

            @Override
            public void showScanIn(JSShowScanParam paramObj) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                Intent scanIntent = new Intent(BaseActivity.activity, QrcodeScanActivity.class); // // mContext->BaseActivity.activity, 解决初始化模板时mContext是MainActivity
                if (paramObj != null) {
                    jsCallbackId = paramObj.getCallbackId();
                    if (paramObj != null && paramObj.getNeedResult() == 1) {
                        scanIntent.putExtra("needReturn", "directReturn");//直接返回
                    } else {
                        scanIntent.putExtra("needReturn", "false");//直接解析
                    }
                    scanIntent.putExtra("no_invite", true);
                    mApplication.setScanListener(WebFragment.this);
                    attachDelegate.startActivityForResult(scanIntent, Constants.REQUEST_SCAN_JSSDK);
                }
            }

            @Override
            protected void startCustomServerIn(JSStartCustomServerParam imConversationParam) {
                Timber.i("startCustomServerIn: ");
                jsCallbackId = imConversationParam.getCallbackId();
                JSReturnParam param = null;
                if (!kefuProcess(imConversationParam)) { //失败
                    param = JSReturnParam.fail(jsCallbackId, "");
                } else { //成功
                    param = JSReturnParam.success(jsCallbackId, "");
                }
                Message msg = handler.obtainMessage(9);
                msg.obj = param;
                handler.sendMessage(msg);
            }

            @Override
            public void startIMConversationIn(JSStartIMConversationParam imConversationParam) {
                if (imConversationParam != null) {
                    jsCallbackId = imConversationParam.getCallbackId();
                    //HashMap<String, String> map = new HashMap<String, String>();
                    JSReturnParam param = null;
                    MessageContent message = null;
                    if (!StringUtil.isEmpty(imConversationParam.getMessageTitle())
                            && !StringUtil.isEmpty(imConversationParam.getMessageUrl())) {
                        message = new TextMessage(imConversationParam.getMessageTitle() + ":" + imConversationParam.getMessageUrl());
                    } else if (!StringUtil.isEmpty(imConversationParam.getTextMessageContent())) {
                        message = new TextMessage(imConversationParam.getTextMessageContent());
                    }
                    String conversationType = imConversationParam.getConversationType();
                    if (null != conversationType) {
                        conversationType = conversationType.toUpperCase();
                    }
                    switch (conversationType) {
                        case "PRIVATE":
                            if (Configuration.isSupport(mContext, R.string.im_switch) && StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                                startConversation(Conversation.ConversationType.PRIVATE, IMUserUtil.convertPIDToIMUserID(imConversationParam.getTargetID()), imConversationParam.getTargetName(), message);
                                param = JSReturnParam.success(jsCallbackId, "");
                            } else {
                                param = JSReturnParam.fail(jsCallbackId, "");
                            }
                            break;
                        case "CUSTOM":
                            if (imConversationParam.isEnterQiYu()) {
                                param = JSReturnParam.fail(jsCallbackId, "");
                            } else {
                                if (Configuration.isSupport(mContext, R.string.im_switch) && StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                                    String customServiceId = ""; //todo 自定义客服id
                                    startConversation(Conversation.ConversationType.CUSTOMER_SERVICE, customServiceId, imConversationParam.getTargetName(), message);
                                    param = JSReturnParam.success(jsCallbackId, "");
                                } else {
                                    param = JSReturnParam.fail(jsCallbackId, "");
                                }
                            }
                            break;
                        case "GROUP":
                            if (Configuration.isSupport(mContext, R.string.im_switch) && StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                                startConversation(Conversation.ConversationType.GROUP, imConversationParam.getTargetID(), imConversationParam.getTargetName(), message);
                                param = JSReturnParam.success(jsCallbackId, "");
                            } else {
                                param = JSReturnParam.fail(jsCallbackId, "");
                            }
                            break;
                        default:
                            param = JSReturnParam.fail(jsCallbackId, "");
                            break;
                    }
                    Message msg = handler.obtainMessage(9);
                    msg.obj = param;
                    handler.sendMessage(msg);
                }
                super.startIMConversationIn(imConversationParam);
            }

            private void startConversation(Conversation.ConversationType conversationType, String targetID, String targetName, MessageContent message) {
                if (null == conversationType) {
                    return;
                }
                if (Conversation.ConversationType.PRIVATE.getValue() == conversationType.getValue()) {
                    RongIM.getInstance().startPrivateChat(mContext, targetID, targetName);
//                    RongIM.getInstance().startConversation(mContext, conversationType, targetID, targetName, null);
                } else if (Conversation.ConversationType.CUSTOMER_SERVICE.getValue() == conversationType.getValue()) {
                    // TODO: 4/17/21 自定义客服
//                    CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//                    csBuilder.nickName(DJCacheUtil.read(CacheUserData.PERSON_NAME));
//                    csBuilder.userId(IMUserUtil.convertPIDToIMUserID(DJCacheUtil.readPersonID()));
//                    CSCustomServiceInfo csInfo = csBuilder.build();
//                    RongIM.getInstance().startCustomerServiceChat(mContext, targetID, targetName, csInfo);
                } else if (Conversation.ConversationType.GROUP.getValue() == conversationType.getValue()) {
                    RongIM.getInstance().startGroupChat(mContext, targetID, targetName);
                } else {
                    return;
                }
                // TODO: 4/17/21 下面是发一条消息
                //没有message时不发消息 直接返回
//                if (null == message) {
//                    return;
//                }
//                final MessageContent messageContent = message;
//                final String target = targetID;
//                final Conversation.ConversationType type = conversationType;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        RongIM.getInstance().sendMessage(type, target, messageContent, null, null, new RongIMClient.SendMessageCallback() {
//
//                            @Override
//                            public void onSuccess(Integer integer) {
//                            }
//
//                            @Override
//                            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
//                            }
//                        }, new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
//                            @Override
//                            public void onSuccess(io.rong.imlib.model.Message message) {
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//                            }
//                        });
//                    }
//                }, 2000);
            }

            @Override
            public void needLoginIn(JSNeedLoginParam loginParam) {
                LogHelper.d("webview---->", "invokeJsMethod4");
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("personID", DJCacheUtil.readPersonID());
                    map.put("companyID", DJCacheUtil.readCommunityID());
                    map.put("accessToken", DJCacheUtil.readToken());
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                    handler.sendMessage(msg);
                    return;
                }
                Intent intent = new Intent(BaseActivity.activity, LoginActivity.class); // mContext->BaseActivity.activity, 解决初始化模板时mContext是MainActivity
                intent.putExtra("communityID", DJCacheUtil.readCommunityID());
                intent.putExtra("communityName", DJCacheUtil.read(DJCacheUtil.COMMUNITY_NAME));
                intent.putExtra("visited", true);
                CacheAppData.keepInt(mContext, BaseConstant.MOBILE_ENTER_TYPE, 4);
//                getActivity().startActivityForResult(intent,Constants.REQUEST_JS_LOGIN);
//                OnActivityForResultUtils.startActivityForResult(getActivity(), Constants.REQUEST_JS_LOGIN, intent,null);
                OnActivityForResultUtils.startActivityForResult(getActivity(), Constants.REQUEST_JS_LOGIN, intent, new SimpleOnActivityForResultCallback() {
                            @Override
                            public void success(Integer resultCode, Intent data) {
                                if (data != null && "Success".equals(data.getStringExtra("result"))) {
                                    return;
                                }
                                if (resultCode == Constants.RESULT_JS_LOGIN) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("personID", DJCacheUtil.readPersonID());
                                    map.put("companyID", DJCacheUtil.readCommunityID());
                                    map.put("accessToken", DJCacheUtil.readToken());
                                    Message msg = handler.obtainMessage(9);
                                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                                    handler.sendMessage(msg);
                                } else {
                                    Message msg = handler.obtainMessage(9);
                                    msg.obj = JSReturnParam.fail(jsCallbackId, null);
                                    handler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void cancel(Intent data) {
                                super.cancel(data);
                            }

                            @Override
                            public void result(Integer resultCode, Intent data) {
                                super.result(resultCode, data);
                            }
                        }
                );
                super.needLoginIn(loginParam);
            }

            @Override
            public void needLoginForThirdIn(JSNeedLoginForThirdParam loginParam) {
                doNeedLoginForThirdIn(loginParam);
                super.needLoginForThirdIn(loginParam);
            }

            private void doNeedLoginForThirdIn(JSNeedLoginForThirdParam loginParam) {
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                    clientID = loginParam.getClientID();
                    Intent intent = new Intent(BaseActivity.activity, LoginActivity.class); // // mContext->BaseActivity.activity, 解决初始化模板时mContext是MainActivity
                    intent.putExtra("communityID", DJCacheUtil.readCommunityID());
                    intent.putExtra("communityName", DJCacheUtil.read(DJCacheUtil.COMMUNITY_NAME));
                    intent.putExtra("visited", true);
                    OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_JS_LOGIN, intent, new SimpleOnActivityForResultCallback() {
                        @Override
                        public void success(Integer resultCode, Intent data) {
                            if (resultCode == Constants.RESULT_JS_LOGIN) {
                                ServiceFactory.getOauthService(mContext).oauth(DJCacheUtil.readToken(), DJCacheUtil.readPersonID(), DJCacheUtil.readCommunityID(), new DefaultDataCallbackHandler<Void, Void, OpenToken>() {
                                    @Override
                                    public void onSuccess(OpenToken open) {
                                        if (open != null) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("companyID", DJCacheUtil.readCommunityID());
                                            map.put("openID", open.getOpenID());
                                            map.put("dajia_rand", open.getDajia_rand());
                                            map.put("dajia_signature", open.getDajia_signature());
                                            map.put("dajia_timestamp", open.getDajia_timestamp());
                                            Message msg = handler.obtainMessage(9);
                                            msg.obj = JSReturnParam.success(jsCallbackId, map);
                                            handler.sendMessage(msg);
                                        } else {
                                            Message msg = handler.obtainMessage(9);
                                            msg.obj = JSReturnParam.fail(jsCallbackId, null);
                                            handler.sendMessage(msg);
                                        }
                                    }

                                    @Override
                                    public void onError(AppException e) {
                                        Message msg = handler.obtainMessage(9);
                                        msg.obj = JSReturnParam.fail(jsCallbackId, null);
                                        handler.sendMessage(msg);
                                    }
                                });
                            } else {
                                Message msg = handler.obtainMessage(9);
                                msg.obj = JSReturnParam.fail(jsCallbackId, null);
                                handler.sendMessage(msg);
                            }
                        }
                    });
                }
            }

            @Override
            public void scanDeviceIn(JSScanBluetoothDeviceParam scanBluetoothDeviceParam) {
                if (scanBluetoothDeviceParam != null) {
                    // android 版本判断
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ToastUtil.showMessage(mContext, R.string.ble_not_supported);
                        failScanBluetooth(Constants.BLUETOOTH_ERROR_NONSUPPORT);
                        return;
                    }
                    jsCallbackId = scanBluetoothDeviceParam.getCallbackId();
                    int reScan = scanBluetoothDeviceParam.getReScan();
                    // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
                    if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                        ToastUtil.showMessage(mContext, R.string.ble_not_supported);
                        failScanBluetooth(Constants.BLUETOOTH_ERROR_UNAUTHORIZED);
                        return;
                    }
                    // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
                    final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                    mBluetoothAdapter = bluetoothManager.getAdapter();
                    // 检查设备上是否支持蓝牙
                    if (mBluetoothAdapter == null) {
                        ToastUtil.showMessage(mContext, R.string.ble_not_supported);
                        failScanBluetooth(Constants.BLUETOOTH_ERROR_NONSUPPORT);
                        return;
                    }
                    // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBtIntent);
                        // 手动触发重新扫
                        failScanBluetooth(Constants.BLUETOOTH_ERROR_OFF);
                    } else {
                        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
                        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                        if (!mScanning) {
                            scanLeDevice(true);
                        }
                    }
                }
                super.scanDeviceIn(scanBluetoothDeviceParam);
            }

            @Override
            public void connectPeripheralAndReadIn(JSConnectDeviceParam readParam) {
                if (readParam != null) {
                    jsCallbackId = readParam.getCallbackId();
                    sid = readParam.getSid();
                    readCid = readParam.getRcid();
                    writeCid = readParam.getWcid();
                    if (StringUtil.isNotEmpty(readParam.getPid()) && !mConnected) {
                        mBluetoothLeService.connect(readParam.getPid());
                        pid = readParam.getPid();
                    }
                }
                super.connectPeripheralAndReadIn(readParam);
            }

            @Override
            public void connectPeripheralAndWriteIn(JSConnectDeviceParam writeParam) {
                if (writeParam != null) {
                    jsCallbackId = writeParam.getCallbackId();
                    sid = writeParam.getSid();
                    writeCid = writeParam.getWcid();
                    if (StringUtil.isNotEmpty(writeParam.getPid())) {
                        pid = writeParam.getPid();
                    }
                    String wContent = writeParam.getContent();
                    byte[] content = NormalUtils.hexStringToByte(wContent);
                    gattCharacteristicWrite.setValue(content);
                    mBluetoothLeService.writeCharacteristic(gattCharacteristicWrite);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("pid", pid);
                    map.put("sid", sid);
                    map.put("rcid", readCid);
                    map.put("wcid", writeCid);
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
                super.connectPeripheralAndWriteIn(writeParam);
            }

            @Override
            public void getPicIn(final JSPicDownloadParam paramObj) {
                if (StringUtil.isBlank(paramObj.getPicID())) {
                    Message msg = new Message();
                    msg.what = 9;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("picID", paramObj.getPicID());
                    msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), map);
                    handler.sendMessage(msg);
                    return;
                }
                String src = null;
                if ("1".equals(paramObj.getType())) {
                    src = UrlUtil.getPersonAvatarUrl(paramObj.getPicID(), paramObj.getSize());
                } else if ("2".equals(paramObj.getType())) {
                    src = UrlUtil.getGroupAvatarUrl(DJCacheUtil.readCommunityID(), paramObj.getPicID(), paramObj.getSize());
                } else if ("3".equals(paramObj.getType())) {
                    src = UrlUtil.getCommunityAvatarUrl(DJCacheUtil.readCommunityID(), paramObj.getSize());
                } else {
                    String picID = paramObj.getPicID();
                    if (picID.startsWith(AssetConstants.COVER_PREIX)) {
                        Message msg = new Message();
                        msg.what = 9;
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("picID", picID);
                        File picFile = ImageUtil.getLocalCover(mContext, picID.substring(picID.lastIndexOf("_") + 1));
                        map.put("url", "file://" + picFile.getAbsolutePath());
                        msg.obj = JSReturnParam.success(paramObj.getCallbackId(), map);
                        handler.sendMessage(msg);
                        return;
                    }
                    if (StringUtil.isBlank(paramObj.getCdnAddr())) {
                        src = UrlUtil.getPictureDownloadUrl(DJCacheUtil.readCommunityID(), paramObj.getPicID(), paramObj.getSize());
                    } else {
                        src = paramObj.getCdnAddr();
                    }
                }
                ImageLoader.loadImage(src, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        File file = DiskCacheUtils.findInCache(imageUri, com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache());
                        if (file != null && file.exists()) {
                            Message msg = new Message();
                            msg.what = 9;
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("picID", paramObj.getPicID());
                            if ("0".equalsIgnoreCase(paramObj.getType())) {
                                map.put("url", "file://" + file.getAbsolutePath());
                            } else {
                                map.put("url", "file://" + file.getAbsolutePath() + "?t=" + new Date().getTime());
                            }
                            msg.obj = JSReturnParam.success(paramObj.getCallbackId(), map);
                            handler.sendMessage(msg);
                        }
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        Message msg = new Message();
                        msg.what = 9;
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("picID", paramObj.getPicID());
                        msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), map);
                        handler.sendMessage(msg);
                        super.onLoadingFailed(imageUri, view, failReason);
                    }
                });
                super.getPicIn(paramObj);
            }

            @Override
            public void showFeedDetailIn(JSFeedParam feedParam) {
                if (null != feedParam) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("feedID", feedParam.getFeedID());
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void setupSupportMethod() {
                super.setupSupportMethod();
                Set<String> set = new HashSet<String>();
                set.add("enterExperience");
                set.add("appLogin");
                set.add("showPrompt");
                set.add("historyBack");
                set.add("statusWindow");
                set.add("payCheck");
                set.add("showScan");
                set.add("showOptMenu");
                set.add("initShareOptMenu");
                set.add("scanDevice");
                set.add("connectPeripheralAndRead");
                set.add("connectPeripheralAndWrite");
                set.add("needLogin");
                set.add("needLoginForThird");
                set.add("startIMConversation");
                set.add("wxpay");
                set.add("alipay");
                set.add("pingpay");
                set.add("hilifepay");
                set.add("h5ToNativeJs");
                addSupportMethods(set);
            }

            @Override
            @JavascriptInterface
            public void sportRefresh(String param) {
                if (null != param && !StringUtil.isEmpty(param)) {
                    BaseJSParam jsParam = JSONUtil.parseJSON(param, BaseJSParam.class);
                    JSReturnParam jsReturnParam = JSReturnParam.success(jsParam.getCallbackId(), buildSportParam());
                    Message msg = handler.obtainMessage(9);
                    msg.obj = jsReturnParam;
                    handler.sendMessage(msg);
                }
            }

            @Override
            @JavascriptInterface
            public void setNavigationTitle(String param) {
                super.setNavigationTitle(param);
                Map paraMap = JSONUtil.parseJSON(param, Map.class);
                if (null != paraMap && paraMap.containsKey("title")) {
                    if (null != attachDelegate) {
                        webTitle = (String) paraMap.get("title");
                        attachDelegate.setTitle(webTitle);
                    }
                }
            }
        }, "dj");
    }

    /**
     * 融云客服
     *
     * @param imConversationParam
     * @return
     */

    private boolean kefuProcess(JSStartCustomServerParam imConversationParam) {
        if (imConversationParam == null || imConversationParam.getParams() == null) {
            return false;
        }
        if (imConversationParam.getParams().getMsgData() == null) {
            return false;
        }
        String serverProvider = imConversationParam.getParams().getServerProvider();
        if (!"rong".equals(serverProvider)) {
            return false;
        }
        ModuleMsgMsgDataBeean msgMsgDataBeean = new ModuleMsgMsgDataBeean();
        JSStartCustomServerParam.ParamsBean.MsgDataBean.ProductMsgInfoBean productMsgInfoBean = imConversationParam.getParams().getMsgData().getProductMsgInfo();
        JSStartCustomServerParam.ParamsBean.MsgDataBean.OrderMsgInfoBean orderMsgInfo = imConversationParam.getParams().getMsgData().getOrderMsgInfo();
        if (productMsgInfoBean != null && !StringUtil.isEmpty(productMsgInfoBean.getProductName())) {
            msgMsgDataBeean.setShopName(productMsgInfoBean.getShopName());
            msgMsgDataBeean.setProductName(productMsgInfoBean.getProductName());
            msgMsgDataBeean.setImgUrl(productMsgInfoBean.getImgUrl());
            msgMsgDataBeean.setLink(productMsgInfoBean.getLink());
            msgMsgDataBeean.setMoneyText(productMsgInfoBean.getMoneyText());
            msgMsgDataBeean.setType("0");
        } else if (orderMsgInfo != null && !TextUtils.isEmpty(orderMsgInfo.getOrderNum())) {
            msgMsgDataBeean.setOrderNum(orderMsgInfo.getOrderNum());
            msgMsgDataBeean.setCreateTimeStr(orderMsgInfo.getCreateTimeStr());
            msgMsgDataBeean.setProductName(orderMsgInfo.getProductName());
            msgMsgDataBeean.setImgUrl(orderMsgInfo.getImgUrl());
            msgMsgDataBeean.setProductCount(orderMsgInfo.getProductCount());
            msgMsgDataBeean.setLink(orderMsgInfo.getLink());
            msgMsgDataBeean.setMoneyText(orderMsgInfo.getMoneyText());
            msgMsgDataBeean.setType("1");
        } else {
            return false;
        }
        ModuleMsgService moduleMsgService = (ModuleMsgService) ARouter.getInstance().build(RouterHub.MSG_SERVICE).navigation();
        if (moduleMsgService == null) {
            return false;
        }
        String targetId = IMUserUtil.convertPIDToIMUserID(imConversationParam.getParams().getTargetID());
        String targetName = imConversationParam.getParams().getTargetName();
        moduleMsgService.goConverSation(getActivity(), targetId, targetName, msgMsgDataBeean);
        if (imConversationParam.getParams().getNeedBack() == 3) {
            // 表示返回时返回到上上级 不返回客服选择列表
            getActivity().finish();
        }
        return true;
    }

    /**
     * 微信支付
     *
     * @param payInfo 支付服务生成的支付参数
     */
    private void doWXPay(String payInfo, JSNeedLoginParam loginParam) {
        //要在支付前调用
        WXPay.init(mContext, BuildConfig.WX_ID);
        WXPay.getInstance().doPay(payInfo, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                String str = "{'code':'success','res':'0'}";
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("res", "0");
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(int error_code) {
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("res", "0");
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.fail(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(mContext, "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(mContext, "参数错误", Toast.LENGTH_SHORT).show();
                        break;
                    case WXPay.ERROR_PAY:
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("res", "0");
                Message msg = handler.obtainMessage(9);
                msg.obj = JSReturnParam.fail(jsCallbackId, map);
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param payInfo 支付服务生成的支付参数
     */
    private void doAlipay(final String payInfo, JSNeedLoginParam loginParam) {
        new Alipay(mContext, payInfo, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                String str = "{'code':'success','res':'0'}";
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("res", "0");
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onDealing() {
                Toast.makeText(mContext, "支付处理中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("res", "0");
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.fail(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
                switch (error_code) {
                    case Alipay.ERROR_RESULT:
                        Toast.makeText(mContext, "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;
                    case Alipay.ERROR_NETWORK:
                        Toast.makeText(mContext, "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;
                    case Alipay.ERROR_PAY:
                        Toast.makeText(mContext, "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "支付错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("res", "0");
                Message msg = handler.obtainMessage(9);
                msg.obj = JSReturnParam.fail(jsCallbackId, map);
                handler.sendMessage(msg);
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            }
        }).doPay();
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
        if (!VisitorCommunityUtil.isVisitorWebViewNeedLogin(mContext)) {
            if (shareBean != null) {
                shareUtils.showShare(attachDelegate, shareBean, "share");
            } else if (shareBean == null) {
                ToastUtil.showMessage(mContext, "数据加载中...");
            } else {
                ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_no_share));
            }
        }
    }

    public void reload() {
        if (needLoad) {
            webView.reload();
        } else {
            invokeJsMethod();
        }
    }

    @Override
    public boolean goBack() {
        if (webView.canGoBack()) {
            shareBean = null;
            // 返回后 顶部h5自定义菜单需要重新初始化
            EventBus.getDefault().post(new ShareMessegeEvent(shareBean, "cancle", rightTag));
            GlobalWebManager.processLcgUserAgent(webView, null);
            webView.goBack();
            String title = titleMap.get(webView.getUrl());
            if (StringUtil.isNotEmpty(title)) {
                webTitle = title;
            }
            LogHelper.d("webview---->", "invokeJsMethod1");
            if (!needLoad) {
                invokeJsMethod();
                LogHelper.d("webview---->", "invokeJsMethod3");
            }
            return true;
        } else if (Constants.WEB_PERSON == category) {
            confirmBack();
            return true;
        } else {
            return super.goBack();
        }
    }

    @Override
    public void onStart() {
        // TODO: 2016/3/28 调用js回调
        super.onStart();
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
//        if (StringUtil.isEmpty(sourceID) || StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
        shareUtils.isSupportTipoff = false;
//        } else {
//            shareUtils.isSupportTipoff = true;
//        }
        switch (category) {
            case -1:
            case Constants.WEB_SHARE:
            case Constants.WEB_PORTAL:
            case Constants.WEB_FORM:
            case Constants.WEB_INVITE:
            case Constants.WEB_THEME_FORM:
            case Constants.WEB_SHOP:
                if (attachDelegate != null) {
                    attachDelegate.setAttachRightVisiable(View.VISIBLE);
                }
                isSupportShare = true;
                break;
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
            case Constants.WEB_NONE:
                attachDelegate.setAttachRightVisiable(View.INVISIBLE);
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
        } else if (requestCode == Constants.REQUEST_SCAN_JSSDK) {
            if (resultCode == Activity.RESULT_OK) {
                JSReturnParam param = JSReturnParam.success(jsCallbackId, data.getStringExtra("text"));
                webView.evaluateJavascript(param.toString(), null);
            }
        } else if (requestCode == Constants.REQUEST_SCAN_WEB) {
            if (resultCode == Activity.RESULT_OK) {
                String url = data.getStringExtra("text");
                if (StringUtil.isNotBlank(url)) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("web_url", url);
                    startActivity(intent);
                }
            }
            mActivity.finish();
        } else if (requestCode == Constants.REQUEST_WEBVIEW_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                String text = data.getStringExtra("text");
                if (StringUtil.isNotEmpty(text)) {
                    text = text.replace("\\", "\\\\").replace("'", "\\'");
                    webView.evaluateJavascript("javascript:_backfill('" + jsCallbackId + "','" + text + "')", null);
                }
            }
        } else if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            JSReturnParam param = null;
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                if ("success".equals(result)) {
                    param = JSReturnParam.success(jsCallbackId, "");
                } else {
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    param = JSReturnParam.fail(jsCallbackId, result + ":" + errorMsg);
                }
            } else {
                param = JSReturnParam.fail(jsCallbackId, "");
            }
            webView.evaluateJavascript(param.toString(), null);
        } else if (requestCode == Constants.REQUEST_JS_LOGIN) {
            if (data != null && "Success".equals(data.getStringExtra("result"))) {
                String url = webView.getUrl();
                if (!StringUtil.isBlank(url)) {
                    if (webErrorView != null && webErrorView.getVisibility() == View.VISIBLE) {
                        webErrorView.setVisibility(View.GONE);
                    }
                    refresh();
                    webView.loadUrl(url, null);
                }
            }
        } else {
            try {
                MContactPerson resultPerson = null;
                MGroup resultGroup = null;
                switch (requestCode) {
                    case Constants.NEW_ADD_SCOPE: {
                        String result = null;
                        if (data != null)
                            result = data.getStringExtra("result");
                        switch (resultCode) {
                            case Constants.NEW_RESULT_PERSON:
                                if (!StringUtil.isEmpty(result)) {
                                    HashMap<String, MContactPerson> temp = JSONUtil.parseJSON(result,
                                            new TypeToken<HashMap<String, MContactPerson>>() {
                                            }.getType());
                                    if (null != temp && null != temp.values()) {
                                        resultPerson = (MContactPerson) temp.values().toArray()[0];
                                    }
                                }
                                break;
                            case Constants.NEW_RESULT_GROUP:
                                if (!StringUtil.isEmpty(result)) {
                                    HashMap<String, MGroup> temp = JSONUtil.parseJSON(result, new TypeToken<HashMap<String, MGroup>>() {
                                    }.getType());
                                    if (null != temp && null != temp.values()) {
                                        resultGroup = (MGroup) temp.values().toArray()[0];
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    default:
                        break;
                }
                if (null != resultGroup || null != resultPerson) {//打开聊天会话窗口，并发送消息
                    MContactPerson resultPersonFinal = resultPerson;
                    MGroup resultGroupFinal = resultGroup;
                    String logo = null;
                    String title = tagName != null ? tagName : (StringUtil.isBlank(webTitle) ? mContext.getResources().getString(R.string.btn_share) : webTitle);
                    String desc = StringUtil.isBlank(injsJavascript.getWebDesc()) ? Configuration.getWebHost(mContext) : injsJavascript.getWebDesc();
                    String href = (shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getAddress())) ? Configuration.getWebShortChain(mContext) + shareUtils.webParam.getAddress() : webUrl; // xWalkView.getUrl()
                    if (StringUtil.isBlank(href)) {
                        DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_url_error));
                        return;
                    }
                    if (null != this.shareBean) {
                        title = this.shareBean.getShareInfo().getShareTitle();
                        desc = this.shareBean.getShareInfo().getShareDescription();
                        href = this.shareBean.getShareInfo().getUrl();
                        logo = this.shareBean.getShareInfo().getImageThumbUrl();
                    } else {
                        if (null != this.optMenuParam) {
                            title = this.optMenuParam.getShareInfo().getShareTitle();
                            desc = this.optMenuParam.getShareInfo().getShareDescription();
                            href = this.optMenuParam.getShareInfo().getUrl();
                            logo = this.optMenuParam.getShareInfo().getImageThumbUrl();
                        }
                    }
                    io.rong.imlib.model.Message message = new io.rong.imlib.model.Message();
                    RichContentMessage messageContent = RichContentMessage.obtain(title, desc, logo, href);
                    message.setContent(messageContent);
                    String targetID = null;
                    String targetName = null;
                    Conversation.ConversationType conversationType;
                    if (null != resultGroupFinal) {
                        targetID = resultGroupFinal.getgID();
                        targetName = resultGroupFinal.getgName();
                        conversationType = Conversation.ConversationType.GROUP;
                        message.setTargetId(resultGroupFinal.getgID());
                        message.setConversationType(conversationType);
                    } else {
                        targetID = IMUserUtil.convertPIDToIMUserID(resultPersonFinal.getpID());
                        targetName = resultPersonFinal.getpName();
                        conversationType = Conversation.ConversationType.PRIVATE;
                        message.setTargetId(targetID);
                        message.setConversationType(conversationType);
                    }
                    RongIM.getInstance().startConversation(mContext, conversationType, targetID, targetName);
                    RongIM.getInstance().sendMessage(message, "", null, new IRongCallback.ISendMessageCallback() {

                        @Override
                        public void onAttached(io.rong.imlib.model.Message message) {
                        }

                        @Override
                        public void onSuccess(io.rong.imlib.model.Message message) {
                        }

                        @Override
                        public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
                            showToast("转发消息失败，请稍后再试");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (category == Constants.WEB_SERVICEFORM && needSendBroadcast) {
            Intent intent = new Intent();
            intent.setAction(Constants.BROADCAST_TYPE_SERVICEFORM);
            mContext.sendBroadcast(intent);
        }
        if (mScanning) {
            mContext.unbindService(mServiceConnection);
            scanLeDevice(false);
        }
        if (mConnected) {
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
        }
        if (mGattUpdateReceiver != null) {
            mContext.unregisterReceiver(mGattUpdateReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void getShareMessageForWeb(final ShareUtils shareUtils, final int platformType, JSShowOptMenuParam menuParam) {
//        HashMap<String, String> shareInfo = menuParam.getShareInfo();
//        final String title = shareInfo.get("shareTitle");
//        final String desc = shareInfo.get("shareDescription");
//        final String url = shareInfo.get("url");
//        String logo = shareInfo.get("imageThumbUrl");
        ShareInfo shareInfo1 = menuParam.getShareInfo();
        final String title = shareInfo1.getShareTitle();
        final String desc = shareInfo1.getShareDescription();
        final String url;
        if (shareInfo1.getUrl().contains("http")) {
            url = shareInfo1.getUrl();
        } else {
            url = Configuration.getWebHost(mContext) + shareInfo1.getUrl();
        }
        String logo = shareInfo1.getImageThumbUrl();
        String accessToken = DJCacheUtil.readToken();
        String communityID = DJCacheUtil.readCommunityID();
        progressShow(mContext.getResources().getString(R.string.processing_waiting), true);
        ShareManager shareManager = new ShareManager();
        shareManager.sharePortalWeixin(mContext, accessToken, communityID, logo, new ShareManager.OnShareListener() {

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

    /**
     * 微信分享数据
     *
     * @param shareUtils
     * @param platformType
     */
    @Override
    public void getShareMessage(final ShareUtils shareUtils, final int platformType) {
        if (shareBean == null) {
            ToastUtil.showMessage(mContext, "数据加载中请稍后");
            return;
        }
        final String title;
        final String desc;
        final String url;
        String logo = null;
        final String imgUrl;
        String accessToken = DJCacheUtil.readToken();
        String communityID = DJCacheUtil.readCommunityID();
        if (shareBean == null || StringUtil.isBlank(shareBean.getShareInfo().getUrl())) {
            //分享标题，传递值、网页标题、按钮名称
            title =
                    tagName != null ? tagName :
                            (StringUtil.isBlank(webTitle) ? mContext.getResources().getString(R.string.btn_share) : webTitle);
            //分享描述，网页描述、域名
            desc =
                    StringUtil.isBlank(injsJavascript.getWebDesc()) ?
                            Configuration.getWebHost(mContext) : injsJavascript.getWebDesc();
            //分享地址，web服务器短链地址（？？？）、网页地址
            String href =
                    (shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getAddress())) ?
                            Configuration.getWebShortChain(mContext) + shareUtils.webParam.getAddress() : webUrl; // xWalkView.getUrl();
            if (StringUtil.isBlank(href)) {
                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_url_error));
                return;
            }
            if (href.indexOf("?") != -1) {
                href += "&openShareSource=dajia";
            } else {
                href += "?openShareSource=dajia";
            }
            url = href;
            if (shareUtils.webParam != null && !StringUtil.isBlank(shareUtils.webParam.getPicID())) {
                logo = shareUtils.webParam.getPicID();
            }
            if (StringUtil.isBlank(logo) && shareUtils.webParam != null && !StringUtil.isEmpty(shareUtils.webParam.getLogoID())) {
                logo = shareUtils.webParam.getLogoID();
            }
            imgUrl = UrlUtil.getPictureDownloadUrl(communityID, logo, 1);
        } else {
            title = shareBean.getShareInfo().getShareTitle();
            desc = shareBean.getShareInfo().getShareDescription();
            if (!shareBean.getShareInfo().getUrl().contains("companyID=")) {
                if (shareBean.getShareInfo().getUrl().contains("http")) {
                    if (!shareBean.getShareInfo().getUrl().contains("?")) {
                        url = shareBean.getShareInfo().getUrl() + "?companyID=" + communityID;
                    } else {
                        url = shareBean.getShareInfo().getUrl() + "&companyID=" + communityID;
                    }
                } else {
                    if (!shareBean.getShareInfo().getUrl().contains("?")) {
                        url = shareBean.getShareInfo().getUrl() + "?companyID=" + communityID;
                    } else {
                        url = Configuration.getWebHost(mContext) + shareBean.getShareInfo().getUrl() + "&companyID=" + communityID;
                    }
                }
            } else {
                String url_R = "";
                if (shareBean.getShareInfo().getUrl().contains("http")) {
                    url_R = shareBean.getShareInfo().getUrl();
                } else {
                    url_R = Configuration.getWebHost(mContext) + shareBean.getShareInfo().getUrl();
                }
                String url_change = null;
                URL url_L = null;
                try {
                    String url_query = "";
                    url_L = new URL(url_R);
                    String query = url_L.getQuery();
                    if (StringUtil.isNotEmpty(query) && query.contains("&")) {
                        String[] allParamArray = query.split("&");
                        for (String paramInArray : allParamArray) {
                            if (paramInArray.contains("companyID") && paramInArray.split("=").length == 2) {
                                paramInArray = "companyID=" + communityID;
                            }
                            url_query = url_query + paramInArray + "&";
                        }
                        url_change = Configuration.getWebHost(mContext) +
                                url_L.getPath().replaceFirst("/", "") + "?" + url_query;
                        url_change = url_change.substring(0, url_change.length() - 1);
                    } else {
                        url_change = Configuration.getWebHost(mContext) +
                                url_L.getPath().replaceFirst("/", "") + "?" + "companyID=" + communityID;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                url = url_change;
            }
            imgUrl = shareBean.getShareInfo().getImageThumbUrl();
        }
        progressShow(mContext.getResources().getString(R.string.processing_waiting), true);
        ShareManager shareManager = new ShareManager();
        shareManager.sharePortalWeixin(
                mContext, accessToken, communityID, imgUrl,
                new ShareManager.OnShareListener() {

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

    /**
     * 分享页面点击回调
     *
     * @param type
     */
    @Override
    public void shareOperate(int type) {
        switch (type) {
            case ShareUtils.RESULT_TIPOFF://举报
                if (shareBean == null)
                    return;
                Intent tipoffIntent = new Intent(mContext, WebActivity.class);
                tipoffIntent.putExtra("category", Constants.WEB_TIPOFF);
                //举报链接
                if (TextUtils.isEmpty(shareBean.getShareInfo().getTipoffUrl())) {
                    tipoffIntent.putExtra("web_url", Configuration.getWebUrl(mContext, R.string.url_gototipoff));
                } else {
                    tipoffIntent.putExtra("web_url", Configuration.getWebHost(mContext) + shareBean.getShareInfo().getTipoffUrl());
                }
                tipoffIntent.putExtra("title", getResources().getString(R.string.btn_tipoff));
                tipoffIntent.putExtra("activityType", "web");
                TipoffParam tipoffParam = new TipoffParam();
                tipoffParam.setTipoffedMsgID(StringUtil.isEmpty(injsJavascript.getSourceID()) ? sourceID : injsJavascript.getSourceID());
                tipoffParam.setTipoffedMsgType(StringUtil.isEmpty(injsJavascript.getSourceType()) ? sourceType : injsJavascript.getSourceType());
                startActivity(tipoffIntent);
                break;
            case ShareUtils.RESULT_TRANFORM:
                if (!Configuration.isSupport(mContext, R.string.im_switch)) {
                    tranformContrnt();
                } else {//已开启IM
                    judgeIsHouseOwner(mContext);
                }
                break;
            case ShareUtils.RESULT_REFRESH:
                scanLeDevice(false);
                if (needLoad) {
                    String url = webView.getUrl();
                    if (!StringUtil.isBlank(url)) {
                        if (webErrorView != null && webErrorView.getVisibility() == View.VISIBLE) {
                            webErrorView.setVisibility(View.GONE);
                        }
                        refresh();
                        //刷新
                        reload();
                    }
                } else {
                    invokeJsMethod();
                }
                break;
            case ShareUtils.RESULT_BROWSER:
                String currentUrl = webUrl; // xWalkView.getUrl();
                if (!StringUtil.isBlank(currentUrl)) {
                    IntentUtil.openWeb(mContext, currentUrl);
                }
                break;
            case ShareUtils.RESULT_COPYLINK:
                break;
            default:
                break;
        }
    }

    private void tranformContrnt() {
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
        transformCard.setData(webUrl); // xWalkView.getUrl()
        newIntent.putExtra("from", Constants.NEW_FROM_SHARE);
        newIntent.putExtra("transformCard", transformCard);
        startActivity(newIntent);
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
//			attachDelegate.setAttachRightEnable(false);
        }
        if (null != shareUtils) {
            shareUtils.webParam = null;
        }
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
            webView.evaluateJavascript(
                    "javascript:try{window.injs.getWebDesc(document.getElementsByName('description')[0].content);}catch(e){window.injs.getWebDesc('');}", null);
            webView.evaluateJavascript(
                    "javascript:try{window.injs.getWebParam(document.getElementsByName('dajiaAppParamJson')[0].text);}catch(e){window.injs.getWebParam('');}", value -> {
                        LogHelper.e("js----17>" + value);
                    }
            );
        }
        webView.evaluateJavascript("javascript:try{window.injs.setTipoffID(document.getElementsByName('sourceID')[0].text);}catch(e){window.injs.setTipoffID('');}", null);
        webView.evaluateJavascript("javascript:try{window.injs.setTipoffType(document.getElementsByName('sourceType')[0].text);}catch(e){window.injs.setTipoffType('');}", null);
        if (category == Constants.WEB_TIPOFF) {
            webView.evaluateJavascript("javascript:initData(" + personJS + ")", value -> {
                LogHelper.e("js----18>" + value);
            });
        }
        super.onPageFinished();
        settingJavascript();
    }

    /**
     * 对外提供的支付宝和微信支付入口   通过入口级区分，为以后扩展做准备  kevin 2015-12-22
     *
     * @param paymentType
     * @param resultContent
     */
    private void callPaymentAppForOuter(String paymentType, String resultContent) {
        if (Constants.PAYMENTTYPE_ZFB.equals(paymentType)) {//支付宝
            if (StringUtil.isBlank(resultContent)) {
                Message msg = new Message();
                msg.what = 16;
                handler.sendMessage(msg);
                return;
            }
            final String content = resultContent;
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(mActivity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(content, true);
                    Message msg = new Message();
                    msg.what = 13;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else if (Constants.PAYMENTTYPE_WX.equals(paymentType)) {//微信
            req = new PayReq();
            IWXPayListener wxPayListener = new IWXPayListener() {
                @Override
                public void onSuccess() {
                    Message msg = new Message();
                    msg.what = 14;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_OK, "支付成功");
                    handler.sendMessage(msg);
                }

                @Override
                public void onCancle() {
                    Message msg = new Message();
                    msg.what = 14;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_USER_CANCEL, "用户取消支付");
                    handler.sendMessage(msg);
                }

                @Override
                public void onFail(String errMsg) {
                    Message msg = new Message();
                    msg.what = 14;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_COMM, errMsg);
                    handler.sendMessage(msg);
                }

                @Override
                public void onNotInstall(String errMsg) {
                    Message msg = new Message();
                    msg.what = 14;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_USER_CANCEL, errMsg);
                    handler.sendMessage(msg);
                }
            };
            if (!wxapi.isWXAppInstalled() || !wxapi.isWXAppSupportAPI()) {
                wxPayListener.onNotInstall("没有安装微信支付");
                return;
            }
            if (StringUtil.isBlank(resultContent)) {
                wxPayListener.onFail("支付失败");
                return;
            }
            Type typeToken = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> weChatInfo = null;
            try {
                weChatInfo = JSONUtil.parseJSON(resultContent, typeToken);
            } catch (Exception e) {
                wxPayListener.onFail("支付失败");
                return;
            }
            if (weChatInfo == null) {
                wxPayListener.onFail("支付失败");
                return;
            }
            if (!Configuration.getWeChat(mContext).equals(weChatInfo.get("appid"))) {
                wxPayListener.onFail("支付失败");
                return;
            }
            WXPayEntryActivity.wxPayListener = wxPayListener;
            sendPayReqOuter(weChatInfo);//发送微信请求
        } else if (Constants.PAYMENTTYPE_PING.equals(paymentType)) {//ping++
            try {
                JSONObject jsonObject = new JSONObject(resultContent);
                if (jsonObject.has("callbackId")) {
                    jsCallbackId = jsonObject.getString("callbackId");
                }
                if (jsonObject.has("content")) {
                    String content = jsonObject.getString("content");
//                    Pingpp.createPayment(WebFragment.this.getActivity(), content);
                    JSNeedLoginParam loginParam = JSONUtil.parseJSON(resultContent, JSNeedLoginParam.class);
                    JSPingpayParam paramObj = JSONUtil.parseJSON(resultContent, JSPingpayParam.class);
                    if ("alipay".equals(paramObj.getContent().getChannel())) {
//                        String aliPayinfo = JSONUtil.toJSON(paramObj.getContent().getCredential().getAlipay());
//                        doAlipay(aliPayinfo, loginParam);
                        Pingpp.createPayment(WebFragment.this.getActivity(), content);
                    } else if ("wx".equals(paramObj.getContent().getChannel())) {
                        JSPingpayParam.ContentBean.CredentialBean.WxBean wxBean = paramObj.getContent().getCredential().getWx();
                        String appId = wxBean.getAppId();
                        String partnerId = wxBean.getPartnerId();
                        String prepayId = wxBean.getPrepayId();
                        String nonceStr = wxBean.getNonceStr();
                        String timeStamp = wxBean.getTimeStamp();
                        String packageValue = wxBean.getPackageValue();
                        String sign = wxBean.getSign();
                        JsonObject object = new JsonObject();
                        object.addProperty("appid", appId);
                        object.addProperty("partnerid", partnerId);
                        object.addProperty("prepayid", prepayId);
                        object.addProperty("noncestr", nonceStr);
                        object.addProperty("timestamp", timeStamp);
                        object.addProperty("package", packageValue);
                        object.addProperty("sign", sign);
                        String wxPayinfo = JSONUtil.toJSON(object);
                        doWXPay(wxPayinfo, loginParam);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//			Logger.E("----------",content);
//			content = "{\"id\":\"ch_CW1OOCqjHerHiLmvLKbXD4m9\",\"object\":\"charge\",\"created\":1476716193,\"livemode\":true,\"paid\":false,\"refunded\":false,\"app\":\"app_LC4G0SaDW5KSW1G8\",\"channel\":\"wx\",\"orderNo\":\"u0000007420161017225633\",\"clientIp\":\"10.161.221.139\",\"amount\":1,\"amountSettle\":1,\"currency\":\"cny\",\"subject\":\"开通租车[武汉]\",\"body\":\"开通租车[武汉]\",\"timeExpire\":1476723393,\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_CW1OOCqjHerHiLmvLKbXD4m9/refunds\",\"hasMore\":false,\"data\":[]},\"amountRefunded\":0,\"metadata\":{},\"credential\":{\"object\":\"credential\",\"wx\":{\"appId\":\"wxe10f764f5971cd82\",\"partnerId\":\"1272545801\",\"prepayId\":\"wx20161017225633ec6bba002b0849332379\",\"nonceStr\":\"121445b790bc03d2a1799a4a7570bd97\",\"timeStamp\":\"1476716193\",\"packageValue\":\"Sign\\u003dWXPay\",\"sign\":\"449F90DF27279037A34F5E6AD520FBFE\"}},\"extra\":{}}";
            //Pingpp.createPayment(WebFragment.this, content);
        }
    }

    private void callPaymentApp(String paymentType, MReturnData<String> result) {
        if (Constants.PAYMENTTYPE_ZFB.equals(paymentType)) {//支付宝
            if (null == result || StringUtil.isBlank(result.getContent())) {
                Message msg = new Message();
                msg.what = 16;
                handler.sendMessage(msg);
                return;
            }
            if (Configuration.isCustomization(mContext) && !companyInfoID.equals(BuildConfig.COMPANY_KEY)) {
                Message msg = new Message();
                msg.what = 16;
                handler.sendMessage(msg);
                return;
            }
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
        } else if (Constants.PAYMENTTYPE_WX.equals(paymentType)) {//微信
            req = new PayReq();
            IWXPayListener wxPayListener = new IWXPayListener() {
                @Override
                public void onSuccess() {
                    Message msg = new Message();
                    msg.what = 12;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_OK, "支付成功");
                    handler.sendMessage(msg);
                }

                @Override
                public void onCancle() {
                    Message msg = new Message();
                    msg.what = 12;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_USER_CANCEL, "用户取消支付");
                    handler.sendMessage(msg);
                }

                @Override
                public void onFail(String errMsg) {
                    Message msg = new Message();
                    msg.what = 12;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_COMM, errMsg);
                    handler.sendMessage(msg);
                }

                @Override
                public void onNotInstall(String errMsg) {
                    Message msg = new Message();
                    msg.what = 12;
                    msg.obj = new WeiXinPayMessage(BaseResp.ErrCode.ERR_USER_CANCEL, errMsg);
                    handler.sendMessage(msg);
                }
            };
            if (!wxapi.isWXAppInstalled() || !wxapi.isWXAppSupportAPI()) {
                wxPayListener.onNotInstall("没有安装微信支付");
                return;
            }
            if (Configuration.isCustomization(mContext) && !companyInfoID.equals(BuildConfig.COMPANY_KEY)) {
                wxPayListener.onFail("支付失败");
                return;
            }
            if (result == null || StringUtil.isBlank(result.getContent())) {
                wxPayListener.onFail("支付失败");
                return;
            }
            Type typeToken = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> weChatInfo = null;
            try {
                weChatInfo = JSONUtil.parseJSON(result.getContent(), typeToken);
            } catch (Exception e) {
                wxPayListener.onFail("支付失败");
                return;
            }
            if (weChatInfo == null) {
                wxPayListener.onFail("支付失败");
                return;
            }
            if (!Configuration.getWeChat(mContext).equals(weChatInfo.get("appid"))) {
                wxPayListener.onFail("支付失败");
                return;
            }
            WXPayEntryActivity.wxPayListener = wxPayListener;
            sendPayReq(weChatInfo);//发送微信请求
        } else if (Constants.PAYMENTTYPE_PING.equals(paymentType)) {//ping++
            try {
                JSONObject jsonObject = new JSONObject(result.getContent());
                if (jsonObject.has("callbackId")) {
                    jsCallbackId = jsonObject.getString("callbackId");
                }
                if (jsonObject.has("content")) {
                    String content = jsonObject.getString("content");
                    Pingpp.createPayment(WebFragment.this.getActivity(), content);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //拉起微信支付
    private void sendPayReq(Map<String, String> weChatInfo) {
        req.appId = weChatInfo.get("appid");
        req.partnerId = weChatInfo.get("partnerid");
        req.prepayId = weChatInfo.get("prepayid");
        req.packageValue = weChatInfo.get("package");
        req.nonceStr = weChatInfo.get("noncestr");
        req.timeStamp = weChatInfo.get("timestamp");
        req.sign = weChatInfo.get("sign");
        wxapi.sendReq(req);
    }

    //拉起微信支付
    private void sendPayReqOuter(Map<String, String> weChatInfo) {
        req.appId = weChatInfo.get("appid");
        req.partnerId = weChatInfo.get("partnerid");
        req.prepayId = weChatInfo.get("prepayid");
        req.packageValue = weChatInfo.get("packageStr");
        req.nonceStr = weChatInfo.get("noncestr");
        req.timeStamp = weChatInfo.get("timestamp");
        req.sign = weChatInfo.get("sign");
        wxapi.sendReq(req);
    }

    @Override
    public boolean processScanResult(MCommand mCommand) {
        if (MCommand.COMMAND_TYPE_PRESETMENU.equals(mCommand.getCommandType())) {
            return true;
        } else if (MCommand.COMMAND_TYPE_ACTION.equals(mCommand.getCommandType())) {
            MAction action = mCommand.getAction();
            if (!action.getCode().equals(MAction.ACTION_UNIQUECODE_LOGIN_BY_PASSWORD)) {
                return true;
            }
        }
        return false;
    }

    private Object getLeScanCallback() {
        if (mLeScanCallback == null) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.D("djsq", "==================" + device);
                                if (!scanDevices.contains(device)) {
                                    scanDevices.add(device);
                                }
                                if (scanDevices.size() > 0) {
                                    successScanBluetooth(scanDevices);
                                }
                                if (mScanCount < 20) {
                                    mScanCount++;
                                } else {
                                    scanLeDevice(false);
                                    failScanBluetooth(Constants.BLUETOOTH_ERROR_SURPASS);
                                }
                            }
                        });
                    }
                }
            };
        }
        return mLeScanCallback;
    }

    private void scanLeDevice(boolean needScan) {
        if (needScan) {
            if (mBluetoothAdapter != null) {
                mScanning = true;
                mBluetoothAdapter.startLeScan((BluetoothAdapter.LeScanCallback) getLeScanCallback());
            }
        } else {
            if (mBluetoothAdapter != null) {
                mScanning = false;
                mBluetoothAdapter.stopLeScan((BluetoothAdapter.LeScanCallback) getLeScanCallback());
                mLeScanCallback = null;
                scanDevices.clear();
                gattCharacteristicRead = null;
                gattCharacteristicWrite = null;
                mScanCount = 0;
            }
        }
    }

    private void successScanBluetooth(List<BluetoothDevice> theDevices) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        for (BluetoothDevice theDevice : theDevices) {
            if (StringUtil.isNotEmpty(theDevice.getAddress())) {
                map.put("pid", theDevice.getAddress());
            } else {
                map.put("pid", "");
            }
            if (StringUtil.isNotEmpty(theDevice.getName())) {
                map.put("name", theDevice.getName());
            } else {
                map.put("name", "");
            }
            list.add(map);
        }
        Message msg = handler.obtainMessage(9);
        msg.obj = JSReturnParam.success(jsCallbackId, list);
        handler.sendMessage(msg);
    }

    private void failScanBluetooth(String code) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("msg", "error");
        Message msg = handler.obtainMessage(9);
        msg.obj = JSReturnParam.fail(jsCallbackId, map);
        handler.sendMessage(msg);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                scanLeDevice(false);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
                if (gattServices == null) return;
                for (BluetoothGattService gattService : gattServices) {
                    Logger.D("djsq", "uuid == " + gattService.getUuid().toString());
                    if (gattService.getUuid().toString().contains(sid.toLowerCase())) {
                        List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                        // Loops through available Characteristics.
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            if (gattCharacteristic.getUuid().toString().contains(readCid.toLowerCase())) {
                                gattCharacteristicRead = gattCharacteristic;
                            }
                            if (gattCharacteristic.getUuid().toString().contains(writeCid.toLowerCase())) {
                                gattCharacteristicWrite = gattCharacteristic;
                            }
                            if (gattCharacteristicRead != null && gattCharacteristicWrite != null) {
                                mBluetoothLeService.readCharacteristic(gattCharacteristic);
                                break;
                            }
                        }
                        break;
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pid", pid);
                map.put("sid", sid);
                map.put("rcid", readCid);
                map.put("wcid", writeCid);
                if (gattCharacteristicRead != null && gattCharacteristicRead.getValue() != null) {
                    try {
                        String token = NormalUtils.bytesToHexString(gattCharacteristicRead.getValue());
                        map.put("content", token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = handler.obtainMessage(9);
                    msg.obj = JSReturnParam.success(jsCallbackId, map);
                    handler.sendMessage(msg);
                }
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    /**
     * 构造调用js方法的参数
     *
     * @return map
     */
    private Map buildSportParam() {
        String personID = DJCacheUtil.readPersonID();
        if (null == personID || personID.equals("") || personID.equals("guest")) {
            return null;
        }
        Map<String, String> paramMap = new HashMap<>();
        // paramMap.put("userName", DJCacheUtil.read(CacheUserData.PERSON_NAME));
        // paramMap.put("loginName", DJCacheUtil.read(CacheUserData.LOGIN_NAME));
        // paramMap.put("personID", personID);
        // paramMap.put("companyID", DJCacheUtil.readCommunityID());
        // paramMap.put("companyName", DJCacheUtil.read(DJCacheUtil.COMMUNITY_NAME));
        // paramMap.put("stepNumber", String.valueOf(StepService.currentStep));
        //
        // if (StepService.isUseStepCounter) {
        //     paramMap.put("code", "200");
        // } else {
        //     paramMap.put("code", "-3");
        // }
        // String themeColor = ThemeEngine.getInstance().getProperties(Constants.TITLEBACKGROUNDCOLOR);
        // paramMap.put("themeColor", themeColor.equals("") ? "#ff00ace6" : themeColor);
        return paramMap;
    }

    public boolean canGoBack() {
        if (webView.canGoBack()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String refresh = getActivity().getIntent().getStringExtra("refresh");
        if (StringUtil.isNotEmpty(refresh) && refresh.equals("1")) {
            reload();
            getActivity().getIntent().putExtra("refresh", "");
        }
    }

    //播放声音
    private void playDoorMus(String name) {
        try {
            AssetManager assetManager = mContext.getAssets();
            AssetFileDescriptor afd = assetManager.openFd(name);
            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //震动milliseconds毫秒
    private void vibrate(long milliseconds) {
        Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 判断登录者是否有认证房屋  1 有认证房间  2待认证  3管家 其他待认证
     *
     * @param mContext
     */
    public void judgeIsHouseOwner(final Context mContext) {
        Map<String, Object> map = new HashMap<>();
        String companyId = DJCacheUtil.readCommunityID();
        String personId = DJCacheUtil.readPersonID();
        if (TextUtils.isEmpty(companyId) || TextUtils.isEmpty(personId)) {
            return;
        }
        map.put("personId", personId);
        map.put("companyId", companyId);
        ServiceFactory.judgeIsHouseOwner(mContext).judgeIsHouseOwner(map, new DataCallbackHandler<Void, Void, RetureObject>() {
            @Override
            public boolean onPreExecute() {
                return super.onPreExecute();
            }

            @Override
            public void onSuccess(RetureObject retureObject) {
                super.onSuccess(retureObject);
                if (retureObject.getResult().equals("success")) {
                    managerType = retureObject.getData().toString();
                    if (TextUtils.isEmpty(managerType)) {
                        //请稍后再试
                        showToast("请稍后再试 ");
                    } else if (managerType.toString().equals("3")) {
                        //管家默认逻辑
                        //进入联系人页面完成联系人多个选择
                        Intent personSelectIntent = new Intent(BaseActivity.activity, ScopeOptionActivity.class);
                        personSelectIntent.putExtra("isPersonSupportMuti", false);
                        personSelectIntent.putExtra("category", Constants.PERSON_SELECT_ALL);
                        startActivityForResult(personSelectIntent, Constants.NEW_ADD_SCOPE);
                    } else if (managerType.toString().equals("2")) {
                        //非认证用户，提示去认证
                        showToast("请认证后，转发给管家 ");
                    } else if (managerType.toString().equals("1")) {
                        //认证用户，跳转管家聊天
                        requestStewardCount();
                    } else {
                        //非认证用户，提示去认证
                        showToast("请认证后，转发给管家 ");
                    }
                } else {
                    ToastUtil.showMessage(mContext, retureObject.getMessage());
                }
            }

            @Override
            public void onError(AppException e) {
                super.onError(e);
            }
        });
    }

    /**
     * 获取管家数量
     * 如果一个管家进入聊天界面
     * 2个及2个以上会跳转到管家列表界面
     */
    private void requestStewardCount() {
        ServiceFactory.getOpenDoorService(mContext).getStewardList(new DataCallbackHandler<Void, Void, RetureObject>() {
            @Override
            public boolean onPreExecute() {
                progressShow(getResources().getString(R.string.processing_loading), false);
                return super.onPreExecute();
            }

            @Override
            public void onSuccess(RetureObject retureObject) {
                int tag = 0;
                if (retureObject.getStatus() == 0) {
                    String data = com.alibaba.fastjson.JSONObject.toJSONString(retureObject.getData());
                    if (!StringUtil.isEmpty(data)) {
                        List<StewardListBean> stewardListBeanList = JSON.parseArray(data, StewardListBean.class);
                        if (stewardListBeanList != null && stewardListBeanList.size() == 1) {
                            //与管家沟通
                            String mPersonID = stewardListBeanList.get(0).getPersonId();
                            String pName = stewardListBeanList.get(0).getName();
                            Gson gson = new Gson();
                            Intent resultIntent = new Intent();
                            Map<String, MContactPerson> selectPerson = new HashMap<String, MContactPerson>();
                            MContactPerson mContactPerson = new MContactPerson();
                            mContactPerson.setpID(mPersonID);
                            mContactPerson.setpName(pName + getResources().getString(R.string.steward_chat_symbol));
                            selectPerson.put(mPersonID, mContactPerson);
                            resultIntent.putExtra("result", gson.toJson(selectPerson));
                            onActivityResult(Constants.NEW_ADD_SCOPE, Constants.NEW_RESULT_PERSON, resultIntent);
                        } else if (stewardListBeanList != null && stewardListBeanList.size() > 1) {
                            //进入我的管家
                            Bundle bundle = new Bundle();
                            bundle.putString("fromType", "forward");
                            IntentUtil.myKeeper(mContext, bundle, Constants.NEW_ADD_SCOPE);
                        } else {
                            showToast("暂无管家");
                        }
                    }
                } else {
                    progressHide();
                    ToastUtil.showMessage(mContext, retureObject.getMsg());
                }
                progressHide();
            }

            @Override
            public void onError(AppException e) {
                progressHide();
                super.onError(e);
            }
        });
    }
}
