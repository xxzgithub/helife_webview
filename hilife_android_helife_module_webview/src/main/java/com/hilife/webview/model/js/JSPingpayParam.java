package com.hilife.webview.model.js;

public class JSPingpayParam extends BaseJSParam {

    /**
     * content : {"metadata":{"orderType":"3","companyID":"24322295870175816778556","companyInfoID":"243222958701758167","callBackType":"3","paymentOrderID":"1588993372403894","personID":"2966574415370208173","shopID":"7326354713687901184","callBakcUrl":null,"projectName":"墨丹农夫专营店-【母亲节花束】紫霞仙子花束  紫玫瑰5枝+..."},"livemode":true,"subject":"墨丹农夫专营店-【母亲节...","channel":"wx","transactionNo":null,"amountSettle":3966,"description":null,"body":"墨丹农夫专营店-【母亲节花束】紫霞仙子花束  紫玫瑰5...","timeSettle":null,"refunds":null,"timeExpire":1589000578,"timePaid":null,"credential":{"object":"credential","wx":{"appId":"wx62d7f2c3cfc04076","partnerId":"1524701451","prepayId":"wx09110259071967116639ad211278795000","nonceStr":"ef58e457b801af6ab301ecba6b618961","timeStamp":"1588993379","packageValue":"Sign=WXPay","sign":"C7F75D632B7283553200DD6B18F3BF1B"}},"amountRefunded":0,"extra":{},"refunded":false,"currency":"cny","id":"ch_101200509407335782400009","failureMsg":null,"app":"app_Li98WTWvDS8OD0e9","amount":3990,"orderNo":"1588993372403894881","failureCode":null,"created":1588993378,"clientIp":"192.168.1.114","paid":false,"reversed":false,"object":"charge"}
     */

    private ContentBean content;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * metadata : {"orderType":"3","companyID":"24322295870175816778556","companyInfoID":"243222958701758167","callBackType":"3","paymentOrderID":"1588993372403894","personID":"2966574415370208173","shopID":"7326354713687901184","callBakcUrl":null,"projectName":"墨丹农夫专营店-【母亲节花束】紫霞仙子花束  紫玫瑰5枝+..."}
         * livemode : true
         * subject : 墨丹农夫专营店-【母亲节...
         * channel : wx
         * transactionNo : null
         * amountSettle : 3966
         * description : null
         * body : 墨丹农夫专营店-【母亲节花束】紫霞仙子花束  紫玫瑰5...
         * timeSettle : null
         * refunds : null
         * timeExpire : 1589000578
         * timePaid : null
         * credential : {"object":"credential","wx":{"appId":"wx62d7f2c3cfc04076","partnerId":"1524701451","prepayId":"wx09110259071967116639ad211278795000","nonceStr":"ef58e457b801af6ab301ecba6b618961","timeStamp":"1588993379","packageValue":"Sign=WXPay","sign":"C7F75D632B7283553200DD6B18F3BF1B"}}
         * amountRefunded : 0
         * extra : {}
         * refunded : false
         * currency : cny
         * id : ch_101200509407335782400009
         * failureMsg : null
         * app : app_Li98WTWvDS8OD0e9
         * amount : 3990
         * orderNo : 1588993372403894881
         * failureCode : null
         * created : 1588993378
         * clientIp : 192.168.1.114
         * paid : false
         * reversed : false
         * object : charge
         */

        private MetadataBean metadata;
        private boolean livemode;
        private String subject;
        private String channel;
        private Object transactionNo;
        private int amountSettle;
        private Object description;
        private String body;
        private Object timeSettle;
        private Object refunds;
        private int timeExpire;
        private Object timePaid;
        private CredentialBean credential;
        private int amountRefunded;
        private ExtraBean extra;
        private boolean refunded;
        private String currency;
        private String id;
        private Object failureMsg;
        private String app;
        private int amount;
        private String orderNo;
        private Object failureCode;
        private int created;
        private String clientIp;
        private boolean paid;
        private boolean reversed;
        private String object;

        public MetadataBean getMetadata() {
            return metadata;
        }

        public void setMetadata(MetadataBean metadata) {
            this.metadata = metadata;
        }

        public boolean isLivemode() {
            return livemode;
        }

        public void setLivemode(boolean livemode) {
            this.livemode = livemode;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public Object getTransactionNo() {
            return transactionNo;
        }

        public void setTransactionNo(Object transactionNo) {
            this.transactionNo = transactionNo;
        }

        public int getAmountSettle() {
            return amountSettle;
        }

        public void setAmountSettle(int amountSettle) {
            this.amountSettle = amountSettle;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Object getTimeSettle() {
            return timeSettle;
        }

        public void setTimeSettle(Object timeSettle) {
            this.timeSettle = timeSettle;
        }

        public Object getRefunds() {
            return refunds;
        }

        public void setRefunds(Object refunds) {
            this.refunds = refunds;
        }

        public int getTimeExpire() {
            return timeExpire;
        }

        public void setTimeExpire(int timeExpire) {
            this.timeExpire = timeExpire;
        }

        public Object getTimePaid() {
            return timePaid;
        }

        public void setTimePaid(Object timePaid) {
            this.timePaid = timePaid;
        }

        public CredentialBean getCredential() {
            return credential;
        }

        public void setCredential(CredentialBean credential) {
            this.credential = credential;
        }

        public int getAmountRefunded() {
            return amountRefunded;
        }

        public void setAmountRefunded(int amountRefunded) {
            this.amountRefunded = amountRefunded;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public boolean isRefunded() {
            return refunded;
        }

        public void setRefunded(boolean refunded) {
            this.refunded = refunded;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getFailureMsg() {
            return failureMsg;
        }

        public void setFailureMsg(Object failureMsg) {
            this.failureMsg = failureMsg;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Object getFailureCode() {
            return failureCode;
        }

        public void setFailureCode(Object failureCode) {
            this.failureCode = failureCode;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public boolean isReversed() {
            return reversed;
        }

        public void setReversed(boolean reversed) {
            this.reversed = reversed;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public static class MetadataBean {
            /**
             * orderType : 3
             * companyID : 24322295870175816778556
             * companyInfoID : 243222958701758167
             * callBackType : 3
             * paymentOrderID : 1588993372403894
             * personID : 2966574415370208173
             * shopID : 7326354713687901184
             * callBakcUrl : null
             * projectName : 墨丹农夫专营店-【母亲节花束】紫霞仙子花束  紫玫瑰5枝+...
             */

            private String orderType;
            private String companyID;
            private String companyInfoID;
            private String callBackType;
            private String paymentOrderID;
            private String personID;
            private String shopID;
            private Object callBakcUrl;
            private String projectName;

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getCompanyID() {
                return companyID;
            }

            public void setCompanyID(String companyID) {
                this.companyID = companyID;
            }

            public String getCompanyInfoID() {
                return companyInfoID;
            }

            public void setCompanyInfoID(String companyInfoID) {
                this.companyInfoID = companyInfoID;
            }

            public String getCallBackType() {
                return callBackType;
            }

            public void setCallBackType(String callBackType) {
                this.callBackType = callBackType;
            }

            public String getPaymentOrderID() {
                return paymentOrderID;
            }

            public void setPaymentOrderID(String paymentOrderID) {
                this.paymentOrderID = paymentOrderID;
            }

            public String getPersonID() {
                return personID;
            }

            public void setPersonID(String personID) {
                this.personID = personID;
            }

            public String getShopID() {
                return shopID;
            }

            public void setShopID(String shopID) {
                this.shopID = shopID;
            }

            public Object getCallBakcUrl() {
                return callBakcUrl;
            }

            public void setCallBakcUrl(Object callBakcUrl) {
                this.callBakcUrl = callBakcUrl;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }
        }

        public static class CredentialBean {
            /**
             * object : credential
             * wx : {"appId":"wx62d7f2c3cfc04076","partnerId":"1524701451","prepayId":"wx09110259071967116639ad211278795000","nonceStr":"ef58e457b801af6ab301ecba6b618961","timeStamp":"1588993379","packageValue":"Sign=WXPay","sign":"C7F75D632B7283553200DD6B18F3BF1B"}
             * alipay : {"orderInfo":"app_id=2018112962381311&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2&timestamp=2020-05-09+14%3A09%3A32&version=1.0&biz_content=%7B%22body%22%3A%22%E5%A2%A8%E4%B8%B9%E5%86%9C%E5%A4%AB%E4%B8%93%E8%90%A5%E5%BA%97-%E3%80%90%E6%AF%8D%E4%BA%B2%E8%8A%82%E8%8A%B1%E6%9D%9F%E3%80%91%E7%B4%AB%E9%9C%9E%E4%BB%99%E5%AD%90%E8%8A%B1%E6%9D%9F++%E7%B4%AB%E7%8E%AB%E7%91%B05...%22%2C%22subject%22%3A%22%E5%A2%A8%E4%B8%B9%E5%86%9C%E5%A4%AB%E4%B8%93%E8%90%A5%E5%BA%97-%E3%80%90%E6%AF%8D%E4%BA%B2%E8%8A%82...%22%2C%22out_trade_no%22%3A%221588993372403894881%22%2C%22total_amount%22%3A%2239.90%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221253m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_101200509521962659840009&sign=tZ31EN2EGe9g8z4szyRphKpsTaCUJrb%2FKUdeFYQKs20A4DRogFOKazuwwh8hSUWKkj7XqpUDCTEzqrZm5UjFNgYuwHWag7%2BZ%2F0%2FXnIHwUQso2rG3vSJ8gNBGQHSU5drlBFAzp93bqWHl4UyS66HqmFWkwbeGGquY0LC5d4vpqg18YI0mymlr%2FROaQoV%2F%2B8P0f5VHnBtR2j8AV2TLh73g3szGVldSc7a%2BRauAmhGEVUsmVhI0pB7%2B6q4v%2B6RCltcGkonseteCrp%2B8g9WBcMk4JuL1i9bxoZ3I4uy3%2FDWQk6MnCrXYCiZxvRe%2FXU2X%2FQVuRyBz31oPGxJZ8vBqlcfbwg%3D%3D"}
             */

            private String object;
            private WxBean wx;
            private AlipayBean alipay;

            public String getObject() {
                return object;
            }

            public void setObject(String object) {
                this.object = object;
            }

            public WxBean getWx() {
                return wx;
            }

            public void setWx(WxBean wx) {
                this.wx = wx;
            }

            public AlipayBean getAlipay() {
                return alipay;
            }

            public void setAlipay(AlipayBean alipay) {
                this.alipay = alipay;
            }

            public static class WxBean {
                /**
                 * appId : wx62d7f2c3cfc04076
                 * partnerId : 1524701451
                 * prepayId : wx09110259071967116639ad211278795000
                 * nonceStr : ef58e457b801af6ab301ecba6b618961
                 * timeStamp : 1588993379
                 * packageValue : Sign=WXPay
                 * sign : C7F75D632B7283553200DD6B18F3BF1B
                 */

                private String appId;
                private String partnerId;
                private String prepayId;
                private String nonceStr;
                private String timeStamp;
                private String packageValue;
                private String sign;

                public String getAppId() {
                    return appId;
                }

                public void setAppId(String appId) {
                    this.appId = appId;
                }

                public String getPartnerId() {
                    return partnerId;
                }

                public void setPartnerId(String partnerId) {
                    this.partnerId = partnerId;
                }

                public String getPrepayId() {
                    return prepayId;
                }

                public void setPrepayId(String prepayId) {
                    this.prepayId = prepayId;
                }

                public String getNonceStr() {
                    return nonceStr;
                }

                public void setNonceStr(String nonceStr) {
                    this.nonceStr = nonceStr;
                }

                public String getTimeStamp() {
                    return timeStamp;
                }

                public void setTimeStamp(String timeStamp) {
                    this.timeStamp = timeStamp;
                }

                public String getPackageValue() {
                    return packageValue;
                }

                public void setPackageValue(String packageValue) {
                    this.packageValue = packageValue;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }
            }

            public static class AlipayBean {
                /**
                 * orderInfo : app_id=2018112962381311&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2&timestamp=2020-05-09+14%3A09%3A32&version=1.0&biz_content=%7B%22body%22%3A%22%E5%A2%A8%E4%B8%B9%E5%86%9C%E5%A4%AB%E4%B8%93%E8%90%A5%E5%BA%97-%E3%80%90%E6%AF%8D%E4%BA%B2%E8%8A%82%E8%8A%B1%E6%9D%9F%E3%80%91%E7%B4%AB%E9%9C%9E%E4%BB%99%E5%AD%90%E8%8A%B1%E6%9D%9F++%E7%B4%AB%E7%8E%AB%E7%91%B05...%22%2C%22subject%22%3A%22%E5%A2%A8%E4%B8%B9%E5%86%9C%E5%A4%AB%E4%B8%93%E8%90%A5%E5%BA%97-%E3%80%90%E6%AF%8D%E4%BA%B2%E8%8A%82...%22%2C%22out_trade_no%22%3A%221588993372403894881%22%2C%22total_amount%22%3A%2239.90%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221253m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_101200509521962659840009&sign=tZ31EN2EGe9g8z4szyRphKpsTaCUJrb%2FKUdeFYQKs20A4DRogFOKazuwwh8hSUWKkj7XqpUDCTEzqrZm5UjFNgYuwHWag7%2BZ%2F0%2FXnIHwUQso2rG3vSJ8gNBGQHSU5drlBFAzp93bqWHl4UyS66HqmFWkwbeGGquY0LC5d4vpqg18YI0mymlr%2FROaQoV%2F%2B8P0f5VHnBtR2j8AV2TLh73g3szGVldSc7a%2BRauAmhGEVUsmVhI0pB7%2B6q4v%2B6RCltcGkonseteCrp%2B8g9WBcMk4JuL1i9bxoZ3I4uy3%2FDWQk6MnCrXYCiZxvRe%2FXU2X%2FQVuRyBz31oPGxJZ8vBqlcfbwg%3D%3D
                 */

                private String orderInfo;

                public String getOrderInfo() {
                    return orderInfo;
                }

                public void setOrderInfo(String orderInfo) {
                    this.orderInfo = orderInfo;
                }
            }
        }

        public static class ExtraBean {
        }
    }
}
