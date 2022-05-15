package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReceivePictureDialogResultInfo {
    private boolean errorExists;
    private int errorCode;
    private String errorMsg;

    private String imgPath;

    private ReceivePictureDialogResultInfo() {}

    public static ReceivePictureDialogResultInfo getSuccessInstance(
        @Nullable String imgPath
    ) {
        ReceivePictureDialogResultInfo takePictureResultInfo = new ReceivePictureDialogResultInfo();
        takePictureResultInfo.errorExists = false;
        takePictureResultInfo.imgPath = imgPath;
        return takePictureResultInfo;
    }

    public static ReceivePictureDialogResultInfo getErrorInstance(
        int errorCode, @NonNull String errorMsg
    ) {
        ReceivePictureDialogResultInfo takePictureResultInfo = new ReceivePictureDialogResultInfo();
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

    @Override
    public String toString() {
        return "ReceivePictureDialogResultInfo{" +
                "errorExists=" + errorExists +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
