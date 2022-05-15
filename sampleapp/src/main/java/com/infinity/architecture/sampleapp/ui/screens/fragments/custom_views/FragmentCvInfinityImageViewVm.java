package com.infinity.architecture.sampleapp.ui.screens.fragments.custom_views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.backservices.AppApplication;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentViewModelOpt;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FragmentCvInfinityImageViewVm extends MyBaseFragmentViewModelOpt {

    private final AppApplication appApplication;

    @Inject
    public FragmentCvInfinityImageViewVm(@NonNull AppApplication appApplication) {
        this.appApplication = appApplication;
    }

    @Override
    protected void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs) {
        setActionBar(ActionBarInfo.getInstance(null, appApplication.getString(R.string.cv_title_infinity_image_view), true, true));
    }

    @Override
    protected boolean onOptionsItemSelected(int menuItemId) {
        if(menuItemId == android.R.id.home) {
            performBackPressed(BackPressedInfo.defaultBackPressed());
        }
        return super.onOptionsItemSelected(menuItemId);
    }
}
