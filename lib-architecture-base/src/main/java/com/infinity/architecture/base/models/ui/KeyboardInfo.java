package com.infinity.architecture.base.models.ui;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.base.enums.ui.KeyboardInfoAction;

public class KeyboardInfo {
    private KeyboardInfoAction action;

    // Change keyboard configuration for screen
    private Boolean closeKeyboardOnClickOutside;

    // Change keyboard state parameters SHOW/HIDE
    private int viewIdOfKeyboardAction = View.NO_ID;
    private boolean showKeyboard;
    private String dialogGuid = null;

    // Toggle keyboard state parameters SHOW/HIDE
    private int showFlags;
    private int hideFlags;

    public static KeyboardInfo showKeyboard(int viewId) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.CHANGE_KEYBOARD_STATE;
        keyboardInfo.showKeyboard = true;
        keyboardInfo.viewIdOfKeyboardAction = viewId;

        return keyboardInfo;
    }

    public static KeyboardInfo showKeyboard(int viewId, @NonNull String dialogGuid) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.CHANGE_KEYBOARD_STATE;
        keyboardInfo.showKeyboard = true;
        keyboardInfo.viewIdOfKeyboardAction = viewId;
        keyboardInfo.dialogGuid = dialogGuid;

        return keyboardInfo;
    }

    public static KeyboardInfo hideKeyboard() {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.CHANGE_KEYBOARD_STATE;
        keyboardInfo.showKeyboard = false;

        return keyboardInfo;
    }

    public static KeyboardInfo hideKeyboard(@NonNull String dialogGuid) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.CHANGE_KEYBOARD_STATE;
        keyboardInfo.showKeyboard = false;
        keyboardInfo.dialogGuid = dialogGuid;

        return keyboardInfo;
    }

    public static KeyboardInfo toggleKeyboardDefault(int viewId) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.TOGGLE_KEYBOARD;
        keyboardInfo.showFlags = InputMethodManager.SHOW_IMPLICIT;
        keyboardInfo.hideFlags = InputMethodManager.HIDE_NOT_ALWAYS;
        keyboardInfo.viewIdOfKeyboardAction = viewId;

        return keyboardInfo;
    }

    public static KeyboardInfo toggleKeyboardDefault(int viewId, @NonNull String dialogGuid) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.TOGGLE_KEYBOARD;
        keyboardInfo.showFlags = InputMethodManager.SHOW_IMPLICIT;
        keyboardInfo.hideFlags = InputMethodManager.HIDE_NOT_ALWAYS;
        keyboardInfo.dialogGuid = dialogGuid;
        keyboardInfo.viewIdOfKeyboardAction = viewId;

        return keyboardInfo;
    }

    public static KeyboardInfo configureKeyboard(@Nullable Boolean closeKeyboardOnClickOutside) {
        KeyboardInfo keyboardInfo = new KeyboardInfo();
        keyboardInfo.action = KeyboardInfoAction.CONFIGURE_KEYBOARD;
        keyboardInfo.closeKeyboardOnClickOutside = closeKeyboardOnClickOutside;
        return keyboardInfo;
    }

    public Boolean getCloseKeyboardOnClickOutside() {
        return closeKeyboardOnClickOutside;
    }

    public KeyboardInfoAction getAction() {
        return action;
    }

    public int getViewIdOfKeyboardAction() {
        return viewIdOfKeyboardAction;
    }

    public boolean isShowKeyboard() {
        return showKeyboard;
    }

    @Nullable
    public String getDialogGuid() {
        return dialogGuid;
    }

    public int getShowFlags() {
        return showFlags;
    }

    public int getHideFlags() {
        return hideFlags;
    }

    @Override
    public String toString() {
        return "KeyboardInfo{" +
            "action=" + action +
            ", closeKeyboardOnClickOutside=" + closeKeyboardOnClickOutside +
            ", viewIdOfKeyboardAction=" + viewIdOfKeyboardAction +
            ", showKeyboard=" + showKeyboard +
            ", dialogGuid='" + dialogGuid + '\'' +
            ", showFlags=" + showFlags +
            ", hideFlags=" + hideFlags +
        '}';
    }
}
