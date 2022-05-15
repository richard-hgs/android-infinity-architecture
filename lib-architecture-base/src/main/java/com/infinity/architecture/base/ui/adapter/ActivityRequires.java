package com.infinity.architecture.base.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public interface ActivityRequires {
    ViewModel getViewModelInst(@Nullable String uniqueVmOwnerGroupGuid, Class<? extends ViewModel> vmClass);

    CompositeDisposable getCompositeDisposable();

    void clearUniqueVmOwnersForGroup(@NonNull String groupGuid);

    void clearUniqueVmOwnerItem(@NonNull String groupGuid, @NonNull String itemGuid);

    FragmentManager getActFragManager();

    Lifecycle getLifecycle();
}
