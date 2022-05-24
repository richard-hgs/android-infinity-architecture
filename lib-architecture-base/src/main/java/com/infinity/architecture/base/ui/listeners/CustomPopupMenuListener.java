package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.IdRes;

public interface CustomPopupMenuListener {
    boolean onMenuItemClick(@IdRes int menuItemId);

    void onDismiss();
}
