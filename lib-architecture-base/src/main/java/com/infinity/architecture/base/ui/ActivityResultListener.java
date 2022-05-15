package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.ActivityResultInfo;

public interface ActivityResultListener {
    void onActivityResult(@NonNull ActivityResultInfo activityResultInfo);
}
