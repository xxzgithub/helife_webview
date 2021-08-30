package com.hilife.webview.model.js;

/**
 * js交互的信息流参数
 * @author adminiatrator
 *
 */
public class JSFeedParam extends BaseJSParam{
	/**
	 * type : 1
	 * mobile : 18201433021
	 * password : 123456
	 */
	private static final long serialVersionUID = 5629912437621336109L;
	private String feedID;
	private String communityID;
	private String companyID;
	private int type;
	private String mobile;
	private String password;
	private String companyName;
	private String shortURLID;

	public String getShortURLID() {
		return shortURLID;
	}

	public void setShortURLID(String shortURLID) {
		this.shortURLID = shortURLID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "JSFeedParam{" +
				"feedID='" + feedID + '\'' +
				", communityID='" + communityID + '\'' +
				", companyID='" + companyID + '\'' +
				", type=" + type +
				", mobile='" + mobile + '\'' +
				", password='" + password + '\'' +
				", companyName='" + companyName + '\'' +
				", shortURLID='" + shortURLID + '\'' +
				'}';
	}
}
