package com.hilife.webview.bowser_client;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;

import com.hilife.webview.R;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import cn.net.cyberwy.hopson.lib_custom_views.dialog.DialogUtil;

/**
 * 对应WebView的WebViewChromeClient
 * Created by huteng on 16/5/27.
 */
public abstract class DefaultXwalkChromeClient extends WebChromeClient {

    private String TAG = "DefaultXwalkChromeClient";

    abstract public void chooseFile(ValueCallback<Uri> uploadMsg, Intent i);

    //扩展支持alert事件
    @Override
    public boolean onJsAlert(WebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult result) {
        DialogUtil.showAlert(view.getContext(), message, null, null, view.getContext().getResources().getString(R.string.btn_submit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
                dialog.dismiss();
            }
        }, false);
        return true;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    // For Android 3.0+
    public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        //由于xiaomi机器的文件路径和后缀的特殊 录音暂时屏蔽
        Intent chooser = createChooserIntent(createCameraIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, i);
        chooseFile(uploadMsg, chooser);
    }
    // For Android 3.0-

    public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooser(uploadMsg);
    }

    // For Android 4.1+
    @Override
    public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg);
    }
    //for 5.0+

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        Log.i(TAG, "onShowFileChooser: " + new Gson().toJson(fileChooserParams.getAcceptTypes()));
        ValueCallback<Uri> tempCallback = new ValueCallback<Uri>() {
            @Override
            public void onReceiveValue(Uri uri) {
                if (uri != null) {
                    Uri[] uris = {uri};
                    valueCallback.onReceiveValue(uris);
                } else {
                    valueCallback.onReceiveValue(null);
                }
            }
        };
        openFileChooser(tempCallback);
//        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String message, String s1, com.tencent.smtt.export.external.interfaces.JsResult result) {
        DialogUtil.showAlert(view.getContext(), message, view.getContext().getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
                dialog.dismiss();
            }
        }, view.getContext().getResources().getString(R.string.btn_submit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
                dialog.dismiss();
            }
        }, false);
        return true;
    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        chooser.putExtra(Intent.EXTRA_TITLE, "选择操作"); // File Chooser
        return chooser;
    }

    public Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		fileChoose = FileUtil.findOpenFile(FileUtil.createPictureName());
//		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//				Uri.fromFile(fileChoose));
        return cameraIntent;
    }

    private Intent createCamcorderIntent() {
        Intent CamcorderIntent = new Intent(Intent.ACTION_GET_CONTENT);
//		fileChoose = FileUtil.findOpenFile(
//				FileUtil.getOutputMediaFile(FileUtil.MEDIA_TYPE_CAMCORDER));
//		CamcorderIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//				Uri.fromFile(fileChoose));
//		CamcorderIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        return CamcorderIntent;
    }
}
