package com.hilife.webview.model.js;

/**
 * JSActionParam
 *
 * @author hewanxian
 * @date 16/3/15
 * Copyright © 2016年 branch.6.3.0.portal.3.2.AppBox. All rights reserved.
 */
public class JSActionParam extends BaseJSParam {

    /**
     * 1 赞  2 收藏
     */
    private Integer type;

    /**
     * 1 添加操作 2 取消操作
     */
    private Integer action;

    /**
     * 操作对象
     */
    private String objID;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }
}
