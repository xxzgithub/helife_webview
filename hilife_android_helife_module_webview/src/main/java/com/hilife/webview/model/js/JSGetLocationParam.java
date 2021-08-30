package com.hilife.webview.model.js;

public class JSGetLocationParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1461080476810728667L;
	private int current;
	private String lon;
	private String lat;
	private String name;
	private String addr;
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
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
