package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.infinity.architecture.base.R;
import com.infinity.architecture.base.databinding.DialogReceivePictureBinding;
import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.ActivityResultInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.base.models.ui.CustomDialogInfo;
import com.infinity.architecture.base.models.ui.DisplayInfo;
import com.infinity.architecture.base.models.ui.EditPictureInfo;
import com.infinity.architecture.base.models.ui.FragmentResultInfo;
import com.infinity.architecture.base.models.ui.KeyboardInfo;
import com.infinity.architecture.base.models.ui.LoadingDialogInfo;
import com.infinity.architecture.base.models.ui.NavigationInfo;
import com.infinity.architecture.base.models.ui.OpenScreenInfo;
import com.infinity.architecture.base.models.ui.PickPictureInfo;
import com.infinity.architecture.base.models.ui.ReceivePictureDialogInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerInfo;
import com.infinity.architecture.base.models.ui.TakePictureInfo;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.base.ui.listeners.DisplayInfoListener;
import com.infinity.architecture.base.ui.listeners.EditPictureListener;
import com.infinity.architecture.base.ui.listeners.PickPictureListener;
import com.infinity.architecture.base.ui.listeners.ReceivePictureDialogListener;
import com.infinity.architecture.base.ui.listeners.TakePictureListener;
import com.infinity.architecture.utils.permission.InterfacePermissao;
import com.infinity.architecture.utils.speech.SpeechRecognizerListener;

public interface BaseActivityViewModelFunctions {
    /**
     * Check if permission is available or request the permission
     */
    void checkOrRequestPermission(String strPermissao, int intPermissao, @Nullable InterfacePermissao interfacePermissao);

    /**
     * Notify that an exception occurred in parent view models
     * @param throwable The exception
     */
    void notifyThrowExceptionState(@Nullable Throwable throwable);

    /**
     * Show toasty
     * @param toastyInfo  The toasty information
     */
    void showToasty(@NonNull ToastyInfo toastyInfo);

    /**
     * Open screen
     * @param openScreenInfo            The screen info to open
     * @param activityResultListener    Activity result listener
     */
    void openScreen(@NonNull OpenScreenInfo openScreenInfo, @Nullable ActivityResultListener activityResultListener);

    /**
     * Finish the screen
     */
    void finishScreen();

    /**
     * Navigate using the navigation controller provided to {@link BaseAppCompatActivity}
     * @param navigationInfo    The navigation information
     */
    void navigate(@NonNull NavigationInfo navigationInfo, @Nullable FragmentResultListener fragmentResultListener);

    /**
     * Call onBackPressed()
     * @param backPressedInfo   The information to be used before the onBackPressed call
     */
    void performBackPressed(@NonNull BackPressedInfo backPressedInfo);

    /**
     * Wen onBackPressed() is called perform the operation in configuration
     * @param backPressedInfo   The information to be used when the onBackPressed is called
     */
    void setOnBackPressedConfiguration(@NonNull BackPressedInfo backPressedInfo);

    /**
     * Set the loading dialog state
     * @param loadingDialogInfo the information of the loading dialog
     */
    void setLoadingDialog(@NonNull LoadingDialogInfo loadingDialogInfo);

    /**
     * Set the custom dialog state
     * @param customDialogInfo  Custom dialog information
     */
    void setCustomDialog(@NonNull CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo);

    /**
     * Checks if the location is enabled
     * @param locationIsEnabledListener listener
     */
    void isLocationEnabled(@NonNull LocationIsEnabledListener locationIsEnabledListener);

    /**
     * Return the activity result to previous activity
     * @param activityResultInfo    The information to return the result
     */
    void returnActivityResult(@NonNull ActivityResultInfo activityResultInfo);

    /**
     * Return the fragment result to previous fragment
     * @param fragmentResultInfo    The information to return the result
     */
    void returnFragmentResult(@NonNull FragmentResultInfo fragmentResultInfo);

    /**
     * Return the adapter intercommunication for getting the requirements
     * @return  {@link AdapterRequires}
     */
    AdapterRequires getAdapterRequires();

    void textToSpeech(SpeechRecognizerInfo speechRecognizerInfo, @Nullable SpeechRecognizerListener speechRecognizerListener);

    void setKeyboard(@NonNull KeyboardInfo keyboardInfo);

    void setActionBar(@NonNull ActionBarInfo actionBarInfo);

    void clearUniqueVmOwnersForGroup(@NonNull String groupGuid);

    void clearVmOwnerGroupItem(@NonNull String groupGuid, @NonNull String itemGuid);

    void takePicture(@NonNull TakePictureInfo takePictureInfo, @NonNull TakePictureListener takePictureListener);

    void editPicture(@NonNull EditPictureInfo editPictureInfo, @NonNull EditPictureListener editPictureListener);

    void pickPicture(@NonNull PickPictureInfo pickPictureInfo, @NonNull PickPictureListener pickPictureListener);

    void receivePictureDialog(@NonNull ReceivePictureDialogInfo receivePictureDialogInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener);

    void getDisplayInfo(@NonNull DisplayInfo displayInfo, @NonNull DisplayInfoListener displayInfoListener);
}
