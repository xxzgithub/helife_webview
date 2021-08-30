package com.hilife.webview.model.js;

public class JSShowLocationParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 430796535386736845L;
	private String lat;
	private String lon;
	private String name;
	private String addr;
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
}
