package com.hilife.webview.model.js;

import java.util.List;
import java.util.Map;

public class JSShowPortalListParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325020365098974639L;

    private String tagID;
    private Integer page;
    private Integer count;
    private boolean isCache;
    private Map<String, Object> object;
    private List<Map<String, Object>> data;


    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }


    public Map<String, Object> getObject() {
        return object;
    }

    public void setObject(Map<String, Object> object) {
        this.object = object;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
