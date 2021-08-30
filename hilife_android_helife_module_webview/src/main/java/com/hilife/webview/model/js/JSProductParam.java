package com.hilife.webview.model.js;

public class JSProductParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -454397370032403727L;

    private String productID;

    /**
     * 模板加载新增属性
     */
    private String shopType;

    /**
     * 是否显示右上角刷新
     */
    private boolean isRightRefresh = false;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public boolean isRightRefresh() {
        return isRightRefresh;
    }

    public void setRightRefresh(boolean rightRefresh) {
        isRightRefresh = rightRefresh;
    }
}
