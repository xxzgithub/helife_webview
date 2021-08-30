package com.hilife.webview.model.js;

public class JSJoinGroupParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6553101491912797385L;
	private String communityID;
	private String groupID;
	public String getCommunityID() {
		return communityID;
	}
	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
}
