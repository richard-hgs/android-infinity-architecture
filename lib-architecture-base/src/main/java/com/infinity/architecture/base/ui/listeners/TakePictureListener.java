package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.TakePictureResultInfo;

public interface TakePictureListener {
    void onPictureReceived(@NonNull TakePictureResultInfo pictureResult);
}
