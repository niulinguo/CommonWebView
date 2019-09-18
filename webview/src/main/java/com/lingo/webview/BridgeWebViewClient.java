package com.lingo.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 如果要自定义WebViewClient必须要集成此类
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {

    private static final String toLoadJs = "WebViewJavascriptBridge.js";

    private final WebViewCallback mWebViewCallback;
    private final BridgeWebView webView;

    BridgeWebViewClient(BridgeWebView webView, WebViewCallback callback) {
        this.webView = webView;
        mWebViewCallback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else {
            return this.onCustomShouldOverrideUrlLoading(view, url) || super.shouldOverrideUrlLoading(view, url);
        }
    }

    // 增加shouldOverrideUrlLoading在api》=24时
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                webView.handlerReturnData(url);
                return true;
            } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                webView.flushMessageQueue();
                return true;
            } else {
                return this.onCustomShouldOverrideUrlLoading(view, url) || super.shouldOverrideUrlLoading(view, request);
            }
        } else {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

        mWebViewCallback.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

        mWebViewCallback.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);

        mWebViewCallback.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);

        mWebViewCallback.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        mWebViewCallback.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        BridgeUtil.webViewLoadLocalJs(view, toLoadJs);

        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }

        onCustomPageFinished(view, url);
    }

    private boolean onCustomShouldOverrideUrlLoading(WebView view, String url) {

        return mWebViewCallback.shouldOverrideUrlLoading(view, url);
    }

    private void onCustomPageFinished(WebView view, String url) {

        mWebViewCallback.onPageFinished(view, url);
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        WebResourceResponse request = mWebViewCallback.shouldInterceptRequest(view, url);

        if (request != null) {
            return request;
        }

        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);

        mWebViewCallback.doUpdateVisitedHistory(view, url, isReload);
    }
}