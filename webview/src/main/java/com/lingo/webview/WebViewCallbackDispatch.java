package com.lingo.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

public class WebViewCallbackDispatch implements WebViewCallback {

    private final List<WebViewCallback> mWebViewCallbacks = new ArrayList<>();

    void addCallback(WebViewCallback callback) {
        if (!mWebViewCallbacks.contains(callback)) {
            mWebViewCallbacks.add(callback);
        }
    }

    void removeCallback(WebViewCallback callback) {
        mWebViewCallbacks.remove(callback);
    }

    void clearCallback() {
        mWebViewCallbacks.clear();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onPageFinished(view, url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        boolean ret = false;

        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            if (webViewCallback.shouldOverrideUrlLoading(view, url)) {
                ret = true;
            }
        }

        return ret;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        WebResourceResponse ret = null;

        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            WebResourceResponse request = webViewCallback.shouldInterceptRequest(view, url);
            if (request != null) {
                ret = request;
            }
        }

        return ret;
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onReceivedSslError(view, handler, error);
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onReceivedHttpError(view, request, errorResponse);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onReceivedError(view, request, error);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        for (WebViewCallback webViewCallback : mWebViewCallbacks) {
            webViewCallback.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}
