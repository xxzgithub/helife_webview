package com.hilife.webview.model.js;

import com.hilife.view.app.model.PortalFeed;

import java.util.List;

public class JSPortalFeedParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8751028173718442400L;
	private String count;
	private String tagID;
	private String tagName;
	private String style;
	private List<PortalFeed> data;
	private boolean isCache;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getTagID() {
		return tagID;
	}

	public void setTagID(String tagID) {
		this.tagID = tagID;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<PortalFeed> getData() {
		return data;
	}

	public void setData(List<PortalFeed> data) {
		this.data = data;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setIsCache(boolean isCache) {
		this.isCache = isCache;
	}
}
