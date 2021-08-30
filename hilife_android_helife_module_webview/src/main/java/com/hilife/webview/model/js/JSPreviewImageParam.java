package com.hilife.webview.model.js;

import java.util.List;

public class JSPreviewImageParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325060727627374920L;
	private String current;
	private List<String> urls;
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public List<String> getUrls() {
		return urls;
	}
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
}
