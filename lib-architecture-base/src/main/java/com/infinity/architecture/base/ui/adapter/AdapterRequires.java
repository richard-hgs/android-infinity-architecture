package com.infinity.architecture.base.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModel;

import com.infinity.architecture.base.ui.BaseActivityViewModel;

import io.reactivex.disposables.CompositeDisposable;

public interface AdapterRequires {
    ViewModel getViewModelInst(@Nullable String uniqueVmOwnerGroupGuid, Class<? extends ViewModel> vmClass);

    CompositeDisposable getCompositeDisposable();

    @Nullable
    String getUniqueVmOwnerGuid();

    @Nullable
    FragmentManager getFragChildFragManager();

    @Nullable
    FragmentManager getFragParentFragManager();

    @NonNull
    FragmentManager getActFragManager();

    Lifecycle getLifecycle();

    BaseActivityViewModel getBaseActivityViewModel();
}
