package com.infinity.architecture.sampleapp.ui.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.infinity.architecture.base.ui.BaseActivityViewModel;
import com.infinity.architecture.base.ui.BaseAppCompatActivityOpt;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

// Hilt - AndroidEntryPoint required by Activities/Fragments/Services/etc...
@AndroidEntryPoint
public class ActivityMain extends BaseAppCompatActivityOpt {
    // BaseAppCompatActivityOpt or BaseAppCompatActivity is the default parent for each activity
    // instead of the AppCompatActivity...

    private ActivityMainVm activityMainVm;

    // --------------- BEGIN ARCHITECTURE BASE IMPLEMENTATION ---------------
    /**
     * You should instantiate here the ViewModel for this screen and return it to be configured
     * @return  ViewModel instance that extends {@link BaseActivityViewModel}
     */
    @NonNull
    @Override
    public BaseActivityViewModel retrieveBaseViewModel() {
        activityMainVm = retrieveViewModelForClass(ActivityMainVm.class);
        return activityMainVm;
    }

    /**
     * The lifecycle owner passed to Architecture library to be used to configure this activity
     * @return  {@link LifecycleOwner}
     */
    @NonNull
    @Override
    public LifecycleOwner retrieveLifecycleOwner() {
        return this;
    }

    /**
     * This screen instance passed to Architecture library to be used to configure this screen
     * @return this
     */
    @NonNull
    @Override
    public Object retrieveInstance() {
        return this;
    }

    /**
     * Method used by the BaseAppCompatActivity for instantiating and injecting new ViewModels
     * @return {@link ViewModel}
     */
    @NonNull
    @Override
    public <T extends ViewModel> T retrieveViewModelForClass(Class<T> vmClass) {
        return new ViewModelProvider(this).get(vmClass);
    }

    /**
     * Method use by the BaseAppCompatActivity for navigating between fragments using the navigation host
     * @return {@link NavController}
     */
    @Nullable
    @Override
    public NavController retrieveNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            return navHostFragment.getNavController();
        }
        return super.retrieveNavController();
    }
    // --------------- END ARCHITECTURE BASE IMPLEMENTATION ---------------

    /**
     * When screen is created
     * @param savedInstanceState    {@link Bundle} Saved instance when activity gets recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set contentView
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Set this current screen ViewModel variable that was created in layout using databinding
        activityMainBinding.setViewModel(activityMainVm);
        // Set this current screen Lifecycle
        activityMainBinding.setLifecycleOwner(retrieveLifecycleOwner());

        // Thats is it the bare minimum configuration for activity is set
    }
}
