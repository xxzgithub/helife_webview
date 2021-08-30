package com.hilife.webview.base.def;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.tencent.smtt.sdk.WebView;

import androidx.core.content.FileProvider;
import androidx.room.util.FileUtil;


import com.google.gson.Gson;
import com.hilife.webview.R;
import com.hilife.webview.base.DJJavaScript;
import com.hilife.webview.model.WebViewUserAgent;
import com.hilife.webview.model.js.BaseJSParam;
import com.hilife.webview.model.js.JSActionParam;
import com.hilife.webview.model.js.JSAddCommentParam;
import com.hilife.webview.model.js.JSBlogParam;
import com.hilife.webview.model.js.JSCheckFunParam;
import com.hilife.webview.model.js.JSChooseImageParam;
import com.hilife.webview.model.js.JSChooseImageReturnParam;
import com.hilife.webview.model.js.JSConnectDeviceParam;
import com.hilife.webview.model.js.JSCreateFeedParam;
import com.hilife.webview.model.js.JSCreateWindowParam;
import com.hilife.webview.model.js.JSEnterCommunityParam;
import com.hilife.webview.model.js.JSEnterExperienceParam;
import com.hilife.webview.model.js.JSFeedListParam;
import com.hilife.webview.model.js.JSFeedParam;
import com.hilife.webview.model.js.JSFormParam;
import com.hilife.webview.model.js.JSForwardParam;
import com.hilife.webview.model.js.JSGetLocationParam;
import com.hilife.webview.model.js.JSGroupParam;
import com.hilife.webview.model.js.JSJoinCommunityParam;
import com.hilife.webview.model.js.JSJoinGroupParam;
import com.hilife.webview.model.js.JSLoginParam;
import com.hilife.webview.model.js.JSMessageParam;
import com.hilife.webview.model.js.JSNeedLoginForThirdParam;
import com.hilife.webview.model.js.JSNeedLoginParam;
import com.hilife.webview.model.js.JSPaymentParam;
import com.hilife.webview.model.js.JSPersonParam;
import com.hilife.webview.model.js.JSPicDownloadParam;
import com.hilife.webview.model.js.JSPortalAllGroupParam;
import com.hilife.webview.model.js.JSPortalFeedParam;
import com.hilife.webview.model.js.JSPortalGroupParam;
import com.hilife.webview.model.js.JSPortalProductParam;
import com.hilife.webview.model.js.JSPortalTopicParam;
import com.hilife.webview.model.js.JSPreviewImageParam;
import com.hilife.webview.model.js.JSProductParam;
import com.hilife.webview.model.js.JSRequestJoinParam;
import com.hilife.webview.model.js.JSReturnParam;
import com.hilife.webview.model.js.JSScanBluetoothDeviceParam;
import com.hilife.webview.model.js.JSShowAllGroupParam;
import com.hilife.webview.model.js.JSShowLocationParam;
import com.hilife.webview.model.js.JSShowOptMenuParam;
import com.hilife.webview.model.js.JSShowPortalDetailParam;
import com.hilife.webview.model.js.JSShowPortalErrorParam;
import com.hilife.webview.model.js.JSShowPortalListParam;
import com.hilife.webview.model.js.JSShowQRCodeParam;
import com.hilife.webview.model.js.JSShowScanParam;
import com.hilife.webview.model.js.JSStartIMConversationParam;
import com.hilife.webview.model.js.JSTabFeedParam;
import com.hilife.webview.model.js.JSTopicParam;
import com.hilife.webview.model.js.JSUploadFileByAppParam;
import com.hilife.webview.model.js.JSUploadFileParam;
import com.hilife.webview.model.js.JSWebParam;
import com.hilife.webview.model.js.JSWindowStateParam;
import com.hilife.webview.model.js.JsFileParam;
import com.hilife.webview.model.js.JsGetFileParam;
import com.hilife.webview.model.js.JsGetTopicParam;
import com.hilife.webview.model.js.JsPicParam;
import com.hilife.webview.model.js.PromptParam;
import com.hilife.webview.ui.WebActivity;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.net.cyberwy.hopson.lib_custom_views.toast.ToastUtil;
import cn.net.cyberwy.hopson.lib_framework.base.BaseActivity;
import cn.net.cyberwy.hopson.lib_util.ImageUtil;
import cn.net.cyberwy.hopson.lib_util.IntentUtil;
import cn.net.cyberwy.hopson.lib_util.JSONUtil;
import cn.net.cyberwy.hopson.lib_util.NetworkUtil;
import cn.net.cyberwy.hopson.lib_util.NumberParser;
import cn.net.cyberwy.hopson.lib_util.PermissionUtil;
import cn.net.cyberwy.hopson.lib_util.StringUtil;
import cn.net.cyberwy.hopson.lib_util.UUIDUtil;
import cn.net.cyberwy.hopson.lib_util.encrypt.Base64Util;
import cn.net.cyberwy.hopson.lib_util.log.LogHelper;
import cn.net.cyberwy.hopson.lib_util.log.Logger;
import cn.net.cyberwy.hopson.lib_util.sdcard.SDCardUtil;
import cn.net.cyberwy.hopson.sdk_public_base.bean.portal.PresetMenu;
import cn.net.cyberwy.hopson.sdk_public_base.constant.Constants;
import io.rong.imlib.model.Conversation;

/**
 * 该JavaScriptImpl用于支持原生webview
 *
 * @author jasonlong
 */
public abstract class DJJavascriptImplOriginal implements DJJavaScript {

    public static String TAG = DJJavascriptImpl.class.getSimpleName();

    protected BaseActivity mContext;
    protected WebView webView;

    private Set<String> funSet = new HashSet<String>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 9: {
                    //js交互回调
                    JSReturnParam param = (JSReturnParam) msg.obj;
                    if (webView != null && param != null && !param.isEmpty()) {
//                        Log.v("ajaxProxy","----param.toString()---- " + param.toString());
                        // Android版本变量
                        final int version = Build.VERSION.SDK_INT;
                        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                        if (version < 18) {
                            webView.loadUrl(param.toString());
                        } else {
                            webView.evaluateJavascript(param.toString(), value -> {
                                LogHelper.e("js----21>" + value);
                            });
                        }
                    }
                }
                break;
                case 8: {
                    //js交互回调
                    JSShowScanParam param = (JSShowScanParam) msg.obj;
                    if (webView != null && param != null && !param.isEmpty()) {
//                        webView.loadUrl(param.toString());
                        final int version = Build.VERSION.SDK_INT;
                        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                        if (version < 18) {
                            webView.loadUrl(param.toString());
                        } else {
                            webView.evaluateJavascript(param.toString(), value -> {
                                LogHelper.e("js----22>" + value);
                            });
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

    public DJJavascriptImplOriginal(BaseActivity context, WebView webView) {
        super();
        this.mContext = context;
        this.webView = webView;
        funSet.add("showReadList");
        funSet.add("showPraiseList");
        funSet.add("showCommentList");
        funSet.add("showPerson");
        funSet.add("showPicList");
        funSet.add("showFile");
        funSet.add("touchPortalTopic");
        funSet.add("requestPortalShop");
        funSet.add("closeWindow");
        funSet.add("createWindow");
        funSet.add("showMessage");
        funSet.add("joinCommunity");
        funSet.add("joinGroup");
        funSet.add("showLocation");
        funSet.add("getLocation");
        funSet.add("showQRCode");
        funSet.add("getNetWorkType");
        funSet.add("showAllGroup");
        funSet.add("chooseImage");
        funSet.add("previewImage");
        funSet.add("uploadFile");
        funSet.add("downloadImage");
        funSet.add("closeWindowWithMessage");
        funSet.add("forward");
        funSet.add("saveFormFeed");
        funSet.add("setWebParam");
        funSet.add("enterCommunity");
        funSet.add("showProduct");
        funSet.add("checkFun");
        funSet.add("feedAction");
        funSet.add("showFeedList");
        funSet.add("showBlog");
        funSet.add("getThemeColor");
        funSet.add("getFile");
        funSet.add("getTopic");
        funSet.add("createNewWindow");
        funSet.add("showPrompt");
        funSet.add("createFeed");
        funSet.add("getWebHost");
        funSet.add("setNavigationTitle");
        funSet.add("getMySelfSendFeed");
        funSet.add("getMySelfCollections");
        funSet.add("findSendFeedQuque");
        funSet.add("sendInvitation");
        funSet.add("systemSetting");
        funSet.add("systemNotice");
        funSet.add("myAddressBookInfo");
        funSet.add("myGroupInfo");
        funSet.add("myPersonInfo");
        funSet.add("myCustomerServiceInfo");
        funSet.add("getCustomMainPage");
        funSet.add("customMainPageFinished");
        funSet.add("openThreeMapNav");
        funSet.add("showMyCommunityList");
        funSet.add("addCommentInfo");
        funSet.add("goBackToSpecifiedPage");
        funSet.add("getSpecifiedPageCount");
        funSet.add("zmOpenMeeting");
        funSet.add("notifyPasswordSuccess");
        funSet.add("showFeedDetail");
        funSet.add("umBridge");
        funSet.add("h5ToNativeJs");
        funSet.add("goAppHomePage");
        funSet.add("pageTitleChanged");//新增，不然checkfun找不到该方法
        funSet.add("getStatusHeight");
        funSet.add("startCustomServer");
        setupSupportMethod();
    }

    public abstract void setupSupportMethod();

    public void addSupportMethods(Collection<String> supportMethods) {
        if (null != supportMethods) {
            funSet.addAll(supportMethods);
        }
    }

    public void showReadListIn(JSFeedParam feed) {
    }

    @Override
    @JavascriptInterface
    public final void notifyPasswordSuccess(String param) {
        Logger.D(TAG, "jsShowReadList:" + param);
        JSFeedParam feed = parse(param, JSFeedParam.class);
        if (feed != null) {
            showReadListIn(feed);
        }
    }


    @Override
    @JavascriptInterface
    public void setTimeOut() {
        LogComponent.i(TAG, "setTimeOut: ");
    }

    @Override
    @JavascriptInterface
    public final void showReadList(String param) {
        Logger.D(TAG, "jsShowReadList:" + param);
        JSFeedParam feed = parse(param, JSFeedParam.class);
        if (feed != null) {
            showReadListIn(feed);
        }
    }

    public void showPraiseListIn(JSFeedParam feed) {
    }

    ;

    @Override
    @JavascriptInterface
    public final void showPraiseList(String param) {
        Logger.D(TAG, "jsShowPraiseList:" + param);
        JSFeedParam feed = parse(param, JSFeedParam.class);
        if (!DJCacheUtil.readCommunityID().equals(feed.getCommunityID()))
            return;
        if (feed != null) {
            showPraiseListIn(feed);
        }
    }

    public void showCommentListIn(JSFeedParam feed) {
    }

    @Override
    @JavascriptInterface
    public final void showCommentList(String param) {
        Logger.D(TAG, "jsShowCommentList:" + param);
        JSFeedParam feed = parse(param, JSFeedParam.class);
        if (!DJCacheUtil.readCommunityID().equals(feed.getCommunityID()))
            return;
        if (feed != null) {
            showCommentListIn(feed);
        }
    }

    @Override
    @JavascriptInterface
    public void showPerson(String param) {
        Logger.D(TAG, "jsShowPerson:" + param);
        JSPersonParam person = parse(param, JSPersonParam.class);
        if (person != null) {
            if (!VisitorCommunityUtil.isVisitorNeedAccess(mContext)) {
                Intent intent = new Intent();
                intent.setClass(mContext, PersonActivity.class);
                intent.putExtra("personID", person.getPersonID());
                intent.putExtra("pName", person.getPersonName());
                mContext.startActivity(intent);
            }
        }
    }

    public void showGroupIn(JSGroupParam group) {
        if (group != null) {
            // TODO: 4/17/21 去群组
           
        }
    }

    @Override
    @JavascriptInterface
    public void showGroup(String param) {
        Logger.D(TAG, "jsShowGroup:" + param);
        JSGroupParam group = parse(param, JSGroupParam.class);
        showGroupIn(group);
    }

    @Override
    @JavascriptInterface
    public void showPicList(String param) {
        Logger.D(TAG, "showPicList:" + param);
        JsPicParam pic = parse(param, JsPicParam.class);
        if (null != pic) {
            List<MAttachment> pics = ObjUtil.convertJS2Pics(pic);
            Intent imgIntent = new Intent(mContext, ImageActivity.class);
            imgIntent.putExtra("pics", (Serializable) pics);
            imgIntent.putExtra("from", "web");
            imgIntent.putExtra("position", NumberParser.toInt(pic.getCurrent(), 0));
            imgIntent.putExtra("isUrl", pic.isUrl());
            mContext.startActivity(imgIntent);
        }
    }

    @Override
    @JavascriptInterface
    public void showFile(String param) {
        Logger.D(TAG, "jsshowFile:" + param);
        JsFileParam file = parse(param, JsFileParam.class);
        MAttachment attach = ObjUtil.convertJS2MAttachment(file);
        if (null != attach) {
            Intent imgIntent = new Intent(mContext, FileActivity.class);
            imgIntent.putExtra("mAttachment", attach);
            mContext.startActivity(imgIntent);
        } else {
            ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_no_exist));
        }

    }

    public void requestPortalTopicIn(JSPortalTopicParam portalParam) {
    }

    @Override
    @JavascriptInterface
    public final void requestPortalTopic(String param) {
        Logger.D(TAG, "requestPortalTopic:" + param);
        JSPortalTopicParam portal = parse(param, JSPortalTopicParam.class);
        if (portal != null) {
            requestPortalTopicIn(portal);
        }
    }

    @Override
    @JavascriptInterface
    public void closeWindow() {
        Logger.D(TAG, "closeWindow");
        mContext.finish();
    }

    public void historyBackIn() {
    }

    ;

    @Override
    @JavascriptInterface
    public void historyBack() {
        Logger.D(TAG, "historyBack");
        historyBackIn();
    }

    public void touchPortalTopicIn(JSTopicParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public final void touchPortalTopic(String param) {
        Logger.D(TAG, "touchPortalTopic:" + param);
        JSTopicParam paramObj = parse(param, JSTopicParam.class);
        if (paramObj != null) {
            touchPortalTopicIn(paramObj);
        }
    }

    public void requestPortalGroupIn(JSPortalGroupParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public final void requestPortalGroup(String param) {
        Logger.D(TAG, "requestPortalGroup:" + param);
        JSPortalGroupParam paramObj = parse(param, JSPortalGroupParam.class);
        if (paramObj != null) {
            requestPortalGroupIn(paramObj);
        }
    }

    public void requestAllGroupIn(JSPortalAllGroupParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public final void requestAllGroup(String param) {
        Logger.D(TAG, "requestAllGroup:" + param);
        JSPortalAllGroupParam paramObj = parse(param, JSPortalAllGroupParam.class);
        if (paramObj != null) {
            requestAllGroupIn(paramObj);
        }
    }

    public void showGroupListIn() {
        IntentUtil.openIntent(mContext, Constants.TOPIC_CODE_GROUP);
    }

    @Override
    @JavascriptInterface
    public void showGroupList() {
        Logger.D(TAG, "showGroupList");
        showGroupListIn();
    }

    @Override
    @JavascriptInterface
    public void showProduct(String param) {
        Logger.D("TAG", "showProduct");
        JSProductParam productParam = parse(param, JSProductParam.class);
        if (productParam != null) {
            showProductIn(productParam);
        }
    }

    public void showProductIn(JSProductParam productParam) {
    }

    public void showGroupSearchIn() {
        // TODO: 4/17/21 去群组搜索 
         
    }

    @Override
    @JavascriptInterface
    public void showGroupSearch() {
        Logger.D(TAG, "showGroupSearch");
        showGroupSearchIn();
    }

    public void requestPortalFeedIn(JSPortalFeedParam portalParam) {
    }

    @Override
    @JavascriptInterface
    public final void requestPortalFeed(String param) {
        Logger.D(TAG, "requestPortalFeed:" + param);
        JSPortalFeedParam portal = parse(param, JSPortalFeedParam.class);
        if (portal != null) {
            requestPortalFeedIn(portal);
        }
    }

    public void requestPortalProductIn(JSPortalProductParam portalParam) {
    }

    @Override
    @JavascriptInterface
    public final void requestPortalShop(String param) {
        Logger.D(TAG, "requestPortalShop:" + param);
        JSPortalProductParam portal = parse(param, JSPortalProductParam.class);
        if (portal != null) {
            requestPortalProductIn(portal);
        }
    }

    public void showFeedDetailIn(JSFeedParam feedParam) {
    }

    @Override
    @JavascriptInterface
    public final void showFeedDetail(String param) {
        Logger.D(TAG, "showFeedDetail:" + param);
        JSFeedParam feed = parse(param, JSFeedParam.class);
        if (feed != null) {
            showFeedDetailIn(feed);
        }
    }

    public void getPicIn(JSPicDownloadParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public final void getPic(String param) {
        Logger.D(TAG, "getPic:" + param);
        JSPicDownloadParam paramObj = parse(param, JSPicDownloadParam.class);
        if (paramObj != null) {
            getPicIn(paramObj);
        }
    }

    public void createWindowIn(final JSCreateWindowParam paramObj) {
        if (paramObj != null) {
            if (StringUtil.isNotEmpty(paramObj.getUrl()) && Integer.valueOf(1).equals(paramObj.getType())) {
                String url;
                if (!paramObj.getUrl().startsWith("http")) {
                    url = Configuration.getWebHost(mContext) + paramObj.getUrl();
                } else {
                    url = paramObj.getUrl();
                }
                if (url.contains("?")) {
                    url += "&access_token=" + DJCacheUtil.readToken();
                } else {
                    url += "?access_token=" + DJCacheUtil.readToken();
                }
                paramObj.setUrl(url);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent webIntent = new Intent(mContext, WebActivity.class);
                    if (StringUtil.isNotBlank(paramObj.getIsShare())) {
                        if (new Boolean(paramObj.getIsShare()) || "1".equals(paramObj.getIsShare())) {
                            webIntent.putExtra("category", Constants.WEB_SHARE);
                            JSShowOptMenuParam optMenuParam = new JSShowOptMenuParam();
                            optMenuParam.setOptList(paramObj.getOptList());
                            optMenuParam.setShareInfo(paramObj.getShareInfo());
                            initShareOptMenuIn(optMenuParam);
                        } else {
                            webIntent.putExtra("category", Constants.WEB_SHOP);
                        }
                    }
                    webIntent.putExtra("web_url", paramObj.getUrl());
                    webIntent.putExtra("noHistory", paramObj.isNoHistory());
                    mContext.startActivity(webIntent);
                }
            });

        }
    }

    @Override
    @JavascriptInterface
    public void createWindow(String param) {
        Logger.D(TAG, "createWindow:" + param);
        JSCreateWindowParam paramObj = parse(param, JSCreateWindowParam.class);
        createWindowIn(paramObj);
    }

    @Override
    @JavascriptInterface
    public void createNewWindow(String param) {
        Logger.D(TAG, "createWindow:" + param);
        JSCreateWindowParam paramObj = parse(param, JSCreateWindowParam.class);
        createWindowIn(paramObj);
    }

    /**
     * 标记提示 1：提示我的服务单
     */
    @Override
    @JavascriptInterface
    public void showPrompt(String param) {
        Logger.D(TAG, "showPrompt:" + param);
        PromptParam paramObj = parse(param, PromptParam.class);
        if (null != paramObj) {
            showPromptIn(paramObj);
        }
    }

    public void showPromptIn(PromptParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void showMessage(String param) {
        Logger.D(TAG, "showMessage:" + param);
        JSMessageParam messageParam = parse(param, JSMessageParam.class);
        if (messageParam != null && StringUtil.isNotEmpty(messageParam.getMsg())) {
            if ("9".equals(messageParam.getType())) {
                DJToastUtil.showCrouton(mContext, messageParam.getMsg());
            } else {
                DJToastUtil.showMessage(mContext, messageParam.getMsg());
            }
        }
    }

    public void createFeedIn(JSCreateFeedParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void createFeed(String param) {
        Logger.D(TAG, "createFeed:" + param);
        JSCreateFeedParam paramObj = parse(param, JSCreateFeedParam.class);
        if (paramObj != null) {
            createFeedIn(paramObj);
        }
    }

    public void joinCommunityIn(JSJoinCommunityParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void joinCommunity(String param) {
        Logger.D(TAG, "joinCommunity:" + param);
        JSJoinCommunityParam paramObj = parse(param, JSJoinCommunityParam.class);
        if (paramObj != null) {
            joinCommunityIn(paramObj);
        }
    }

    private void joinGroupIn(JSJoinGroupParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void joinGroup(String param) {
        Logger.D(TAG, "joinGroup:" + param);
        JSJoinGroupParam paramObj = parse(param, JSJoinGroupParam.class);
        if (paramObj != null) {
            joinGroupIn(paramObj);
        }
    }

    @Override
    @JavascriptInterface
    public void showLocation(String param) {
        Logger.D(TAG, "showLocation:" + param);
        JSShowLocationParam paramObj = parse(param, JSShowLocationParam.class);
        if (paramObj != null) {
            MLocation mLocation = new MLocation();
            mLocation.setLat(paramObj.getLat());
            mLocation.setLon(paramObj.getLon());
            mLocation.setName(paramObj.getName());
            mLocation.setAddr(paramObj.getAddr());
            Intent mIntent = new Intent(mContext, MapViewActivity.class);
            mIntent.putExtra("location", mLocation);
            mContext.startActivity(mIntent);
        }
    }

    /**
     * @param param
     */
    @Override
    @JavascriptInterface
    public void getLocation(String param) {
        Logger.D(TAG, "getLocation:" + param);
        final JSGetLocationParam paramObj = parse(param, JSGetLocationParam.class);
        if (paramObj != null) {
            getLocationIn(paramObj);
        }
    }

    public void getLocationIn(final JSGetLocationParam paramObj) {
        if (PermissionUtil.verifyLocationPermissions(mContext)) {
            return; //没有定位权限就不能执行逻辑
        }
        if (paramObj != null) {
            if (paramObj.getCurrent() == 1) {
                LocationService.getInstance().startLocation(true, new LocationService.DJLocationListener() {
                    @Override
                    public void onLocationChanged(MNativeLocation mNativeLocation) {
                        JSReturnParam param;
                        if (mNativeLocation != null) {
                            //获取位置信息
                            Map location = new HashMap();
                            location.put("name", mNativeLocation.getPoiName());
                            location.put("addr", mNativeLocation.getAddr());
                            location.put("lon", mNativeLocation.getLng());
                            location.put("lat", mNativeLocation.getLat());
                            location.put("city", mNativeLocation.getCityName());
                            param = JSReturnParam.success(paramObj.getCallbackId(), location);
                        } else {
                            param = JSReturnParam.fail(paramObj.getCallbackId(), "");
                        }
                        Message msg = handler.obtainMessage(9);
                        msg.obj = param;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onLocationError() {
                    }

                });
            } else {
                Intent mapSelectIntent = new Intent();
                mapSelectIntent.setClass(mContext, MapSelectActivity.class);
                if (paramObj.getLat() != null && paramObj.getLon() != null) {
                    MLocation location = new MLocation();
                    location.setAddr(paramObj.getAddr());
                    location.setName(paramObj.getName());
                    location.setLat(paramObj.getLat());
                    location.setLon(paramObj.getLon());
                    mapSelectIntent.putExtra("location", (Serializable) location);
                }
                OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_AMAP_JSSDK, mapSelectIntent, new SimpleOnActivityForResultCallback() {
                    @Override
                    public void success(Integer resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            MLocation loc = (MLocation) data.getSerializableExtra("location");
                            Map location = new HashMap();
                            location.put("name", loc.getName());
                            location.put("addr", loc.getAddr());
                            location.put("lon", loc.getLon());
                            location.put("lat", loc.getLat());
                            JSReturnParam param = JSReturnParam.success(paramObj.getCallbackId(), location);
                            webView.loadUrl(param.toString());
                        } else {
                            JSReturnParam param = JSReturnParam.fail(paramObj.getCallbackId(), "");
                            webView.loadUrl(param.toString());
                        }
                    }
                });
            }
        }
    }

    public void showScanIn(JSShowScanParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void showScan(String param) {
        Logger.D(TAG, "showScan:" + param);
        JSShowScanParam paramObj = parse(param, JSShowScanParam.class);
        if (paramObj != null) {
            showScanIn(paramObj);
        }
    }

    public void requestJoinGroupIn(final JSRequestJoinParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void requestJoinGroup(String param) {
        Logger.D(TAG, "requestJoinGroup:" + param);
        JSRequestJoinParam paramObj = parse(param, JSRequestJoinParam.class);
        if (paramObj != null) {
            requestJoinGroupIn(paramObj);
        }
    }

    public void showAllGroupIn(final JSShowAllGroupParam paramObj) {
        if (paramObj.getType() != null) {
            if (Integer.valueOf(1).equals(paramObj.getType())) {// 1 是进搜索群界面
                // TODO: 4/17/21 展示所有群
//                Intent intent = new Intent(mContext, GroupOnlineSearchActivity.class);
//                OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_JS_REFRESH_GROUPLIST, intent, new SimpleOnActivityForResultCallback() {
//                    @Override
//                    public void success(Integer resultCode, Intent data) {
//                        if (resultCode == Constants.RESULT_SEARCH) {
//                            Message msg = handler.obtainMessage(9);
//                            msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
//                            handler.sendMessage(msg);
//                        }
//                    }
//                });
            } else if (Integer.valueOf(0).equals(paramObj.getType())) {// 0 是进全部群组
                Intent intent = new Intent(mContext, CommonActivity.class);
                PresetMenu presetMenu = ProviderFactory.getTopicReceiveProviderDB(mContext).loadPresetMenuByID(DJCacheUtil.readCommunityID(), Constants.MENU_UNIQUE_ALLGROUP);
                intent.putExtra("presetMenu", presetMenu);
                intent.putExtra("code", presetMenu.getCode());
                mContext.startActivity(intent);
            } else {
                // 不支持项
            }
        }
    }

    @Override
    @JavascriptInterface
    public void showAllGroup(String param) {
        Logger.D(TAG, "showAllGroup:" + param);
        JSShowAllGroupParam paramObj = parse(param, JSShowAllGroupParam.class);
        if (param != null) {
            showAllGroupIn(paramObj);
        }
    }

    @Override
    @JavascriptInterface
    public void showQRCode(String param) {
        Logger.D(TAG, "showQRCode:" + param);
        JSShowQRCodeParam paramObj = parse(param, JSShowQRCodeParam.class);
        Message msg = new Message();
        msg.what = 9;
        msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), "");
        if (paramObj != null && paramObj.getUrl() != null) {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(paramObj.getUrl());
            if (bitmap != null) {
                String url = "";
                List<String> list = QRCodeManager.parseMultiQRCode(bitmap);
                if (null != list && list.size() > 0) {
                    url = list.get(0);
                } else {
                    url = QRCodeManager.parseQRCode(mContext, bitmap);
                }
                msg.obj = JSReturnParam.success(paramObj.getCallbackId(), url);
            }
        }
        handler.sendMessage(msg);

    }

    /**
     * 获取当前的网络类型
     * 返回网络类型2g，3g，4g，wifi, unknown, none
     */
    @Override
    @JavascriptInterface
    public void getNetWorkType(String param) {
        Logger.D(TAG, "getNetWorkType:" + param);
        BaseJSParam jsParam = JSONUtil.parseJSON(param, BaseJSParam.class);
        if (jsParam != null) {
            Message msg = new Message();
            msg.what = 9;
            try {
                String state = "none";
                MLogConstant.NETWORK_TYPE netType = NetworkUtil.getNetType(mContext);
                switch (netType) {
                    case NET_2G:
                        state = "2g";
                        break;
                    case NET_3G:
                        state = "3g";
                        break;
                    case NET_4G:
                        state = "4g";
                        break;
                    case NET_WIFI:
                        state = "wifi";
                        break;
                    case NET_UNKNOWN:
                        state = "2g";
                        break;
                    default:
                        break;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("networkType", state);
                msg.obj = JSReturnParam.success(jsParam.getCallbackId(), map);
            } catch (Exception e) {
                e.printStackTrace();
                msg.obj = JSReturnParam.fail(jsParam.getCallbackId(), null);
            } finally {
                handler.sendMessage(msg);
            }
        }
    }

    public void showOptMenuIn(JSShowOptMenuParam optMenuParam) {
    }

    @Override
    @JavascriptInterface
    public void showOptMenu(String param) {
        Logger.D(TAG, "showOptMenu:" + param);
        JSShowOptMenuParam optMenuParam = parse(param, JSShowOptMenuParam.class);
        if (optMenuParam != null) {
            showOptMenuIn(optMenuParam);
        }
    }

    public void initShareOptMenuIn(JSShowOptMenuParam optMenuParam) {
    }

    @Override
    @JavascriptInterface
    public void initShareOptMenu(String param) {
        Log.i("um-----", "djjsImplORIG" + param);
        Logger.D(TAG, "initShareOptMenu:" + param);
        JSShowOptMenuParam optMenuParam = parse(param, JSShowOptMenuParam.class);
        if (optMenuParam != null) {
            initShareOptMenuIn(optMenuParam);
        }
    }

    public void showPortalErrorIn(JSShowPortalErrorParam portalErrorParam) {
    }

    @Override
    @JavascriptInterface
    public void showPortalError(String param) {
        Logger.D(TAG, "showPortalError:" + param);
        JSShowPortalErrorParam portalErrorParam = parse(param, JSShowPortalErrorParam.class);
        if (portalErrorParam != null) {
            showPortalErrorIn(portalErrorParam);
        }
    }

    public void requestPortalListIn(JSShowPortalListParam portalListParam) {
    }

    @Override
    @JavascriptInterface
    public void requestPortalList(String param) {
        Logger.D(TAG, "requestPortalList:" + param);
        JSShowPortalListParam portalListParam = JSONUtil.parseJSON(param, JSShowPortalListParam.class);
        if (portalListParam != null) {
            requestPortalListIn(portalListParam);
        }
    }

    public void showPortalDetailIn(JSShowPortalDetailParam showPortalDetailParam) {
    }

    @Override
    @JavascriptInterface
    public void showPortalDetail(String param) {
        Logger.D(TAG, "showPortalDetail:" + param);
        JSShowPortalDetailParam showPortalDetailParam = JSONUtil.parseJSON(param, JSShowPortalDetailParam.class);
        if (showPortalDetailParam != null) {
            showPortalDetailIn(showPortalDetailParam);
        }
    }

    public void connectPeripheralAndWriteIn(JSConnectDeviceParam writeParam) {
    }

    @Override
    @JavascriptInterface
    public void connectPeripheralAndWrite(String param) {
        Logger.D(TAG, "connectPeripheralAndWrite:" + param);
        JSConnectDeviceParam writeParam = parse(param, JSConnectDeviceParam.class);
        if (writeParam != null) {
            connectPeripheralAndWriteIn(writeParam);
        }
    }

    public void connectPeripheralAndReadIn(JSConnectDeviceParam readParam) {
    }

    @Override
    @JavascriptInterface
    public void connectPeripheralAndRead(String param) {
        Logger.D(TAG, "connectPeripheralAndRead:" + param);
        JSConnectDeviceParam readParam = parse(param, JSConnectDeviceParam.class);
        if (readParam != null) {
            connectPeripheralAndReadIn(readParam);
        }
    }

    public void scanDeviceIn(JSScanBluetoothDeviceParam scanParam) {
    }

    @Override
    @JavascriptInterface
    public void scanDevice(String param) {
        Logger.D(TAG, "scanDevic:" + param);
        JSScanBluetoothDeviceParam scanParam = parse(param, JSScanBluetoothDeviceParam.class);
        if (scanParam != null) {
            scanDeviceIn(scanParam);
        }
    }

    public void needLoginIn(JSNeedLoginParam loginParam) {
    }

    /**
     * needLogin
     */
    @Override
    @JavascriptInterface
    public void needLogin(String param) {
        Logger.D(TAG, "needLogin:" + param);
        JSNeedLoginParam loginParam = parse(param, JSNeedLoginParam.class);
        if (loginParam != null) {
            needLoginIn(loginParam);
        }
    }

    public void startIMConversationIn(JSStartIMConversationParam imConversationParam) {
    }

    @Override
    public void startCustomServer(String param) {

    }


    /**
     * startIMConversation
     */
    @Override
    @JavascriptInterface
    public void startIMConversation(String param) {
        Logger.D(TAG, "startIMConversation:" + param);
        JSStartIMConversationParam imConversationParam = parse(param, JSStartIMConversationParam.class);
        if (imConversationParam != null) {
            startIMConversationIn(imConversationParam);
        }
    }

    public void needLoginForThirdIn(JSNeedLoginForThirdParam loginParam) {
    }

    /**
     * needLogin
     */
    @Override
    @JavascriptInterface
    public void needLoginForThird(String param) {
        Logger.D(TAG, "needLoginForThird:" + param);
        JSNeedLoginForThirdParam loginParam = parse(param, JSNeedLoginForThirdParam.class);
        if (loginParam != null && StringUtil.isEmpty(DJCacheUtil.readToken())) {
            needLoginForThirdIn(loginParam);
        }
    }

    public void chooseImageIn(final JSChooseImageParam imageParam) {
        if (imageParam != null) {
            int type = imageParam.getType();
            int current = imageParam.getCurrent();
            if (!SDCardUtil.checkSDCardState()) { // 检测sd是否可用
                DJToastUtil.showErrorToast(mContext, mContext.getResources().getString(R.string.text_sd_unusable));
                return;
            }
            if (9 == current) {
                DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_overtop_pic_count2));
                return;
            }
            if (type == 1) { // 打开摄像头
                if (PermissionUtil.verifyCameraPermission(mContext)) {
                    return; //没有定位权限就不能执行逻辑
                }
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File capturePath = FileUtil.findUploadPicture(FileUtil.createPictureName());
                final String filePath = capturePath.getAbsolutePath();
                Uri imageUri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", capturePath);
                    captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    imageUri = Uri.fromFile(capturePath);
                }
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_CAPTURE, captureIntent, new SimpleOnActivityForResultCallback() {
                    @Override
                    public void success(Integer resultCode, Intent data) {
                        JSReturnParam param;
                        if (resultCode == Activity.RESULT_OK) {
                            String url = filePath;//FileUtil.findUploadPicture(FileUtil.createPictureName()).getAbsolutePath();
                            ArrayList<String> urls = new ArrayList<String>();
                            urls.add(url);
                            param = returnImage(imageParam, urls);
                        } else {
                            param = JSReturnParam.success(imageParam.getCallbackId(), "");
                        }
                        Message msg = handler.obtainMessage(9);
                        msg.obj = param;
                        handler.sendMessage(msg);
                    }
                });
            } else { // 打开相册
                Intent selectIntent = new Intent(mContext, ImageFolerActivity.class);
                selectIntent.putExtra("category", "web");
                selectIntent.putExtra("haveSelectedCount", current);
                OnActivityForResultUtils.startActivityForResult(mContext, Constants.REQUEST_CHOOSE_PICS, selectIntent, new SimpleOnActivityForResultCallback() {
                    @Override
                    public void success(Integer resultCode, Intent data) {
                        JSReturnParam param;
                        if (resultCode == Activity.RESULT_OK) {
                            ArrayList<String> urls = (ArrayList<String>) data.getSerializableExtra(ImageFolerActivity.IMAGE_PATHS);
                            param = returnImage(imageParam, urls);
                        } else {
                            param = JSReturnParam.success(imageParam.getCallbackId(), "");
                        }
                        Message msg = handler.obtainMessage(9);
                        msg.obj = param;
                        handler.sendMessage(msg);
                    }
                });
            }
        }
    }

    public void uploadFileByAppIn(final JSUploadFileByAppParam uploadFileByAppParam) {
        new com.hilife.mobile.android.framework.handler.AsyncTask<Void, Void, List<Map<String, Object>>>() {
            @Override
            protected List<Map<String, Object>> doInBackground(Void... params) {
                if (Integer.valueOf(1).equals(uploadFileByAppParam.getIsShowProgress())) {
                    DJJavascriptImplOriginal.this.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressLoading.progressShow(mContext, "请稍候");
                        }
                    });
                }
                List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                List<String> files = uploadFileByAppParam.getFiles();
                if (null != files && 0 != files.size()) {
                    for (String filePath : files) {
                        Map<String, Object> map = ProviderFactory.getAttachProvider(mContext).uploadFormImg(filePath, DJCacheUtil.readCommunityID());
                        result.add(map);
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Map<String, Object>> result) {
                if (Integer.valueOf(1).equals(uploadFileByAppParam.getIsShowProgress())) {
                    DJJavascriptImplOriginal.this.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressLoading.progressHide();
                        }
                    });
                }
                List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> resItem : result) {
                    Map<String, Object> res = new HashMap<String, Object>();
                    String flag = (String) resItem.get("flag");
                    if ("success".equalsIgnoreCase(flag)) {
                        res.put("id", resItem.get("fileID"));
                        res.put("fileName", resItem.get("fileName"));
                        res.put("fileSize", resItem.get("fileSize"));
                        retList.add(res);
                    } else {
                        Logger.E(TAG, "文件上传失败！");
                    }
                }
                JSReturnParam param = JSReturnParam.success(uploadFileByAppParam.getCallbackId(), retList);
                Message msg = handler.obtainMessage(9);
                msg.obj = param;
                DJJavascriptImplOriginal.this.handler.sendMessage(msg);
                super.onPostExecute(result);
            }
        }.execute();
    }

    public JSReturnParam returnImage(JSChooseImageParam imageParam, List<String> urls) {
        JSReturnParam param;
        if (JSChooseImageParam.RETURN_TYPE_BASE64.equals(imageParam.getReturnType())) {
            List<JSChooseImageReturnParam> returnParamList = new ArrayList<>();
            for (String filePath : urls) {
                JSChooseImageReturnParam returnParam = new JSChooseImageReturnParam();
                returnParam.setFilePath(filePath);
                returnParam.setThumbData(Base64Util.encode(ImageUtil.bmpToByteArray(ImageUtil.centerSquareScaleBitmap(filePath, 140), true)));
                returnParamList.add(returnParam);
            }
            param = JSReturnParam.success(imageParam.getCallbackId(), returnParamList);
        } else {
            param = JSReturnParam.success(imageParam.getCallbackId(), urls);
        }
        return param;
    }

    /**
     * chooseImage
     */
    @Override
    @JavascriptInterface
    public void chooseImage(String param) {
        Logger.D(TAG, "chooseImage:" + param);
        JSChooseImageParam imageParam = parse(param, JSChooseImageParam.class);
        if (imageParam != null) {
            chooseImageIn(imageParam);
        }
    }

    @Override
    @JavascriptInterface
    public void uploadFileByApp(String param) {
        Logger.D(TAG, "uploadFileByApp:" + param);
        JSUploadFileByAppParam uploadFileByAppParam = parse(param, JSUploadFileByAppParam.class);
        if (null != uploadFileByAppParam) {
            uploadFileByAppIn(uploadFileByAppParam);
        }
    }

    @Override
    @JavascriptInterface
    public void previewImage(String param) {
        Logger.D(TAG, "previewImage:" + param);
        JSPreviewImageParam pics = parse(param, JSPreviewImageParam.class);
        if (pics != null && pics.getUrls() != null && pics.getUrls().size() > 0) {
            List<String> finalUrls = new ArrayList<String>();
            for (String imgUrl :
                    pics.getUrls()) {
                if (!imgUrl.contains("://")) {
                    imgUrl = Configuration.getWebHost(mContext) + imgUrl;
                }
                finalUrls.add(imgUrl);
            }
            Intent imgIntent = new Intent(mContext, ImageActivity.class);
            imgIntent.putExtra("urls", (Serializable) finalUrls);
            imgIntent.putExtra("from", "web");
            imgIntent.putExtra("position", NumberParser.toInt(pics.getCurrent(), 0));
            mContext.startActivity(imgIntent);
        }
    }

    @Override
    @JavascriptInterface
    public void uploadFile(String param) {
        Logger.D(TAG, "uploadFile:" + param);
        final JSUploadFileParam paramObj = parse(param, JSUploadFileParam.class);
        if (paramObj != null && paramObj.getFiles() != null) {
            if (Integer.valueOf(1).equals(paramObj.getIsShowProgress())) {
                DJJavascriptImplOriginal.this.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressLoading.progressShow(mContext, "请稍候");
                    }
                });
            }
            new AsyncTask<Void, Void, List<Map<String, String>>>() {

                @Override
                protected List<Map<String, String>> doInBackground(Void... params) {
                    List<Map<String, String>> result = null;
                    try {
                        result = new ArrayList<Map<String, String>>();
                        Map<String, Object> params2 = new HashMap<String, Object>();
                        params2.put("from", Constants.C_iFileFrom_Feed + "");
                        params2.put("communityID", DJCacheUtil.readCommunityID());
                        for (String filePath : paramObj.getFiles()) {
                            String fileID = ProviderFactory.getAttachProvider(mContext).uploadFile(filePath, params2);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("filePath", filePath);
                            map.put("fileID", fileID);
                            result.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = null;
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(List<Map<String, String>> result) {
                    if (Integer.valueOf(1).equals(paramObj.getIsShowProgress())) {
                        DJJavascriptImplOriginal.this.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ProgressLoading.progressHide();
                            }
                        });
                    }
                    Message msg = new Message();
                    if (result == null) {
                        msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), null);
                    } else {
                        msg.obj = JSReturnParam.success(paramObj.getCallbackId(), result);
                    }
                    msg.what = 9;
                    DJJavascriptImplOriginal.this.handler.sendMessage(msg);
                    super.onPostExecute(result);
                }
            }.execute();
        }
    }

    /**
     * downloadImage
     */
    @Override
    @JavascriptInterface
    public void downloadImage(String param) {
        Logger.D(TAG, "downloadImage:" + param);
    }

    /**
     * closeWindowWithMessage
     */
    @Override
    @JavascriptInterface
    public void closeWindowWithMessage(String param) {
        Logger.D(TAG, "closeWindowWithMessage:" + param);
        JSMessageParam messageParam = parse(param, JSMessageParam.class);
        if (messageParam != null && StringUtil.isNotEmpty(messageParam.getMsg())) {
            ToastUtil.showMessage(mContext, messageParam.getMsg());
        }
        mContext.finish();
    }

    public void appLoginIn(JSLoginParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void appLogin(String param) {
        Logger.E(TAG, "appLogin:" + param);
        JSLoginParam paramObj = parse(param, JSLoginParam.class);
        if (paramObj != null) {
            appLoginIn(paramObj);
        }
    }

    /**
     * forward
     */
    @Override
    @JavascriptInterface
    public void forward(String param) {
        Logger.E(TAG, "forward:" + param);
        JSForwardParam paramObj = parse(param, JSForwardParam.class);
        if (paramObj != null) {
        }
    }

    @Override
    @JavascriptInterface
    public void saveFormFeed(String param) {
        Logger.E(TAG, "forward:" + param);
        JSFormParam paramObj = parse(param, JSFormParam.class);
        if (paramObj != null) {
        }
    }

    @Override
    @JavascriptInterface
    public void setWebParam(String param) {
        Logger.E(TAG, "forward:" + param);
        JSWebParam paramObj = parse(param, JSWebParam.class);
        if (paramObj != null) {
        }
    }

    public void enterCommunityIn(JSEnterCommunityParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void enterCommunity(String param) {
        Logger.E(TAG, "enterCommunity:" + param);
        JSEnterCommunityParam paramObj = parse(param, JSEnterCommunityParam.class);
        if (paramObj != null) {
            enterCommunityIn(paramObj);
        }
    }

    public void enterExperienceIn(JSEnterExperienceParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void enterExperience(String param) {
        Logger.E(TAG, "enterExperience:" + param);
        JSEnterExperienceParam paramObj = parse(param, JSEnterExperienceParam.class);
        if (paramObj != null) {
            enterExperienceIn(paramObj);
        }
    }

    // chechFun
    @Override
    @JavascriptInterface
    public void checkFun(String param) {
        Logger.E(TAG, "checkFun" + param);
        JSCheckFunParam paramObj = parse(param, JSCheckFunParam.class);
        if (paramObj != null) {
            if (funSet != null) {
                Message msg = new Message();
                if (funSet.contains(paramObj.getFunName())) {
                    msg.obj = JSReturnParam.success(paramObj.getCallbackId(), null);
                } else {
                    msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), null);
                }
                msg.what = 9;
                handler.sendMessage(msg);
            }
        }
    }

    public void statusWindowIn(JSWindowStateParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void statusWindow(String param) {
        Logger.E(TAG, "statusWindow" + param);
        JSWindowStateParam paramObj = parse(param, JSWindowStateParam.class);
        if (paramObj != null) {
            statusWindowIn(paramObj);
        }
    }

    public void hilifepayIn(String param) {
    }

    @Override
    @JavascriptInterface
    public void hilifepay(String param) {
//        Logger.E(TAG, "payCheck" + param);
        if (!StringUtil.isBlank(param)) {
            hilifepayIn(param);
        }
    }

    public void payCheckIn(JSPaymentParam paramObj) {
    }

    public void wxpayIn(String param) {
    }

    public void alipayIn(String param) {
    }

    public void pingpayIn(String param) {
    }

    @Override
    @JavascriptInterface
    public void payCheck(String param) {
        Logger.E(TAG, "payCheck" + param);
        JSPaymentParam paramObj = parse(param, JSPaymentParam.class);
        if (paramObj != null) {
            payCheckIn(paramObj);
        }
    }

    @Override
    @JavascriptInterface
    public void wxpay(String param) {
        Logger.E(TAG, "wxpay" + param);
        if (!StringUtil.isBlank(param)) {
            wxpayIn(param);
        }
    }

    @Override
    @JavascriptInterface
    public void alipay(String param) {
        Logger.E(TAG, "alipay" + param);
        if (!StringUtil.isBlank(param)) {
            alipayIn(param);
        }
    }

    @Override
    @JavascriptInterface
    public void pingpay(String param) {
        Logger.E(TAG, "pingpay" + param);
        if (!StringUtil.isBlank(param)) {
            pingpayIn(param);
        }
    }

    @Override
    @JavascriptInterface
    public void getFile(String param) {
        Logger.D(TAG, "getFile:" + param);
        if (StringUtil.isNotEmpty(param)) {
            JsGetFileParam paramObj = parse(param, JsGetFileParam.class);
            getFileIn(paramObj);
        }
    }

    private void getFileIn(final JsGetFileParam paramObj) {
        if (paramObj != null) {
            if (Integer.valueOf(2).equals(paramObj.getType())) {
                String soundPath = SoundDownloadUtil.loadSound(mContext,
                        DJCacheUtil.readToken(), DJCacheUtil.readCommunityID(), paramObj.getFileID(), new SoundDownloadUtil.ISoundCallback() {
                            @Override
                            public void onSoundLoaded(String soundPath) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("fileID", paramObj.getFileID());
                                map.put("url", "file://" + soundPath);
                                Message msg = new Message();
                                msg.what = 9;
                                msg.obj = JSReturnParam.success(paramObj.getCallbackId(), map);
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onSoundError() {
                                Message msg = new Message();
                                msg.what = 9;
                                msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), null);
                                handler.sendMessage(msg);
                            }
                        });
                if (!StringUtil.isEmpty(soundPath)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("fileID", paramObj.getFileID());
                    map.put("url", "file://" + soundPath);
                    Message msg = new Message();
                    msg.what = 9;
                    msg.obj = JSReturnParam.success(paramObj.getCallbackId(), map);
                    handler.sendMessage(msg);
                }
            }
        }
    }

    public void feedActionIn(final JSActionParam paramObj) {
        if (paramObj != null && paramObj.getType() != null) {
            if (!VisitorCommunityUtil.isVisitorNeedAccess(mContext)) {
                switch (paramObj.getType()) {
                    case 1: {// 赞
                        if (Integer.valueOf(1).equals(paramObj.getAction())) {
                            ServiceFactory.getFeedService(mContext).insertPraise(paramObj.getObjID(), DJCacheUtil.readCommunityID(), new DataCallbackHandler<Void, Void, Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                                    handler.sendMessage(msg);
                                    super.onSuccess(aVoid);
                                }

                                @Override
                                public void onError(AppException e) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                                    handler.sendMessage(msg);
                                    super.onError(e);
                                }
                            });
                        } else if (Integer.valueOf(2).equals(paramObj.getAction())) {
                            ServiceFactory.getFeedService(mContext).deletePraise(paramObj.getObjID(), DJCacheUtil.readCommunityID(), new DataCallbackHandler<Void, Void, Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                                    handler.sendMessage(msg);
                                    super.onSuccess(aVoid);
                                }

                                @Override
                                public void onError(AppException e) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                                    handler.sendMessage(msg);
                                    super.onError(e);
                                }
                            });
                        }
                    }
                    break;
                    case 2: { // 收藏
                        if (Integer.valueOf(1).equals(paramObj.getAction())) {
                            ServiceFactory.getFavoriteService(mContext).add(paramObj.getObjID(), Constants.C_sFavoriteType_Feed,
                                    DJCacheUtil.readCommunityID(),
                                    new DefaultDataCallbackHandler<Void, Void, Integer>() {

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            Message msg = new Message();
                                            msg.what = 9;
                                            msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                                            handler.sendMessage(msg);
                                            super.onSuccess(integer);
                                        }

                                        @Override
                                        public void onError(AppException e) {
                                            Message msg = new Message();
                                            msg.what = 9;
                                            msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                                            handler.sendMessage(msg);
                                            super.onError(e);
                                        }
                                    });
                        } else if (Integer.valueOf(2).equals(paramObj.getAction())) {
                            ServiceFactory.getFavoriteService(mContext).cancel(paramObj.getObjID(), Constants.C_sFavoriteType_Feed,
                                    DJCacheUtil.readCommunityID(),
                                    new DefaultDataCallbackHandler<Void, Void, Integer>() {
                                        @Override
                                        public void onSuccess(Integer integer) {
                                            Message msg = new Message();
                                            msg.what = 9;
                                            msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                                            handler.sendMessage(msg);
                                            super.onSuccess(integer);
                                        }

                                        @Override
                                        public void onError(AppException e) {
                                            Message msg = new Message();
                                            msg.what = 9;
                                            msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                                            handler.sendMessage(msg);
                                            super.onError(e);
                                        }
                                    });
                        }
                    }
                    break;
                    default:
                        break;
                }
            } else {
                Message msg = new Message();
                msg.what = 9;
                msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    @JavascriptInterface
    public void feedAction(String param) {
        Logger.E(TAG, "feedAction" + param);
        if (StringUtil.isNotBlank(param)) {
            JSActionParam paramObj = parse(param, JSActionParam.class);
            feedActionIn(paramObj);
        }
    }

    public void addCommentIn(final JSAddCommentParam paramObj) {
        if (paramObj != null && StringUtil.isNotEmpty(paramObj.getFeedID())) {
            if (!VisitorCommunityUtil.isVisitorNeedAccess(mContext)) {
                ServiceFactory.getFeedService(mContext).getFeed(paramObj.getFeedID(), DJCacheUtil.readCommunityID(), new DefaultDataCallbackHandler<Void, Void, MFeed>() {
                    @Override
                    public void onSuccess(final MFeed feed) {
                        if (feed != null) {
                            if (StringUtil.isNotEmpty(paramObj.getCommentID())) {
                                ServiceFactory.getCommentService(mContext).getComment(DJCacheUtil.readCommunityID(), paramObj.getFeedID(), paramObj.getCommentID(), new DefaultDataCallbackHandler<Void, Void, MComment>(mContext.errorHandler) {
                                    @Override
                                    public void onSuccess(MComment comment) {
                                        if (comment != null) {
                                            Intent commentIntent = new Intent(mContext, CommentActivity.class);
                                            commentIntent.putExtra(Constants.CATEGORY, CommentActivity.COMMENT_COMMENT);
                                            commentIntent.putExtra("mFeed", feed);
                                            commentIntent.putExtra("comment", comment);
                                            mContext.startActivity(commentIntent);
                                        }
                                        super.onSuccess(comment);
                                    }
                                });
                            } else {
                                Intent commentIntent = new Intent(mContext, CommentActivity.class);
                                commentIntent.putExtra(Constants.CATEGORY, CommentActivity.COMMENT_FEED);
                                commentIntent.putExtra("mFeed", feed);
                                mContext.startActivity(commentIntent);
                            }
                        }
                        super.onSuccess(feed);
                    }
                });

            }
        }
    }

    @Override
    @JavascriptInterface
    public void addComment(String param) {
        Logger.E(TAG, "addComment" + param);
        if (StringUtil.isNotBlank(param)) {
            JSAddCommentParam paramObj = parse(param, JSAddCommentParam.class);
            addCommentIn(paramObj);
        }
    }

    public void requestFeedIn(JSTabFeedParam paramObj) {
    }

    @Override
    @JavascriptInterface
    public void requestFeed(String param) {
        Logger.E(TAG, "requestFeed" + param);
        if (StringUtil.isNotBlank(param)) {
            JSTabFeedParam paramObj = parse(param, JSTabFeedParam.class);
            requestFeedIn(paramObj);
        }
    }

    @Override
    @JavascriptInterface
    public void showFeedList(String param) {
        Logger.E(TAG, "showFeedList:" + param);
        if (StringUtil.isNotBlank(param)) {
            JSFeedListParam paramObj = parse(param, JSFeedListParam.class);
            if (paramObj != null) {
                if (Integer.valueOf(1).equals(paramObj.getType())) {
                    Intent intent = new Intent(mContext, TopicActivity.class);
                    intent.putExtra("topicID", paramObj.getObjID());
                    intent.putExtra("topicTitle", paramObj.getObjName());
                    mContext.startActivity(intent);
                }
            }
        }
    }

    @Override
    @JavascriptInterface
    public void showBlog(String param) {
        Logger.E(TAG, "showBlog:" + param);
        if (StringUtil.isNotBlank(param)) {
            JSBlogParam paramObj = parse(param, JSBlogParam.class);
            if (paramObj != null) {
                Intent blogIntent = new Intent(mContext, BlogDetailActivity.class);
                blogIntent.putExtra("mBlogID", paramObj.getBlogID());
                blogIntent.putExtra("feedID", paramObj.getFeedID());
                mContext.startActivity(blogIntent);
            }
        }
    }

    @Override
    @JavascriptInterface
    public void getThemeColor(String param) {
        Logger.D(TAG, "getThemeColor:" + param);
        if (StringUtil.isNotBlank(param)) {
            BaseJSParam paramObj = parse(param, BaseJSParam.class);
            if (paramObj != null) {
                Message msg = new Message();
                msg.what = 9;
                Map<String, String> resultMap = new HashMap<String, String>();
                String colorStr = Integer.toHexString(ThemeEngine.getInstance().getColor(Constants.TITLECOLOR, R.color.color_00ace6));
                if (StringUtil.isNotBlank(colorStr) && colorStr.length() >= 6) {
                    colorStr = "#" + colorStr.substring(colorStr.length() - 6);
                } else {
                    colorStr = "#00ace6";
                }
                resultMap.put("titleColor", colorStr);
                msg.obj = JSReturnParam.success(paramObj.getCallbackId(), resultMap);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    @JavascriptInterface
    public void getTopic(String param) {
        Logger.D(TAG, "getTopic:" + param);
        if (StringUtil.isNotBlank(param)) {
            final JsGetTopicParam paramObj = parse(param, JsGetTopicParam.class);
            if (paramObj != null) {
                ServiceFactory.getTopicService(mContext).loadPresetMenuByID(DJCacheUtil.readCommunityID(), paramObj.getTagID(), new DefaultDataCallbackHandler<Void, Void, PresetMenu>() {
                    @Override
                    public void onSuccess(PresetMenu presetMenu) {
                        if (null == presetMenu) {
                            Message msg = new Message();
                            msg.what = 9;
                            paramObj.setSupport(false);
                            msg.obj = JSReturnParam.fail(paramObj.getCallbackId(), paramObj);
                            handler.sendMessage(msg);
                            return;
                        }
                        String subMainPageCompanyID = presetMenu.getSubMainPageCompanyID();
                        if (StringUtil.isNotEmpty(subMainPageCompanyID) && presetMenu.getTopicPreset() == null) {
                            ServiceFactory.getTopicService(mContext).loadPresetMenuByID(subMainPageCompanyID, presetMenu.getmID(), new DefaultDataCallbackHandler<Void, Void, PresetMenu>() {
                                @Override
                                public void onSuccess(PresetMenu presetMenu) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    if (presetMenu != null && presetMenu.getTopicPreset() != null) {
                                        paramObj.setSupport(true);
                                    } else {
                                        paramObj.setSupport(false);
                                    }
                                    msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                                    handler.sendMessage(msg);
                                    super.onSuccess(presetMenu);
                                }
                            });
                        } else {
                            Message msg = new Message();
                            msg.what = 9;
                            if (presetMenu.getTopicPreset() != null) {
                                paramObj.setSupport(true);
                            } else {
                                paramObj.setSupport(false);
                            }
                            msg.obj = JSReturnParam.success(paramObj.getCallbackId(), paramObj);
                            handler.sendMessage(msg);
                            super.onSuccess(presetMenu);

                        }
                    }
                });
            }
        }
    }

    @Override
    @JavascriptInterface
    public void showAllCommunity() {
        Logger.D(TAG, "showAllCommunity");
        Intent intent = new Intent(mContext, CommunityCategoryActivity.class);
        intent.putExtra("noLogin", Constants.GUEST_NOLOGIN_BACK);
        mContext.startActivity(intent);
    }

    public <T> T parse(String param, Class<T> c) {
        try {
            return JSONUtil.parseJSON(param, c);
        } catch (AppException e) {
            DJToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.tips_parsing_error));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @JavascriptInterface
    public void sportRefresh(String param) {
        Logger.D(TAG, "sportRefresh");
    }

    @Override
    @JavascriptInterface
    public void getWebHost(String param) {
        Logger.D(TAG, "getWebHost");
    }

    @Override
    @JavascriptInterface
    public void ajaxProxy(String param) {
        final Map paraMap = JSONUtil.parseJSON(param, Map.class);
        final String callbackId = (String) paraMap.get("callbackId");
        if (null != paraMap) {
            if (paraMap.containsKey("url")) {
                String url = (String) paraMap.get("url");
                if (StringUtil.isEmpty(url)) return;
                String methodType = "get";
                Map<Object, Object> mapForRequest = new HashMap();
                if (paraMap.containsKey("data")) {
                    Map dataMap = (Map) paraMap.get("data");
                    if (null != dataMap) {
                        for (Iterator iterator = dataMap.keySet().iterator(); iterator.hasNext(); ) {
                            Object next = iterator.next();
                            if (next instanceof String && ((String) next).equalsIgnoreCase("access_token"))
                                continue;
                            Object nextValue = dataMap.get(next);
                            if (null != nextValue && !"{}".equals(String.valueOf(nextValue))) {
                                if (nextValue instanceof Double) {
                                    String doubleStr = String.valueOf(nextValue);
                                    mapForRequest.put(next, doubleStr.substring(0, doubleStr.indexOf(".")));
                                } else {
                                    mapForRequest.put(next, nextValue);
                                }
                            }
                        }
                    }
                }
                if (paraMap.containsKey("type")) {
                    methodType = (String) paraMap.get("type");
                }
                if (null != mapForRequest) {
                    mapForRequest.put("UserAgent", new WebViewUserAgent(mContext).toString());
                }
                String url_whole = "";
                if (!url.contains("http")) {
                    url_whole = Configuration.getWebHost(mContext) + url;
                } else {
                    url_whole = url;
                }
                Log.v("ajaxProxy", "---- " + param);
                Log.v("ajaxProxy", "----2 " + url_whole);
                ServiceFactory.getH5TemplateService(mContext).ajaxProxy(url_whole, methodType, mapForRequest, new DataCallbackHandler<Void, Void, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Message msg = new Message();
                        msg.what = 9;
                        try {
                            String encodingStr = "AX005" + URLEncoder.encode(s.replaceAll(" ", "%20"), "utf-8");
                            msg.obj = JSReturnParam.success(callbackId, encodingStr);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                        // ajax执行结束时，方式广播设置标题
                        Intent intent = new Intent();
                        intent.setAction(Constants.BROADCAST_TYPE_AJAX);
                        intent.putExtra("result", "success");
                        mContext.sendBroadcast(intent);
//						super.onSuccess(s);
                    }

                    @Override
                    public void onError(AppException e) {
//						super.onError(e);
                        Message msg = new Message();
                        msg.what = 9;
                        msg.obj = JSReturnParam.fail(callbackId, "");
                        handler.sendMessage(msg);
                        // ajax执行结束时，方式广播设置标题
                        Intent intent = new Intent();
                        intent.setAction(Constants.BROADCAST_TYPE_AJAX);
                        intent.putExtra("result", "fail");
                        mContext.sendBroadcast(intent);
                    }
                });
            }
        } else {
            Message msg = new Message();
            msg.what = 9;
            msg.obj = JSReturnParam.fail(callbackId, "");
            handler.sendMessage(msg);
        }
    }

    @Override
    @JavascriptInterface
    public void getMySelfSendFeed(String param) {
        Map parse = parse(param, Map.class);
        if (null != parse) {
            String enterMyProfile = (String) parse.get("enterMyProfile");
            if ("1".equals(enterMyProfile)) {
                Intent intent = new Intent(mContext, PersonActivity.class);
                intent.putExtra("personID", DJCacheUtil.readPersonID());
                mContext.startActivity(intent);
                return;
            }
        }
        Intent myfeedIntent = new Intent(mContext, MyFeedActivity.class);
        myfeedIntent.putExtra("show_title", mContext.getString(R.string.title_mysend));
        mContext.startActivity(myfeedIntent);

    }

    @Override
    @JavascriptInterface
    public void getMySelfCollections() {
        Intent favIntent = new Intent(mContext, PersonFavActivity.class);
        favIntent.putExtra("show_title", mContext.getString(R.string.title_favorite));
        mContext.startActivity(favIntent);
    }

    @Override
    @JavascriptInterface
    public void findSendFeedQuque() {
        Intent sendingIntent = new Intent(mContext, SendingActivity.class);
        sendingIntent.putExtra("show_title", mContext.getString(R.string.title_send_queue));
        mContext.startActivity(sendingIntent);
    }

    @Override
    @JavascriptInterface
    public void sendInvitation() {
        Intent inviteIntent = new Intent(mContext, InviteActivity.class);
        inviteIntent.putExtra("show_title", mContext.getString(R.string.title_invite));
        mContext.startActivity(inviteIntent);
    }

    @Override
    @JavascriptInterface
    public void systemSetting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    @JavascriptInterface
    public void systemNotice() {
        Intent intentNotification = new Intent(mContext, CommonActivity.class);
        intentNotification.putExtra("code", Constants.TOPIC_CODE_MESSAGE);
        intentNotification.putExtra("from", Constants.APPFROM_NOTIFICTION);
        PresetMenu presetMenu = new PresetMenu();
        presetMenu.setCode(Constants.TOPIC_CODE_MESSAGE);
        presetMenu.setmName(mContext.getString(R.string.menu_notify));
        intentNotification.putExtra("presetMenu", presetMenu);
        mContext.startActivity(intentNotification);
    }

    @Override
    @JavascriptInterface
    public void myAddressBookInfo() {
        Intent data = new Intent();
        PresetMenu presetMenu = new PresetMenu();
        presetMenu.setCode(Constants.TOPIC_CODE_ADDRESSBOOK);
        data.putExtra("presetMenu", presetMenu);
        presetMenu.setmName(mContext.getString(R.string.menu_contact));
        IntentUtil.openIntent(mContext, data, presetMenu.getCode());
    }

    @Override
    @JavascriptInterface
    public void myGroupInfo() {
        Intent data = new Intent();
        PresetMenu presetMenu = new PresetMenu();
        presetMenu.setCode(Constants.TOPIC_CODE_GROUP);
        data.putExtra("presetMenu", presetMenu);
        presetMenu.setmName(mContext.getString(R.string.menu_group));
        IntentUtil.openIntent(mContext, data, presetMenu.getCode());
    }

    @Override
    @JavascriptInterface
    public void myPersonInfo() {
        Intent webIntent = new Intent(mContext, WebActivity.class);
        int category = Constants.WEB_SHOP;
        webIntent.putExtra("category", category);
        String url = Configuration.getPersonDetailUrl(mContext, DJCacheUtil.readPersonID());
        webIntent.putExtra("web_url", url);
        // webIntent.putExtra("title", mContext.getResources().getString(R.string.btn_self_profile));
        //webIntent.putExtra("tagName", mContext.getResources().getString(R.string.btn_self_profile));
        webIntent.putExtra("sourceID", Constants.TOPIC_CODE_PERSONAL_INFO);
        webIntent.putExtra("sourceType", Constants.TIPOFF_TYPE_PRESET);
        mContext.startActivity(webIntent);
    }

    @Override
    @JavascriptInterface
    public void myCustomerServiceInfo() {
        // IM客服
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                .appendPath(Conversation.ConversationType.CUSTOMER_SERVICE.getName().toLowerCase())
                .appendQueryParameter("targetId", "")// TODO: 4/17/21 添加自定义客服
                .appendQueryParameter("title", "")
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra("customServiceInfo", Conversation.ConversationType.CUSTOMER_SERVICE);
        mContext.startActivity(intent);
    }

    @Override
    @JavascriptInterface
    public void getCustomMainPage(String param) {
        Logger.D(TAG, "getCustomMainPage");
        getCustomMainPageIn(param);
    }

    public void getCustomMainPageIn(String param) {
    }

    @Override
    @JavascriptInterface
    public void customMainPageFinished(String param) {
        Logger.D(TAG, "customMainPageFinished");
        customMainPageFinishedIn(param);
    }

    public void customMainPageFinishedIn(String param) {
    }

    @Override
    @JavascriptInterface
    public void openThreeMapNav(String param) {
        Logger.D("openThreeMapNav", param);
        Map parse = parse(param, Map.class);
        if (null != parse) {
            String targetName = (String) parse.get("targetName");  //目的地名称
            String mapType = (String) parse.get("mapType");        //你要打开地图的类型
            String targetLatitude = (String) parse.get("targetLatitude");
            String targetLongitude = (String) parse.get("targetLongitude");
            String directionsmode = (String) parse.get("directionsmode");  // 路线 是走路还是开车 默认 开车？
            if (StringUtil.isEmpty(directionsmode)) {
                directionsmode = "driving"; //默认开车
            }
            if (StringUtil.isNotEmpty(mapType) && mapType.equals("1")) {
                if (Utils.isAvilible(mContext, "com.baidu.BaiduMap")) {
                    Intent intent2 = new Intent();
                    intent2.setData(Uri.parse("baidumap://map/direction?"
                            //+ "origin=30.585515,104.061247&"  起点是当前位置就不用传
                            + "destination=" + targetLatitude + "," + targetLongitude
                            + "&mode=" + directionsmode));
                    mContext.startActivity(intent2);
                } else {
                    //没有就下载百度app
                    Utils.launchAppDetail(mContext, "com.baidu.BaiduMap", null);
                }
            } else if (StringUtil.isNotEmpty(mapType) && mapType.equals("2")) {
                if (Utils.isAvilible(mContext, "com.autonavi.minimap")) {
                    String t = "0";
                    if ("driving".equals(directionsmode)) {
                        t = "0";
                    } else if ("walking".equals(directionsmode)) {
                        t = "4";
                    }
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("androidamap://route?sourceApplication="
                            + mContext.getString(R.string.app_name)
                            //+ "&slat=30.585515" + "&slon=104.061247"   起点是当前位置就不用传
                            + "&dlat=" + targetLatitude + "&dlon=" + targetLongitude
                            + "&dev=0&t=" + t    //t=0驾车 t=1公交 t=4步行
                            + "&dname=" + targetName));  ///目的地名称
                    mContext.startActivity(intent);
                } else {
                    Utils.launchAppDetail(mContext, "com.autonavi.minimap", null);
                }
            } else if (StringUtil.isNotEmpty(mapType) && mapType.equals("3")) {
                String t = "drive";
                if ("driving".equals(directionsmode)) {
                    t = "drive";
                } else if ("walking".equals(directionsmode)) {
                    t = "walk";
                }
                if (Utils.isAvilible(mContext, "com.tencent.map")) {
                    Intent intent1 = new Intent();
                    intent1.setData(Uri.parse("qqmap://map/routeplan?"
                            + "type=" + t
                            //+ "&fromcoord=39.873145,116.413306"
                            + "&to=" + targetName + "&tocoord=" + targetLatitude + "," + targetLongitude));  ///目的地名称
                    mContext.startActivity(intent1);
                } else {
                    Utils.launchAppDetail(mContext, "com.tencent.map", null);
                }
            }
        }
    }

    @Override
    @JavascriptInterface
    public void showMyCommunityList() {
        if (StringUtil.isEmpty(DJCacheUtil.readToken())) {
            Intent intent = new Intent(mContext, CommunityCategoryActivity.class);
            intent.putExtra("noLogin", Constants.GUEST_NOLOGIN_BACK_IN);
            mContext.startActivity(intent);
        } else {
            if (!DJCacheUtil.readBoolean(mContext, Constants.PERSON_HAS_COMMUNITY, false) && Configuration.ExpState.ExpStateNo == Configuration.getMISEXP(mContext)) {
                Intent intent = new Intent(mContext, CommunityCategoryActivity.class);
                intent.putExtra("noLogin", Constants.GUEST_NOLOGIN_BACK_OUT);
                mContext.startActivity(intent);
            } else {
                if (Configuration.ExpState.ExpStateNo != Configuration.getMISEXP(mContext)) {
                    if (Configuration.ExpState.ExpStateFormal == Configuration.getMISEXP(mContext)) {
                        DJCacheUtil.clear(DJCacheUtil.ClearType.ClearTypeExperienced);
                        Configuration.setMISEXP(mContext, Configuration.ExpState.ExpStateNo);
                        ((GlobalApplication) mContext.getApplication()).exitToMainActivity(mContext);
                        Intent communityIntent = new Intent(mContext, MCommunityActivity.class);
                        mContext.startActivityForResult(communityIntent, Constants.REQUEST_COMMUNITY);
                        IntentUtil.openTemplateList(mContext, Constants.WEB_COMMUNITY_TEMPLATE);
                    } else {
                        IntentUtil.openTemplateList(mContext, Constants.WEB_COMMUNITY_TEMPLATE_NO_SUBMIT);
                    }
                } else {
                    Intent communityIntent = new Intent(mContext, MCommunityActivity.class);
                    mContext.startActivityForResult(communityIntent, Constants.REQUEST_COMMUNITY);
                    mContext.overridePendingTransition(R.anim.push_up_in, R.anim.hold);
                }
            }
        }
    }

    @Override
    @JavascriptInterface
    public void addCommentInfo(String param) {
        Logger.E(TAG, "addCommentInfo" + param);
        if (StringUtil.isNotBlank(param)) {
            Map paramObj = null;
            try {
                paramObj = JSONUtil.parseJSON(param, Map.class);
            } catch (Exception e) {
                param = param.replace(":\"{", ":{").replace("}\",", "},");
                paramObj = parse(param, Map.class);
            }
            addCommentInfoIn(paramObj);
        }

    }

    public void addCommentInfoIn(final Map paramObj) {
        if (null != paramObj) {
            final String callbackId = (String) paramObj.get("callbackId");
            Double replyType1 = (Double) paramObj.get("replyType"); //0对feed的评论,1对评论的评论,2对笔记的评论
            Integer replyType = 0;
            if (null != replyType1) {
                replyType = (int) (double) replyType1; //0对feed的评论,1对评论的评论,2对笔记的评论
            }
            Integer from = (int) (double) (Double) paramObj.get("from");  //默认0
            String objType = (String) paramObj.get("objType");  //默认"feed"
            String showMenu = (String) paramObj.get("showMenu");  //回复界面显示类型
            Integer messageHiding = (Integer) paramObj.get("messageHiding");
            String objID = (String) paramObj.get("objID");  //feedID
            Double ctoMessageHiding1 = (Double) paramObj.get("ctoMessageHiding");
            Integer ctoMessageHiding = null;
            if (null != ctoMessageHiding1) {
                ctoMessageHiding = (int) (double) ctoMessageHiding1; //0对feed的评论,1对评论的评论,2对笔记的评论
            }
            String ctoMessage = (String) paramObj.get("ctoMessage");  //对什么信息回复可能是feed信息 可能是评论信息
            String objMessage = (String) paramObj.get("objMessage");    //feed信息
            String toAuthorId = (String) paramObj.get("toAuthorId");    //如果是对feed评论就是原始作者personId了
            String toAuthorName = (String) paramObj.get("toAuthorName");//如果是对feed评论就是原始作者名字了
            String originalAuthorId = (String) paramObj.get("originalAuthorId"); //原始作者的personId
            Map extensionDict = (Map) paramObj.get("extensionDict");
            String commentID = (String) paramObj.get("commentID");
            String communityID = (String) paramObj.get("communityID"); //一般有值的话就是submainpageID
            MComment mComment = new MComment();
            if (StringUtil.isNotEmpty(objType)) {
                mComment.setObjType(objType);
            } else {
                mComment.setObjType("feed");
            }
            mComment.setMessageHiding(messageHiding);
            mComment.setObjID(objID);
            mComment.setCommentID(commentID);   //一般为空
            mComment.setCtomessageHiding(ctoMessageHiding);
            mComment.setCtoMessage(ctoMessage);
            mComment.setObjMessage(objMessage);
            mComment.setPersonID(originalAuthorId);  //feed作者原始personID
            mComment.setToAuthorId(toAuthorId);
            mComment.setToAuthorName(toAuthorName);
            mComment.setReplyType(replyType);
            mComment.setExtensionDict(JSONUtil.toJSON(extensionDict));
            UploadCommentBean uploadCommentBean = new UploadCommentBean();
            int c_iFileFrom_feed = Constants.C_iFileFrom_Feed;
            if (from != 0) {
                c_iFileFrom_feed = from;
            }
            uploadCommentBean.from = c_iFileFrom_feed;
            uploadCommentBean.comment = mComment;
            uploadCommentBean.commentID = UUIDUtil.LongUUID();
            if (StringUtil.isEmpty(communityID)) {
                communityID = DJCacheUtil.readCommunityID();
            }
            uploadCommentBean.communityID = communityID;
            uploadCommentBean.userID = DJCacheUtil.readPersonID();
            Intent commentIntent = new Intent(mContext, CommentActivity.class);
            commentIntent.putExtra(Constants.CATEGORY, CommentActivity.COMMENT_WEB);    //来自jssdk
            commentIntent.putExtra("uploadCommentBean", uploadCommentBean);
            if (!StringUtil.isEmpty(showMenu)) {
                if (showMenu.equals("1")) {
                    commentIntent.putExtra("isNote", true);
                }
            } else if (replyType == 2) {
                commentIntent.putExtra("isNote", true);
            }
            mContext.startActivity(commentIntent);
            //回复成功后的回调
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // 评论发送成功，需要刷新评论数量
                    String type = intent.getStringExtra("type");
                    if (Constants.MESSAGE_SEND_SUCCESS.equals(type)) {
                        Message msg = new Message();
                        msg.what = 9;
                        msg.obj = JSReturnParam.success(callbackId, paramObj);
                        handler.sendMessage(msg);
                    } else if (Constants.MESSAGE_SEND_FAIL.equals(type)) {
                        Message msg = new Message();
                        msg.what = 9;
                        msg.obj = JSReturnParam.fail(callbackId, "");
                        handler.sendMessage(msg);
                    }
                    mContext.unregisterReceiver(this);
                }
            };
            mContext.registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_TYPE_COMMENT));
        }
    }

    @Override
    @JavascriptInterface
    public void goBackToSpecifiedPage(String param) {
        //返回指定页面
        String pageIndex = null;
        String refreshPage = null;
        String notSkip = null;
        if (!StringUtil.isEmpty(param)) {
            Map<String, Object> map = JSONUtil.parseJSON(param, Map.class);
            if (null != map) {
                pageIndex = (String) map.get("pageIndex");
                refreshPage = (String) map.get("refreshPage");
                notSkip = (String) map.get("notSkip");  //是否不关闭页面只到指定页面时刷新
            }
        }
        LinkedList<Activity> activityList = ((GlobalApplication) GlobalApplication.getContext()).getActivityList();
        LinkedList<Activity> activities = new LinkedList<>();
        boolean begin = false;
        for (Activity activity : activityList) {
            if (activity instanceof MainActivity) {
                begin = true;
            }
            if (begin) {
                activities.add(activity);
            }
        }
        if (StringUtil.isEmpty(pageIndex)) {
            return;
        }
        //结束指定页面前面所有的页面
        int page = Integer.valueOf(pageIndex);
        if (page + 1 < activities.size() && page >= 0 && !"0".equals(notSkip)) {
            for (int i = page + 1; i < activities.size(); i++) {
                activities.get(i).finish();
            }
        }
        //是否刷新最前面的页面
        if (page < activities.size() && StringUtil.isNotEmpty(refreshPage) && refreshPage.equals("1")) {
            Activity lastActivity = activities.get(page);
            lastActivity.getIntent().putExtra("refresh", "1");
        }
    }

    @Override
    @JavascriptInterface
    public void getSpecifiedPageCount(String param) {
        //获取web页面个数
        String callbackId = null;
        Map<String, Object> map1 = new HashMap<>();
        if (!StringUtil.isEmpty(param)) {
            Map<String, Object> map = JSONUtil.parseJSON(param, Map.class);
            if (null != map && map.containsKey("callbackId")) {
                callbackId = (String) map.get("callbackId");
            }
        }
        LinkedList<Activity> activityList = ((GlobalApplication) GlobalApplication.getContext()).getActivityList();
        LinkedList<Map> webList = new LinkedList<Map>();
        int pageCount = 0;
        boolean begin = false;
        for (Activity activity : activityList) {
            if (activity instanceof MainActivity) {
                begin = true;
            }
            if (begin) {
                Map<String, Object> map = new HashMap<>();
                map.put("className", activity.getClass().getSimpleName());
                if (activity instanceof WebActivity) {
                    //map.put("firstUrl", ((WebActivity) activity).getIntent().getStringExtra("web_url"));
                    //map.put("currentUrl", ((WebActivity) activity).getIntent().getStringExtra("currentUrl"));
                    map.put("isWeb", "0");
                    webList.add(map);
                } else if (activity instanceof CommonActivity) {
                    //map.put("firstUrl", ((CommonActivity) activity).getIntent().getStringExtra("web_url"));
                    //map.put("currentUrl", ((CommonActivity) activity).getIntent().getStringExtra("currentUrl"));
                    map.put("isWeb", "1");
                    webList.add(map);
                } else {
                    //map.put("firstUrl", "");
                    //map.put("currentUrl", "");
                    map.put("isWeb", "1");
                    webList.add(map);
                }
                pageCount++;
            }
        }
        map1.put("count", pageCount + "");
        map1.put("pages", webList);
        JSReturnParam jsReturnParam;
        jsReturnParam = JSReturnParam.success(callbackId, map1);
        Message msg = handler.obtainMessage(9);
        msg.obj = jsReturnParam;
        handler.sendMessage(msg);
    }

    @Override
    @JavascriptInterface
    public void updateUnReadDataByTagID(String param) {
        Logger.D(TAG, "updateUnReadDataByTagID");
        updateUnReadDataByTagIDIn(param);
    }

    public void updateUnReadDataByTagIDIn(String param) {
    }

    @Override
    @JavascriptInterface
    public void umBridge(String param) {
        Logger.D(TAG, "umBridge----->" + param);
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

    @JavascriptInterface
    @Override
    public void h5ToNativeJs(String param) {
        NativeJsParam nativeJsParam = new Gson().fromJson(param, NativeJsParam.class);
        String type = nativeJsParam.getType();
        if (!TextUtils.isEmpty(type) && !type.equals("PosterShare")) {
            JsHandlerDispatcher.getInstance().dispatch(param, webView, mContext, handler);
            return;
        }
        Logger.D(TAG, "h5ToNativeJs---->" + param);
    }

    @JavascriptInterface
    @Override
    public void openFlutterPage(String param) {

    }

    @Override
    @JavascriptInterface
    public void goAppHomePage() {

        if (webView == null) {
            return;
        }
        Intent intent = new Intent(webView.getContext(), MainActivity.class);
        webView.getContext().startActivity(intent);
    }
}
