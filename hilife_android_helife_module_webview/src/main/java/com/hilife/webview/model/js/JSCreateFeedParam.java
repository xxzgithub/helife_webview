package com.hilife.webview.model.js;

public class JSCreateFeedParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -584152399861851600L;
	private String topicID;
	private String tagID;
	public String getTopicID() {
		return topicID;
	}
	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}
	public String getTagID() {
		return tagID;
	}
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
}
