package com.infinity.architecture.base.models.ui;

import androidx.annotation.Nullable;

public class PickPictureInfo {

    private String copyToPath;

    private PickPictureInfo() {}

    public static PickPictureInfo getInstance(@Nullable String copyToPath) {
        PickPictureInfo pickPictureInfo = new PickPictureInfo();
        pickPictureInfo.copyToPath = copyToPath;
        return pickPictureInfo;
    }

    public void setCopyToPath(@Nullable String copyToPath) {
        this.copyToPath = copyToPath;
    }

    @Nullable
    public String getCopyToPath() {
        return copyToPath;
    }
}
