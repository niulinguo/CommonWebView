package com.lingo.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public interface WebViewCallback {

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    boolean shouldOverrideUrlLoading(WebView view, String url);

    WebResourceResponse shouldInterceptRequest(WebView view, String url);

    void doUpdateVisitedHistory(WebView view, String url, boolean isReload);

    void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

    void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse);

    void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
}
