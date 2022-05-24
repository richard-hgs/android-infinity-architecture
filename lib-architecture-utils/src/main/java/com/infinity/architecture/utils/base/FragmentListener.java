package com.infinity.architecture.utils.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public interface FragmentListener {
    void onCreateView(
        @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState,
        @NonNull ViewModel baseActivityViewModel, @NonNull ViewModel baseFragmentViewModel
    );
}
