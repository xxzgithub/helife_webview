package com.hilife.webview.model.js;

import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;


/**
 * js交互的回调参数
 *
 * @author adminiatrator
 */
public class JSReturnParam extends BaseJSParam {
    /**
     *
     */
    private static final long serialVersionUID = 8541962534378679116L;
    /**
     * 回调类型<br>
     * success fail complete cancel
     */
    private String code;
    /**
     * 回调返回值
     */
    private Object res;

    public JSReturnParam() {
    }

    public JSReturnParam(String callbackId, String code) {
        super(callbackId);
        this.code = code;
    }

    public JSReturnParam(String callbackId, String code, Object res) {
        super(callbackId);
        this.code = code;
        this.res = res;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }

    public static JSReturnParam success(String callBackId, Object res) {
        return new JSReturnParam(callBackId, "success", res);
    }


    public static JSReturnParam fail(String callBackId, Object res) {
        return new JSReturnParam(callBackId, "fail", res);
    }


    public static JSReturnParam templete(String callBackId, Object res) {
        return new JSReturnParam(callBackId, "templete", res);
    }


    public static JSReturnParam cancel(String callBackId, Object res) {
        return new JSReturnParam(callBackId, "cancel", res);
    }

    public boolean isEmpty() {
        return StringUtil.isBlank(this.getCallbackId());
    }

    @Override
    public String toString() {
        if (!isEmpty()) {
            try {
                String result = JSONUtil.toJSON(this);
                return "javascript:djInternal.k('" + result + "')";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
