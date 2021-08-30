package com.hilife.webview.model.js;

/**
 * JSAddCommentParam
 *
 * @author hewanxian
 * @date 16/3/15
 * Copyright © 2016年 branch.6.3.0.portal.3.2.AppBox. All rights reserved.
 */
public class JSAddCommentParam extends BaseJSParam {

    /**
     * 评论的feedID
     */
    private String feedID;

    /**
     * 评论的commentID
     */
    private String commentID;


    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
}
