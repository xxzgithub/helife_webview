package com.hilife.webview.model.js;

/**
 * Created by admin on 2015/8/3.
 */
public class JSFeedListParam extends BaseJSParam{

    public Integer type;

    private String objID;

    private String objName;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
