package com.hilife.webview.model.js;

public class JSPicDownloadParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2657267511864734433L;
	private String picID;
	private String size;
	private String type;
	private String cdnAddr;
	public String getPicID() {
		return picID;
	}
	public void setPicID(String picID) {
		this.picID = picID;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getCdnAddr() {
		return cdnAddr;
	}

	public void setCdnAddr(String cdnAddr) {
		this.cdnAddr = cdnAddr;
	}
}
