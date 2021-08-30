package com.hilife.webview.ui.xwalk;

public class WeiXinPayMessage {

    public Integer errCode;
    public String errMsg;

    public WeiXinPayMessage(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
