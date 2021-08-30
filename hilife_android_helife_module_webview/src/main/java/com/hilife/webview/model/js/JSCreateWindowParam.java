package com.hilife.webview.model.js;

public class JSCreateWindowParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -497697370032403727L;
	/**
	 * type = 1 需要添加webHost
	 */
	private Integer type;
	private String url;
	private String isShare; //默认是true不影响其他以前功能

	private String[] optList;

	private ShareInfo shareInfo;

	private boolean noHistory;

	public boolean isNoHistory() {
		return noHistory;
	}

	public void setNoHistory(boolean noHistory) {
		this.noHistory = noHistory;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

	public String[] getOptList() {
		return optList;
	}

	public void setOptList(String[] optList) {
		this.optList = optList;
	}

	public ShareInfo getShareInfo() {
		return shareInfo;
	}

	public void setShareInfo(ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}
}
