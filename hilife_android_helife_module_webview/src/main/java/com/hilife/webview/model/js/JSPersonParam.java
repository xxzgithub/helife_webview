package com.hilife.webview.model.js;

/**
 * js交互的人员参数
 * @author adminiatrator
 *
 */
public class JSPersonParam  extends BaseJSParam{
	/**
	 * 
	 */
	private static final long serialVersionUID = 186037854643341134L;
	private String personID;
	private String personName;
	
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
}
