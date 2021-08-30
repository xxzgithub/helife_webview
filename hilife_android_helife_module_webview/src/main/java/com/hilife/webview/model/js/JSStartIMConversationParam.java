package com.hilife.webview.model.js;

public class JSStartIMConversationParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2765060766997390209L;

    private String conversationType;
    private String companyID;
    private String targetID;
    private String targetName;
    private String messageTitle;
    private String messageDigest;
    private String messageImageURL;
    private String messageUrl;
    private String messageExtra;
    private String textMessageContent;
    private String productId;

    private Object extra;
    private boolean enterQiYu = false;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getConversationType() {
        return conversationType;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageDigest() {
        return messageDigest;
    }

    public void setMessageDigest(String messageDigest) {
        this.messageDigest = messageDigest;
    }

    public String getMessageImageURL() {
        return messageImageURL;
    }

    public void setMessageImageURL(String messageImageURL) {
        this.messageImageURL = messageImageURL;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getMessageExtra() {
        return messageExtra;
    }

    public void setMessageExtra(String messageExtra) {
        this.messageExtra = messageExtra;
    }

    public String getTextMessageContent() {
        return textMessageContent;
    }

    public void setTextMessageContent(String textMessageContent) {
        this.textMessageContent = textMessageContent;
    }

    public boolean isEnterQiYu() {
        return enterQiYu;
    }

    public void setEnterQiYu(boolean enterQiYu) {
        this.enterQiYu = enterQiYu;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
