package com.hilife.webview.model.js;

import java.util.List;

public class JsPicParam extends BaseJSParam {
	
	
	private static final long serialVersionUID = 1617234382152134229L;
	
	private String current;
	private List<String> ids;
	private boolean isUrl;

	public boolean isUrl() {
		return isUrl;
	}

	public void setUrl(boolean url) {
		isUrl = url;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

}
