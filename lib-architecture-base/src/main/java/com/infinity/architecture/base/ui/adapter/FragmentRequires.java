package com.infinity.architecture.base.ui.adapter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

public interface FragmentRequires {
    String getViewOwnerGroupGuid();

    FragmentManager getFragChildFragManager();

    FragmentManager getFragParentFragManager();

    Lifecycle getLifecycle();

    @Nullable
    Bundle getArguments();
}
