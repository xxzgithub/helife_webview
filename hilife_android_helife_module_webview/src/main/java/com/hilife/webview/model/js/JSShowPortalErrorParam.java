package com.hilife.webview.model.js;

public class JSShowPortalErrorParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325020365997374639L;

    private String file;
    private String code;
    private String error;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
