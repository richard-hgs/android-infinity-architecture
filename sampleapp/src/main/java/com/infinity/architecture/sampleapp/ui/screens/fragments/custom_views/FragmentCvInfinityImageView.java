package com.infinity.architecture.sampleapp.ui.screens.fragments.custom_views;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.infinity.architecture.base.ui.BaseFragmentViewModel;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentOpt;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentCvInfinityImageView extends MyBaseFragmentOpt {
    @Override
    public Class<? extends BaseFragmentViewModel> screenVmClass() {
        return FragmentCvInfinityImageViewVm.class;
    }

    @Override
    public int screenLayout() {
        return R.layout.fragment_cv_infinity_image_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
