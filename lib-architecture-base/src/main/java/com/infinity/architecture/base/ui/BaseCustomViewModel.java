package com.infinity.architecture.base.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

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
import com.infinity.architecture.base.models.ui.PopupMenuInfo;
import com.infinity.architecture.base.models.ui.ReceivePictureDialogInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerInfo;
import com.infinity.architecture.base.models.ui.SystemAppConfigInfo;
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

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseCustomViewModel extends ViewModel implements BaseActivityViewModelFunctions {

    private BaseActivityViewModel baseActivityViewModel;

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Composite disposable
     */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * When all required values are instantiated to allow inter communication between the CustomView and the BaseAppcompatActivity
     */
    protected abstract void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs);

    /**
     * View Model cleared
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void setBaseActivityViewModel(BaseActivityViewModel baseActivityViewModel) {
        this.baseActivityViewModel = baseActivityViewModel;
    }

    @Override
    public void checkOrRequestPermission(String strPermissao, int intPermissao, @Nullable InterfacePermissao interfacePermissao) {
        baseActivityViewModel.checkOrRequestPermission(strPermissao, intPermissao, interfacePermissao);
    }

    @Override
    public void notifyThrowExceptionState(@Nullable Throwable throwable) {
        baseActivityViewModel.notifyThrowExceptionState(throwable);
    }

    @Override
    public void showToasty(@NonNull ToastyInfo toastyInfo) {
        baseActivityViewModel.showToasty(toastyInfo);
    }

    @Override
    public void openScreen(@NonNull OpenScreenInfo openScreenInfo, @Nullable ActivityResultListener activityResultListener) {
        baseActivityViewModel.openScreen(openScreenInfo, activityResultListener);
    }

    @Override
    public void finishScreen() {
        baseActivityViewModel.finishScreen();
    }

    @Override
    public void navigate(@NonNull NavigationInfo navigationInfo, @Nullable FragmentResultListener fragmentResultListener) {
        baseActivityViewModel.navigate(navigationInfo, fragmentResultListener);
    }

    @Override
    public void performBackPressed(@NonNull BackPressedInfo backPressedInfo) {
        baseActivityViewModel.performBackPressed(backPressedInfo);
    }

    @Override
    public void setOnBackPressedConfiguration(@NonNull BackPressedInfo backPressedInfo) {
        baseActivityViewModel.setOnBackPressedConfiguration(backPressedInfo);
    }

    @Override
    public void setLoadingDialog(@NonNull LoadingDialogInfo loadingDialogInfo) {
        baseActivityViewModel.setLoadingDialog(loadingDialogInfo);
    }

    @Override
    public void setCustomDialog(@NonNull CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo) {
        baseActivityViewModel.setCustomDialog(customDialogInfo);
    }

    @Override
    public void isLocationEnabled(@NonNull LocationIsEnabledListener locationIsEnabledListener) {
        baseActivityViewModel.isLocationEnabled(locationIsEnabledListener);
    }

    @Override
    public void returnActivityResult(@NonNull ActivityResultInfo activityResultInfo) {
        baseActivityViewModel.returnActivityResult(activityResultInfo);
    }

    @Override
    public void returnFragmentResult(@NonNull FragmentResultInfo fragmentResultInfo) {
        baseActivityViewModel.returnFragmentResult(fragmentResultInfo);
    }

    @Override
    public AdapterRequires getAdapterRequires() {
        return baseActivityViewModel.getAdapterRequires();
    }

    @Override
    public void textToSpeech(SpeechRecognizerInfo speechRecognizerInfo, @Nullable SpeechRecognizerListener speechRecognizerListener) {
        baseActivityViewModel.textToSpeech(speechRecognizerInfo, speechRecognizerListener);
    }

    @Override
    public void setKeyboard(@NonNull KeyboardInfo keyboardInfo) {
        baseActivityViewModel.setKeyboard(keyboardInfo);
    }

    @Override
    public void setActionBar(@NonNull ActionBarInfo actionBarInfo) {
        baseActivityViewModel.setActionBar(actionBarInfo);
    }

    @Override
    public void clearUniqueVmOwnersForGroup(@NonNull String groupGuid) {
        if (baseActivityViewModel != null) {
            baseActivityViewModel.clearUniqueVmOwnersForGroup(groupGuid);
        }
    }

    @Override
    public void clearVmOwnerGroupItem(@NonNull String groupGuid, @NonNull String itemGuid) {
        if (baseActivityViewModel != null) {
            baseActivityViewModel.clearVmOwnerGroupItem(groupGuid, itemGuid);
        }
    }

    @Override
    public void takePicture(@NonNull TakePictureInfo takePictureInfo, @NonNull TakePictureListener takePictureListener) {
        baseActivityViewModel.takePicture(takePictureInfo, takePictureListener);
    }

    @Override
    public void editPicture(@NonNull EditPictureInfo editPictureInfo, @NonNull EditPictureListener editPictureListener) {
        baseActivityViewModel.editPicture(editPictureInfo, editPictureListener);
    }

    @Override
    public void pickPicture(@NonNull PickPictureInfo pickPictureInfo, @NonNull PickPictureListener pickPictureListener) {
        baseActivityViewModel.pickPicture(pickPictureInfo, pickPictureListener);
    }

    @Override
    public void receivePictureDialog(@NonNull ReceivePictureDialogInfo receivePictureDialogInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener) {
        baseActivityViewModel.receivePictureDialog(receivePictureDialogInfo, receivePictureDialogListener);
    }

    @Override
    public void getDisplayInfo(@NonNull DisplayInfo displayInfo, @NonNull DisplayInfoListener displayInfoListener) {
        baseActivityViewModel.getDisplayInfo(displayInfo, displayInfoListener);
    }

    @Override
    public void setPopupMenu(@NonNull PopupMenuInfo popupMenuInfo) {
        baseActivityViewModel.setPopupMenu(popupMenuInfo);
    }

    @Override
    public void openSystemAppConfig(@NonNull SystemAppConfigInfo systemAppConfigInfo) {
        baseActivityViewModel.openSystemAppConfig(systemAppConfigInfo);
    }

    /**
     * Returns the disposable for the current view model
     */
    protected CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
