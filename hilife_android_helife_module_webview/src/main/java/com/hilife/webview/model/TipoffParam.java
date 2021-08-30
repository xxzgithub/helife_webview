package com.hilife.webview.model;

import java.io.Serializable;

public class TipoffParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8742283614456846595L;
	private String tipoffedMsgID;
	private String tipoffedMsgType;
	public String getTipoffedMsgID() {
		return tipoffedMsgID;
	}
	public void setTipoffedMsgID(String tipoffedMsgID) {
		this.tipoffedMsgID = tipoffedMsgID;
	}
	public String getTipoffedMsgType() {
		return tipoffedMsgType;
	}
	public void setTipoffedMsgType(String tipoffedMsgType) {
		this.tipoffedMsgType = tipoffedMsgType;
	}
}
