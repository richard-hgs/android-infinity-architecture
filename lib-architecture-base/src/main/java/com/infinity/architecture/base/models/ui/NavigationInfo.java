package com.infinity.architecture.base.models.ui;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class NavigationInfo {
    private int actionId;
    private Bundle bundle;

    /**
     * When opening activity for result
     */
    private boolean isNavigateForResult;

    private NavigationInfo() {
    }

    public static NavigationInfo defNavigation(@IdRes int actionId, boolean isNavigateForResult, @Nullable Bundle bundle) {
        NavigationInfo navigationInfo = new NavigationInfo();
        navigationInfo.actionId = actionId;
        navigationInfo.bundle = bundle;
        navigationInfo.isNavigateForResult = isNavigateForResult;

        return navigationInfo;
    }

    @IdRes
    public int getActionId() {
        return actionId;
    }

    @Nullable
    public Bundle getBundle() {
        return bundle;
    }

    public boolean isNavigateForResult() {
        return isNavigateForResult;
    }

    @Override
    public String toString() {
        return "NavigationInfo{" +
                "actionId=" + actionId +
                ", bundle=" + bundle +
                ", isNavigateForResult=" + isNavigateForResult +
                '}';
    }
}
