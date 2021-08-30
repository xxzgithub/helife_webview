package com.hilife.webview.model.js;

import java.util.List;

/**
 * Created by Wanxian.He on 2016/5/5.
 */
public class JSUploadFileParam extends BaseJSParam {
    private List<String> files;
    private Integer isShowProgress;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Integer getIsShowProgress() {
        return isShowProgress;
    }

    public void setIsShowProgress(Integer isShowProgress) {
        this.isShowProgress = isShowProgress;
    }
}
