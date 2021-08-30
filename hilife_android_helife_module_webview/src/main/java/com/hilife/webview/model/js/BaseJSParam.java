package com.hilife.webview.model.js;

import java.io.Serializable;

/**
 * js交互的参数对象
 *
 * @author adminiatrator
 */
public class BaseJSParam implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6847638679047758433L;
    //回调函数ID
    private String callbackId;

    public BaseJSParam() {
    }

    public BaseJSParam(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

}
