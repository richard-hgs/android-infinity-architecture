package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.LocationIsEnabledInfo;

public interface LocationIsEnabledListener {
    void onResult(@NonNull LocationIsEnabledInfo locationIsEnabledInfo);
}
