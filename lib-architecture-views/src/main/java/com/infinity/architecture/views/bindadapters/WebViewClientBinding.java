package com.infinity.architecture.views.bindadapters;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

public interface WebViewClientBinding {
    void onReceivedError(WebResourceRequest request, WebResourceError error);

    void onReceivedHttpError(WebResourceRequest request, WebResourceResponse errorResponse);

    void onPageFinished(String url);
}
