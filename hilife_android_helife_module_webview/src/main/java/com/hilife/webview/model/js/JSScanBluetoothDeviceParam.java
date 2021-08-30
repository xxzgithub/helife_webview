package com.hilife.webview.model.js;

public class JSScanBluetoothDeviceParam extends BaseJSParam {

    /**
     *
     */
    private static final long serialVersionUID = -2325360766945374209L;

    // 是否需要重新扫描二维码
    private int reScan;

    private String sid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getReScan() {
        return reScan;
    }

    public void setReScan(int reScan) {
        this.reScan = reScan;
    }
}
