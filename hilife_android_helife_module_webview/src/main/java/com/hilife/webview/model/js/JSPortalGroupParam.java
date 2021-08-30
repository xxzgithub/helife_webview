package com.hilife.webview.model.js;

import com.hilife.view.app.model.MPortalGroup;

import java.util.List;

public class JSPortalGroupParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8751028173718442400L;
	private Integer page = 1;
	private String count;

	private String view;
	private String style;
	private boolean isCache;
	private List<MPortalGroup> data;
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

	public List<MPortalGroup> getData() {
		return data;
	}

	public void setData(List<MPortalGroup> data) {
		this.data = data;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
