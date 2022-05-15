package com.infinity.architecture.sampleapp.ui.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.infinity.architecture.base.ui.BaseActivityViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

// Hilt - Required ViewModel
@HiltViewModel
public class ActivityMainVm extends BaseActivityViewModel {
    // Activities View Models should extends BaseActivityViewModel

    // Hilt - Required Dependency Injection
    @Inject
    public ActivityMainVm() {
    }

    /**
     * When all the architecture base screen components are configured this constructor should be
     * used instead the class constructor when the screen comes alive this is the main method
     * @param lifecycleOwner    The View owner
     * @param actArgs           Arguments received by the activity of this viewModel
     */
    @Override
    protected void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle actArgs) {

    }
}
