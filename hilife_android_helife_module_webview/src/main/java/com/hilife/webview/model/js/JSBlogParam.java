package com.hilife.webview.model.js;

/**
 * js交互的信息流参数
 * @author adminiatrator
 *
 */
public class JSBlogParam extends BaseJSParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5629912437621336109L;
	private String feedID;
	private String communityID;
	private String blogID;
	public String getFeedID() {
		return feedID;
	}
	public void setFeedID(String feedID) {
		this.feedID = feedID;
	}
	public String getCommunityID() {
		return communityID;
	}
	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}

	public String getBlogID() {
		return blogID;
	}

	public void setBlogID(String blogID) {
		this.blogID = blogID;
	}
}
