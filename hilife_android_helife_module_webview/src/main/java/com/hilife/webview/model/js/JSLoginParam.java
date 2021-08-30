package com.hilife.webview.model.js;

public class JSLoginParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7123034467576804929L;
	private String name;
	private String pwd;
	/**
	 * 登录行为	-1: (默认)进入登录页并填充数据 
				 0: 后台登录并进入主页 
				 1: 后台登录但不做页面跳转，返回登录结果
	 */
	private Integer action;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
}
