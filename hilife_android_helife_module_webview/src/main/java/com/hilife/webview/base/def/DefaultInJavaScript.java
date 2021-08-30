/**
 * 
 */
package com.hilife.webview.base.def;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.hilife.webview.base.InJavaScript;
import com.hilife.webview.ui.WebActivity;
//import android.webkit.JavascriptInterface;



/**
 * @author hewx
 * @version 1.0
 * @since SDK1.6
 */
public class DefaultInJavaScript implements InJavaScript {

	protected  Context mContext;
	protected Activity mActivity;
	protected Context context;
	public DefaultInJavaScript(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	public DefaultInJavaScript(Context mContext, Activity activity) {
		super();
		this.mContext = mContext;
		this.mActivity = mActivity;
	}

	@Override
	@JavascriptInterface
	public void registerSuccess(String username, String password, String cStatus) {}

	@Override
	@JavascriptInterface
	public void pLogin(String phone) {}

	@Override
	@JavascriptInterface
	public void modifyPasswordSuccess(String password) {}

	@Override
	@JavascriptInterface
	public void expFeedBackSuccess() {}

	@Override
	@JavascriptInterface
	public void modifyPersonInfoSuccess() {}

	@Override
	@JavascriptInterface
	public void goBackup() {}

	@Override
	@JavascriptInterface
	public void goFinish(String mobile, String pwd) {}

	@Override
	@JavascriptInterface
	public void goOpenFile(String fileID,String fileName, String widgetID, 
			String contentID, String shortChain,String fileSize) {}

	@Override
	@JavascriptInterface
	public void joinWork(String communityID, String communityName) {}

	@Override
	@JavascriptInterface
	public void getWebDesc(String summary) {}
	
	@Override
	@JavascriptInterface
	public void getWebParam(String webParamJson){}
	
	@Override
	@JavascriptInterface
	public void saveFormFeed(String status,String formID,String formRecordID,String title){}

	@Override
	@JavascriptInterface
	public void openurl(String url) {
		Intent intent = new Intent(mActivity, WebActivity.class);
		intent.putExtra("web_url", url);
		mActivity.startActivity(intent);
	}

	@Override
	@JavascriptInterface
	public void setTipoffID(String sourceID){}


	@Override
	@JavascriptInterface
	public void setTipoffType(String sourceType){}

	@Override
	@JavascriptInterface
	public void openSuccess(){}

	@Override
	@JavascriptInterface
	public void openFailed(){}
	@Override
	@JavascriptInterface
	public void Finish(){}

}
