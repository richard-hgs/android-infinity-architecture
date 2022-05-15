package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

public abstract class CustomDialogListenerBase<VM extends BaseDialogViewModel> implements CustomDialogListener<VM> {
    @Override
    public void onCreated(@NonNull LifecycleOwner lifecycleOwner, @Nullable VM viewModel) {

    }

    @Override
    public void onDismiss() {

    }

    @Override
    public boolean onNegativeButtonClick(Object[] params) {
        return false;
    }

    @Override
    public boolean onPositiveButtonClick(Object[] params) {
        return false;
    }
}
