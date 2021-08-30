package com.hilife.webview.model.js;

public class JSEnterExperienceParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4908283807174064213L;
	private String username;
	private String password;
	private String communityID;
	private String communityName;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCommunityID() {
		return communityID;
	}
	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
}
