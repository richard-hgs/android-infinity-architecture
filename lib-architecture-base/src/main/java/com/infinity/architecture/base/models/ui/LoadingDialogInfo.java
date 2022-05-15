package com.infinity.architecture.base.models.ui;

import androidx.annotation.Nullable;

public class LoadingDialogInfo {
    private boolean visible;
    @Nullable
    private String loadingMsg;
    @Nullable
    private String loadingProgressMsg;

    private boolean cancelable = true;

    /**
     * Private constructor should never be used
     */
    private LoadingDialogInfo() {}

    /**
     * Retrieve a visible loading dialog instance
     * @param loadingMsg            {@link String} Loading message title
     * @param loadingProgressMsg    {@link String} Loading message of the progress
     * @return                      {@link LoadingDialogInfo} Instance
     */
    public static LoadingDialogInfo showLoading(@Nullable String loadingMsg, @Nullable String loadingProgressMsg) {
        return showLoading(loadingMsg, loadingProgressMsg, true);
    }

    /**
     * Retrieve a visible loading dialog instance
     * @param loadingMsg            {@link String} Loading message title
     * @param loadingProgressMsg    {@link String} Loading message of the progress
     * @param cancelable            {@link Boolean} True=Dismissible on outside press, false=Not
     * @return                      {@link LoadingDialogInfo} Instance
     */
    public static LoadingDialogInfo showLoading(@Nullable String loadingMsg, @Nullable String loadingProgressMsg, boolean cancelable) {
        LoadingDialogInfo mLoadingDialogInfo = new LoadingDialogInfo();
        mLoadingDialogInfo.visible = true;
        mLoadingDialogInfo.loadingMsg = loadingMsg;
        mLoadingDialogInfo.loadingProgressMsg = loadingProgressMsg;
        mLoadingDialogInfo.cancelable = cancelable;
        return mLoadingDialogInfo;
    }

    /**
     * Retrieve a dismiss loading dialog instance
     * @return {@link LoadingDialogInfo} Dismiss loading dialog instance
     */
    public static LoadingDialogInfo dismissLoading() {
        return new LoadingDialogInfo();
    }

    public boolean isVisible() {
        return visible;
    }

    @Nullable
    public String getLoadingMsg() {
        return loadingMsg;
    }

    @Nullable
    public String getLoadingProgressMsg() {
        return loadingProgressMsg;
    }

    public boolean isCancelable() {
        return cancelable;
    }
}
