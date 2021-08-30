package com.hilife.webview.model.js;

public class JSConnectDeviceParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2390760766945374349L;
    // 需要连接的设备号
    private String pid;
    private String sid;
    private String rcid;
    private String wcid;
    private String content;

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setRcid(String rcid) {
        this.rcid = rcid;
    }

    public void setWcid(String wcid) {
        this.wcid = wcid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public String getRcid() {
        return rcid;
    }

    public String getWcid() {
        return wcid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
