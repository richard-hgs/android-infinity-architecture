package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.infinity.architecture.base.ui.BaseActivityViewModelFunctions;
import com.infinity.architecture.base.ui.BaseAppCompatActivity;

import javax.annotation.Nullable;

public class BackPressedInfo {

    @Nullable
    private OpenScreenInfo openScreenInfo;

    private Boolean backActionDisabled;
    private Boolean clearThisConfigOnFragmentDestroy = true;

    /**
     * Block constructor
     */
    private BackPressedInfo() {
    }

    /**
     * Configuration used with {@link BaseActivityViewModelFunctions#performBackPressed(BackPressedInfo)}<br/><br/>
     * Default back pressed operation, same as calling {@link AppCompatActivity#onBackPressed()}
     *
     * @return {@link BackPressedInfo} Default
     */
    @NonNull
    public static BackPressedInfo defaultBackPressed() {
        return new BackPressedInfo();
    }

    /**
     * Configuration used with {@link BaseActivityViewModelFunctions#setOnBackPressedConfiguration(BackPressedInfo)}<br/><br/>
     * Open a new screen when {@link BaseActivityViewModelFunctions#performBackPressed(BackPressedInfo)} is called
     *
     * @param openScreenInfo {@link OpenScreenInfo} Screen info to open
     * @return  {@link BackPressedInfo} Open Screen
     */
    @NonNull
    public static BackPressedInfo openScreenOnBackPressed(@NonNull OpenScreenInfo openScreenInfo) {
        BackPressedInfo backPressedInfo = new BackPressedInfo();
        backPressedInfo.openScreenInfo = openScreenInfo;
        return backPressedInfo;
    }

    /**
     * Configuration used with {@link BaseActivityViewModelFunctions#setOnBackPressedConfiguration(BackPressedInfo)}<br/><br/>
     * Disable the {@link AppCompatActivity#onBackPressed()} operation<br/>
     * Disable the {@link BaseActivityViewModelFunctions#performBackPressed(BackPressedInfo)} operation
     *
     * @param disabled  true=Disable backPress operation, false=Enabled backPress operation
     * @param clearThisConfigOnFragmentDestroy  true=Clear this configuration on navigation changed.
     * @return {@link BackPressedInfo} Configuration
     */
    @NonNull
    public static BackPressedInfo setBackActionDisabled(@NonNull Boolean disabled, @Nullable Boolean clearThisConfigOnFragmentDestroy) {
        BackPressedInfo backPressedInfo = new BackPressedInfo();
        backPressedInfo.backActionDisabled = disabled;
        backPressedInfo.clearThisConfigOnFragmentDestroy = clearThisConfigOnFragmentDestroy;
        return backPressedInfo;
    }

    /**
     * Screen information to be opened
     *
     * @return  {@link OpenScreenInfo} Screen info
     */
    @Nullable
    public OpenScreenInfo getOpenScreenInfo() {
        return openScreenInfo;
    }

    /**
     * Return if backPressed action is disabled using {@link #setBackActionDisabled(Boolean, Boolean)}
     *
     * @return  true=Disabled, false=Not Disabled
     */
    @Nullable
    public Boolean isBackActionDisabled() {
        return backActionDisabled;
    }

    /**
     * Return if this configuration should be cleared on {@link NavController} destination change
     *
     * @return  true=Clear, false=Persist configuration as long as the {@link BaseAppCompatActivity} is not destroyed
     */
    @Nullable
    public Boolean getClearThisConfigOnFragmentDestroy() {
        return clearThisConfigOnFragmentDestroy;
    }

    /**
     * @return This object as string
     */
    @NonNull
    @Override
    public String toString() {
        return "BackPressedInfo{" +
               "openScreenInfo=" + openScreenInfo +
               ", backActionDisabled=" + backActionDisabled +
               ", clearThisConfigOnFragmentDestroy=" + clearThisConfigOnFragmentDestroy +
               '}';
    }
}
