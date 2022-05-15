package com.infinity.architecture.sampleapp.ui.screens.fragments.custom_views;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.infinity.architecture.base.ui.BaseFragmentViewModel;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentOpt;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentCvInfinityImageButton extends MyBaseFragmentOpt {
    @Override
    public Class<? extends BaseFragmentViewModel> screenVmClass() {
        return FragmentCvInfinityImageButtonVm.class;
    }

    @Override
    public int screenLayout() {
        return R.layout.fragment_cv_infinity_image_button;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
