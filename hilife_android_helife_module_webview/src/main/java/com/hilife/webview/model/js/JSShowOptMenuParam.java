package com.hilife.webview.model.js;

public class JSShowOptMenuParam extends BaseJSParam {

    /**
     *
     */
    private static final long serialVersionUID = -2325030366997374659L;

    private String[] optList;

    private ShareInfo shareInfo;

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    public String[] getOptList() {
        return optList;
    }

    public void setOptList(String[] optList) {
        this.optList = optList;
    }
}
