package com.hilife.webview.model.js;

import java.util.Map;

public class JSShowPortalDetailParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325000985077654639L;

    private String tagID;
    private Map<String, String> object;


    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }


    public Map<String, String> getObject() {
        return object;
    }

    public void setObject(Map<String, String> object) {
        this.object = object;
    }

}
