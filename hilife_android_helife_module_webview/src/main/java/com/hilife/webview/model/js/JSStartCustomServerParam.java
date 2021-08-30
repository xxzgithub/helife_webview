package com.hilife.webview.model.js;

import java.io.Serializable;

public class JSStartCustomServerParam extends BaseJSParam implements Serializable {

    private static final long serialVersionUID = -4935550368466530305L;

    /**
     * targetID :  //string 客服imId
     * targetName : //string 客服名称
     * companyID : //string 企业id
     * seatId : //string 坐席id
     * serverProvider :  //string 客服提供商  rong:融云 qiyu:七鱼 study:研究院
     * msgData : {"productMsgInfo":{"imgUrl":"//string 商品图片","productName":"//string 商品名称","moneyText":"//string 商品价格文案","link":"//string 商品连接","shopName":"//string 商品所属商店名称","productId":"//string 商品id"},"orderMsgInfo":{"imgUrl":"//string 商品图片","productName":"//string 商品名称","productCount":"//string 总商品个数","moneyText":"//string 订单费用文案","link":"//string 订单链接","productId":"//string 商品id","orderNum":"//string 订单号","createTimeStr":"//string 下单时间文案","orderId":"//string 订单id"},"linkMsgInfo":{"title":"//string 网页标题","subTitle":" //string 网页内容摘要","link":"//string 网页连接","imgUrl":"//string 图片连接"},"imgMsgInfo":{"imgUrl":"//string 图片地址"}}
     */

    private ParamsBean params;

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean implements Serializable {
        private static final long serialVersionUID = -2153567355089620637L;
        private String targetID;
        private String targetName;
        private String companyID;
        private String seatId;
        private String serverProvider;
        private int needBack; // 表示返回时返回到上上级 不返回客服选择列表
        /**
         * productMsgInfo : {"imgUrl":"//string 商品图片","productName":"//string 商品名称","moneyText":"//string 商品价格文案","link":"//string 商品连接","shopName":"//string 商品所属商店名称","productId":"//string 商品id"}
         * orderMsgInfo : {"imgUrl":"//string 商品图片","productName":"//string 商品名称","productCount":"//string 总商品个数","moneyText":"//string 订单费用文案","link":"//string 订单链接","productId":"//string 商品id","orderNum":"//string 订单号","createTimeStr":"//string 下单时间文案","orderId":"//string 订单id"}
         * linkMsgInfo : {"title":"//string 网页标题","subTitle":" //string 网页内容摘要","link":"//string 网页连接","imgUrl":"//string 图片连接"}
         * imgMsgInfo : {"imgUrl":"//string 图片地址"}
         */

        private MsgDataBean msgData;

        public String getTargetID() {
            return targetID;
        }

        public void setTargetID(String targetID) {
            this.targetID = targetID;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getCompanyID() {
            return companyID;
        }

        public void setCompanyID(String companyID) {
            this.companyID = companyID;
        }

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getServerProvider() {
            return serverProvider;
        }

        public void setServerProvider(String serverProvider) {
            this.serverProvider = serverProvider;
        }

        public MsgDataBean getMsgData() {
            return msgData;
        }

        public void setMsgData(MsgDataBean msgData) {
            this.msgData = msgData;
        }

        public int getNeedBack() {
            return needBack;
        }

        public static class MsgDataBean implements Serializable {
            private static final long serialVersionUID = 934245571070194511L;
            /**
             * imgUrl : //string 商品图片
             * productName : //string 商品名称
             * moneyText : //string 商品价格文案
             * link : //string 商品连接
             * shopName : //string 商品所属商店名称
             * productId : //string 商品id
             */

            private ProductMsgInfoBean productMsgInfo;
            /**
             * imgUrl : //string 商品图片
             * productName : //string 商品名称
             * productCount : //string 总商品个数
             * moneyText : //string 订单费用文案
             * link : //string 订单链接
             * productId : //string 商品id
             * orderNum : //string 订单号
             * createTimeStr : //string 下单时间文案
             * orderId : //string 订单id
             */

            private OrderMsgInfoBean orderMsgInfo;
            /**
             * title : //string 网页标题
             * subTitle :  //string 网页内容摘要
             * link : //string 网页连接
             * imgUrl : //string 图片连接
             */

            private LinkMsgInfoBean linkMsgInfo;
            /**
             * imgUrl : //string 图片地址
             */

            private ImgMsgInfoBean imgMsgInfo;

            public ProductMsgInfoBean getProductMsgInfo() {
                return productMsgInfo;
            }

            public void setProductMsgInfo(ProductMsgInfoBean productMsgInfo) {
                this.productMsgInfo = productMsgInfo;
            }

            public OrderMsgInfoBean getOrderMsgInfo() {
                return orderMsgInfo;
            }

            public void setOrderMsgInfo(OrderMsgInfoBean orderMsgInfo) {
                this.orderMsgInfo = orderMsgInfo;
            }

            public LinkMsgInfoBean getLinkMsgInfo() {
                return linkMsgInfo;
            }

            public void setLinkMsgInfo(LinkMsgInfoBean linkMsgInfo) {
                this.linkMsgInfo = linkMsgInfo;
            }

            public ImgMsgInfoBean getImgMsgInfo() {
                return imgMsgInfo;
            }

            public void setImgMsgInfo(ImgMsgInfoBean imgMsgInfo) {
                this.imgMsgInfo = imgMsgInfo;
            }

            public static class ProductMsgInfoBean implements Serializable {
                private static final long serialVersionUID = 6498367988800580427L;
                private String imgUrl;
                private String productName;
                private String moneyText;
                private String link;
                private String shopName;
                private String productId;

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getMoneyText() {
                    return moneyText;
                }

                public void setMoneyText(String moneyText) {
                    this.moneyText = moneyText;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getShopName() {
                    return shopName;
                }

                public void setShopName(String shopName) {
                    this.shopName = shopName;
                }

                public String getProductId() {
                    return productId;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }
            }

            public static class OrderMsgInfoBean implements Serializable {
                private static final long serialVersionUID = -4399695334966520549L;
                private String imgUrl;
                private String productName;
                private String productCount;
                private String moneyText;
                private String link;
                private String productId;
                private String orderNum;
                private String createTimeStr;
                private String orderId;

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getProductCount() {
                    return productCount;
                }

                public void setProductCount(String productCount) {
                    this.productCount = productCount;
                }

                public String getMoneyText() {
                    return moneyText;
                }

                public void setMoneyText(String moneyText) {
                    this.moneyText = moneyText;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getProductId() {
                    return productId;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public String getOrderNum() {
                    return orderNum;
                }

                public void setOrderNum(String orderNum) {
                    this.orderNum = orderNum;
                }

                public String getCreateTimeStr() {
                    return createTimeStr;
                }

                public void setCreateTimeStr(String createTimeStr) {
                    this.createTimeStr = createTimeStr;
                }

                public String getOrderId() {
                    return orderId;
                }

                public void setOrderId(String orderId) {
                    this.orderId = orderId;
                }
            }

            public static class LinkMsgInfoBean implements Serializable {
                private static final long serialVersionUID = -878384914889698506L;
                private String title;
                private String subTitle;
                private String link;
                private String imgUrl;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getSubTitle() {
                    return subTitle;
                }

                public void setSubTitle(String subTitle) {
                    this.subTitle = subTitle;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }

            public static class ImgMsgInfoBean implements Serializable {
                private static final long serialVersionUID = 6089757756266327253L;
                private String imgUrl;

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }
        }
    }
}
