package com.lingo.webview;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;

import java.util.ArrayList;
import java.util.List;

public class AppWebView extends BridgeWebView {

    private static final List<AppWebView> sUseList = new ArrayList<>();
    private static final List<AppWebView> sUselessList = new ArrayList<>();

    private final Config mConfig;

    private AppWebView(Context context, Config config) {
        super(context);

        mConfig = config;

        WebSettings websettings = getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCachePath(context.getCacheDir().getAbsolutePath());
        websettings.setAllowFileAccess(true);    // 可以读取文件缓存
        websettings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
        websettings.setDatabaseEnabled(true);
        websettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (!TextUtils.isEmpty(config.userAgentAppend)) {
            websettings.setUserAgentString(websettings.getUserAgentString() + " " + config.userAgentAppend);
        }
    }

    public boolean goback() {
        if (canGoBack()) {
            goBack();
            return true;
        }
        return false;
    }

    public void attachToParent(ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.removeView(this);
            viewGroup.addView(this);
        }
    }

    public static AppWebView obtain(Application app, Config config, AppWebViewCreator creator) {

        AppWebView appWebView;

        if (sUselessList.isEmpty()) {
            if (creator == null) {
                appWebView = new AppWebView(app, config);
            } else {
                appWebView = creator.create();
                if (appWebView == null) {
                    appWebView = new AppWebView(app, config);
                }
            }
        } else {
            appWebView = sUselessList.remove(sUselessList.size() - 1);
        }

        sUseList.add(appWebView);

        if (config.printBufferLog) {
            Log.e("Lingo", "web buffer use:" + sUseList.size() + " useless:" + sUselessList.size());
        }

        return appWebView;
    }

    public void release() {

        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(this);
        }

        unregisterAllHandler();
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        clearCallback();

        sUseList.remove(this);
        sUselessList.add(this);

        // 释放没用的WebView
        while (sUselessList.size() > 2) {
            sUselessList.remove(sUselessList.size() - 1);
        }

        if (mConfig.printBufferLog) {
            Log.e("Lingo", "web buffer use:" + sUseList.size() + " useless:" + sUselessList.size());
        }
    }

    public static final class Config {

        public boolean printBufferLog;
        public String userAgentAppend;

    }

    interface AppWebViewCreator {

        AppWebView create();
    }
}
