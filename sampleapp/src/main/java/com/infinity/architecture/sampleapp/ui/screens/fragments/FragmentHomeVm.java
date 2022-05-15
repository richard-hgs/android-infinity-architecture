package com.infinity.architecture.sampleapp.ui.screens.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.NavigationInfo;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.backservices.AppApplication;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentViewModelOpt;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

// Hilt - ViewModel injection
@HiltViewModel
public class FragmentHomeVm extends MyBaseFragmentViewModelOpt {

    private AppApplication appApplication;

    // Hilt - Inject constructor
    @Inject
    public FragmentHomeVm(@NonNull AppApplication appApplication) {
        this.appApplication = appApplication;
    }

    /**
     * When the view model super functions are ready to be called, works like the onCreate
     * @param lifecycleOwner    The lifecycleOwner of this model
     * @param fragArgs          null or Fragment arguments Bundle
     * @param actArgs           null or Activity arguments Bundle
     */
    @Override
    protected void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs) {
        setActionBar(ActionBarInfo.getInstance(appApplication.getString(R.string.app_name), "", false, true));
    }

    /**
     * View Binded
     */
    public void onBtnClick(int btnId) {
        if (btnId == R.id.btn_custom_views) {
            navigate(NavigationInfo.defNavigation(R.id.action_fragment_home_to_fragment_custom_views, false, null), null);
        }
    }
}
