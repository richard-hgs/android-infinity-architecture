package com.infinity.architecture.base.models.ui;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActionBarInfo {

    private String title;
    private String subTitle;
    private Boolean homeAsUpEnabled;
    private Integer homeAsUpIcon;
    private Boolean visible;

    private ActionBarInfo() {}

    @NonNull
    public static ActionBarInfo getInstance(@Nullable String title) {
        return getInstance(title, null, null);
    }

    @NonNull
    public static ActionBarInfo getInstance(@Nullable String title, @Nullable Boolean homeAsUpEnabled, @Nullable Boolean visible) {
        return getInstance(title, null, homeAsUpEnabled, visible);
    }

    @NonNull
    public static ActionBarInfo getInstance(@Nullable String title, @Nullable String subTitle, @Nullable Boolean homeAsUpEnabled, @Nullable Boolean visible) {
        return getInstance(title, subTitle, homeAsUpEnabled, null, visible);
    }


    @NonNull
    public static ActionBarInfo getInstance(@Nullable String title, @Nullable String subTitle, @Nullable Boolean homeAsUpEnabled, @Nullable @DrawableRes Integer homeAsUpIcon, @Nullable Boolean visible) {
        ActionBarInfo actionBarInfo = new ActionBarInfo();
        actionBarInfo.title = title;
        actionBarInfo.subTitle = subTitle;
        actionBarInfo.homeAsUpIcon = homeAsUpIcon;
        actionBarInfo.homeAsUpEnabled = homeAsUpEnabled;
        actionBarInfo.visible = visible;
        return actionBarInfo;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubTitle() {
        return subTitle;
    }

    @Nullable
    public Boolean getHomeAsUpEnabled() {
        return homeAsUpEnabled;
    }

    @DrawableRes
    @Nullable
    public Integer getHomeAsUpIcon() {
        return homeAsUpIcon;
    }

    @Nullable
    public Boolean getVisible() {
        return visible;
    }
}