package com.infinity.architecture.sampleapp.ui.screens.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.infinity.architecture.base.ui.BaseFragmentViewModel;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentOpt;

import dagger.hilt.android.AndroidEntryPoint;

// Hilt - AndroidEntryPoint required by Activities/Fragments/Services/etc...
@AndroidEntryPoint
public class FragmentInfinityViews extends MyBaseFragmentOpt {
    // MyBaseFragment is the default parent for each fragment
    // instead of the BaseAppCompatFragment...

    @Override
    public Class<? extends BaseFragmentViewModel> screenVmClass() {
        return FragmentInfinityViewsVm.class;
    }

    @Override
    public int screenLayout() {
        return R.layout.fragment_infinity_views;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Needed to receive onOptionsItemSelected from actionBar inside fragment
        setHasOptionsMenu(true);
    }
}
