package com.hilife.webview.model.js;

/**
 * js交互的群组参数
 * @author adminiatrator
 *
 */
public class JSGroupParam  extends BaseJSParam{
	/**
	 * 
	 */
	private static final long serialVersionUID = 186037854643341134L;
	private String groupID;
	private String groupName;
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
