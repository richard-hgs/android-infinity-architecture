package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.models.ui.FragmentResultInfo;

public interface FragmentResultListener {
    void onFragmentResult(@NonNull FragmentResultInfo fragmentResultInfo);
}
