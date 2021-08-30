package com.hilife.webview.model.js;

import java.util.List;

/**
 * Created by lihuanzhong on 10/13/16.
 */
public class JSUploadFileByAppParam extends BaseJSParam {
    public static final Integer RETURN_TYPE_BASE64 = 2;

    private List<String> files;
    private Integer isShowProgress;

    public Integer getIsShowProgress() {
        return isShowProgress;
    }

    public void setIsShowProgress(Integer isShowProgress) {
        this.isShowProgress = isShowProgress;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
