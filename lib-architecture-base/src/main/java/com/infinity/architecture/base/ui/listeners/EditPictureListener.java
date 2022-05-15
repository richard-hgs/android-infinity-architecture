package com.infinity.architecture.base.ui.listeners;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.EditPictureResultInfo;

public interface EditPictureListener {
    void onPictureEdited(@NonNull EditPictureResultInfo editPictureResultInfo);
}
