package com.hilife.webview.model.js;

/**
 * js交互的社区参数
 * @author adminiatrator
 *
 */
public class JSCommunityParam  extends BaseJSParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6112662933552147573L;
	private String communityID;
	private String communityName;

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
