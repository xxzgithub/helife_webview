package com.hilife.webview.model.js;

import com.dajia.android.base.exception.AppException;
import com.dajia.android.base.util.JSONUtil;
import com.dajia.android.base.util.StringUtil;

public class JSShowScanParam extends BaseJSParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7700085356338006733L;
    /**
     * 	回调类型<br>
     *  success fail complete cancel
     */
    private String code;
    /**
     * 回调返回值
     */
    private Object res;

	/**
	 * 默认为0，扫描结果由大家社区处理，1则直接返回扫描结果
	 */
	private int needResult;

    public JSShowScanParam(){};
    public JSShowScanParam(String callbackId,String code){
        super(callbackId);
        this.code = code;
    }
    public JSShowScanParam(String callbackId,String code,Object res){
        super(callbackId);
        this.code = code;
        this.res = res;
    }

	public int getNeedResult() {
		return needResult;
	}

	public void setNeedResult(int needResult) {
		this.needResult = needResult;
	}

    public static JSShowScanParam success(String callBackId,  Object res){
        return new JSShowScanParam(callBackId,"success",res);
    }

    public boolean isEmpty(){
        return StringUtil.isBlank(this.getCallbackId());
    }

    @Override
    public String toString() {
        if(!isEmpty()){
            try {
                return "javascript:djInternal.k('"+ JSONUtil.toJSON(this)+"')";
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
