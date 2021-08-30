package com.hilife.webview.model.js;

public class JSPortalTopicParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8751028173718442400L;
	
	private String version;
	
	private boolean isCache;
//	private MobileMainPage data;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

//	public MobileMainPage getData() {
//		return data;
//	}
//
//	public void setData(MobileMainPage data) {
//		this.data = data;
//	}
	
}
