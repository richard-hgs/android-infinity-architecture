package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

public interface CustomDialogListener<VM extends BaseDialogViewModel> {
    void onCreated(@NonNull LifecycleOwner lifecycleOwner, @Nullable VM viewModel);

    void onDismiss();

    boolean onNegativeButtonClick(Object[] params);

    boolean onPositiveButtonClick(Object[] params);
}
