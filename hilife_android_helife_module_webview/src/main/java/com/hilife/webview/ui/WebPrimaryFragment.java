package com.hilife.webview.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dajia.android.base.exception.AppException;
import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;
import com.google.gson.JsonObject;
import com.hilife.mobile.android.base.cache.CacheAppData;
import com.hilife.mobile.android.base.cache.CacheUserData;
import com.hilife.mobile.android.base.constant.BaseConstant;
import com.hilife.mobile.android.framework.component.onActivityForResult.OnActivityForResultUtils;
import com.hilife.mobile.android.framework.component.onActivityForResult.SimpleOnActivityForResultCallback;
import com.hilife.mobile.android.framework.handler.def.DefaultDataCallbackHandler;
import com.hilife.mobile.android.framework.model.oauth.OpenToken;
import com.hilife.mobile.android.tools.ToastUtil;
import com.hilife.mobile.android.tools.log.Logger;
import com.dajia.mobile.esn.im.util.IMUserUtil;
import com.dajia.mobile.esn.model.common.MReturnData;
import com.dajia.mobile.esn.model.community.MCommunity;
import com.dajia.mobile.esn.model.personInfo.MPersonCard;
import com.hilife.pay.alipay.Alipay;
import com.hilife.pay.weixin.WXPay;
import com.hilife.view.analytics.UmTrackingUploadUtils;
import com.hilife.view.app.model.PresetMenu;
import com.hilife.view.feed.model.TransformCard;
import com.hilife.view.feed.ui.NewActivity;

import com.hilife.view.utils.NormalUtils;
import com.hilife.view.login.ui.LoginActivity;
import com.hilife.view.main.service.ServiceFactory;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.main.ui.MainActivity;
import com.hilife.view.main.util.ShareManager;
import com.hilife.view.main.util.VisitorCommunityUtil;
import com.hilife.view.me.ui.PersonFragment;
import com.hilife.view.other.bluetooth.BluetoothLeService;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.view.other.component.net.UrlUtil;
import com.hilife.view.other.component.qrcode.ui.QrcodeScanActivity;
import com.hilife.view.other.component.webview.base.impl.BasicDJJavascriptOriginal;
import com.hilife.view.other.component.webview.base.impl.BasicJavaScript;
import com.hilife.view.other.component.webview.model.TipoffParam;
import com.hilife.view.other.component.webview.model.js.JSConnectDeviceParam;
import com.hilife.view.other.component.webview.model.js.JSEnterExperienceParam;
import com.hilife.view.other.component.webview.model.js.JSLoginParam;
import com.hilife.view.other.component.webview.model.js.JSNeedLoginForThirdParam;
import com.hilife.view.other.component.webview.model.js.JSNeedLoginParam;
import com.hilife.view.other.component.webview.model.js.JSPaymentParam;
import com.hilife.view.other.component.webview.model.js.JSPingpayParam;
import com.hilife.view.other.component.webview.model.js.JSReturnParam;
import com.hilife.view.other.component.webview.model.js.JSScanBluetoothDeviceParam;
import com.hilife.view.other.component.webview.model.js.JSShowOptMenuParam;
import com.hilife.view.other.component.webview.model.js.JSShowScanParam;
import com.hilife.view.other.component.webview.model.js.JSStartIMConversationParam;
import com.hilife.view.other.component.webview.model.js.JSTopicParam;
import com.hilife.view.other.component.webview.model.js.JSWindowStateParam;
import com.hilife.view.other.component.webview.model.js.PromptParam;
import com.hilife.view.other.component.webview.model.js.ShareInfo;
import com.hilife.view.other.component.webview.settings.GlobalWebManager;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.IntentUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.pay.model.PayResult;
import com.hilife.view.pay.util.IWXPayListener;
import com.hilife.view.share.model.ShareMessage;
import com.hilife.view.share.ui.ShareUtils;
import com.hilife.view.BuildConfig;
import com.hilife.view.R;
import com.hilife.view.weight.Configuration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hilife.view.weight.dialog.SharePosterDialog;
import com.hopson.hilife.commonbase.util.LogHelper;
import com.pingplusplus.android.Pingpp;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.net.cyberway.hosponlife.main.wxapi.WXPayEntryActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 原生webview
 * Created by huteng on 2017/3/15.
 */

public class WebPrimaryFragment extends BaseWebFragment implements ShareUtils.IShare {

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
    private boolean mReceiverTag = false;   //广播接受者标识

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
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
    final int version = Build.VERSION.SDK_INT;
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
                        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                        if (version < 18) {
                            webView.loadUrl(param.toString(), null);
                        } else {
                            webView.evaluateJavascript(param.toString(), value -> {
                                LogHelper.e("js----19>" + value);
                            });

                        }
//                        webView.loadUrl(param.toString(), null);
                    }
                }
                break;
                case 10: {
                    final JSEnterExperienceParam paramObj = (JSEnterExperienceParam) msg.obj;
                    if (paramObj != null && StringUtil.isNotEmpty(paramObj.getUsername()) && StringUtil.isNotEmpty(paramObj.getPassword())) {
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
                                        JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                                        webLoadUrl(param.toString(), null);
                                        super.onHandleFinal();
                                    }
                                });
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            //DJToastUtil.showHintToast(mContext, "支付结果确认中");
                            JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
//							DJToastUtil.showHintToast(mContext, "您已取消支付");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6002")) {
//							DJToastUtil.showHintToast(mContext, "网络出错，请稍后再试");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
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
                                        JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                                        webLoadUrl(param.toString(), null);
                                        super.onHandleFinal();
                                    }
                                });
                    } else if (payResult.errCode == -2) {
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							DJToastUtil.showHintToast(mContext, "您已取消支付");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webLoadUrl(param.toString(), null);

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (!StringUtil.isBlank(payResult.errMsg)) {
//							DJToastUtil.showHintToast(mContext, payResult.errMsg);
                        } else {
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                        }
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webLoadUrl(param.toString(), null);
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
                        webLoadUrl(param.toString(), null);

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
//							DJToastUtil.showHintToast(mContext, "支付结果确认中");
                            JSReturnParam param = JSReturnParam.success(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
//							DJToastUtil.showHintToast(mContext, "您已取消支付");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
                        } else if (TextUtils.equals(resultStatus, "6002")) {
//							DJToastUtil.showHintToast(mContext, "网络出错，请稍后再试");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //DJToastUtil.showHintToast(mContext, "支付失败");
                            JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                            webLoadUrl(param.toString(), null);
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
                        webLoadUrl(param.toString(), null);

                    } else if (payResult.errCode == -2) {
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							DJToastUtil.showHintToast(mContext, "您已取消支付");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webLoadUrl(param.toString(), null);

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						/*if(!StringUtil.isBlank(payResult.errMsg)){
							DJToastUtil.showHintToast(mContext, payResult.errMsg);
						}else{
							//DJToastUtil.showHintToast(mContext, "支付失败");
						}*/
                        JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                        webLoadUrl(param.toString(), null);
                    }

                }
                break;
                case 16: {//支付宝支付失败
//						DJToastUtil.showHintToast(mContext, "支付失败");
                    JSReturnParam param = JSReturnParam.fail(jsCallbackId, null);
                    webLoadUrl(param.toString(), null);

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

    public void webLoadUrl(String param, String Header) {
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            webView.loadUrl(param, null);
        } else {
            webView.evaluateJavascript(param, value -> {
                LogHelper.e("js----20>" + value);
            });
        }

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
                if (gattServices == null) {
                    return;
                }
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

    public WebPrimaryFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public WebPrimaryFragment(AttachDelegate attachDelegate) {
        super(attachDelegate);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_web_primary;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        wxapi = WXAPIFactory.createWXAPI(this.getActivity(), Configuration.getWeChat(mContext));
        shareUtils = new ShareUtils(mContext);
        shareUtils.setShareListenr(this);
//        if (StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
//            shareUtils.isSupportForward = false;
//        } else {
//            shareUtils.isSupportForward = true;
//        }
//        shareUtils.isSupportBrowser = true;
//        shareUtils.isSuppertRefresh = true;
        mReceiverTag = true;
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void settingJavascript() {
        injsJavascript = new BasicJavaScript(mActivity, mContext, shareUtils) {
            @Override
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
        webView.addJavascriptInterface(new BasicDJJavascriptOriginal(mActivity, webView) {

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
                    webView.post(() -> new SharePosterDialog(mActivity).playDialog(imgUrl));
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
            public void getStatusHeight(String param) {

            }

            @Override
            public void closeWindow() {
//                super.closeWindow();
                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                mActivity.finish();
            }

            @Override
            public void showPortalDetail(String param) {
            }

            /**
             * 选择小区
             * @param param
             */
            @Override
            public void chooseCommunity(String param) {
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
                Intent scanIntent = new Intent(mContext, QrcodeScanActivity.class);
                if (paramObj != null) {
                    jsCallbackId = paramObj.getCallbackId();
                    if (paramObj != null && paramObj.getNeedResult() == 1) {
                        scanIntent.putExtra("needReturn", "directReturn");//直接返回
                    } else {
                        scanIntent.putExtra("needReturn", "false");//直接解析
                    }
                    scanIntent.putExtra("no_invite", true);
                    attachDelegate.startActivityForResult(scanIntent, Constants.REQUEST_SCAN_JSSDK);
                }
            }

            @Override
            public void startIMConversationIn(JSStartIMConversationParam imConversationParam) {
                // TODO: 4/17/21 打开聊天 
//                if (imConversationParam != null) {
//                    jsCallbackId = imConversationParam.getCallbackId();
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    JSReturnParam param = null;
//                    MessageContent message = null;
//                    if (!StringUtil.isEmpty(imConversationParam.getMessageTitle())
//                            && !StringUtil.isEmpty(imConversationParam.getMessageUrl())) {
//                        //这里安卓和IOS有区别，安卓的Rich类型消息跳转没有自定义实现，所以会有问题，由于支持项目时间原因暂时不处理
//                        //以后有时间可以调整一下，两个端就一致了
////						message = new RichContentMessage(imConversationParam.getMessageTitle(), imConversationParam.getMessageDigest()
////							, imConversationParam.getMessageImageURL(), imConversationParam.getMessageUrl());
//                        message = new TextMessage(imConversationParam.getMessageTitle() + ":" + imConversationParam.getMessageUrl());
//                    } else if (!StringUtil.isEmpty(imConversationParam.getTextMessageContent())) {
//                        message = new TextMessage(imConversationParam.getTextMessageContent());
//                    }
//                    if (Configuration.isSupport(mContext, R.string.im_switch) && StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
//                        String conversationType = imConversationParam.getConversationType();
//                        if (null != conversationType) {
//                            conversationType = conversationType.toUpperCase();
//                        }
//                        switch (conversationType) {
//                            case "PRIVATE":
//                                startConversation(Conversation.ConversationType.PRIVATE, IMUserUtil.convertPIDToIMUserID(imConversationParam.getTargetID()), imConversationParam.getTargetName(), message);
//                                param = JSReturnParam.success(jsCallbackId, "");
//                                break;
//                            case "CUSTOM":
//                                startConversation(Conversation.ConversationType.CUSTOMER_SERVICE, ConnectRongUtils.getCustomerServiceID(), imConversationParam.getTargetName(), message);
//                                param = JSReturnParam.success(jsCallbackId, "");
//                                break;
//                            default:
//                                param = JSReturnParam.fail(jsCallbackId, "");
//                                break;
//                        }
//                    } else {
//                        param = JSReturnParam.fail(jsCallbackId, "");
//                    }
//                    Message msg = handler.obtainMessage(9);
//                    msg.obj = param;
//                    handler.sendMessage(msg);
//                }
//                super.startIMConversationIn(imConversationParam);
            }

            private void startConversation(Conversation.ConversationType conversationType, String targetID, String targetName, MessageContent message) {
                if (null == conversationType) {
                    return;
                }
                if (Conversation.ConversationType.PRIVATE.getValue() == conversationType.getValue()) {
                    RongIM.getInstance().startPrivateChat(mContext, targetID, targetName);
                } else if (Conversation.ConversationType.CUSTOMER_SERVICE.getValue() == conversationType.getValue()) {
                    // TODO: 4/17/21 自定义客服 
//                    CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//                    csBuilder.nickName(DJCacheUtil.read(CacheUserData.PERSON_NAME));
//                    csBuilder.userId(IMUserUtil.convertPIDToIMUserID(DJCacheUtil.readPersonID()));
//                    CSCustomServiceInfo csInfo = csBuilder.build();
//                    RongIM.getInstance().startCustomerServiceChat(mContext, targetID, targetName, csInfo);
                } else {
                    return;
                }
                //没有message时不发消息 直接返回
                if (null == message) {
                    return;
                }
                // TODO: 4/17/21 发送一条消息
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
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("communityID", DJCacheUtil.readCommunityID());
                intent.putExtra("communityName", DJCacheUtil.read(DJCacheUtil.COMMUNITY_NAME));
                intent.putExtra("visited", true);
                CacheAppData.keepInt(mContext, BaseConstant.MOBILE_ENTER_TYPE, 4);
                OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_JS_LOGIN, intent, new SimpleOnActivityForResultCallback() {
                    @Override
                    public void success(Integer resultCode, Intent data) {
                        if (data != null && "Success".equals(data.getStringExtra("result"))) {
                            String url = webView.getUrl();
                            if (!StringUtil.isBlank(url)) {
                                if (webErrorView != null && webErrorView.getVisibility() == View.VISIBLE) {
                                    webErrorView.setVisibility(View.GONE);
                                }
                                refresh();
                                webView.reload();
                            }
                        } else if (resultCode == Constants.RESULT_JS_LOGIN) {
                            refresh();
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
                });
                super.needLoginIn(loginParam);
            }

            @Override
            public void needLoginForThirdIn(JSNeedLoginForThirdParam loginParam) {
                if (loginParam != null) {
                    jsCallbackId = loginParam.getCallbackId();
                    clientID = loginParam.getClientID();
                    Intent intent = new Intent(mContext, LoginActivity.class);
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
                super.needLoginForThirdIn(loginParam);
            }

            @Override
            public void showOptMenuIn(JSShowOptMenuParam optMenuParam) {
                if (optMenuParam != null) {
                    shareUtils.showShare(attachDelegate, optMenuParam);
                }
                super.showOptMenuIn(optMenuParam);
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
            public void initShareOptMenuIn(JSShowOptMenuParam optMenuParam) {
                Logger.D(TAG, "initShareOptMenu:---web0->" + optMenuParam.toString());
                if (optMenuParam != null && optMenuParam.getShareInfo() != null) {
                    if (!optMenuParam.getShareInfo().isTouchShow()) {
                        shareUtils.showShare(attachDelegate, optMenuParam);
                        attachDelegate.setAttachRightEnable(false);
                    } else {
                        if (attachDelegate != null) {
                            try {
                                if (optMenuParam.getOptList() != null
                                        && optMenuParam.getOptList().length == 1
                                        && "menuItem:refresh".equals(optMenuParam.getOptList()[0])) {
//                                attachDelegate.setAttachRightVisiable(View.GONE);
//                                attachDelegate.setAttachRightEnable(true);
                                    attachDelegate.setAttachRightIcon(R.string.icon_refresh);
                                } else if (optMenuParam.getOptList() != null
                                        && optMenuParam.getOptList().length > 1) {
//                                attachDelegate.setAttachRightVisiable(View.GONE);
//                                attachDelegate.setAttachRightEnable(true);
                                    attachDelegate.setAttachRightIcon(R.string.icon_operation_more);
                                }
                                WebPrimaryFragment.this.optMenuParam = optMenuParam;
                            } catch (Exception ex) {
                            }

                        } else {
                            attachDelegate.setAttachRightIcon(R.string.string_null);
                        }
                    }
                }
                super.showOptMenuIn(optMenuParam);
            }

            @Override
            public void sportRefresh(String param) {
            }

            @Override
            public void setNavigationTitle(String param) {
            }

            @Override
            public void closeWindowAndChangeCompany(String param) {
            }

            @Override
            public void getDeviceInfo(String param) {
            }

            @Override
            public void hilifepay(String param) {
            }

            @Override
            public void pageTitleChanged() {
                mContext.setTitle(webView.getTitle());
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
        }, "dj");
        webView.addJavascriptInterface(new JsObject(), "onClick");
//        webView.addJavascriptInterface(new AndroidJsO(),"dj");
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

    public class AndroidJsO {

        @JavascriptInterface
        public void umBridge(String param) {
            Log.i("um-----", "djjsImplORIG" + param);
            try {
                JSONObject jsonObject = new JSONObject(param);
                String um_eventId = jsonObject.getString("um_eventId");
                String content = jsonObject.getString("content");
                Map<String, Object> map = JSONUtil.parseJSON(content, Map.class);
                UmTrackingUploadUtils.UmUpload(mContext, um_eventId, map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        if (!VisitorCommunityUtil.isVisitorNeedAccess(mContext)) {
            String url = webView.getUrl();
            if (!StringUtil.isBlank(url)) {
                if (optMenuParam != null) {
                    shareUtils.showShare(attachDelegate, optMenuParam);
                } else {
                    shareUtils.showShare(attachDelegate, null);
                }
            } else {
                ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.web_no_share));
            }
        }
    }

    @Override
    public void reload() {
        webView.reload();
    }

    @Override
    public boolean goBack() {
        if (category != Constants.WEB_THEME_FORM && webView.canGoBack()) {
            webView.goBack();
            String title = titleMap.get(webView.getUrl());
            if (StringUtil.isNotEmpty(title)) {
                webTitle = title;
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
        if (StringUtil.isEmpty(sourceID) || StringUtil.isEmpty(DJCacheUtil.readCommunityID())) {
            shareUtils.isSupportTipoff = false;
        } else {
            shareUtils.isSupportTipoff = true;
        }
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
                if (attachDelegate != null) {
                    attachDelegate.setAttachRightVisiable(View.INVISIBLE);
                }
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
            if (null == mUploadMessage) {
                return;
            }
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
                webView.loadUrl(param.toString(), null);
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
                    webView.loadUrl("javascript:_backfill('" + jsCallbackId + "','" + text + "')", null);
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
            webView.loadUrl(param.toString(), null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            destroyWebView(webView);
        }
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
        if (mGattUpdateReceiver != null && mReceiverTag) {
            mReceiverTag = false;
            mContext.unregisterReceiver(mGattUpdateReceiver);
        }
        super.onDestroy();
    }

    private void destroyWebView(WebView wv) {
        GlobalWebManager.destroyWebView(wv);
        GlobalWebManager.clearWebviewCache(wv);

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
        final String url = shareInfo1.getUrl();
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
                scanLeDevice(false);
                String url = webView.getUrl();
                if (!StringUtil.isBlank(url)) {
                    webView.loadUrl(url, null);
                }
                break;
            case ShareUtils.RESULT_BROWSER:
                String currentUrl = webView.getUrl();
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
            webView.loadUrl("javascript:try{window.injs.getWebDesc(document.getElementsByName('description')[0].content);}catch(e){window.injs.getWebDesc('');}", null);
            webView.loadUrl("javascript:try{window.injs.getWebParam(document.getElementsByName('dajiaAppParamJson')[0].text);}catch(e){window.injs.getWebParam('');}", null);
        }
        webView.loadUrl("javascript:try{window.injs.setTipoffID(document.getElementsByName('sourceID')[0].text);}catch(e){window.injs.setTipoffID('');}", null);
        webView.loadUrl("javascript:try{window.injs.setTipoffType(document.getElementsByName('sourceType')[0].text);}catch(e){window.injs.setTipoffType('');}", null);
        if (category == Constants.WEB_TIPOFF) {
            webView.loadUrl("javascript:initData(" + personJS + ")", null);
        }
        super.onPageFinished();
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
                        Pingpp.createPayment(this.getActivity(), content);
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
                    Pingpp.createPayment(WebPrimaryFragment.this, content);
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

    class WeiXinPayMessage {
        public Integer errCode;
        public String errMsg;

        public WeiXinPayMessage(Integer errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }
    }

    public boolean canGoBack() {
        if (category != Constants.WEB_THEME_FORM && webView.canGoBack()) {
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
}
