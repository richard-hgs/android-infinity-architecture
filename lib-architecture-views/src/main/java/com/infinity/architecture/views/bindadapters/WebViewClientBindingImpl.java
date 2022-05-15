package com.infinity.architecture.views.bindadapters;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

public class WebViewClientBindingImpl implements WebViewClientBinding {
    @Override
    public void onReceivedError(WebResourceRequest request, WebResourceError error) {

    }

    @Override
    public void onReceivedHttpError(WebResourceRequest request, WebResourceResponse errorResponse) {

    }

    @Override
    public void onPageFinished(String url) {

    }
}
