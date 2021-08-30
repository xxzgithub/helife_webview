package com.hilife.webview.model.js;

import com.google.gson.annotations.SerializedName;

public class JSHilifepayWxParam extends BaseJSParam {

    /**
     * content : {"appid":"wx62d7f2c3cfc04076","noncestr":"yK6H4lOZyYzpjxy6Xja3Ys0Fchk9JvMX","package":"Sign=WXPay","partnerid":"1524701451","prepayid":"wx231551390687902ec58aab961846732500","sign":"E81AC2E796816DE3DB688B936F80243D","timestamp":"1569225099"}
     * type : wxpay
     */

    @SerializedName("content")
    private PayInfo payinfo;
    private String type;

    public PayInfo getPayinfo() {
        return payinfo;
    }

    public void setPayinfo(PayInfo payinfo) {
        this.payinfo = payinfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class PayInfo {
        /**
         * appid : wx62d7f2c3cfc04076
         * noncestr : yK6H4lOZyYzpjxy6Xja3Ys0Fchk9JvMX
         * package : Sign=WXPay
         * partnerid : 1524701451
         * prepayid : wx231551390687902ec58aab961846732500
         * sign : E81AC2E796816DE3DB688B936F80243D
         * timestamp : 1569225099
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
