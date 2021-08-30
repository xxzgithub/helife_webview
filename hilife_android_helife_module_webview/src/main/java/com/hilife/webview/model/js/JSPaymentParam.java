package com.hilife.webview.model.js;

/**
 * Created by admin on 2015/8/3.
 */
public class JSPaymentParam  extends BaseJSParam{

    /**
     * 订单号
     */
    public String orderID;
    /**
     * 商户ID
     */
    private String merchantId;
    /**
     * 商品名称
     */
    private String  subject;
    /**
     * 商品描述
     */
    private String desc;
    /**
     * 商品总金额
     */
    private String orderAmount;
    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 企业ID
     * @return
     */
    private String companyID;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
