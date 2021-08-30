package com.hilife.webview.base;

import android.webkit.JavascriptInterface;

public interface InJavaScript {

    /**
     * 注册成功回调
     *
     * @param username 用户名
     * @param password 密码
     * @param cStatus  是否同时创建社区
     */
    public void registerSuccess(String username, String password, String cStatus);

    /**
     * 注册成功回调
     *
     * @param phone 注册手机号
     */

    public void pLogin(String phone);

    /**
     * 修改密码成功回调
     *
     * @param password新密码
     */

    public void modifyPasswordSuccess(String password);

    /**
     * 体验环境，意见反馈成功回调
     */

    public void expFeedBackSuccess();

    /**
     * 修改个人信息成功回调
     */

    public void modifyPersonInfoSuccess();

    /**
     * 后退
     */

    public void goBackup();

    /**
     * 注册成功，回调
     *
     * @param mobile 用户名
     * @param pwd    密码
     */

    public void goFinish(String mobile, String pwd);

    /**
     * 打开文件（文档类型查看）
     *
     * @param fileID     文件ID
     * @param fileName   文件名字
     * @param widgetID   栏目ID
     * @param contentID  内容ID
     * @param shortChain 短链
     * @param fileSize   文件大小
     * @since 20140506加入三个字段
     */

    public void goOpenFile(String fileID, String fileName, String widgetID,
                           String contentID, String shortChain, String fileSize);

    /**
     * 加入社区成功回调
     *
     * @param communityID   社区ID
     * @param communityName 社区名字
     */

    public void joinWork(String communityID, String communityName);

    /**
     * 设置移动端网页内容摘要
     *
     * @param summary 摘要
     */

    public void getWebDesc(String summary);

    /**
     * 设置dajia自定义参数
     *
     * @param webParamJson
     */

    public void getWebParam(String webParamJson);

    /**
     * 表单内容保存回调
     *
     * @param status       0：成功  ;""：失败，无提示； "XXXX"：失败，有提示
     * @param formID       表单ID
     * @param formRecordID 表单记录ID
     * @param title        表单名称
     */

    public void saveFormFeed(String status, String formID, String formRecordID, String title);

    public void openurl(String url);

    public void setTipoffID(String sourceID);

    public void setTipoffType(String sourceType);

    @JavascriptInterface
    void openSuccess();

    @JavascriptInterface
    void openFailed();

    @JavascriptInterface
    void Finish();
}
