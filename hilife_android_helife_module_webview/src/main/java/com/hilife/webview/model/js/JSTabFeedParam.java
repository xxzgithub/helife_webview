package com.hilife.webview.model.js;

import com.dajia.mobile.esn.model.feed.MFeed;

import java.util.List;

public class JSTabFeedParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8751028173718442400L;
	private Integer page;
	private String count;
	private String tagID;
	private String tagName;
	private String maxTime;
	private String style;
	private List<MFeed> data;
	private boolean isCache;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

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

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<MFeed> getData() {
		return data;
	}

	public void setData(List<MFeed> data) {
		this.data = data;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setIsCache(boolean isCache) {
		this.isCache = isCache;
	}
}
