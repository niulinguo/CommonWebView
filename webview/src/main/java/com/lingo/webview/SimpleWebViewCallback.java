package com.lingo.webview;

import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public class SimpleWebViewCallback implements WebViewCallback {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return null;
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {

    }
}
