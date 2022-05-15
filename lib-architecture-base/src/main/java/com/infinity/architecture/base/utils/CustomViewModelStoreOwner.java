package com.infinity.architecture.base.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;

public class CustomViewModelStoreOwner implements
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner {

    private final CustomLifecycle customLifecycle = new CustomLifecycle();
    private final ViewModelStore viewModelStore;
    private ViewModelProvider.Factory mDefaultFactory;
    private final SavedStateRegistryController mSavedStateRegistryController = SavedStateRegistryController.create(this);
    private final ViewModelStoreOwnerProvider viewModelStoreOwnerProvider;

    public CustomViewModelStoreOwner(@NonNull ViewModelStoreOwnerProvider viewModelStoreOwnerProvider) {
        this.viewModelStore = new ViewModelStore();
        this.viewModelStoreOwnerProvider = viewModelStoreOwnerProvider;
    }

    public SavedStateRegistryController getSavedStateRegistryController() {
        return mSavedStateRegistryController;
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        if (mDefaultFactory == null) {
            if (viewModelStoreOwnerProvider.getAppContext() == null) {
                throw new IllegalStateException("Your activity is not yet attached to the "
                    + "Application instance. You can't request ViewModel before onCreate call.");
            }
            mDefaultFactory = new SavedStateViewModelFactory(
                viewModelStoreOwnerProvider.getAppContext(),
                this,
                viewModelStoreOwnerProvider.getIntent() != null ? viewModelStoreOwnerProvider.getIntent().getExtras() : null);
        }
        return mDefaultFactory;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @NonNull
    @Override
    public SavedStateRegistry getSavedStateRegistry() {
        return mSavedStateRegistryController.getSavedStateRegistry();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return customLifecycle.getLifecycle();
    }

    public LifecycleRegistry getLifecycleRegistry() {
        return customLifecycle.getLifecycleRegistry();
    }
}
