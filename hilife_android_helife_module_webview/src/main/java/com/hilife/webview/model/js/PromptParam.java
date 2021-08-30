package com.hilife.webview.model.js;

public class PromptParam extends BaseJSParam {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8083306316084060747L;
	private int type; //标记提示	0:失败返回;  1:	提示我的服务单;  2:成功返回 ;  98:取消页面动作操作   99：恢复页面动作操作
    private String style; //显示提示的样式 0：普通(默认)  1:警告 2:错误   9:顶部红条
    private String msg; //提示语， 或者动作类型flush


    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
