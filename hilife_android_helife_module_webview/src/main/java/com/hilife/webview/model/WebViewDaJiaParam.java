package com.hilife.webview.model;

import java.io.Serializable;


/**
 * 网页隐藏数据
 * @author hewanxian
 *
 */
public class WebViewDaJiaParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2979403626675486242L;
	
	private String title;
	private String description;
	private String address;
	private String picID;
	private String logoID;
	private String pageType;
	private String pageID;
	private boolean isPreview;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPicID() {
		return picID;
	}
	public void setPicID(String picID) {
		this.picID = picID;
	}
	public String getLogoID() {
		return logoID;
	}
	public void setLogoID(String logoID) {
		this.logoID = logoID;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getPageID() {
		return pageID;
	}
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}
	public boolean isPreview() {
		return isPreview;
	}
	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}
	
}
