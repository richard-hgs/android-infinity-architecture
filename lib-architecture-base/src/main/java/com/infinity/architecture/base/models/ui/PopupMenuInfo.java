package com.infinity.architecture.base.models.ui;

import androidx.annotation.IdRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.infinity.architecture.base.ui.listeners.CustomPopupMenuListener;
import com.infinity.architecture.base.ui.listeners.CustomPopupMenuListenerBase;

import java.util.ArrayList;

public class PopupMenuInfo {

    private String guid;
    private int menuToInflate;
    private int viewIdToBindPopupMenu;

    private ArrayList<MenuItemConfig> menuItensConfigs;
    private CustomPopupMenuListener   menuListener;

    private PopupMenuInfo() {}

    @NonNull
    public static PopupMenuInfo simpleMenu(@NonNull String guid, @MenuRes int menuToInflate, @IdRes int viewIdToBindPopupMenu, @Nullable ArrayList<MenuItemConfig> menuItensConfigs, @Nullable CustomPopupMenuListenerBase menuListener) {
        PopupMenuInfo popupMenuInfo = new PopupMenuInfo();
        popupMenuInfo.guid = guid;
        popupMenuInfo.menuToInflate = menuToInflate;
        popupMenuInfo.viewIdToBindPopupMenu = viewIdToBindPopupMenu;
        popupMenuInfo.menuItensConfigs = menuItensConfigs;
        popupMenuInfo.menuListener = menuListener;

        return popupMenuInfo;
    }


    public String getGuid() {
        return guid;
    }

    @MenuRes
    public int getMenuToInflate() {
        return menuToInflate;
    }

    @IdRes
    public int getViewIdToBindPopupMenu() {
        return viewIdToBindPopupMenu;
    }

    @Nullable
    public ArrayList<MenuItemConfig> getMenuItensConfigs() {
        return menuItensConfigs;
    }

    @Nullable
    public CustomPopupMenuListener getMenuListener() {
        return menuListener;
    }

    public static class MenuItemConfig {
        private int menuItemId;
        private Boolean visible;

        @NonNull
        public static MenuItemConfig getInstance(@IdRes int menuItemId, @Nullable Boolean visible) {
            MenuItemConfig menuItemConfig = new MenuItemConfig();
            menuItemConfig.menuItemId = menuItemId;
            menuItemConfig.visible = visible;
            return menuItemConfig;
        }

        public MenuItemConfig() {
        }

        public int getMenuItemId() {
            return menuItemId;
        }

        @Nullable
        public Boolean isVisible() {
            return visible;
        }
    }
}
