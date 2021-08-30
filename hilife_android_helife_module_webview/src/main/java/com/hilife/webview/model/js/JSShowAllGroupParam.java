package com.hilife.webview.model.js;

public class JSShowAllGroupParam extends BaseJSParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 730008679038006742L;

    private Integer type; // 0:全部群组  1:全部群组搜索;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
