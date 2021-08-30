package com.hilife.webview.base;

import android.webkit.JavascriptInterface;

/**
 * js交互android接口
 *
 * @author adminiatrator
 */
public interface DJJavaScript {

    /**
     * Notify password success.
     *
     * @param param the param
     */
    @JavascriptInterface
    void notifyPasswordSuccess(String param);

    /**
     * 显示信息流的阅列表
     *
     * @param param :JSFeedParam
     */
    public void showReadList(String param);

    /**
     * 显示信息流的赞列表
     *
     * @param param :JSFeedParam
     */
    public void showPraiseList(String param);

    /**
     * 显示信息流的回复列表
     *
     * @param param :JSFeedParam
     */
    public void showCommentList(String param);


    public void setTimeOut();

    /**
     * 显示个人空间
     *
     * @param param :JSPersonParam
     */
    public void showPerson(String param);

    /**
     * 显示群组空间
     *
     * @param param :JSGroupParam
     */
    public void showGroup(String param);

    /**
     * 图片预览
     *
     * @param param :JSGroupParam
     */
    public void showPicList(String param);

    /**
     * 文件下载
     *
     * @param param :JSGroupParam
     */
    public void showFile(String param);

    /**
     * 选择小区
     *
     * @param param the param
     */
    public void chooseCommunity(String param);

    /**
     * 获取首页应用信息
     *
     * @param param :JSPortalParam
     */
    public void requestPortalTopic(String param);

    /**
     * 触发首页应用
     *
     * @param param the param
     */
    public void touchPortalTopic(String param);

    /**
     * 获取首页群组信息
     *
     * @param param the param
     */
    public void requestPortalGroup(String param);

    /**
     * Request all group.
     *
     * @param param the param
     */
    public void requestAllGroup(String param);

    /**
     * 触发我的群组
     */
    public void showGroupList();

    /**
     * 显示商品单条页信息
     *
     * @param param the param
     */
    public void showProduct(String param);

    /**
     * 触发群组搜索
     */
    public void showGroupSearch();

    /**
     * 获取首页信息流信息
     *
     * @param param the param
     */
    public void requestPortalFeed(String param);

    /**
     * 显示信息流详情
     *
     * @param param the param
     */
    public void showFeedDetail(String param);

    /**
     * 获取首页商品信息
     *
     * @param param the param
     */
    public void requestPortalShop(String param);

    /**
     * 新建信息流
     *
     * @param param the param
     */
    public void createFeed(String param);

    /**
     * 获取首页图片地址信息
     *
     * @param param the param
     */
    public void getPic(String param);

    /**
     * 关闭网页
     */
    public void closeWindow();

    /**
     * 新开网页
     *
     * @param param the param
     */
    public void createWindow(String param);

    /**
     * 网页后退
     */
    public void historyBack();

    /**
     * 展示提示
     *
     * @param param the param
     */
    public void showPrompt(String param);

    /**
     * 展示信息
     *
     * @param param the param
     */
    public void showMessage(String param);

    /**
     * 加入社区
     *
     * @param param the param
     */
    public void joinCommunity(String param);

    /**
     * 空实现
     *
     * @param param the param
     */
    public void joinGroup(String param);

    /**
     * Show location.
     *
     * @param param the param
     */
    public void showLocation(String param);

    /**
     * Gets location.
     *
     * @param param the param
     */
    public void getLocation(String param);

    /**
     * Show scan.
     *
     * @param param the param
     */
    public void showScan(String param);

    /**
     * Show qr code.
     *
     * @param param the param
     */
    public void showQRCode(String param);

    /**
     * 获取网络类型
     *
     * @param param the param
     */
    public void getNetWorkType(String param);

    /**
     * 获取分享列表
     *
     * @param param the param
     */
    public void showOptMenu(String param);

    /**
     * Init share opt menu.
     *
     * @param param the param
     */
    @JavascriptInterface
    public void initShareOptMenu(String param);

    /**
     * Show all group.
     *
     * @param param the param
     */
    public void showAllGroup(String param);

    /**
     * Request join group.
     *
     * @param param the param
     */
    public void requestJoinGroup(String param);

    /**
     * Show portal error.
     *
     * @param param the param
     */
    public void showPortalError(String param);

    /**
     * Request portal list.
     *
     * @param param the param
     */
    public void requestPortalList(String param);

    /**
     * Show portal detail.
     *
     * @param param the param
     */
    public void showPortalDetail(String param);

    /**
     * Scan device.
     *
     * @param param the param
     */
    public void scanDevice(String param);

    /**
     * Connect peripheral and read.
     *
     * @param param the param
     */
    public void connectPeripheralAndRead(String param);

    /**
     * Connect peripheral and write.
     *
     * @param param the param
     */
    public void connectPeripheralAndWrite(String param);

    /**
     * Need login.
     *
     * @param param the param
     */
    void needLogin(String param);

    /**
     * Need login for third.
     *
     * @param param the param
     */
    void needLoginForThird(String param);

    /**
     * Choose image.
     *
     * @param param the param
     */
    void chooseImage(String param);

    /**
     * Preview image.
     *
     * @param param the param
     */
    void previewImage(String param);

    /**
     * Start im conversation.
     *
     * @param param the param
     */
    void startIMConversation(String param);

    void startCustomServer(String param);

    /**
     * Upload file.
     *
     * @param param the param
     */
    public void uploadFile(String param);

    /**
     * Download image.
     *
     * @param param the param
     */
    public void downloadImage(String param);

    /**
     * Close window with message.
     *
     * @param param the param
     */
    public void closeWindowWithMessage(String param);

    /**
     * App login.
     *
     * @param param the param
     */
    public void appLogin(String param);

    /**
     * Forward.
     *
     * @param param the param
     */
    public void forward(String param);

    /**
     * Save form feed.
     *
     * @param param the param
     */
    public void saveFormFeed(String param);

    /**
     * Sets web param.
     *
     * @param param the param
     */
    public void setWebParam(String param);

    /**
     * Enter community.
     *
     * @param param the param
     */
    public void enterCommunity(String param);

    /**
     * Enter experience.
     *
     * @param param the param
     */
    public void enterExperience(String param);

    /**
     * Check fun.
     *
     * @param param the param
     */
    public void checkFun(String param);

    /**
     * Status window.
     *
     * @param param the param
     */
    public void statusWindow(String param);

    /**
     * Pay check.
     *
     * @param param the param
     */
    public void payCheck(String param);

    /**
     * Wxpay.
     *
     * @param param the param
     */
    public void wxpay(String param);

    /**
     * Alipay.
     *
     * @param param the param
     */
    public void alipay(String param);

    /**
     * Feed action.
     *
     * @param param the param
     */
    void feedAction(String param);

    /**
     * Add comment.
     *
     * @param param the param
     */
    void addComment(String param);

    /**
     * Request feed.
     *
     * @param param the param
     */
    void requestFeed(String param);

    /**
     * Show feed list.
     *
     * @param param the param
     */
    void showFeedList(String param);

    /**
     * Show blog.
     *
     * @param param the param
     */
    void showBlog(String param);

    /**
     * Gets theme color.
     *
     * @param param the param
     */
    void getThemeColor(String param);

    /**
     * Gets file.
     *
     * @param param the param
     */
    void getFile(String param);

    /**
     * Gets topic.
     *
     * @param param the param
     */
    void getTopic(String param);

    /**
     * Show all community.
     */
    void showAllCommunity();

    /**
     * Upload file by app.
     *
     * @param param the param
     */
//App 上传服务单图片
    public void uploadFileByApp(String param);

    /**
     * Sport refresh.
     *
     * @param param the param
     */
    void sportRefresh(String param);

    /**
     * Gets web host.
     *
     * @param param the param
     */
    void getWebHost(String param);

    /**
     * Ajax proxy.
     *
     * @param param the param
     */
    void ajaxProxy(String param);

    /**
     * Sets navigation title.
     *
     * @param param the param
     */
    void setNavigationTitle(String param);

    /**
     * Pingpay.
     *
     * @param param the param
     */
    void pingpay(String param);

    /**
     * 我发的消息
     *
     * @param param the param
     */
    void getMySelfSendFeed(String param);

    /**
     * 我的收藏
     */
    void getMySelfCollections();

    /**
     * 发送队列
     */
    void findSendFeedQuque();

    /**
     * 邀请
     */
    void sendInvitation();

    /**
     * 系统设置
     */
    void systemSetting();

    /**
     * 通知
     */
    void systemNotice();

    /**
     * 我的联系人
     */
    void myAddressBookInfo();

    /**
     * 我的群组
     */
    void myGroupInfo();

    /**
     * 个人资料
     */
    void myPersonInfo();

    /**
     * 客户咨询
     */
    void myCustomerServiceInfo();

    /**
     * tab中加载二级页面
     *
     * @param param the param
     */
    void getCustomMainPage(String param);

    /**
     * tab中二级页面加载完成
     *
     * @param param the param
     */
    void customMainPageFinished(String param);

    /**
     * 开启第三方地图app
     *
     * @param param the param
     */
    void openThreeMapNav(String param);

    /**
     * 可以对笔记评论
     *
     * @param param the param
     */
    void addCommentInfo(String param);

    /**
     * 打开我的社区页面
     */
    void showMyCommunityList();

    /**
     * 新打开页面方式为了解决IOS问题
     *
     * @param param the param
     */
    void createNewWindow(String param);

    /**
     * 返回到指定web页面
     *
     * @param param the param
     */
    void goBackToSpecifiedPage(String param);

    /**
     * 获取web页面个数
     *
     * @param param the param
     */
    void getSpecifiedPageCount(String param);

    /**
     * 更新应用的时间
     *
     * @param param the param
     */
    void updateUnReadDataByTagID(String param);

    /**
     * 认证完成，切换小区
     *
     * @param param the param
     */
    void closeWindowAndChangeCompany(String param);

    /**
     * 传递手机相应信息（平安银行暂用）
     *
     * @param param the param
     */
    void getDeviceInfo(String param);

    /**
     * 监听网页title变化
     */
    void pageTitleChanged();

    /**
     * 友盟埋点，H5桥接，H5传递数据，本地上报参数
     *
     * @param param the param
     */
    void umBridge(String param);

    /**
     * 获取支付订单参数
     *
     * @param param the param
     */
    void hilifepay(String param);

    /**
     * h5与navtive交互的方法 蚂蚁雄兵-分享海报
     *
     * @param param the param
     */
    void h5ToNativeJs(String param);

    /**
     * 打开Flutter页面
     *
     * @param param
     */
    void openFlutterPage(String param);

    /**
     * 跳转到app首页
     */
    void goAppHomePage();

    @JavascriptInterface
    void getStatusHeight(String param);

}
