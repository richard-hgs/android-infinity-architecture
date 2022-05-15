package com.infinity.architecture.base.models.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.base.annotations.utils.Flags;

public class OpenScreenInfo {

    /**
     * Screen class to be opened
     */
    private Class<?> screenToOpen;

    /**
     * Arguments
     */
    private Bundle bundle;

    /**
     * Intent Flags
     */
    private Integer flags;

    /**
     * Finishes the current screen true or false
     */
    private boolean finishCurrentScreen;

    /**
     * When opening activity for result
     */
    private boolean isOpenForResult;

    private OpenScreenInfo(
        @NonNull Class<?> screenToOpen,
        @Nullable @Flags Integer flags,
        @Nullable Bundle bundle,
        boolean finishCurrentScreen,
        boolean isOpenForResult
    ) {
        this.screenToOpen = screenToOpen;
        this.flags = flags;
        this.bundle = bundle;
        this.finishCurrentScreen = finishCurrentScreen;
        this.isOpenForResult = isOpenForResult;
    }

    public static OpenScreenInfo getInstance(@NonNull Class<?> screenToOpen, boolean finishCurrentScreen) {
        return getInstance(screenToOpen, finishCurrentScreen, false, null);
    }

    public static OpenScreenInfo getInstance(@NonNull Class<?> screenToOpen, boolean finishCurrentScreen, boolean isOpenForResult, @Nullable Bundle bundle) {
        return new OpenScreenInfo(
            screenToOpen, null, bundle, finishCurrentScreen, isOpenForResult
        );
    }

    @NonNull
    public Class<?> getScreenToOpen() {
        return screenToOpen;
    }

    public void setScreenToOpen(@NonNull Class<?> screenToOpen) {
        this.screenToOpen = screenToOpen;
    }

    @Nullable
    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(@Nullable Bundle bundle) {
        this.bundle = bundle;
    }

    @Nullable
    @Flags
    public Integer getFlags() {
        return flags;
    }

    public void setFlags(@Flags int flags) {
        this.flags = flags;
    }

    public boolean isFinishCurrentScreen() {
        return finishCurrentScreen;
    }

    public void setFinishCurrentScreen(boolean finishCurrentScreen) {
        this.finishCurrentScreen = finishCurrentScreen;
    }

    public boolean isOpenForResult() {
        return isOpenForResult;
    }

    public void setOpenForResult(boolean openForResult) {
        isOpenForResult = openForResult;
    }

    @Override
    public String toString() {
        return "OpenScreenInfo{" +
                "screenToOpen=" + screenToOpen +
                ", bundle=" + bundle +
                ", flags=" + flags +
                ", finishCurrentScreen=" + finishCurrentScreen +
                ", isOpenForResult=" + isOpenForResult +
            '}';
    }
}
