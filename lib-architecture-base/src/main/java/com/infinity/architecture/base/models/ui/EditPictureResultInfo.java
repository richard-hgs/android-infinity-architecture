package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditPictureResultInfo {
    private boolean errorExists;
    private int errorCode;
    private String errorMsg;

    private EditPictureResultInfo() { }

    public static EditPictureResultInfo getSuccessInstance() {
        EditPictureResultInfo editPictureResultInfo = new EditPictureResultInfo();
        editPictureResultInfo.errorExists = false;
        return editPictureResultInfo;
    }

    public static EditPictureResultInfo getErrorInstance(
        int errorCode, @NonNull String errorMsg
    ) {
        EditPictureResultInfo editPictureResultInfo = new EditPictureResultInfo();
        editPictureResultInfo.errorExists = true;
        editPictureResultInfo.errorCode = errorCode;
        editPictureResultInfo.errorMsg = errorMsg;

        return editPictureResultInfo;
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
}
