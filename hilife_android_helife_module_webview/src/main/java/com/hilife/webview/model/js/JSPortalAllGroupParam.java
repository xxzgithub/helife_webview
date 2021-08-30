package com.hilife.webview.model.js;

import java.util.List;

public class JSPortalAllGroupParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8734528173718878400L;
	private Integer page = 1;
	private String count;

	private String view;
	private String style;
	private boolean isCache;
	private List<?> data;
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

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
