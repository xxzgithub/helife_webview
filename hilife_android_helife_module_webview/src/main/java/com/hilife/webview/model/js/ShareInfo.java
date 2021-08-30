package com.hilife.webview.model.js;

import java.util.Map;

public class ShareInfo extends BaseJSParam {

    /**
     *
     */
    private static final long serialVersionUID = -2325909366997374659L;

    private String url;

    private String imageThumbUrl;

    private String shareDescription;

    private String shareTitle;

    private boolean touchShow;

    private String shortChain;
    private String type;
    private Map extras;

    public boolean isTouchShow() {
        return touchShow;
    }

    public void setTouchShow(boolean touchShow) {
        this.touchShow = touchShow;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public String getUrl() {
        return url;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getShortChain() {
        return shortChain;
    }

    public void setShortChain(String shortChain) {
        this.shortChain = shortChain;
    }

    public Map getExtras() {
        return extras;
    }

    public void setExtras(Map extras) {
        this.extras = extras;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
