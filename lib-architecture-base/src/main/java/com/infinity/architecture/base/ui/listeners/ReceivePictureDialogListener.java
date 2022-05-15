package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.ReceivePictureDialogResultInfo;

public interface ReceivePictureDialogListener {
    void onPictureReceived(@NonNull ReceivePictureDialogResultInfo pickPictureResultInfo);
}
