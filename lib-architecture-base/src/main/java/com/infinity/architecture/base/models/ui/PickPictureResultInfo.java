package com.infinity.architecture.base.models.ui;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PickPictureResultInfo {
    private boolean errorExists;
    private int errorCode;
    private String errorMsg;

    private Bitmap bmpImg;
    private String imgPath;

    private PickPictureResultInfo() { }

    public static PickPictureResultInfo getSuccessInstance(@NonNull Bitmap bmpImg, @Nullable String imgPath) {
        PickPictureResultInfo pickPictureResultInfo = new PickPictureResultInfo();
        pickPictureResultInfo.errorExists = false;
        pickPictureResultInfo.bmpImg = bmpImg;
        pickPictureResultInfo.imgPath = imgPath;
        return pickPictureResultInfo;
    }

    public static PickPictureResultInfo getErrorInstance(
        int errorCode, @NonNull String errorMsg
    ) {
        PickPictureResultInfo pickPictureResultInfo = new PickPictureResultInfo();
        pickPictureResultInfo.errorExists = true;
        pickPictureResultInfo.errorCode = errorCode;
        pickPictureResultInfo.errorMsg = errorMsg;

        return pickPictureResultInfo;
    }

    public boolean isErrorExists() {
        return errorExists;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }

    @Nullable
    public Bitmap getBmpImg() {
        return bmpImg;
    }

    @Nullable
    public String getImgPath() {
        return imgPath;
    }
}
