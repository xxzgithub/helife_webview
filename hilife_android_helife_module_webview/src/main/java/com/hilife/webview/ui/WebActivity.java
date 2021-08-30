package com.hilife.webview.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dajia.android.base.util.StringUtil;
import com.dajia.mobile.esn.model.community.MCommunity;
import com.hilife.view.R;
import com.hilife.view.main.base.DajiaBaseActivity;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.main.ui.MainActivity;
import com.hilife.view.mcompnents.LogComponent;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.view.other.component.skin.ThemeEngine;
import com.hilife.view.other.component.webview.ui.xwalk.WebFragment;
import com.hilife.view.other.context.GlobalApplication;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.other.util.PhoneUtil;
import com.hilife.view.other.util.Utils;
import com.hilife.view.other.widget.IconView;
import com.hilife.view.share.tools.ShareMessegeEvent;
import com.hilife.view.weight.Configuration;
import com.hopson.hilife.commonbase.util.StatusBarUtil;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.net.cyberwy.hopson.sdk_public_base_service.router.RouterHub;


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
 * Constants.WEB_COLLECT_CAN_SKIP 社区收集信息 可跳过
 * web_url 需要打开的链接
 */

@Route(path = RouterHub.WBVIEW_ACTIVITY)
public class WebActivity extends DajiaBaseActivity implements AttachDelegate {

    private RelativeLayout top_bar;
    private IconView topLeftIC;
    private TextView goback_tv;
    private TextView close_tv;
    private TextView topTitleTV;
    private TextView topRightTV;
    private LinearLayout topbar_right;
    private LinearLayout topbar_center;
    private RelativeLayout topbar_left;

    private WebFragment webFragment;
    private int category;
    private MCommunity mCommunity;
    public TextView iv_more;
    public TextView iv_refresh;
    private String url;
    private String target_url;
    private String back_url;
    private boolean prohibitGoBack;
    private boolean modifyGoBackKey;
    //两秒内按返回键两次退出程序
    private long exitTime;

    private String rightTag = this.toString();
    private WebPrimaryFragment webPrimaryFragment;

    private AjaxFinishReceiver ajaxFinishReceiver;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    //右上角刷新、分享控制
                    if (msg.obj.toString().equals("cancle")) {
                        clickRight(false);
                        iv_refresh.setVisibility(View.GONE);
                    } else if (msg.obj.toString().equals("refresh")) {
                        clickRight(false);
                        iv_refresh.setVisibility(View.VISIBLE);
                    } else if (msg.obj.toString().equals("shares")) {
                        clickRight(true);
                        iv_refresh.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_pullweb);
        EventBus.getDefault().register(this);
        setSwipeBackEnable(false);
        Intent intent = getIntent();
        category = intent.getIntExtra("category", -1);
        prohibitGoBack = intent.getBooleanExtra("prohibitGoBack", false);
        modifyGoBackKey = intent.getBooleanExtra("modifyGoBackKey", false);
        mCommunity = (MCommunity) intent.getSerializableExtra("community");
        url = getIntent().getStringExtra("web_url");
        LogComponent.i(TAG, "webUrl=" + url);
        target_url = getIntent().getStringExtra("target_url");
        if (!TextUtils.isEmpty(target_url)) {
            back_url = url;
            url = target_url;
            intent.putExtra("web_url", url);
        }
        if (StringUtil.isNotEmpty(url)
                && (url.contains("/meeting/sign/sign.action") || url.contains("meeting/meetingEntrance.action"))) {
            category = Constants.WEB_NONE;
        }
    }

    /**
     * 根据url中的相关参数，控制顶部导航
     * frameng中 setwebtitle的地方也需要调用此方法，动态控制顶部导航
     *
     * @param url
     */

    public void adjustTheme(String url) {
        String themeValue = com.hopson.hilife.baseservice.util.StringUtil.getValueAsKey(url, "theme");
        LogComponent.i("adjustTheme", url + " , " + themeValue);
        if (TextUtils.equals("translucent", (themeValue))) {
            top_bar.setVisibility(View.GONE);
            StatusBarUtil.setTranslucentForImageView(this, 0, null);
            StatusBarUtil.setLightMode(this, true); //状态栏字体为深色模式
        } else {
            top_bar.setVisibility(View.VISIBLE);
            StatusBarUtil.setColor(this, Color.WHITE, 0);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            StatusBarUtil.setLightMode(this, false);
        }
    }

    @Override
    protected void findView() {
        top_bar = (RelativeLayout) findViewById(R.id.top_bar);
        topLeftIC = (IconView) findViewById(R.id.topLeftIC);
        goback_tv = (TextView) findViewById(R.id.goback_tv);
        close_tv = (TextView) findViewById(R.id.close_tv);
        topTitleTV = (TextView) findViewById(R.id.topTitleTV);
        topRightTV = (TextView) findViewById(R.id.topRightTV);
        iv_more = (TextView) findViewById(R.id.iv_more);
        iv_refresh = (TextView) findViewById(R.id.iv_refresh);
        topbar_left = (RelativeLayout) findViewById(R.id.topbar_left);
        topbar_right = (LinearLayout) findViewById(R.id.topbar_right);
        topbar_center = (LinearLayout) findViewById(R.id.topbar_center);
        if (category == Constants.WEB_COLLECT) {
            if (getIntent().getBooleanExtra("canSkip", false)) {
                topRightTV.setVisibility(View.VISIBLE);
                clickRight(false);
                topRightTV.setText(getResources().getString(R.string.btn_skip));
                goback_tv.setVisibility(View.GONE);
                topLeftIC.setText(getResources().getString(R.string.icon_del));
            } else {
                goback_tv.setVisibility(View.GONE);
                topLeftIC.setText(getResources().getString(R.string.icon_del));
            }
        }
        if (prohibitGoBack) {
            goback_tv.setVisibility(View.GONE);
            topLeftIC.setVisibility(View.GONE);
        }
        //web页面自适应屏幕大小加宽标题内容宽度大小
        ViewTreeObserver viewTreeObserver = topbar_left.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (topbar_left.getViewTreeObserver().isAlive()) {
                    topbar_left.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                int topbar_leftWidth = topbar_left.getMeasuredWidth();
                int topbar_rightWidth = topbar_right.getMeasuredWidth();
                int layout_margin = 0;
                if (topbar_leftWidth > topbar_rightWidth) {
                    layout_margin = topbar_leftWidth + PhoneUtil.dip2px(3);
                } else {
                    layout_margin = topbar_rightWidth + PhoneUtil.dip2px(3);
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topbar_center.getLayoutParams();
                layoutParams.setMargins(layout_margin, 0, layout_margin, 0);
                topbar_center.setLayoutParams(layoutParams);
                return true;
            }
        });
    }

    @Override
    protected void setListener() {
        topLeftIC.setOnClickListener(this);
        goback_tv.setOnClickListener(this);
        if (category != Constants.WEB_NONE) {
            topbar_right.setOnClickListener(this);
        }
        close_tv.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                if (null != webFragment) {
                    webFragment.reload();
                }
                if (null != webPrimaryFragment) {
                    webPrimaryFragment.reload();
                }
                break;
            case R.id.goback_tv:
            case R.id.topLeftIC:
                boolean noHistory = getIntent().getBooleanExtra("noHistory", false);
                if (noHistory) {
                    finish();
                    break;
                }
                String currentUrl = getIntent().getStringExtra("currentUrl");  //重定向后 的url
                if (StringUtil.isNotEmpty(currentUrl)) {
                    HashMap<String, String> urlParams = Utils.getUrlParams(currentUrl);
                    if (null != urlParams && urlParams.containsKey("customReturn")) {
                        //是否自定义跳转页面
                        String customReturn = urlParams.get("customReturn");
                        if ("1".equals(customReturn)) {
                            Intent webIntent = new Intent(mContext, WebActivity.class);
                            webIntent.putExtra("category", Constants.WEB_SHOP);
                            webIntent.putExtra("web_url", Configuration.getMyOrdersUrl(mContext));
                            mContext.startActivity(webIntent);
                            break;
                        }
                    }
                }
                //订单详情页返回到指定页面
                if (StringUtil.isNotEmpty(url) && url.equals(Configuration.getMyOrdersUrl(mContext))) {
                    LinkedList<Activity> activityList = ((GlobalApplication) GlobalApplication.getContext()).getActivityList();
                    if (activityList.size() > 2) {
                        Activity activity = activityList.get(activityList.size() - 2);
                        if (!(activity instanceof MainActivity)) {
                            activity.finish();
                        }
                        finish();
                        break;
                    }
                }
                if (StringUtil.isNotEmpty(back_url)) {
                    Intent webIntent = new Intent(mContext, WebActivity.class);
                    webIntent.putExtra("category", category);
                    webIntent.putExtra("tagName", getTagName());
                    webIntent.putExtra("sourceType", Constants.TIPOFF_TYPE_PRESET);
                    startActivity(webIntent);
                }
                if (category == Constants.WEB_COLLECT && !getIntent().getBooleanExtra("canSkip", false)) {
                    setResult(Constants.RESULT_CHOOSE_OTHER_COMMUNTITY);
                } else if (Constants.WEB_BADGE == category) {
                    setResult(Constants.RESULT_BADGE);
                    finish();
                } else {
                    if ((null != webFragment && webFragment.canGoBack()) || (null != webPrimaryFragment && webPrimaryFragment.canGoBack())) {
                        close_tv.setVisibility(View.VISIBLE);
                    }
                }
                if (null != webFragment) {
                    webFragment.clearCache(true);
                    webFragment.goBack();
                }
                if (null != webPrimaryFragment) {
                    webPrimaryFragment.goBack();
                }
                break;
            case R.id.topbar_right:
                if (category == Constants.WEB_COLLECT && getIntent().getBooleanExtra("canSkip", false)) {
                    Intent data = new Intent();
                    if (null != mCommunity) {
                        data.putExtra("community", mCommunity);
                    }
                    setResult(Constants.RESULT_JOIN_COMMUNITY, data);
                    finish();
                } else if (category == Constants.WEB_SHOP) {
                    if (null != webFragment) {
                        webFragment.reload();
                    }
                    if (null != webPrimaryFragment) {
                        webPrimaryFragment.reload();
                    }
                } else {
                    if (null != webFragment) {
                        webFragment.clickRight();
                    }
                    if (null != webPrimaryFragment) {
                        webPrimaryFragment.clickRight();
                    }
                }
                break;
            case R.id.close_tv:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void processLogic() {
        adjustTheme(url);
        if (!StringUtil.isEmpty(url) && url.contains("youku.com") && Build.MANUFACTURER.equalsIgnoreCase("oppo") || (!StringUtil.isEmpty(url) && url.startsWith("http://221.232.78.203:8098"))) {
            // 如果加载的页面是优酷的视频，则用原生的webView打开，解决在OPPO手机上通过xwalkview打开优酷视频不能播放的问题
            webPrimaryFragment = new WebPrimaryFragment(this);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFL, webPrimaryFragment);
            ft.commit();
        } else {
            Log.v("ajax", "webactivity start: " + System.currentTimeMillis());
            String mapKey = manageWebUrl(url);
            webFragment = new WebFragment(this, rightTag);
            if (StringUtil.isNotEmpty(mapKey)) {
                String xwalkview_version = DJCacheUtil.read(mapKey + "_version", "0");
                WebView xWalkView = (WebView) mApplication.getXWalkViewMap().get(mapKey + "_" + xwalkview_version);
                if (null != xWalkView && StringUtil.isNotEmpty(xWalkView.getUrl())
                        && DJCacheUtil.readBoolean(this, mapKey + "_" + xwalkview_version + "_load", false)) {
                    Log.v("ajax", "activity, " + mapKey + "_" + xwalkview_version + ",state: " + DJCacheUtil.readBoolean(this, mapKey + "_" + xwalkview_version + "_load", false));
                    webFragment.setxWalkView(xWalkView, mapKey, xwalkview_version);
                    ajaxFinishReceiver = new AjaxFinishReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Constants.BROADCAST_TYPE_AJAX);
                    registerReceiver(ajaxFinishReceiver, intentFilter);
                } else {
                    // 模板加载出错，使用非模板加载
                    Log.v("ajax", "activity, " + mapKey + "_" + xwalkview_version + ",xwalkview is null or url is null");
                    Intent intent = new Intent();
                    intent.setAction(Constants.BROADCAST_TYPE_XWALKVIEW);
                    intent.putExtra("mapKey", mapKey + "_" + xwalkview_version);
                    intent.putExtra(mapKey + "_version", xwalkview_version);
                    sendBroadcast(intent);
                }
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFL, webFragment);
            ft.commit();
        }
    }

    @Override
    protected void refreshTheme() {
        super.refreshTheme();
        setThemeICAndTextColor(); //更改主题颜色
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (modifyGoBackKey) {
                exit();
                return false;
            } else {
                if (null != webFragment) {
                    if (webFragment.isFullScreen()) {
                        webFragment.quitFullScreen();
                    } else {
                        webFragment.goBack();
                    }
                }
                if (null != webPrimaryFragment) {
                    webPrimaryFragment.goBack();
                }
                return true;
            }
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public String getPageID() {
        return Constants.MONITOR_PAGE_WEB;
    }

    @Override
    public String getTagName() {
        if (null != webFragment) {
            return webFragment.getTagName();
        }
        if (null != webPrimaryFragment) {
            return webPrimaryFragment.getTagName();
        }
        return "";
    }

    @Override
    public void setAttachRightEnable(boolean isEnable) {
        topbar_right.setEnabled(isEnable);
    }

    @Override
    public void setAttachRightVisiable(int visiablity) {
        topbar_right.setVisibility(visiablity);
    }

    @Override
    public void setAttachRightIcon(int icon) {
    }

    @Override
    public void setTitle(final String title) {
        topTitleTV.post(new Runnable() {
            @Override
            public void run() {
                topTitleTV.setText(title);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != webFragment) {
            webFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (null != webPrimaryFragment) {
            webPrimaryFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 获取顶部导航菜单
     *
     * @return
     */
    public View getTopBar() {
        return top_bar;
    }

    private String manageWebUrl(String webUrl) {
        try {
            URL url = new URL(webUrl);
            if (null != url) {
                String path = url.getPath().replaceFirst("/", "");
                if (StringUtil.isNotEmpty(path) && path.contains(".")) {
                    String newPath = path.substring(0, path.indexOf(".")).replace("/", "_");
                    String query = url.getQuery();
                    if (StringUtil.isNotEmpty(query) && query.contains("&")) {
                        boolean isLoadTemplate = false; // 是否需要走模板加载
                        String[] allParamArray = query.split("&");
                        for (String paramInArray : allParamArray) {
                            if (paramInArray.contains("shopType") && paramInArray.split("=").length == 2) {
                                String paramInArrayValue = paramInArray.split("=")[1];
                                if (paramInArrayValue.equals("0")) {
                                    isLoadTemplate = true;
                                    break;
                                }
                            }
                        }
                        if (isLoadTemplate) {
                            Map viewInfoUniqueParamNameMap = mApplication.getViewInfoUniqueParamNameMap();
                            Map viewInfoRUniqueParamValueMap = mApplication.getViewInfoRUniqueParamValueMap();
                            String newPathReplace = newPath.replace("_", "/");
                            String uniqueParamName = (String) viewInfoUniqueParamNameMap.get(newPathReplace);
                            String uniqueParamNameValue = "";
                            if (StringUtil.isNotEmpty(uniqueParamName)) {
                                for (String paramInArray : allParamArray) {
                                    if (paramInArray.contains(uniqueParamName) && paramInArray.split("=").length == 2) {
                                        uniqueParamNameValue = paramInArray.split("=")[1];
                                        break;
                                    }
                                }
                            }
                            String viewType = (String) viewInfoRUniqueParamValueMap.get(uniqueParamNameValue);
                            if (StringUtil.isEmpty(viewType)) {
                                viewType = "0";
                            }
                            if (newPathReplace.equals("mobileshop/findProductByShopId")) {
                                if (!viewType.equals("1")) return "";
                            } else if (newPathReplace.equals("mobileproduct/findProductIndex")) {
                                if (!viewType.equals("0")) return "";
                            }
                            String templateVersion = DJCacheUtil.read(newPath + "_version", "0");
                            String tempPath = newPath;
                            if (templateVersion.equals("0")) {
                                tempPath += "_0";
                            } else {
                                tempPath += "_1";
                            }
                            String filePath = DJCacheUtil.read(tempPath);
                            if (StringUtil.isNotEmpty(filePath)) {
                                File file = new File(filePath);
                                if (null != file && file.exists()) {
                                    return newPath;
                                }
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShareMessegeEvent shareMessegeEvent) {
        if (iv_refresh == null || !TextUtils.equals(shareMessegeEvent.getRightTag(), (rightTag))) {
            return;
        }
        if (TextUtils.equals(shareMessegeEvent.getType(), ("cancle"))) {
            Message msg = handler.obtainMessage(101);
            msg.obj = "cancle";
            handler.sendMessage(msg);
        } else if (shareMessegeEvent.getShareBean().getOptList() == null
                || shareMessegeEvent.getShareBean().getOptList().size() == 0
                || shareMessegeEvent.getShareBean().getShareInfo() == null
                || (shareMessegeEvent.getShareBean().getOptList().size() == 1
                && TextUtils.equals(shareMessegeEvent.getShareBean().getOptList().get(0), "menuItem:refresh"))) {
            Message msg = handler.obtainMessage(101);
            msg.obj = "refresh";
            handler.sendMessage(msg);
        } else {
            Message msg = handler.obtainMessage(101);
            msg.obj = "shares";
            handler.sendMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != ajaxFinishReceiver) {
            unregisterReceiver(ajaxFinishReceiver);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private class AjaxFinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent && intent.getAction().equals(Constants.BROADCAST_TYPE_AJAX) && intent.hasExtra("result")) {
                if (null != webFragment) {
                    webFragment.setTitleAjaxUse(intent.getStringExtra("result"));
                }
            }
        }
    }

    private void setThemeICAndTextColor() {
        topLeftIC.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        goback_tv.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        close_tv.setText("");
        close_tv.setVisibility(View.VISIBLE);
        close_tv.setBackgroundResource(R.drawable.nav_icon_closed_black);
        close_tv.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        topTitleTV.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMETITLECOLOR, R.color.color_333333));
        topRightTV.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        iv_more.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        iv_refresh.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_333333));
        top_bar.setBackgroundColor(ThemeEngine.getInstance().getColor(Constants.TITLEBACKGROUNDCOLOR, R.color.topbar_blue));
        //top_bar.getBackground().setAlpha(NumberParser.parseString(ThemeEngine.getInstance().getProperties(Constants.TITLEBACKGROUNDOPACITY), 100) * 255 / 100);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            DJToastUtil.showMessage(GlobalApplication.getContext(), getResources().getString(R.string.tips_exit_again));
            exitTime = System.currentTimeMillis();
        } else {
            ((GlobalApplication) GlobalApplication.getContext()).exitApp(null);
        }
    }

    public void clickRight(boolean click) {
        if (click) {
            iv_more.setVisibility(View.VISIBLE);
        } else {
            iv_more.setVisibility(View.GONE);
        }
        iv_more.setEnabled(click);
        topbar_right.setEnabled(click);
    }
}
