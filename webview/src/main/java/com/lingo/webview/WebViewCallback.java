package com.lingo.webview;

import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public interface WebViewCallback {

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    boolean shouldOverrideUrlLoading(WebView view, String url);

    WebResourceResponse shouldInterceptRequest(WebView view, String url);

    void doUpdateVisitedHistory(WebView view, String url, boolean isReload);
}
