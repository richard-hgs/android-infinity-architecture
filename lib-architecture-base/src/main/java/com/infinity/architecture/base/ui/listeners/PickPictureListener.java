package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.PickPictureResultInfo;

public interface PickPictureListener {
    void onPicturePicked(@NonNull PickPictureResultInfo pickPictureResultInfo);
}
