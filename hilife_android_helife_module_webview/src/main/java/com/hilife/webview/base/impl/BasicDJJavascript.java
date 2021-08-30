package com.hilife.webview.base.impl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.hilife.webview.R;
import com.hilife.webview.base.def.DJJavascriptImpl;
import com.hilife.webview.model.js.JSEnterCommunityParam;
import com.hilife.webview.model.js.JSJoinCommunityParam;
import com.hilife.webview.ui.WebActivity;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebView;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.net.cyberwy.hopson.lib_framework.base.BaseActivity;

/**
 * BasicDJJavascript
 *
 * @author hewanxian
 * @date 15/10/28
 * Copyright © 2015年 DJAppVideo. All rights reserved.
 */
public class BasicDJJavascript extends DJJavascriptImpl {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3: {
                    Map<String, String> params = (Map<String, String>) msg.obj;
                    String communityID = params.get("communityID");
                    Intent mIntent = new Intent(mContext, QrcodeResultActivity.class);
                    mIntent.putExtra("joinCommunityID", communityID);
                    mContext.startActivity(mIntent);
                }
                break;
                case 4: {
                    Map<String, String> params = (Map<String, String>) msg.obj;
                    String url = Configuration.getWebUrl(mContext, R.string.community_join_url) +
                            "?cID="+params.get("communityID");
                    Intent mIntent =new Intent(mContext, WebActivity.class);
                    mIntent.putExtra("web_url", url);
                    mContext.startActivity(mIntent);
                }
                break;
            }
            super.handleMessage(msg);
        }
    };

    public BasicDJJavascript(BaseActivity context, WebView webView) {
        super(context, webView);
    }



    @Override
    public void joinCommunityIn(JSJoinCommunityParam paramObj) {

        Logger.t("注册流程").i("joinCommunityIn");
        if (paramObj != null) {
            final Message message = Message.obtain();
            Map<String, String> params = new HashMap<String, String>();
            String communityID = paramObj.getCommunityID();
            params.put("communityID", communityID);
            params.put("jsCallbackId",paramObj.getCallbackId());
            message.obj = params;

            //账号隔离社区只能加入本企业社区，否则无效
            final String customID = CacheAppData.read(mContext, BaseConstant.MOBILE_ACCOUNT_ISOLATION, "10000");
            if("10000".equals(customID)){
                CommunityService communityService = ServiceFactory.getCommunityService(mContext);
                communityService.accessCommunity(DJCacheUtil.readPersonID(), communityID,new DefaultDataCallbackHandler<Void, Void, MCommunity>() {
                    @Override
                    public void onSuccess(MCommunity com) {
                        if(com != null && (customID.equals(com.getCustomID()) || com.getCustomID() == null )){
                            message.what = 3;
                        }else{
                            message.what = 4;
                        }
                        handler.sendMessage(message);
                    }
                });
            }else{
                if((communityID != null && communityID.startsWith(customID))){
                    message.what = 3;
                }else{
                    message.what = 4;
                }
                handler.sendMessage(message);
            }

        }
        super.joinCommunityIn(paramObj);
    }

    @Override
    public void enterCommunityIn(JSEnterCommunityParam paramObj) {
        Logger.t("注册流程").i("enterCommunityIn");
        if (paramObj != null) {
            DJCacheUtil.keepCommunityID(paramObj.getCommunityID());
            DJCacheUtil.keep(DJCacheUtil.COMMUNITY_NAME, paramObj.getCommunityName());
            ((GlobalApplication)GlobalApplication.getContext()).exitToMainActivity(mContext);
            mContext.overridePendingTransition(R.anim.hold, R.anim.slide_down_out);
        }
    }

    @Override
    public void setupSupportMethod() {
        Set<String> set = new HashSet<String>();
        set.add("enterCommunity");
        set.add("joinCommunity");
        addSupportMethods(set);
    }


    @Override
    public void umBridge(String param) {
        super.umBridge(param);
        Log.i("um-----","DJJS-"+param);
    }


}
