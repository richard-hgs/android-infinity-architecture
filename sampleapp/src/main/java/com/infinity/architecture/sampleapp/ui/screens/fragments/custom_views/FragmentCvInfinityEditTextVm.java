package com.infinity.architecture.sampleapp.ui.screens.fragments.custom_views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.infinity.architecture.base.enums.ui.ToastyLength;
import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.views.bindadapters.CustomViewBindingAdapter;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.backservices.AppApplication;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentViewModelOpt;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FragmentCvInfinityEditTextVm extends MyBaseFragmentViewModelOpt {

    private final AppApplication appApplication;

    @Inject
    public FragmentCvInfinityEditTextVm(@NonNull AppApplication appApplication) {
        this.appApplication = appApplication;
    }

    @Override
    protected void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs) {
        setActionBar(ActionBarInfo.getInstance(null, appApplication.getString(R.string.cv_title_infinity_edit_text), true, true));
    }

    @Override
    protected boolean onOptionsItemSelected(int menuItemId) {
        if (menuItemId == android.R.id.home) {
            performBackPressed(BackPressedInfo.defaultBackPressed());
            return true;
        }
        return super.onOptionsItemSelected(menuItemId);
    }

    /**
     * View Binded
     */
    public void onDrawableLeftClick(@NonNull CustomViewBindingAdapter.BindingReturnListener<Boolean> handledCallback) {
        showToasty(ToastyInfo.defaul(ToastyLength.SHORT, appApplication.getString(R.string.dl_clicked)));
        // Notify that drawable click was handled preventing focusing EditText
        handledCallback.onReturn(true);
    }

    /**
     * View Binded
     */
    public void onDrawableTopClick(@NonNull CustomViewBindingAdapter.BindingReturnListener<Boolean> handledCallback) {
        showToasty(ToastyInfo.defaul(ToastyLength.SHORT, appApplication.getString(R.string.dt_clicked)));
        // Notify that drawable click was handled preventing focusing EditText
        handledCallback.onReturn(true);
    }

    /**
     * View Binded
     */
    public void onDrawableRightClick(@NonNull CustomViewBindingAdapter.BindingReturnListener<Boolean> handledCallback) {
        showToasty(ToastyInfo.defaul(ToastyLength.SHORT, appApplication.getString(R.string.dr_clicked)));
        // Notify that drawable click was handled preventing focusing EditText
        handledCallback.onReturn(true);
    }

    /**
     * View Binded
     */
    public void onDrawableBottomClick(@NonNull CustomViewBindingAdapter.BindingReturnListener<Boolean> handledCallback) {
        showToasty(ToastyInfo.defaul(ToastyLength.SHORT, appApplication.getString(R.string.db_clicked)));
        // Notify that drawable click was handled preventing focusing EditText
        handledCallback.onReturn(true);
    }
}
