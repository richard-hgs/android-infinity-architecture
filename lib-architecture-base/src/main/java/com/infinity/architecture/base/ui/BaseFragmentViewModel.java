package com.infinity.architecture.base.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
import com.infinity.architecture.base.models.ui.ReceivePictureDialogInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerInfo;
import com.infinity.architecture.base.models.ui.TakePictureInfo;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.base.ui.adapter.FragmentRequires;
import com.infinity.architecture.base.ui.listeners.DisplayInfoListener;
import com.infinity.architecture.base.ui.listeners.EditPictureListener;
import com.infinity.architecture.base.ui.listeners.PickPictureListener;
import com.infinity.architecture.base.ui.listeners.ReceivePictureDialogListener;
import com.infinity.architecture.base.ui.listeners.TakePictureListener;
import com.infinity.architecture.utils.permission.InterfacePermissao;
import com.infinity.architecture.utils.speech.SpeechRecognizerListener;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragmentViewModel extends ViewModel implements BaseActivityViewModelFunctions {
    private final String TAG = "BaseFragViewModel";

    private BaseActivityViewModel baseActivityViewModel;

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Composite disposable
     */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * --COMMUNICATE WITH BASE FRAGMENT--
     * Open, close or configure keyboard
     */
    private final MutableLiveData<KeyboardInfo> keyboardInfoState = new MutableLiveData<>(null);

    private FragmentRequires fragmentRequires = null;

    private AdapterRequires adapterRequires = new AdapterRequires() {
        @Override
        public ViewModel getViewModelInst(@Nullable String uniqueVmOwnerGroupGuid, Class<? extends ViewModel> vmClass) {
            return baseActivityViewModel.getAdapterRequires().getViewModelInst(uniqueVmOwnerGroupGuid, vmClass);
        }

        @Override
        public CompositeDisposable getCompositeDisposable() {
            return baseActivityViewModel.getAdapterRequires().getCompositeDisposable();
        }

        @Nullable
        @Override
        public String getUniqueVmOwnerGuid() {
            return fragmentRequires.getViewOwnerGroupGuid();
        }

        @Nullable
        @Override
        public FragmentManager getFragChildFragManager() {
            return fragmentRequires.getFragChildFragManager();
        }

        @Nullable
        @Override
        public FragmentManager getFragParentFragManager() {
            return fragmentRequires.getFragParentFragManager();
        }

        @Nullable
        @Override
        public FragmentManager getActFragManager() {
            return baseActivityViewModel.getAdapterRequires().getActFragManager();
        }

        @Override
        public Lifecycle getLifecycle() {
            return fragmentRequires.getLifecycle();
        }

        @Override
        public BaseActivityViewModel getBaseActivityViewModel() {
            return baseActivityViewModel;
        }
    };

    /**
     * When all required values are instantiated to allow inter communication between the fragment and the BaseAppcompatActivity
     */
    protected abstract void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs);

    /**
     * When menu item is pressed
     * @return  true=HANDLED false=OTHERWISE
     */
    protected abstract boolean onOptionsItemSelected(int menuItemId);

    /**
     * View Model cleared
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared - viewModel: " + getClass().getSimpleName());
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
        customDialogInfo.setUniqueVmOwnerGroupGuid(fragmentRequires.getViewOwnerGroupGuid());
        customDialogInfo.setFragArgs(fragmentRequires.getArguments());
        baseActivityViewModel.setCustomDialog(customDialogInfo);
    }

    @Override
    public void isLocationEnabled(@NonNull @NotNull LocationIsEnabledListener locationIsEnabledListener) {
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
        return adapterRequires;
    }

    @Override
    public void textToSpeech(SpeechRecognizerInfo speechRecognizerInfo, @Nullable SpeechRecognizerListener speechRecognizerListener) {
        baseActivityViewModel.textToSpeech(speechRecognizerInfo, speechRecognizerListener);
    }

    @Override
    public void setKeyboard(@NonNull KeyboardInfo keyboardInfo) {
        if (keyboardInfo.getDialogGuid() != null) {
            baseActivityViewModel.setKeyboard(keyboardInfo);
        } else {
            keyboardInfoState.setValue(keyboardInfo);
        }
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

    /**
     * Returns the disposable for the current view model
     */
    protected CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public LiveData<KeyboardInfo> getKeyboardInfoState() {
        return keyboardInfoState;
    }

    public void setFragmentRequires(FragmentRequires fragmentRequires) {
        this.fragmentRequires = fragmentRequires;
    }
}
