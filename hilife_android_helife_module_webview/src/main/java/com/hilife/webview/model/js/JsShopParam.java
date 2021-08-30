package com.hilife.webview.model.js;

/**
 * Created by admin on 2015/11/16.
 */
public class JsShopParam extends BaseJSParam{
    /**
     *
     */
    private static final long serialVersionUID = 5629912437621331239L;
    private String count;
    private boolean isCache;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setIsCache(boolean isCache) {
        this.isCache = isCache;
    }
}
