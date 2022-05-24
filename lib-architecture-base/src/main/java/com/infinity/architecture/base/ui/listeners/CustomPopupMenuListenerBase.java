package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.IdRes;

public abstract class CustomPopupMenuListenerBase implements CustomPopupMenuListener {
    @Override
    public boolean onMenuItemClick(@IdRes int menuItemId) {
        return true;
    }

    @Override
    public void onDismiss() {

    }
}
