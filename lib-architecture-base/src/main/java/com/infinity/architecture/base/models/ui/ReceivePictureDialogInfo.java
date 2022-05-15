package com.infinity.architecture.base.models.ui;

import androidx.annotation.Nullable;

public class ReceivePictureDialogInfo {

    private TakePictureInfo takePictureInfo;
    private PickPictureInfo pickPictureInfo;
    private EditPictureInfo editPictureInfo;

    private ReceivePictureDialogInfo() {}

    public static ReceivePictureDialogInfo getInstance(
        @Nullable TakePictureInfo takePictureInfo,
        @Nullable PickPictureInfo pickPictureInfo,
        @Nullable EditPictureInfo editPictureInfo
    ) {
        if (takePictureInfo == null && pickPictureInfo == null) {
            throw new RuntimeException("takePictureInfo and pickPictureInfo are null one of them should not be null.");
        }
        ReceivePictureDialogInfo receivePictureDialogInfo = new ReceivePictureDialogInfo();
        receivePictureDialogInfo.takePictureInfo = takePictureInfo;
        receivePictureDialogInfo.pickPictureInfo = pickPictureInfo;
        receivePictureDialogInfo.editPictureInfo = editPictureInfo;

        return receivePictureDialogInfo;
    }

    @Nullable
    public TakePictureInfo getTakePictureInfo() {
        return takePictureInfo;
    }

    @Nullable
    public PickPictureInfo getPickPictureInfo() {
        return pickPictureInfo;
    }

    @Nullable
    public EditPictureInfo getEditPictureInfo() {
        return editPictureInfo;
    }
}
