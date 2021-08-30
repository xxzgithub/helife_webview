package com.hilife.webview.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.net.cyberwy.hopson.lib_framework.base.delegate.AppLifecycles;
import timber.log.Timber;

public class WebModuleAppLifecycleImpl implements AppLifecycles {

    private static Context context;
    private Application mApplication;
    private boolean isBackground = false;

    @Override
    public void attachBaseContext(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@NonNull Application application) {
        this.mApplication=application;
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onTrimMemory(int i) {

    }

    @Override
    public void onLowMemory() {

    }
    /**
     * 获取Application的 上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }
    /**
     * 监听前后台变化
     */
    private void listenForgroundChange() {
        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                if (isBackground) {
                    isBackground = false;//从后台切换至前台了
                    Timber.i("切换至前台: ");
                    UnreadMessgeHelper.getInstance(mApplication).startTask();
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }


}
