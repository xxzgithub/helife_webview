package com.hilife.webview.model.js;

public class JSNeedLoginForThirdParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325060123547374209L;


    private String currentUrl;
    private String clientID;

    public String getCurrentUrl() {
        return currentUrl;
    }

    public String getClientID() {
        return clientID;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

}
