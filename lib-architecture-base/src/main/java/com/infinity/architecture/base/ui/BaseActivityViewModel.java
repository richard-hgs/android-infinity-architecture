package com.infinity.architecture.base.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.infinity.architecture.base.BaseApplication;
import com.infinity.architecture.base.R;
import com.infinity.architecture.base.databinding.DialogReceivePictureBinding;
import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.ActivityResultInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.base.models.ui.CustomDialogInfo;
import com.infinity.architecture.base.models.ui.DisplayInfo;
import com.infinity.architecture.base.models.ui.DisplayResultInfo;
import com.infinity.architecture.base.models.ui.EditPictureResultInfo;
import com.infinity.architecture.base.models.ui.FragmentResultInfo;
import com.infinity.architecture.base.models.ui.KeyboardInfo;
import com.infinity.architecture.base.models.ui.LoadingDialogInfo;
import com.infinity.architecture.base.models.ui.LocationIsEnabledInfo;
import com.infinity.architecture.base.models.ui.NavigationInfo;
import com.infinity.architecture.base.models.ui.OpenScreenInfo;
import com.infinity.architecture.base.models.ui.PermissionInfo;
import com.infinity.architecture.base.models.ui.EditPictureInfo;
import com.infinity.architecture.base.models.ui.PickPictureInfo;
import com.infinity.architecture.base.models.ui.PickPictureResultInfo;
import com.infinity.architecture.base.models.ui.PopupMenuInfo;
import com.infinity.architecture.base.models.ui.ReceivePictureDialogInfo;
import com.infinity.architecture.base.models.ui.ReceivePictureDialogResultInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerResultInfo;
import com.infinity.architecture.base.models.ui.SystemAppConfigInfo;
import com.infinity.architecture.base.models.ui.TakePictureInfo;
import com.infinity.architecture.base.models.ui.TakePictureResultInfo;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.ui.adapter.ActivityRequires;
import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.base.ui.dialog.SimpleDialogReceivePictureViewModel;
import com.infinity.architecture.base.ui.listeners.DisplayInfoListener;
import com.infinity.architecture.base.ui.listeners.EditPictureListener;
import com.infinity.architecture.base.ui.listeners.PickPictureListener;
import com.infinity.architecture.base.ui.listeners.ReceivePictureDialogListener;
import com.infinity.architecture.base.ui.listeners.TakePictureListener;
import com.infinity.architecture.utils.permission.AbstractPermissao;
import com.infinity.architecture.utils.permission.InterfacePermissao;
import com.infinity.architecture.utils.permission.VerificaPermissoes;
import com.infinity.architecture.utils.reflection.TypeWrapper;
import com.infinity.architecture.utils.speech.SpeechRecognizerListener;
import com.infinity.architecture.utils.string.StringUtils;

import java.io.File;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivityViewModel extends ViewModel implements BaseActivityViewModelFunctions {
    private final String TAG = "BaseActViewModel";

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Composite disposable
     */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Exception state to be notified in base AppcompatActivity
     */
    private final MutableLiveData<Throwable> throwExceptionState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Loading dialog state
     */
    private final MutableLiveData<LoadingDialogInfo> loadingDialogState = new MutableLiveData<>();

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Custom dialog state
     */
    private final MutableLiveData<CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel>> customDialogState = new MutableLiveData<>();

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Loading dialog state
     */
    private final MutableLiveData<ToastyInfo> toastyState = new MutableLiveData<>();

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * true=Request the permission
     */
    private final MutableLiveData<PermissionInfo> requestPermissionState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Open a new screen with the information provided
     */
    private final MutableLiveData<OpenScreenInfo> openActivityState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Finish current screen with the information provided
     */
    private final MutableLiveData<Boolean> finishScreenState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Open a new screen with the information provided
     */
    private final MutableLiveData<NavigationInfo> navigationState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Perform back pressed with the information provided
     */
    private final MutableLiveData<BackPressedInfo> backPressedState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * When the back pressed is performed do the task for it
     */
    private final MutableLiveData<BackPressedInfo> onBackPressedState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * When the activity result is returning some data
     */
    private final MutableLiveData<ActivityResultInfo> onReturnActivityResultState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * When the fragment result is returning some data
     */
    private final MutableLiveData<FragmentResultInfo> onReturnFragmentResultState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Checks wathever the location provider is enabled or not
     */
    private final MutableLiveData<Boolean> locationIsEnabledState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Request text to speech using dialogs
     */
    private final MutableLiveData<SpeechRecognizerInfo> speechRecognizerState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Open, close or configure keyboard
     */
    private final MutableLiveData<KeyboardInfo> keyboardInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Configure action bar
     */
    private final MutableLiveData<ActionBarInfo> actionBarInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Open the camera to take a picture
     */
    private final MutableLiveData<TakePictureInfo> takePictureInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Start activity for result to edit a picture
     */
    private final MutableLiveData<EditPictureInfo> editPictureInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Start activity for result to pick a picture
     */
    private final MutableLiveData<PickPictureInfo> pickPictureInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Get display info
     */
    private final MutableLiveData<DisplayInfo> getDisplayInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Inflate a popup menu
     */
    private final MutableLiveData<PopupMenuInfo> popupMenuInfoState = new MutableLiveData<>(null);

    /**
     * --COMMUNICATE WITH BASE ACTIVITY--
     * Open android operating system configuration screen for application package name
     */
    private final MutableLiveData<SystemAppConfigInfo> openSystemAppConfigState = new MutableLiveData<>(null);


    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Check if location is enabled
     */
    private final ObservableField<LocationIsEnabledInfo> locationIsEnabledResultState = new ObservableField<>(null);

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Check if some permission is enabled and request when it is needed
     */
    private final ObservableField<Boolean> permissionAcceptedState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Called when the result for activity result is handled
     */
    private final ObservableField<ActivityResultInfo> openActivityResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     * Called when the result for fragment result is handled
     */
    private final ObservableField<FragmentResultInfo> openFragmentResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     *
     */
    private final ObservableField<SpeechRecognizerResultInfo> speechRecognizerResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     *
     */
    private final ObservableField<TakePictureResultInfo> takePictureResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     *
     */
    private final ObservableField<EditPictureResultInfo> editPictureResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     *
     */
    private final ObservableField<PickPictureResultInfo> pickPictureResultState = new ObservableField<>();

    /**
     * --INTERNAL VIEWMODEL COMMUNICATION--
     *
     */
    private final ObservableField<DisplayResultInfo> getDisplayResultState = new ObservableField<>();

    /**
     * The permission request is single event at once
     */
    private boolean isRequestingPermission = false;

    /**
     * The location is enabled is single event at once
     */
    private boolean isRequestingLocationIsEnabled = false;

    /**
     * The openScreenForResult is single event at once
     */
    private boolean isOpeningScreenForResult = false;

    private ActivityRequires activityAdapterRequires = null;

    /**
     * Interface for the adapters requirements
     */
    private final AdapterRequires adapterRequires = new AdapterRequires() {
        @Override
        public ViewModel getViewModelInst(@Nullable String uniqueVmOwnerGroupGuid, Class<? extends ViewModel> vmClass) {
            return activityAdapterRequires.getViewModelInst(uniqueVmOwnerGroupGuid, vmClass);
        }

        @Override
        public CompositeDisposable getCompositeDisposable() {
            return compositeDisposable;
        }

        @Nullable
        @Override
        public String getUniqueVmOwnerGuid() {
            return null;
        }

        @Nullable
        @Override
        public FragmentManager getFragChildFragManager() {
            return null;
        }

        @Nullable
        @Override
        public FragmentManager getFragParentFragManager() {
            return null;
        }

        @Nullable
        @Override
        public FragmentManager getActFragManager() {
            return activityAdapterRequires.getActFragManager();
        }

        @Override
        public Lifecycle getLifecycle() {
            return activityAdapterRequires.getLifecycle();
        }

        @Override
        public BaseActivityViewModel getBaseActivityViewModel() {
            return BaseActivityViewModel.this;
        }
    };

    /**
     * When all required values are instantiated in BaseAppcompatActivity
     */
    protected abstract void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle actArgs);

    /**
     * View Model cleared
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    /**
     * Check if permission is available or request the permission
     */
    @Override
    public void checkOrRequestPermission(String strPermissao, int intPermissao, @Nullable InterfacePermissao interfacePermissao) {
        Log.d(TAG, "checkOrRequestPermission: " + isRequestingPermission + " - strPermissao:" + strPermissao);
        if (!isRequestingPermission) {
            isRequestingPermission = true;
            permissionAcceptedState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    isRequestingPermission = false;
                    Boolean permissionAccepted = permissionAcceptedState.get();
                    if (permissionAccepted != null) {
                        try {
                            sender.removeOnPropertyChangedCallback(this);
                            requestPermissionState.setValue(null);
                            permissionAcceptedState.set(null);

                            if (interfacePermissao != null) {
                                interfacePermissao.statusPermissao(permissionAccepted);
                            }

                            // Log.d(TAG, "senderValue: " + permissionAcceptedState.get());
                        } catch (Exception e) {
                            throwExceptionState.setValue(e);
                            sender.removeOnPropertyChangedCallback(this);
                            requestPermissionState.setValue(null);
                            permissionAcceptedState.set(null);
                        }
                    }
                }
            });

            requestPermissionState.setValue(new PermissionInfo(strPermissao, intPermissao));
        }
    }

    /**
     * When requested permission result
     * @param status    true=Permission Allowed, false=Permission denied
     */
    void onRequestedPermissionResult(boolean status) {
        permissionAcceptedState.set(status);
    }

    /**
     * When requested location enabled result
     * @param gpsEnabled        true=Enabled, false=Disabled
     * @param networkEnabled    true=Enabled, false=Disabled
     */
    void onLocationIsEnabledResult(boolean gpsEnabled, boolean networkEnabled) {
        locationIsEnabledResultState.set(LocationIsEnabledInfo.getInstance(gpsEnabled, networkEnabled));
    }

    /**
     * When open activity for result, the result is handled
     * @param activityResultInfo    Result information
     */
    void onOpenActivityResult(@NonNull ActivityResultInfo activityResultInfo) {
        openActivityResultState.set(activityResultInfo);
    }

    /**
     * When request text to speech is handled
     * @param speechRecognizerResultInfo    Result information
     */
    void setTextToSpeechResult(SpeechRecognizerResultInfo speechRecognizerResultInfo) {
        speechRecognizerResultState.set(speechRecognizerResultInfo);
    }

    /**
     * When open fragment for result, the result is handled
     * @param fragmentResultInfo    Result information
     */
    void onOpenFragmentResult(@NonNull FragmentResultInfo fragmentResultInfo) {
        openFragmentResultState.set(fragmentResultInfo);
    }

    /**
     * When take picture result is handled
     * @param takePictureResultInfo    Result information
     */
    void onTakePictureResult(@NonNull TakePictureResultInfo takePictureResultInfo) {
        takePictureResultState.set(takePictureResultInfo);
    }

    /**
     * When edit picture result is handled
     * @param editPictureResultInfo    Result information
     */
    void onEditPictureResult(@NonNull EditPictureResultInfo editPictureResultInfo) {
        editPictureResultState.set(editPictureResultInfo);
    }

    /**
     * When pick picture result is handled
     * @param pickPictureResultInfo    Result information
     */
    void onPickPictureResult(@NonNull PickPictureResultInfo pickPictureResultInfo) {
        pickPictureResultState.set(pickPictureResultInfo);
    }

    /**
     * When display info result is handled
     * @param displayResultInfo
     */
    void onDisplayInfoResult(@NonNull DisplayResultInfo displayResultInfo) {
        getDisplayResultState.set(displayResultInfo);
    }

    /**
     * Notify that an exception occurred in parent view models
     * @param throwable The exception
     */
    @Override
    public void notifyThrowExceptionState(@Nullable Throwable throwable) {
        throwExceptionState.setValue(throwable);
        throwExceptionState.setValue(null);
    }

    /**
     * Show toasty
     * @param toastyInfo  The toasty information
     */
    @Override
    public void showToasty(@NonNull ToastyInfo toastyInfo) {
        toastyState.setValue(toastyInfo);
        toastyState.setValue(null);
    }

    /**
     * Open screen
     * @param openScreenInfo            The screen info to open
     * @param activityResultListener    Activity result listener
     */
    @Override
    public void openScreen(@NonNull OpenScreenInfo openScreenInfo, @Nullable ActivityResultListener activityResultListener) {
        if (!isOpeningScreenForResult) {
            if (activityResultListener != null) {
                isOpeningScreenForResult = true;
                openActivityResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        isOpeningScreenForResult = false;
                        ActivityResultInfo activityResultInfo = openActivityResultState.get();
                        if (activityResultInfo != null) {
                            try {
                                sender.removeOnPropertyChangedCallback(this);
                                openActivityState.setValue(null);
                                openActivityResultState.set(null);

                                activityResultListener.onActivityResult(activityResultInfo);
                            } catch (Exception e) {
                                throwExceptionState.setValue(e);

                                sender.removeOnPropertyChangedCallback(this);
                                openActivityState.setValue(null);
                                openActivityResultState.set(null);
                            }
                        }
                    }
                });
            }
            openActivityState.setValue(openScreenInfo);
            openActivityState.setValue(null);
        }
    }

    /**
     * Finish screen
     */
    public void finishScreen() {
        finishScreenState.setValue(true);
        finishScreenState.setValue(null);
    }

    /**
     * Navigate using the navigation controller provided to {@link BaseAppCompatActivity}
     * @param navigationInfo    The navigation information
     */
    @Override
    public void navigate(@NonNull NavigationInfo navigationInfo, @Nullable FragmentResultListener fragmentResultListener) {
        if (fragmentResultListener != null) {
            openFragmentResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    FragmentResultInfo fragmentResultInfo = openFragmentResultState.get();
                    if (fragmentResultInfo != null) {
                        try {
                            sender.removeOnPropertyChangedCallback(this);
                            navigationState.setValue(null);
                            openFragmentResultState.set(null);

                            fragmentResultListener.onFragmentResult(fragmentResultInfo);
                        } catch (Exception e) {
                            throwExceptionState.setValue(e);

                            sender.removeOnPropertyChangedCallback(this);
                            navigationState.setValue(null);
                            openFragmentResultState.set(null);
                        }
                    }
                }
            });
        }
        navigationState.setValue(navigationInfo);
        navigationState.setValue(null);
    }

    /**
     * Call onBackPressed()
     * @param backPressedInfo   The information to be used before the onBackPressed call
     */
    @Override
    public void performBackPressed(@NonNull BackPressedInfo backPressedInfo) {
        backPressedState.setValue(backPressedInfo);
        backPressedState.setValue(null);
    }

    /**
     * Wen onBackPressed() is called perform the operation in configuration
     * @param backPressedInfo   The information to be used when the onBackPressed is called
     */
    @Override
    public void setOnBackPressedConfiguration(@NonNull BackPressedInfo backPressedInfo) {
        onBackPressedState.setValue(backPressedInfo);
    }

    /**
     * Set the loading dialog state
     * @param loadingDialogInfo the information of the loading dialog
     */
    @Override
    public void setLoadingDialog(@NonNull LoadingDialogInfo loadingDialogInfo) {
        loadingDialogState.setValue(loadingDialogInfo);
        loadingDialogState.setValue(null);
    }

    /**
     * Set the custom dialog state
     * @param customDialogInfo  Custom dialog information
     */
    @Override
    public void setCustomDialog(@NonNull CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo) {
        customDialogState.setValue(customDialogInfo);
        customDialogState.observe(activityAdapterRequires.getLifecycleOwner(), new Observer<CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel>>() {
            @Override
            public void onChanged(CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo) {
                customDialogState.removeObserver(this);
                customDialogState.setValue(null);
            }
        });
    }

    /**
     * Checks if the location is enabled
     * @param locationIsEnabledListener listener
     */
    @Override
    public void isLocationEnabled(@NonNull LocationIsEnabledListener locationIsEnabledListener) {
        if (!isRequestingLocationIsEnabled) {
            isRequestingLocationIsEnabled = true;
            locationIsEnabledResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    isRequestingLocationIsEnabled = false;
                    LocationIsEnabledInfo locationIsEnabledInfo = locationIsEnabledResultState.get();
                    if (locationIsEnabledInfo != null) {
                        try {
                            sender.removeOnPropertyChangedCallback(this);
                            locationIsEnabledState.setValue(null);
                            locationIsEnabledResultState.set(null);

                            locationIsEnabledListener.onResult(locationIsEnabledInfo);
                        } catch (Exception e) {
                            throwExceptionState.setValue(e);

                            sender.removeOnPropertyChangedCallback(this);
                            locationIsEnabledState.setValue(null);
                            locationIsEnabledResultState.set(null);
                        }
                    }
                }
            });

            locationIsEnabledState.setValue(true);
        }
    }

    /**
     * Return the activity result to previous activity
     * @param activityResultInfo    The information to return the result
     */
    @Override
    public void returnActivityResult(@NonNull ActivityResultInfo activityResultInfo) {
        onReturnActivityResultState.setValue(activityResultInfo);
        onReturnActivityResultState.setValue(null);
    }

    /**
     * Return the fragment result to previous fragment
     * @param fragmentResultInfo    The information to return the result
     */
    @Override
    public void returnFragmentResult(@NonNull FragmentResultInfo fragmentResultInfo) {
        onReturnFragmentResultState.setValue(fragmentResultInfo);
        onReturnFragmentResultState.setValue(null);
    }

    /**
     * Return the adapter intercommunication for getting the requirements
     * @return  {@link AdapterRequires}
     */
    @Override
    public AdapterRequires getAdapterRequires() {
        return this.adapterRequires;
    }

    @Override
    public void textToSpeech(SpeechRecognizerInfo speechRecognizerInfo, @Nullable SpeechRecognizerListener speechRecognizerListener) {
        speechRecognizerResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SpeechRecognizerResultInfo speechRecognizerResultInfo = speechRecognizerResultState.get();
                if (speechRecognizerResultInfo != null) {
                    speechRecognizerResultState.removeOnPropertyChangedCallback(this);
                    speechRecognizerState.setValue(null);

                    if (speechRecognizerListener != null) {
                        if (speechRecognizerResultInfo.isErrorExists()) {
                            speechRecognizerListener.onError(speechRecognizerResultInfo.getErrorCode(), speechRecognizerResultInfo.getErrorMsg());
                        } else {
                            speechRecognizerListener.onResults(speechRecognizerResultInfo.getResults(), speechRecognizerResultInfo.getFirstTextResult());
                        }
                        // speechRecognizerListener.onResults();
                    }
                }
            }
        });

        speechRecognizerState.setValue(speechRecognizerInfo);
        speechRecognizerState.setValue(null);
    }

    @Override
    public void setKeyboard(@NonNull KeyboardInfo keyboardInfo) {
        keyboardInfoState.setValue(keyboardInfo);
        keyboardInfoState.setValue(null);
    }

    @Override
    public void setActionBar(@NonNull ActionBarInfo actionBarInfo) {
        actionBarInfoState.setValue(actionBarInfo);
        // actionBarInfoState.setValue(null);
    }

    @Override
    public void clearUniqueVmOwnersForGroup(@NonNull String groupGuid) {
        this.activityAdapterRequires.clearUniqueVmOwnersForGroup(groupGuid);
    }

    @Override
    public void clearVmOwnerGroupItem(@NonNull String groupGuid, @NonNull String itemGuid) {
        this.activityAdapterRequires.clearUniqueVmOwnerItem(groupGuid, itemGuid);
    }

    @Override
    public void takePicture(@NonNull TakePictureInfo takePictureInfo, @NonNull TakePictureListener takePictureListener) {
        takePictureResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                TakePictureResultInfo takePictureResultInfo = takePictureResultState.get();
                if (takePictureResultInfo != null) {
                    takePictureResultState.removeOnPropertyChangedCallback(this);
                    takePictureInfoState.setValue(null);
                    takePictureListener.onPictureReceived(takePictureResultInfo);
                }
            }
        });
        takePictureInfoState.setValue(takePictureInfo);
        takePictureInfoState.setValue(null);
    }

    @Override
    public void editPicture(@NonNull EditPictureInfo editPictureInfo, @NonNull EditPictureListener editPictureListener) {
        editPictureResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                EditPictureResultInfo editPictureResultInfo = editPictureResultState.get();
                if (editPictureResultInfo != null) {
                    editPictureResultState.removeOnPropertyChangedCallback(this);
                    editPictureInfoState.setValue(null);
                    editPictureListener.onPictureEdited(editPictureResultInfo);
                }
            }
        });
        editPictureInfoState.setValue(editPictureInfo);
        editPictureInfoState.setValue(null);
    }

    @Override
    public void pickPicture(@NonNull PickPictureInfo pickPictureInfo, @NonNull PickPictureListener pickPictureListener) {
        pickPictureResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                PickPictureResultInfo pickPictureResultInfo = pickPictureResultState.get();
                if (pickPictureResultInfo != null) {
                    pickPictureResultState.removeOnPropertyChangedCallback(this);
                    pickPictureInfoState.setValue(null);
                    pickPictureListener.onPicturePicked(pickPictureResultInfo);
                }
            }
        });
        pickPictureInfoState.setValue(pickPictureInfo);
        pickPictureInfoState.setValue(null);
    }

    @Override
    public void receivePictureDialog(@NonNull ReceivePictureDialogInfo receivePictureDialogInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener) {
        Log.d(TAG, "receivePictureDialog");

        setCustomDialog(CustomDialogInfo.dialogCustom(StringUtils.generateGuid(), R.layout.dialog_receive_picture, DialogReceivePictureBinding.class, SimpleDialogReceivePictureViewModel.class, false, false, null, null, null, new CustomDialogListenerBase<SimpleDialogReceivePictureViewModel>() {
            private boolean buttonClicked = false;

            @Override
            public void onCreated(@NonNull LifecycleOwner lifecycleOwner, @Nullable SimpleDialogReceivePictureViewModel viewModel) {
                super.onCreated(lifecycleOwner, viewModel);

                if (viewModel != null) {
                    BaseDialogReceivePictureViewModel baseVm = viewModel;
                    TypeWrapper<Observable.OnPropertyChangedCallback> takePictureCallback = TypeWrapper.getInstance(null);
                    TypeWrapper<Observable.OnPropertyChangedCallback> pickPictureCallback = TypeWrapper.getInstance(null);

                    takePictureCallback.setData(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            Boolean clicked = baseVm.getBtnTakePictureClickState().get();
                            if (clicked != null && clicked) {
                                buttonClicked = true;
                                baseVm.getBtnTakePictureClickState().removeOnPropertyChangedCallback(takePictureCallback.getData());
                                baseVm.getBtnPickPictureClickState().removeOnPropertyChangedCallback(pickPictureCallback.getData());
                                baseVm.dismissDialog();

                                if (receivePictureDialogInfo.getTakePictureInfo() != null) {
                                    receivePictureDialogTakePicture(receivePictureDialogInfo.getTakePictureInfo(), receivePictureDialogInfo.getEditPictureInfo(), receivePictureDialogListener);
                                }
                            }
                        }
                    });
                    pickPictureCallback.setData(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            Boolean clicked = baseVm.getBtnPickPictureClickState().get();
                            if (clicked != null && clicked) {
                                buttonClicked = true;
                                baseVm.getBtnTakePictureClickState().removeOnPropertyChangedCallback(takePictureCallback.getData());
                                baseVm.getBtnPickPictureClickState().removeOnPropertyChangedCallback(pickPictureCallback.getData());
                                baseVm.dismissDialog();

                                if (receivePictureDialogInfo.getPickPictureInfo() != null) {
                                    receivePictureDialogPickPicture(receivePictureDialogInfo.getPickPictureInfo(), receivePictureDialogInfo.getEditPictureInfo(), receivePictureDialogListener);
                                }
                            }
                        }
                    });

                    baseVm.getBtnTakePictureClickState().addOnPropertyChangedCallback(takePictureCallback.getData());
                    baseVm.getBtnPickPictureClickState().addOnPropertyChangedCallback(pickPictureCallback.getData());
                }
            }

            @Override
            public void onDismiss() {
                super.onDismiss();
                if (!buttonClicked) {
                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                        2, "Operação cancelada pelo usuário."
                    ));
                }
            }
        }));
    }

    @Override
    public void getDisplayInfo(@NonNull DisplayInfo displayInfo, @NonNull DisplayInfoListener displayInfoListener) {
        getDisplayResultState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DisplayResultInfo displayResultInfo = getDisplayResultState.get();
                if (displayResultInfo != null) {
                    getDisplayResultState.removeOnPropertyChangedCallback(this);
                    getDisplayResultState.set(null);
                    getDisplayInfoState.setValue(null);
                    displayInfoListener.onResult(displayResultInfo);
                }
            }
        });
        getDisplayInfoState.setValue(displayInfo);
        getDisplayInfoState.setValue(null);
    }

    @Override
    public void setPopupMenu(@NonNull PopupMenuInfo popupMenuInfo) {
        popupMenuInfoState.setValue(popupMenuInfo);
    }

    @Override
    public void openSystemAppConfig(@NonNull SystemAppConfigInfo systemAppConfigInfo) {
        openSystemAppConfigState.setValue(systemAppConfigInfo);
    }


    /**
     * Returns the disposable for the current view model
     */
    protected CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    /**
     * Return the loading state dialog info
     * @return  {@link LoadingDialogInfo}
     */
    LiveData<LoadingDialogInfo> getLoadingDialogState() {
        return loadingDialogState;
    }

    /**
     * Return the custom dialog state info
     * @return  {@link CustomDialogInfo}
     */
    LiveData<CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel>> getCustomDialogState() {
        return customDialogState;
    }

    /**
     * When an exception occurs inside the view model send it to the activity to be handled
     * @return  The
     */
    LiveData<Throwable> getThrowExceptionState() {
        return throwExceptionState;
    }

    /**
     * Return the livedata that is changed with the permission to be reqeuested to the user
     * @return {@link MutableLiveData<PermissionInfo>}
     */
    LiveData<PermissionInfo> getRequestPermissionState() {
        return requestPermissionState;
    }

    /**
     * Return the livedata that is changed to show the toasty info
     * @return  The toasty information to be shown
     */
    LiveData<ToastyInfo> getToastyState() {
        return toastyState;
    }

    /**
     * Return the live data that is changed to open a new screen with the info
     * @return  The state
     */
    LiveData<OpenScreenInfo> getOpenActivityState() {
        return openActivityState;
    }

    /**
     * Return the live data that is changed to finish current screen
     */
    LiveData<Boolean> getFinishScreenState() {
        return finishScreenState;
    }

    /**
     * Return the live data that is changed to navigate using {@link NavController}
     */
    LiveData<NavigationInfo> getNavigationState() {
        return navigationState;
    }

    /**
     * Return the live data that is changed to call backPressed with the info
     * @return  The state
     */
    LiveData<BackPressedInfo> getBackPressedState() {
        return backPressedState;
    }

    /**
     * Return the live data that is changed to perform the onBackPressed with the info
     * @return  The state
     */
    LiveData<BackPressedInfo> getOnBackPressedState() {
        return onBackPressedState;
    }

    /**
     * Return the live data that is changed to check if the location is enabled
     * @return  The state
     */
    LiveData<Boolean> getLocationIsEnabledState() {
        return locationIsEnabledState;
    }

    /**
     * Return the live data that is changed when the activity result is returning some data
     * @return  The state
     */
    LiveData<ActivityResultInfo> getOnReturnActivityResultState() {
        return onReturnActivityResultState;
    }

    /**
     * Return the live data that is changed when the fragment result is returning some data
     * @return  The state
     */
    LiveData<FragmentResultInfo> getOnReturnFragmentResultState() {
        return onReturnFragmentResultState;
    }

    LiveData<SpeechRecognizerInfo> getSpeechRecognizerState() {
        return speechRecognizerState;
    }

    LiveData<KeyboardInfo> getKeyboardInfoState() {
        return keyboardInfoState;
    }

    LiveData<ActionBarInfo> getActionBarInfoState() {
        return actionBarInfoState;
    }

    LiveData<TakePictureInfo> getTakePictureInfoState() {
        return takePictureInfoState;
    }

    LiveData<EditPictureInfo> getEditPictureInfoState() {
        return editPictureInfoState;
    }

    LiveData<PickPictureInfo> getPickPictureInfoState() {
        return pickPictureInfoState;
    }

    LiveData<DisplayInfo> getDisplayInfoState() {
        return getDisplayInfoState;
    }

    LiveData<PopupMenuInfo> getPopupMenuInfoState() {
        return popupMenuInfoState;
    }

    LiveData<SystemAppConfigInfo> getOpenSystemAppConfigState() {
        return openSystemAppConfigState;
    }


    public void setActivityAdapterRequires(ActivityRequires activityAdapterRequires) {
        this.activityAdapterRequires = activityAdapterRequires;
    }

    private void receivePictureDialogTakePicture(@NonNull TakePictureInfo takePictureInfo, @Nullable EditPictureInfo editPictureInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener) {
        checkOrRequestPermission(VerificaPermissoes.PERMISSAO_CAMERA, VerificaPermissoes.PERMISSAO_CAMERA_CODIGO, new AbstractPermissao() {
            @Override
            public void statusPermissao(boolean status) {
                super.statusPermissao(status);
                if (status) {
                    takePicture(takePictureInfo, new TakePictureListener() {
                        @Override
                        public void onPictureReceived(@NonNull TakePictureResultInfo pictureResult) {
                            if (pictureResult.getImgPath() != null) {
                                Log.d(TAG, "pictureResult:" + pictureResult.getImgPath());
                                if (editPictureInfo != null) {
                                    Uri toEditUri = FileProvider.getUriForFile(
                                        BaseApplication.getAppContext(),
                                        BaseApplication.getBaseFileProviderName(),
                                        new File(takePictureInfo.getImgPath())
                                    );
                                    editPictureInfo.setSourceUri(toEditUri);
                                    editPictureInfo.setDestUri(toEditUri);
                                    receivePictureDialogEditTakedPicture(editPictureInfo, receivePictureDialogListener);
                                } else {
                                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getSuccessInstance(pictureResult.getImgPath()));
                                }
                            } else {
                                receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                                    pictureResult.getErrorCode(), pictureResult.getErrorMsg() != null ? pictureResult.getErrorMsg() : "Não foi possível receber a imagem, tente novamente."
                                ));
                            }
                        }
                    });
                } else {
                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                        0, "Não foi possível receber a imagem, devido à permissao de camera recusada"
                    ));
                }
            }
        });
    }

    private void receivePictureDialogPickPicture(@NonNull PickPictureInfo pickPictureInfo, @Nullable EditPictureInfo editPictureInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener) {
        checkOrRequestPermission(VerificaPermissoes.PERMISSAO_ARMAZENAMENTO_LEITURA, VerificaPermissoes.PERMISSAO_ARMAZENAMENTO_LEITURA_CODIGO, new AbstractPermissao() {
            @Override
            public void statusPermissao(boolean status) {
                super.statusPermissao(status);
                if (status) {
                    if (editPictureInfo != null) {
                        if (pickPictureInfo.getCopyToPath() == null) {
                            pickPictureInfo.setCopyToPath(editPictureInfo.getSourcePath());
                        }
                    }
                    pickPicture(pickPictureInfo, new PickPictureListener() {
                        @Override
                        public void onPicturePicked(@NonNull PickPictureResultInfo pickPictureResultInfo) {
                            if (pickPictureResultInfo.getErrorMsg() == null && pickPictureResultInfo.getImgPath() != null) {
                                if (editPictureInfo != null) {
                                    Uri toEditUri = FileProvider.getUriForFile(
                                        BaseApplication.getAppContext(),
                                        BaseApplication.getBaseFileProviderName(),
                                        new File(pickPictureInfo.getCopyToPath())
                                    );
                                    editPictureInfo.setSourceUri(toEditUri);
                                    editPictureInfo.setDestUri(toEditUri);
                                    receivePictureDialogEditTakedPicture(editPictureInfo, receivePictureDialogListener);
                                } else {
                                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getSuccessInstance(pickPictureResultInfo.getImgPath()));
                                }
                            } else {
                                receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                                    0, pickPictureResultInfo.getErrorMsg() != null ? pickPictureResultInfo.getErrorMsg() : "Não foi possível receber a imagem"
                                ));
                            }
                        }
                    });
                } else {
                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                        0, "Não foi possível receber a imagem, devido à permissao de leitura de arquivos recusada"
                    ));
                }
            }
        });
    }

    private void receivePictureDialogEditTakedPicture(@NonNull EditPictureInfo editPictureInfo, @NonNull ReceivePictureDialogListener receivePictureDialogListener) {
        editPicture(editPictureInfo, new EditPictureListener() {
            @Override
            public void onPictureEdited(@NonNull EditPictureResultInfo editPictureResultInfo) {
                if (!editPictureResultInfo.isErrorExists()) {
                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getSuccessInstance(
                        editPictureInfo.getDestinationPath()
                    ));
                } else {
                    receivePictureDialogListener.onPictureReceived(ReceivePictureDialogResultInfo.getErrorInstance(
                        0, "Não foi possível editar a imagem."
                    ));
                }
            }
        });
    }
}
