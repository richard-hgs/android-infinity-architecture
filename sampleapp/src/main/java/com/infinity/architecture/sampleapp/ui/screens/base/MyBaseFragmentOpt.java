package com.infinity.architecture.sampleapp.ui.screens.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infinity.architecture.base.ui.BaseAppCompatFragmentOpt;
import com.infinity.architecture.base.ui.BaseFragmentViewModel;

import java.lang.reflect.Method;

/**
 * Class created to reduce FragmentBoilerPlate code
 */
public abstract class MyBaseFragmentOpt extends BaseAppCompatFragmentOpt {
    // BaseAppCompatFragment is the default parent for each fragment
    // instead of the Fragment...

    private BaseFragmentViewModel viewModel;

    /**
     * Get the ViewModel class to be instantiated
     * @return      The view model class
     */
    public abstract Class<? extends BaseFragmentViewModel> screenVmClass();

    /**
     * Get the layout to be inflated
     * @return  The layout
     */
    public abstract int screenLayout();

    // --------------- BEGIN ARCHITECTURE BASE IMPLEMENTATION ---------------
    /**
     * Method used by the BaseAppCompatFragment for instantiating and injecting new ViewModels
     * @return {@link ViewModel}
     */
    @NonNull
    @Override
    public <T extends ViewModel> T retrieveViewModelForClass(Class<T> vmClass) {
        return new ViewModelProvider(this).get(vmClass);
    }

    /**
     * You should instantiate here the ViewModel for this screen and return it to be configured
     * @return  ViewModel instance that extends {@link BaseFragmentViewModel} or {@link com.infinity.architecture.base.ui.BaseFragmentViewModelOpt}
     */
    @NonNull
    @Override
    public BaseFragmentViewModel retrieveBaseViewModel() {
        viewModel = retrieveViewModelForClass(screenVmClass());
        return viewModel;
    }

    /**
     * The lifecycle owner passed to Architecture library to be used to configure this fragment
     * @return  {@link LifecycleOwner}
     */
    @NonNull
    @Override
    public LifecycleOwner retrieveLifecycleOwner() {
        return this;
    }
    // --------------- END ARCHITECTURE BASE IMPLEMENTATION ---------------

    /**
     * When fragment view is inflated
     * @param inflater              Inflater
     * @param container             Container
     * @param savedInstanceState    SavedInstance
     * @return                      View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, screenLayout(), container, false);
        binding.setLifecycleOwner(retrieveLifecycleOwner());

        try {
            // Use reflection to set ViewModel to the dataBinding variable for current screen
            // NOTE: Since we are using reflection the proguard needs to keep this method to avoid errors by optimizing function name
            Method setVmMethod = binding.getClass().getDeclaredMethod("setViewModel", screenVmClass());
            setVmMethod.invoke(binding, viewModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new Exception("Could not call method setViewModel using reflection for dataView class, did you forget about proguard rules?", e));
        }

        return binding.getRoot();
    }
}
