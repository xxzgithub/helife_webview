package com.hilife.webview.model.js;

public class JSFormParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 911031735782756931L;
	private String formID;
	private String formRecordID;
	private String title;
	public String getFormID() {
		return formID;
	}
	public void setFormID(String formID) {
		this.formID = formID;
	}
	public String getFormRecordID() {
		return formRecordID;
	}
	public void setFormRecordID(String formRecordID) {
		this.formRecordID = formRecordID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
