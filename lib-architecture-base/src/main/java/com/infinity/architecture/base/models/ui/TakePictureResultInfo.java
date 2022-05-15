package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakePictureResultInfo {
    private boolean errorExists;
    private int errorCode;
    private String errorMsg;

    private String imgPath;

    private TakePictureResultInfo() { }

    public static TakePictureResultInfo getSuccessInstance(
        @Nullable String imgPath
    ) {
        TakePictureResultInfo takePictureResultInfo = new TakePictureResultInfo();
        takePictureResultInfo.errorExists = false;
        takePictureResultInfo.imgPath = imgPath;
        return takePictureResultInfo;
    }

    public static TakePictureResultInfo getErrorInstance(
        int errorCode, @NonNull String errorMsg
    ) {
        TakePictureResultInfo takePictureResultInfo = new TakePictureResultInfo();
        takePictureResultInfo.errorExists = true;
        takePictureResultInfo.errorCode = errorCode;
        takePictureResultInfo.errorMsg = errorMsg;

        return takePictureResultInfo;
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
    public String getImgPath() {
        return imgPath;
    }
}
