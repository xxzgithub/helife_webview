package com.hilife.webview.model.js;

public class JSChooseImageParam extends BaseJSParam {

    public static final Integer RETURN_TYPE_BASE64 = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325060727627374209L;

    private int type;
    private int current;
    private int max;
    private Integer returnType;

    public void setType(int type) {
        this.type = type;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getType() {
        return type;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }
}
