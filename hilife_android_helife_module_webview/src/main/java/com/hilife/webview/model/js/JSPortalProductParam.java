package com.hilife.webview.model.js;

import com.hilife.view.app.model.MPortalProduct;

import java.util.List;

public class JSPortalProductParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4321028173718442400L;
	private Integer page = 1;
	private String count;
	private boolean isCache;
	private String tagID;
	private String rowType;
	private List<MPortalProduct> data;

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

	public boolean isCache() {
		return isCache;
	}

	public void setIsCache(boolean isCache) {
		this.isCache = isCache;
	}

	public String getTagID() {
		return tagID;
	}

	public void setTagID(String tagID) {
		this.tagID = tagID;
	}

	public List<MPortalProduct> getData() {
		return data;
	}

	public void setData(List<MPortalProduct> data) {
		this.data = data;
	}

	public String getRowType() {
		return rowType;
	}

	public void setRowType(String rowType) {
		this.rowType = rowType;
	}
}
