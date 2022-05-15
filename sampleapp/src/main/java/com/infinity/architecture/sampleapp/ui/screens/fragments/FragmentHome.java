package com.infinity.architecture.sampleapp.ui.screens.fragments;

import com.infinity.architecture.base.ui.BaseFragmentViewModel;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentOpt;

import dagger.hilt.android.AndroidEntryPoint;

// Hilt - AndroidEntryPoint required by Activities/Fragments/Services/etc...
@AndroidEntryPoint
public class FragmentHome extends MyBaseFragmentOpt {

    @Override
    public Class<? extends BaseFragmentViewModel> screenVmClass() {
        return FragmentHomeVm.class;
    }

    @Override
    public int screenLayout() {
        return R.layout.fragment_home;
    }
}
