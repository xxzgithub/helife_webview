package com.hilife.webview.model.js;

public class JsGetFileParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5674743935072422668L;

	private String fileID;

	private Integer type;

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
