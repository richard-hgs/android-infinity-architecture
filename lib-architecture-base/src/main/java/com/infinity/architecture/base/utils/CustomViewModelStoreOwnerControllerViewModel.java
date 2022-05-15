package com.infinity.architecture.base.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class CustomViewModelStoreOwnerControllerViewModel extends ViewModel {

    private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            CustomViewModelStoreOwnerControllerViewModel viewModel = new CustomViewModelStoreOwnerControllerViewModel();
            return (T) viewModel;
        }
    };

    @NonNull
    public static CustomViewModelStoreOwnerControllerViewModel getInstance(ViewModelStore viewModelStore) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, FACTORY);
        return viewModelProvider.get(CustomViewModelStoreOwnerControllerViewModel.class);
    }

    private final HashMap<UUID, ViewModelStore> mViewModelStores = new HashMap<>();

    void clear(@NonNull UUID backStackEntryUUID) {
        // Clear and remove the NavGraph's ViewModelStore
        ViewModelStore viewModelStore = mViewModelStores.remove(backStackEntryUUID);
        if (viewModelStore != null) {
            viewModelStore.clear();
        }
    }

    @Override
    protected void onCleared() {
        for (ViewModelStore store: mViewModelStores.values()) {
            store.clear();
        }
        mViewModelStores.clear();
    }

    @NonNull
    ViewModelStore getViewModelStore(@NonNull UUID backStackEntryUUID) {
        ViewModelStore viewModelStore = mViewModelStores.get(backStackEntryUUID);
        if (viewModelStore == null) {
            viewModelStore = new ViewModelStore();
            mViewModelStores.put(backStackEntryUUID, viewModelStore);
        }
        return viewModelStore;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("NavControllerViewModel{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("} ViewModelStores (");
        Iterator<UUID> viewModelStoreIterator = mViewModelStores.keySet().iterator();
        while (viewModelStoreIterator.hasNext()) {
            sb.append(viewModelStoreIterator.next());
            if (viewModelStoreIterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
