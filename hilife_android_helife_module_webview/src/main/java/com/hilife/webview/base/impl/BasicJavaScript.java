package com.hilife.webview.base.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import com.hilife.webview.R;
import com.hilife.webview.base.def.DefaultInJavaScript;
import com.hilife.webview.model.WebViewDaJiaParam;
import com.hilife.webview.ui.WebActivity;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import cn.net.cyberwy.hopson.lib_custom_views.dialog.DialogUtil;
import cn.net.cyberwy.hopson.lib_util.JSONUtil;
import cn.net.cyberwy.hopson.lib_util.StringUtil;
import cn.net.cyberwy.hopson.sdk_public_base.constant.Constants;


/**
 * Created by hewanxian on 15/10/26.
 */
public class BasicJavaScript extends DefaultInJavaScript {

    private ShareUtils shareUtils;
    private String webDesc;
    protected String sourceID;
    protected String sourceType;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3: {
                    Map<String, String> params = (Map<String, String>) msg.obj;
                    String communityID = params.get("communityID");
                    Intent mIntent = new Intent(mActivity, QrcodeResultActivity.class);
                    mIntent.putExtra("joinCommunityID", communityID);
                    mActivity.startActivity(mIntent);
                }
                break;
                case 4: {
                    Map<String, String> params = (Map<String, String>) msg.obj;
                    String url = Configuration.getWebUrl(mActivity, R.string.community_join_url) +
                            "?cID=" + params.get("communityID") + "&cName=" + params.get("communityName");
                    Intent mIntent = new Intent(mActivity, WebActivity.class);
                    mIntent.putExtra("web_url", url);
                    mActivity.startActivity(mIntent);
                }
                break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public BasicJavaScript(Activity activity, Context mContext, ShareUtils shareUtils) {
        super(activity);
        this.mContext = mContext;
        this.shareUtils = shareUtils;
    }

    public BasicJavaScript(Activity activity) {
        super(activity);
    }


    @Override
    @JavascriptInterface
    public void getWebDesc(String summary) {
        webDesc = summary;
    }


    @Override
    @JavascriptInterface
    public void goOpenFile(String fileID, String fileName, String widgetID,
                           String contentID, String shortChain, String fileSize) {
        String communityID = DJCacheUtil.readCommunityID();
        if (!StringUtil.isEmpty(communityID)) {
            Intent intent = new Intent();
            intent.setClass(mActivity, FileActivity.class);
            intent.putExtra(Constants.CATEGORY, FileActivity.FILE_TRANFORM);
            MAttachment mAttachment = new MAttachment();
            mAttachment.setFileID(fileID);
            mAttachment.setFileName(fileName);
            if (StringUtil.isNotBlank(fileSize)) {
                mAttachment.setFileSize(Long.parseLong(fileSize));
            }
            if (StringUtil.isNotBlank(fileName)) {
                int lastIndex = fileName.lastIndexOf(".");
                if (lastIndex != -1 && fileName.length() > (lastIndex + 1)) {
                    mAttachment.setFileSuffix(fileName.substring(lastIndex + 1));
                } else {
                    mAttachment.setFileSuffix(fileName);
                }
            }
            intent.putExtra("mAttachment", mAttachment);
            intent.putExtra("shortChain", shortChain);
            intent.putExtra("sourceID", sourceID);
            intent.putExtra("sourceType", sourceType);
            mActivity.startActivity(intent);
        }
        super.goOpenFile(fileID, fileName, widgetID, contentID, shortChain, fileSize);
    }


    @Override
    @JavascriptInterface
    public void goBackup() {
        mActivity.finish();
    }

    @JavascriptInterface
    public void initShareOptMenu(String param) {
        Toast.makeText(mContext, "jiushou", Toast.LENGTH_SHORT).show();
    }

    @Override
    @JavascriptInterface
    public void getWebParam(String webParamJson) {

        if (null != shareUtils) {
            if (StringUtil.isNotEmpty(webParamJson)) {
                shareUtils.webParam = JSONUtil.parseJSON(webParamJson, WebViewDaJiaParam.class);
            } else {
                shareUtils.webParam = null;
            }
        }
    }

    @Override
    @JavascriptInterface
    public void setTipoffID(String sourceID) {
        if (StringUtil.isNotEmpty(sourceID)) {
            this.sourceID = sourceID;
        }
    }


    @Override
    @JavascriptInterface
    public void setTipoffType(String sourceType) {
        if (StringUtil.isNotEmpty(sourceType)) {
            this.sourceType = sourceType;
        }
    }

    @Override
    @JavascriptInterface
    public void openSuccess() {
    }

    @Override
    @JavascriptInterface
    public void Finish() {
    }

    @Override
    @JavascriptInterface
    public synchronized void openFailed() {
    }


    @Override
    @JavascriptInterface
    public void joinWork(String communityID, String communityName) {
        final Message message = Message.obtain();

        Logger.t("注册流程").i("joinWork");
        Map<String, String> params = new HashMap<String, String>();
        params.put("communityID", communityID);
        params.put("communityName", communityName);
        message.obj = params;
        //账号隔离社区只能加入本企业社区，否则无效
        final String customID = CacheAppData.read(mActivity, BaseConstant.MOBILE_ACCOUNT_ISOLATION, "10000");
        if ("10000".equals(customID)) {
            CommunityService communityService = ServiceFactory.getCommunityService(mActivity);
            communityService.accessCommunity(DJCacheUtil.readPersonID(), communityID, new DefaultDataCallbackHandler<Void, Void, MCommunity>() {
                @Override
                public void onSuccess(MCommunity com) {
                    if (com != null && (customID.equals(com.getCustomID()) || com.getCustomID() == null)) {
                        message.what = 3;
                    } else {
                        message.what = 4;
                    }
                    handler.sendMessage(message);
                }
            });
        } else {
            if ((communityID != null && communityID.startsWith(customID))) {
                message.what = 3;
            } else {
                message.what = 4;
            }
            handler.sendMessage(message);
        }
    }

    @Override
    @JavascriptInterface
    public void registerSuccess(String username, String password, String cStatus) {
        Logger.t("注册流程").i("registerSuccess");
        Intent data = new Intent();
        data.putExtra("username", username);
        data.putExtra("password", password);
        data.putExtra("cStatus", cStatus);
        mActivity.setResult(Constants.WEB_REGISTER, data);
        DJToastUtil.showMessage(mActivity, mActivity.getResources().getString(R.string.web_registered_success));
        mActivity.finish();
        mActivity.overridePendingTransition(0, R.anim.slide_down_out);
    }

    @Override
    @JavascriptInterface
    public void pLogin(String phone) {
        Intent data = new Intent();
        Logger.t("注册流程").i("pLogin");
        data.putExtra("username", phone);
        mActivity.setResult(Constants.WEB_REGISTER, data);
        mActivity.finish();
    }

    /**
     * 与黄总确认
     */
    @Override
    @JavascriptInterface
    public void modifyPersonInfoSuccess() {
        DJToastUtil.showImageToast(mActivity, mActivity.getResources().getString(R.string.web_save_success), R.drawable.prompt_success, Toast.LENGTH_SHORT);
        mActivity.setResult(PersonFragment.PERSON_EDIT_SUCCESS);
        mActivity.finish();
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    @JavascriptInterface
    public void modifyPasswordSuccess(String password) {
        DialogUtil.showAlert(mContext,
                mContext.getResources().getString(R.string.relogin_exit_sure),
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, mContext.getResources().getString(R.string.reLogin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        NormalUtils.doLogout(mActivity, (GlobalApplication) mActivity.getApplication(), false);
                    }
                }, false).show();

    }

    public String getWebDesc() {
        return webDesc;
    }

    public String getSourceID() {
        return sourceID;
    }

    public String getSourceType() {
        return sourceType;
    }

    @JavascriptInterface
    public void onInputFoucs(String inputID, String type, String inputValue) {
    }

}
