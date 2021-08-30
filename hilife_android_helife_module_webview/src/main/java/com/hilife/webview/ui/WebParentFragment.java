package com.hilife.webview.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.framework.activity.BaseActivity;
import com.hilife.view.app.model.PresetMenu;
import com.hilife.view.main.base.BaseFragment;
import com.hilife.view.main.ui.AttachDelegate;
import com.hilife.view.main.ui.MainActivity;
import com.hilife.view.other.component.webview.ui.xwalk.WebFragment;
import com.hilife.view.weight.Configuration;
import com.hilife.view.other.component.skin.ThemeEngine;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJToastUtil;
import com.hilife.view.R;

public class WebParentFragment extends BaseFragment implements AttachDelegate {
    private TextView topTitleTV;
    private TextView topRightTV;
    private LinearLayout topbar_right;
    private WebFragment webFragment;
    private View top_bar;
    private PresetMenu presetMenu;

    @Override
    protected int getContentView() {
        return R.layout.fragment_webparent;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void findView() {
        top_bar = findViewById(R.id.top_bar);
        topTitleTV = (TextView) findViewById(R.id.topTitleTV);
        topRightTV = (TextView) findViewById(R.id.topRightTV);
        topbar_right = (LinearLayout) findViewById(R.id.topbar_right);
        presetMenu = (PresetMenu) getArguments().getSerializable("presetMenu");
    }

    @Override
    protected void setListener() {
        topbar_right.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.topbar_right: {
                if (Configuration.ExpState.ExpStateNo != Configuration.getMISEXP(mContext)) {
                    DJToastUtil.showHintToast(mContext, mContext.getResources().getString(R.string.prompt_nologin));
                    return;
                }
                String code = presetMenu.getCode();
                if (StringUtil.isEmpty(topRightTV.getText().toString())) {
                    return;
                } else if (mContext.getResources().getString(R.string.icon_refresh).equals(topRightTV.getText().toString())) {
                    webFragment.reload();
                    return;
                } else if (mContext.getResources().getString(R.string.icon_operation_more).equals(topRightTV.getText().toString())) {
                    webFragment.clickRight();
                }

            }
            break;
            default:
                break;
        }

    }

    @Override
    protected void processLogic() {
        webFragment = new WebFragment(this);
        if (null != getArguments()) {
            int category = -1;
            Bundle arguments = new Bundle();
            String code = presetMenu.getCode();
            if (StringUtil.isNotEmpty(code)) {
                if (Constants.TOPIC_CODE_WEB.equalsIgnoreCase(code)
                        || Constants.TOPIC_CODE_INQUIRYAPP.equalsIgnoreCase(code)) {
                    category = Constants.WEB_PORTAL;
                    if (Constants.TOPIC_CODE_INQUIRYAPP.equalsIgnoreCase(code)) {
                        category = Constants.WEB_SERVICEFORM;
                    } else if (presetMenu.getContent() != null
                            && presetMenu.getContent().indexOf(Configuration.getFormShowUrl(mContext)) != -1) {
                        category = Constants.WEB_THEME_FORM;
                    } else if (Constants.TOPIC_CODE_SERVICEFORM.equalsIgnoreCase(code)) {
                        category = Constants.WEB_SERVICEFORM;
                    }
                } else if (Constants.TOPICPRESET_STORE_SHOPPING.equals(code)
                        || Constants.TOPICPRESET_STORE_SHOPPING_COMMODITYLIST.equals(code)
                        || Constants.TOPICPRESET_STORE_JISHISPACEEDIT.equals(code)
                        || Constants.TOPICPRESET_STORE_ORDERDEAL.equals(code)) {
                    category = Constants.WEB_SHOP;
                } else if (Constants.TOPIC_CODE_SERVICEFORM.equals(code)) {
                    category = Constants.WEB_NONE;
                } else if (Constants.TOPIC_CODE_WORKSSHOW.equalsIgnoreCase(code)
                        || Constants.TOPIC_CODE_COURCE.equals(code)
                        || Constants.TOPIC_CODE_CROSSSHOPCOURCELIST.equals(code)
                        || Constants.TOPIC_CODE_BALANCE.equalsIgnoreCase(code)) {
                    topRightTV.setText(mContext.getResources().getString(R.string.icon_refresh));
                    category = Constants.WEB_SHOP;
                }
            }
            String presetContent = presetMenu.getContent();
            if (StringUtil.isNotEmpty(presetContent) && (presetContent.contains("meeting/meetingEntrance.action?") || presetContent.contains("meeting/sign/sign.action"))) {
                category = Constants.WEB_NONE;
            }
            arguments.putInt("category", category);
            arguments.putString("web_url", presetContent);
            arguments.putString("title", presetMenu.getmName());
            arguments.putString("tagName", presetMenu.getmName());
            arguments.putString("sourceID", presetMenu.getmID());
            arguments.putString("sourceType", Constants.TIPOFF_TYPE_PRESET);
            webFragment.setArguments(arguments);
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.contentFL, webFragment);
        ft.commit();
    }


    @Override
    protected void refreshTheme() {
        super.refreshTheme();
        top_bar.setBackgroundColor(ThemeEngine.getInstance().getColor(Constants.TITLEBACKGROUNDCOLOR, R.color.topbar_blue));
        topTitleTV.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMETITLECOLOR, R.color.color_white));
        topRightTV.setTextColor(ThemeEngine.getInstance().getColor(Constants.THEMEICONCOLOR, R.color.color_white));
    }


    @Override
    public String getPageID() {
        return Constants.MONITOR_PAGE_WEB;
    }

    @Override
    public String getTagName() {
        return webFragment.getTagName();
    }

    @Override
    public void setAttachRightEnable(boolean isEnable) {
        topbar_right.setEnabled(isEnable);
    }

    @Override
    public void setTitle(String title) {
        if (topTitleTV != null) {
            topTitleTV.setText(title);
        }
    }

    @Override
    public void setAttachRightVisiable(int visiablity) {
        topbar_right.setVisibility(visiablity);
    }


    @Override
    public void setAttachRightIcon(final int icon) {
        topRightTV.post(new Runnable() {
            @Override
            public void run() {
                topRightTV.setText(mContext.getResources().getString(icon));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        webFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setTitleBarPadding() {
        if (mContext instanceof MainActivity) {
            BaseActivity context = (BaseActivity) mContext;
            int statusBarHeight = context.getStatusBarHeight();
            top_bar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams layoutParams = top_bar.getLayoutParams();
            if (null != layoutParams) {
                layoutParams.height = layoutParams.height + statusBarHeight - 10;
            }
        }
    }

}
