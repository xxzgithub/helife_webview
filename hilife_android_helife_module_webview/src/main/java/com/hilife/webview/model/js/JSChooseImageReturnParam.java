package com.hilife.webview.model.js;

import java.io.Serializable;

public class JSChooseImageReturnParam implements Serializable {

    private String filePath;
    private String thumbData;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbData() {
        return thumbData;
    }

    public void setThumbData(String thumbData) {
        this.thumbData = thumbData;
    }
}
