package com.infinity.architecture.sampleapp.model;

import androidx.annotation.NonNull;

import com.infinity.architecture.sampleapp.enums.EnScreen;

/**
 * The item of recycler adapter
 */
public class RvAdptInfinityViewsItem {

    private String text;
    private EnScreen screenType;

    public RvAdptInfinityViewsItem(@NonNull String text, @NonNull EnScreen enScreen) {
        this.text = text;
        this.screenType = enScreen;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public EnScreen getScreenType() {
        return screenType;
    }

    public void setScreenType(@NonNull EnScreen screenType) {
        this.screenType = screenType;
    }
}
