package com.hilife.webview.model.js;

public class JsGetTopicParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5674743935072422668L;

	private String tagID;

	private Boolean isSupport;

	public String getTagID() {
		return tagID;
	}

	public void setTagID(String tagID) {
		this.tagID = tagID;
	}

	public Boolean getSupport() {
		return isSupport;
	}

	public void setSupport(Boolean support) {
		isSupport = support;
	}
}
