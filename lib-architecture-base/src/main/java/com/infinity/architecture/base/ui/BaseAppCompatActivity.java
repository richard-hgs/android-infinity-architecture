package com.infinity.architecture.base.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.infinity.architecture.base.BaseApplication;
import com.infinity.architecture.base.R;
import com.infinity.architecture.base.constants.ConstantsBundleBase;
import com.infinity.architecture.base.constants.ConstantsParamsBase;
import com.infinity.architecture.base.enums.ui.DefaultDialogType;
import com.infinity.architecture.base.enums.ui.KeyboardInfoAction;
import com.infinity.architecture.base.enums.ui.ToastyLength;
import com.infinity.architecture.base.enums.ui.ToastyType;
import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.ActivityResultInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.base.models.ui.CustomDialogInfo;
import com.infinity.architecture.base.models.ui.DisplayInfo;
import com.infinity.architecture.base.models.ui.DisplayResultInfo;
import com.infinity.architecture.base.models.ui.EditPictureInfo;
import com.infinity.architecture.base.models.ui.EditPictureResultInfo;
import com.infinity.architecture.base.models.ui.FragmentResultInfo;
import com.infinity.architecture.base.models.ui.KeyboardInfo;
import com.infinity.architecture.base.models.ui.LoadingDialogInfo;
import com.infinity.architecture.base.models.ui.NavigationInfo;
import com.infinity.architecture.base.models.ui.OpenScreenInfo;
import com.infinity.architecture.base.models.ui.PermissionInfo;
import com.infinity.architecture.base.models.ui.PickPictureInfo;
import com.infinity.architecture.base.models.ui.PickPictureResultInfo;
import com.infinity.architecture.base.models.ui.PopupMenuInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerInfo;
import com.infinity.architecture.base.models.ui.SpeechRecognizerResultInfo;
import com.infinity.architecture.base.models.ui.SystemAppConfigInfo;
import com.infinity.architecture.base.models.ui.TakePictureInfo;
import com.infinity.architecture.base.models.ui.TakePictureResultInfo;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.ui.adapter.ActivityRequires;
import com.infinity.architecture.base.utils.BaseViewModelFactory;
import com.infinity.architecture.base.utils.CustomViewModelStoreOwner;
import com.infinity.architecture.base.utils.ViewModelStoreOwnerProvider;
import com.infinity.architecture.utils.dialog.ClasseCarregamento;
import com.infinity.architecture.utils.file.FileHelper;
import com.infinity.architecture.utils.keyboard.KeyboardListener;
import com.infinity.architecture.utils.keyboard.KeyboardUtils;
import com.infinity.architecture.utils.permission.InterfacePermissao;
import com.infinity.architecture.utils.permission.VerificaPermissoes;
import com.infinity.architecture.utils.reflection.TypeWrapper;
import com.infinity.architecture.utils.string.StringUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.hilt.EntryPoint;
import dagger.hilt.EntryPoints;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private final String TAG = "BaseAppCompatActivity";

    private BaseViewModelFactory baseViewModelFactory;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * {@link BaseActivityViewModel} instance
     */
    private BaseActivityViewModel baseActivityViewModelInstance;

    /**
     * {@link NavController} instance or null
     */
    @Nullable
    private NavController navController;

    /**
     * Fullscreen Loading dialog instance
     */
    private ClasseCarregamento classeCarregamento;

    /**
     * Check if some permission is enabled and request when it is needed
     */
    private VerificaPermissoes verificaPermissoes;

    /**
     * Stores the custom dialog instances to be used or cleaned later
     */
    private HashMap<String, Dialog> customDialogInstances = new HashMap<>();

    private HashMap<String, ? extends BaseDialogViewModel> customDialogVmInstances = new HashMap<>();

    /**
     * Stores the poupMenu instances to be used or cleaned later
     */
    private HashMap<String, PopupMenu> popupMenuInstances = new HashMap<>();

    /**
     * Stores the last toasts to be cleared the references
     */
    private ArrayList<Toast> lastToasts = new ArrayList<>();

    private final String BASE_OWNER_GROUP_GUID = "BASE_OWNER_GROUP_GUID";

    /**
     * Stores the viewModelStoreOwners grouped by guid groups,
     * GroupGuid -> String, HashMap<String, HashMap>
     *     - Can be a guid for a fragment
     *     Each group can have a list of owners vinculated to unique ids;
     */
    private ConcurrentHashMap<String, ConcurrentHashMap<String, CustomViewModelStoreOwner>> customViewModelStoreOwners = new ConcurrentHashMap<>();

    /**
     * Var to check if activity is destroyed and prevent ui operations
     */
    private boolean isDestroyed;

    /**
     * Var to check if keyboard is visible
     */
    private boolean keyboardIsOpen;

    /**
     * Stores the back pressed configuration for individuals fragment
     * Key= {@link Integer} Navigation id,
     * Value={@link BackPressedInfo}
     */
    private HashMap<Integer, BackPressedInfo> navigationsBackPressedConfig = new HashMap<>();

    /**
     *  Destination change listener for {@link NavController} provided by {@link #retrieveNavController}
     */
    private NavController.OnDestinationChangedListener navDestinationChangeListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            if (ConstantsParamsBase.LOG_ENABLED) {
                Log.d(TAG, "currentNavId: " + getCurrentNavId() + " - destination: " + getResources().getResourceName(destination.getId()));
            }
            for(Map.Entry<Integer, BackPressedInfo> entry : navigationsBackPressedConfig.entrySet()) {
                Integer keyAt = entry.getKey();
                BackPressedInfo backPressedInfoAt = entry.getValue();
                if (backPressedInfoAt.getClearThisConfigOnFragmentDestroy() != null && backPressedInfoAt.getClearThisConfigOnFragmentDestroy() && keyAt == destination.getId()) {
                    if (ConstantsParamsBase.LOG_ENABLED) {
                        Log.d(TAG, "REMOVING_BACK_PRESSED_CONFIG - navId: " + keyAt + " - navIdName: " + getResources().getResourceName(keyAt));
                    }
                    navigationsBackPressedConfig.remove(keyAt);
                }
            }
        }
    };

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private ActivityResultLauncher<Intent> baseActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (!isDestroyed) {
                    ActivityResultInfo activityResultInfo = ActivityResultInfo.from(result);
                    baseActivityViewModelInstance.onOpenActivityResult(activityResultInfo);
                }
            }
        });

    private ActivityResultLauncher<Intent> baseTextToSpeechResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (!isDestroyed) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> strTextResult = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (strTextResult != null && strTextResult.size() > 0) {
                            baseActivityViewModelInstance.setTextToSpeechResult(SpeechRecognizerResultInfo.getSuccessInstance(
                                result.getData().getExtras(),
                                strTextResult.get(0),
                                strTextResult
                            ));
                        } else {
                            baseActivityViewModelInstance.setTextToSpeechResult(SpeechRecognizerResultInfo.getErrorInstance(
                                0,
                                "Não foi possível realizar o reconhecimento de texto"
                            ));
                        }
                    } else {
                        baseActivityViewModelInstance.setTextToSpeechResult(SpeechRecognizerResultInfo.getErrorInstance(
                            0,
                            "Não foi possível realizar o reconhecimento de texto"
                        ));
                    }
                }
            }
        });

    private String currentTakePictureFilePath = null;
    private String currentPickPictureFilePath = null;

    private ActivityResultLauncher<Intent> takePictureResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (!isDestroyed) {
                    if (result.getResultCode() == Activity.RESULT_OK && currentTakePictureFilePath != null) {
                        baseActivityViewModelInstance.onTakePictureResult(TakePictureResultInfo.getSuccessInstance(
                            currentTakePictureFilePath
                        ));

//                        if (extras != null && extras.containsKey("data")) {
//                            Bitmap bitmap = (Bitmap) extras.get("data");
//                            baseActivityViewModelInstance.onTakePictureResult(TakePictureResultInfo.getSuccessInstance(
//                                bitmap
//                            ));
//                        } else {
//                            baseActivityViewModelInstance.onTakePictureResult(TakePictureResultInfo.getErrorInstance(
//                                0,
//                                "Não foi possível receber a foto, tente novamente."
//                            ));
//                        }
                    } else {
                        baseActivityViewModelInstance.onTakePictureResult(TakePictureResultInfo.getErrorInstance(
                            0,
                            "Não foi possível receber a foto, tente novamente."
                        ));
                    }
                }
            }
        });

    private ActivityResultLauncher<Intent> editPictureResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (!isDestroyed) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        final Uri resultUri = UCrop.getOutput(result.getData());
                        baseActivityViewModelInstance.onEditPictureResult(EditPictureResultInfo.getSuccessInstance());
                    } else {
                        baseActivityViewModelInstance.onEditPictureResult(EditPictureResultInfo.getErrorInstance(
                            0,
                            "Não foi possível receber a foto, tente novamente."
                        ));
                    }
                }
            }
        });

    private ActivityResultLauncher<Intent> pickPictureResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    if (!isDestroyed) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            if (ConstantsParamsBase.LOG_ENABLED) {
                                Log.d(TAG, result.getData().toString());
                            }

                            final Uri imageUri = result.getData().getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            if (currentPickPictureFilePath != null) {
                                FileHelper.copyFile(
                                    getApplicationContext(),
                                    imageUri,
                                    currentPickPictureFilePath
                                );
                            }

                            baseActivityViewModelInstance.onPickPictureResult(PickPictureResultInfo.getSuccessInstance(selectedImage, imageUri.getPath()));
                        } else {
                            baseActivityViewModelInstance.onPickPictureResult(PickPictureResultInfo.getErrorInstance(
                                0,
                                "Não foi possível receber a foto, tente novamente."
                            ));
                        }
                    }
                } catch (Exception e) {
                    onBaseAppCompatException(e);
                    baseActivityViewModelInstance.onPickPictureResult(PickPictureResultInfo.getErrorInstance(
                        0,
                        "Não foi possível receber a foto, tente novamente."
                    ));
                }
            }
        });

    private KeyboardListener.SoftKeyboardToggleListener keyboardToggleListener = new KeyboardListener.SoftKeyboardToggleListener(){
        @Override
        public void onToggleSoftKeyboard(boolean isVisible) {
            keyboardIsOpen = isVisible;
        }
    };

    private ViewModelStoreOwnerProvider viewModelStoreOwnerProvider = new ViewModelStoreOwnerProvider() {
        @Override
        public Application getAppContext() {
            return BaseAppCompatActivity.this.getApplication();
        }

        @Override
        public Intent getIntent() {
            return BaseAppCompatActivity.this.getIntent();
        }
    };

    @EntryPoint
    @InstallIn(ActivityComponent.class)
    interface ActivityCreatorEntryPoint {
        @HiltViewModelMap.KeySet
        Set<String> getViewModelKeys();
        ViewModelComponentBuilder getViewModelComponentBuilder();
    }

    /**
     * Provides the {@link BaseActivityViewModel} instance to bind commons activity boilerplates
     * @return {@link BaseActivityViewModel} instance
     */
    @NonNull
    public abstract BaseActivityViewModel retrieveBaseViewModel();

    /**
     * Provides the {@link NavController} instance to bind commons activity boilerplates
     * @return {@link NavController} instance
     */
    @Nullable
    public abstract NavController retrieveNavController();

    /**
     * Provides the {@link androidx.lifecycle.LifecycleOwner} instance to bind commons activity boilerplates
     * @return {@link androidx.lifecycle.LifecycleOwner} instance
     */
    @NonNull
    public abstract LifecycleOwner retrieveLifecycleOwner();

    /**
     * Provides Instance
     */
    @NonNull
    public abstract Object retrieveInstance();

    /**
     * Provides the {@link ViewModel}
     * @param vmClass   Class to get viewModel instance
     * @return          Instance
     */
    @NonNull
    public abstract <T extends ViewModel> T retrieveViewModelForClass(Class<T> vmClass);

    /**
     * When some error in base activity happens it gets notified to main inheritance
     * @param t     The exception
     */
    public abstract void onBaseAppCompatException(@NonNull Throwable t);

    /**
     * When the activity is created
     * @param savedInstanceState    {@link Bundle} Saved instance when activity gets recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConstantsParamsBase.LOG_ENABLED) {
            Log.d(TAG, "onCreate");
        }
        baseViewModelFactory = new BaseViewModelFactory();

        // Get the baseViewModelInstance
        baseActivityViewModelInstance = retrieveBaseViewModel();

        // Instantiate permission check class
        verificaPermissoes = new VerificaPermissoes();

        // Instantiate loading screen
        classeCarregamento = new ClasseCarregamento(this);

        // Setup observers
        setupObservers();

        baseActivityViewModelInstance.safeConstructor(retrieveLifecycleOwner(), getIntent() != null ? getIntent().getExtras() : null);
    }

//    @CallSuper
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState");
//        Lifecycle lifecycle = customViewModelStoreOwner.getLifecycle();
//        if (lifecycle instanceof LifecycleRegistry) {
//            ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.CREATED);
//        }
//        super.onSaveInstanceState(outState);
//        customViewModelStoreOwner.getSavedStateRegistryController().performSave(outState);
//    }

    /**
     * When the result of a requested permission is handled
     * @param requestCode       Code of the request
     * @param permissions       Permissions info
     * @param grantResults      Grant result info
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            verificaPermissoes.onResultadoPermissao(requestCode, permissions, grantResults);
        } catch (Exception e) {
            onBaseAppCompatException(e);
        }
    }

    /**
     * When activity is destroyed
     */
    @Override
    protected void onDestroy() {
        if (ConstantsParamsBase.LOG_ENABLED) {
            Log.d(TAG, "onDestroy");
        }
        isDestroyed = true;
        clearReferences();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        performBackPressed(true);
    }

    /**
     * Performs the back pressed operation for current {@link NavController} destination {@link BackPressedInfo} configuration
     * that is stored in {@link #navigationsBackPressedConfig} map.
     *
     * If no configuration found performs the {@link AppCompatActivity#onBackPressed}
     *
     * @param closeKeyboardIfOpen   true=CloseKeyboard first if is open, false=Don't close keyboard
     */
    private void performBackPressed(boolean closeKeyboardIfOpen) {
        // Ensures the pending fragment transactions is executed before executing any further changes
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void run() {
                if (closeKeyboardIfOpen && keyboardIsOpen) {
                    baseActivityViewModelInstance.setKeyboard(KeyboardInfo.hideKeyboard());
                } else {
                    BackPressedInfo onBackPressedInfo = getBackPressedInfoConfigForCurrentNav();
                    boolean isBackActionDisabled = (onBackPressedInfo != null && onBackPressedInfo.isBackActionDisabled() != null && onBackPressedInfo.isBackActionDisabled());
                    if (
                        onBackPressedInfo == null || (
                            onBackPressedInfo.getOpenScreenInfo() == null &&
                            !isBackActionDisabled
                        )
                    ) {
                        BaseAppCompatActivity.super.onBackPressed();
                    } else {
                        if (
                            onBackPressedInfo.getOpenScreenInfo() != null &&
                            !isBackActionDisabled
                        ) {
                            openScreenForInfo(onBackPressedInfo.getOpenScreenInfo());
                        }
                    }
                }
            }
        });
    }

    /**
     * Clear references
     */
    private void clearReferences() {
        try {
            compositeDisposable.dispose();

            baseActivityViewModelInstance = null;
            verificaPermissoes = null;
            if (classeCarregamento != null) {
                classeCarregamento.finalizarCarregamento();
                classeCarregamento.clearReferences();
                classeCarregamento = null;
            }

            removeAllCustomDialogReferences(true);

            removeAllPopupMenuReferences(true);

            for (Toast toastAt : lastToasts) {
                toastAt.cancel();
            }
            lastToasts.clear();

            KeyboardListener.removeKeyboardToggleListener(keyboardToggleListener);

            clearAllViewModelStoreOwners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup the observers
     */
    private void setupObservers() {
        try {
            KeyboardListener.addKeyboardToggleListener(this, getWindow(), keyboardToggleListener);

            TypeWrapper<String> navigateResultGuid = TypeWrapper.getInstance(null);

            baseActivityViewModelInstance.getThrowExceptionState().observe(this, new Observer<Throwable>() {
                @Override
                public void onChanged(@Nullable Throwable throwable) {
                    if (!isDestroyed) {
                        if (throwable != null) {
                            onBaseAppCompatException(throwable);
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getRequestPermissionState().observe(this, new Observer<PermissionInfo>() {
                @Override
                public void onChanged(PermissionInfo permissionInfo) {
                    if (!isDestroyed) {
                        if (permissionInfo != null) {
                            verificaPermissoes.verificarPermissao(BaseAppCompatActivity.this, permissionInfo.getStrPermission(), permissionInfo.getIntPermission(), new InterfacePermissao() {
                                @Override
                                public void statusPermissao(boolean status) {
                                    baseActivityViewModelInstance.onRequestedPermissionResult(status);
                                }
                            });
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getToastyState().observe(this, new Observer<ToastyInfo>() {
                @Override
                public void onChanged(ToastyInfo toastyInfo) {
                    if (!isDestroyed) {
                        if (toastyInfo != null) {
                            showToastyForInfo(toastyInfo);
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getOpenActivityState().observe(this, new Observer<OpenScreenInfo>() {
                @Override
                public void onChanged(@Nullable OpenScreenInfo openScreenInfo) {
                    if (!isDestroyed) {
                        openScreenForInfo(openScreenInfo);
                    }
                }
            });

            baseActivityViewModelInstance.getFinishScreenState().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (!isDestroyed) {
                        if (aBoolean != null && aBoolean) {
                            finish();
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getNavigationState().observe(this, new Observer<NavigationInfo>() {
                @Override
                public void onChanged(NavigationInfo navigationInfo) {
                    if (!isDestroyed) {
                        try {
                            if (navigationInfo != null) {
                                NavController navController = getNavController();
                                if (navController != null) {
                                    Bundle bundle = navigationInfo.getBundle();
                                    if (bundle == null) {
                                        bundle = new Bundle();
                                    }
                                    bundle.putBoolean(ConstantsBundleBase.BUNDLE_FRAGMENT_IS_OPEN_FOR_RESULT, navigationInfo.isNavigateForResult());

                                    if (navigationInfo.isNavigateForResult()) {
                                        navigateResultGuid.setData(StringUtils.generateGuid());
                                        getSupportFragmentManager().setFragmentResultListener(navigateResultGuid.getData(), retrieveLifecycleOwner(), new FragmentResultListener() {
                                            @Override
                                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                                if (requestKey.equals(navigateResultGuid.getData())) {
                                                    FragmentResultInfo fragmentResultInfo = FragmentResultInfo.fromBundle(result);
                                                    if (fragmentResultInfo != null) {
                                                        baseActivityViewModelInstance.onOpenFragmentResult(fragmentResultInfo);
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    navController.navigate(navigationInfo.getActionId(), bundle);
                                } else {
                                    throw new java.lang.NullPointerException("you are trying to access navController that is null, did you forget to implement the @Override public void NavController retrieveNavController(); inside your Activity?");
                                }
                            }
                        } catch (Exception e) {
                            onBaseAppCompatException(e);
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getBackPressedState().observe(this, new Observer<BackPressedInfo>() {
                @Override
                public void onChanged(@Nullable BackPressedInfo backPressedInfo) {
                    if (!isDestroyed) {
                        if (backPressedInfo != null) {
                            if (backPressedInfo.getOpenScreenInfo() != null) {
                                openScreenForInfo(backPressedInfo.getOpenScreenInfo());
                            } else {
                                performBackPressed(false);
                            }
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getOnBackPressedState().observe(this, new Observer<BackPressedInfo>() {
                @Override
                public void onChanged(BackPressedInfo backPressedInfo) {
                    if (backPressedInfo != null) {
                        Integer currentNavId = getCurrentNavId();
                        if (currentNavId != null) {
                            if (ConstantsParamsBase.LOG_ENABLED) {
                                Log.d(TAG, "BACK_PRESSED_CONFIG_CHANGED - navId: " + currentNavId + " - navIdName:" + getResources().getResourceName(currentNavId) + " - backPressedInfo: " + backPressedInfo.toString());
                            }
                            navigationsBackPressedConfig.put(currentNavId, backPressedInfo);
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getLoadingDialogState().observe(this, new Observer<LoadingDialogInfo>() {
                @Override
                public void onChanged(@Nullable LoadingDialogInfo loadingDialogInfo) {
                    if (!isDestroyed) {
                        if (loadingDialogInfo != null) {
                            if (loadingDialogInfo.isVisible()) {
                                if (!classeCarregamento.dialogVisivel()) {
                                    // Dialog not visible
                                    classeCarregamento.exibirCarregamento();
                                }

                                if (classeCarregamento.dialogVisivel()) {
                                    // Dialog is visible
                                    classeCarregamento.podeSerCancelado(loadingDialogInfo.isCancelable());

                                    if (classeCarregamento.getTvTitulo() != null) {
                                        if (loadingDialogInfo.getLoadingMsg() != null) {
                                            classeCarregamento.getTvTitulo().setText(loadingDialogInfo.getLoadingMsg());
                                        } else {
                                            classeCarregamento.getTvTitulo().setText("Carregando...");
                                        }
                                    }

                                    if (classeCarregamento.getTvDescricao() != null) {
                                        if (loadingDialogInfo.getLoadingProgressMsg() != null) {
                                            classeCarregamento.getTvDescricao().setText(loadingDialogInfo.getLoadingProgressMsg());
                                        } else {
                                            classeCarregamento.getTvDescricao().setText("");
                                        }
                                    }
                                }
                            } else {
                                classeCarregamento.finalizarCarregamento();
                            }
                        } else {
                            classeCarregamento.finalizarCarregamento();
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getCustomDialogState().observe(this, new Observer<CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel>>() {
                @Override
                public void onChanged(CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo) {
                    if (!isDestroyed) {
                        if (customDialogInfo != null) {
                            boolean isDefaultDialog = customDialogInfo.getLayout() == 0;
                            Dialog dialogInstance = null;
                            Object[] fullVmInstance = null;
                            // BaseDialogViewModel vmInstance = null;

                            if (customDialogInstances.containsKey(customDialogInfo.getGuid())) {
                                dialogInstance = customDialogInstances.get(customDialogInfo.getGuid());
                            } else if (customDialogInfo.isShowing()) {
                                if (customDialogInfo.getDefaultDialogType() == DefaultDialogType.ANDROID) {
                                    dialogInstance = new AlertDialog.Builder(BaseAppCompatActivity.this).create();
                                } else if (isDefaultDialog) {
                                    dialogInstance = new SweetAlertDialog(BaseAppCompatActivity.this, translateCustomDialogTypeToSweetAlertType(customDialogInfo.getDefaultDialogType()));
                                } else {
                                    // Is custom dialog
                                    if (customDialogInfo.getBindingClass() != null && customDialogInfo.getVmClass() != null) {
                                        try {
                                            Class<? extends BaseDialogViewModel> vmClass = customDialogInfo.getVmClass();
                                            Class<? extends ViewDataBinding> vbClass = customDialogInfo.getBindingClass();

                                            fullVmInstance = retrieveUniqueVmInstanceFull(customDialogInfo.getUniqueVmOwnerGroupGuid(), customDialogInfo.getGuid(), vmClass);
                                            BaseDialogViewModel vmInstance = (BaseDialogViewModel) fullVmInstance[0];
                                            CustomViewModelStoreOwner lifecycleOwner = (CustomViewModelStoreOwner) fullVmInstance[1];

                                            ViewDataBinding dataBindingInstance = DataBindingUtil.inflate(getLayoutInflater(), customDialogInfo.getLayout(), null, false);
                                            // dataBindingInstance.setLifecycleOwner(retrieveLifecycleOwner());
                                            dataBindingInstance.setLifecycleOwner(lifecycleOwner);

                                            // Set width and height
                                            if (
                                                (customDialogInfo.getWidth() != null ||
                                                 customDialogInfo.getHeight() != null) &&
                                                dataBindingInstance.getRoot() instanceof ConstraintLayout
                                            ) {
                                                View rootView = dataBindingInstance.getRoot();

                                                Log.d(TAG, "rootView: " + rootView);

                                                // ViewGroup.LayoutParams layParams = dataBindingInstance.getRoot().getLayoutParams();
                                                int minWidth = 0;
                                                int minHeight = 0;

                                                if (rootView instanceof ConstraintLayout) {
                                                    minWidth = ((ConstraintLayout) rootView).getMinWidth();
                                                    minHeight = ((ConstraintLayout) rootView).getMinHeight();
                                                }

                                                if (customDialogInfo.getWidth() != null) {
                                                    minWidth = customDialogInfo.getWidth();
                                                }
                                                if (customDialogInfo.getHeight() != null) {
                                                    minHeight = customDialogInfo.getHeight();
                                                }

                                                DisplayResultInfo displayResultInfo = getDisplayInfo(DisplayInfo.getClientDisplayInfo());
                                                if (minHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                                                    minHeight = displayResultInfo.getClientHeightPx();
                                                    if (rootView instanceof ConstraintLayout) {
                                                        ((ConstraintLayout) rootView).setMinHeight(minHeight);
                                                    }
                                                } else if (minHeight != ViewGroup.LayoutParams.WRAP_CONTENT) {
                                                    if (rootView instanceof ConstraintLayout) {
                                                        ((ConstraintLayout) rootView).setMinHeight(minHeight);
                                                    }
                                                }
                                                if (minWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                                                    minWidth = displayResultInfo.getClientWidthPx();
                                                    if (rootView instanceof ConstraintLayout) {
                                                        ((ConstraintLayout) rootView).setMinWidth(minWidth);
                                                    }
                                                } else if (minWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
                                                    if (rootView instanceof ConstraintLayout) {
                                                        ((ConstraintLayout) rootView).setMinWidth(minWidth);
                                                    }
                                                }
                                            }

                                            vmInstance.setVmGroupOwnerGuid(customDialogInfo.getUniqueVmOwnerGroupGuid());
                                            vmInstance.setBaseActivityViewModel(baseActivityViewModelInstance);
                                            vmInstance.setDialogGuid(customDialogInfo.getGuid());

                                            Method vbSetVmMethod = vbClass.getDeclaredMethod("setViewModel", vmClass);
                                            vbSetVmMethod.invoke(dataBindingInstance, vmInstance);

                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BaseAppCompatActivity.this)
                                                    .setView(dataBindingInstance.getRoot());

                                            dialogInstance = dialogBuilder.create();

                                            setupCustomDialogVmObservers(customDialogInfo, vmInstance);

                                            // Forward place dialogInstance preventing bugs for others FunctionsCalls
                                            // customDialogInstances.put(customDialogInfo.getGuid(), dialogInstance);

                                            // dialogInstance = customDialogBuilder.getAlertDialog();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        throw new RuntimeException("Custom dialog must have binding class and view model class to be created.");
                                    }
                                }
                            }

                            if (dialogInstance != null) {
                                dialogInstance.setCancelable(customDialogInfo.isCancelable());

                                if (dialogInstance.isShowing() && !customDialogInfo.isShowing()) {
                                    // Dismiss dialog
                                    if (dialogInstance instanceof SweetAlertDialog) {
                                        ((SweetAlertDialog) dialogInstance).dismissWithAnimation();
                                    } else {
                                        dialogInstance.dismiss();
                                    }
                                    // customDialogInstances.remove(customDialogInfo.getGuid());
                                    removeCustomDialogReferences(customDialogInfo.getGuid(), false);
                                }

                                // Set width and height
                                if (!dialogInstance.isShowing() &&
                                    dialogInstance.getWindow() != null && (
                                        customDialogInfo.getWidth() != null ||
                                        customDialogInfo.getHeight() != null
                                    )
                                ) {
                                    int width = dialogInstance.getWindow().getDecorView().getWidth();
                                    int height = dialogInstance.getWindow().getDecorView().getHeight();

                                    if (customDialogInfo.getWidth() != null) {
                                        width = customDialogInfo.getWidth();
                                    }
                                    if (customDialogInfo.getHeight() != null) {
                                        height = customDialogInfo.getHeight();
                                    }

                                    dialogInstance.getWindow().setLayout(width, height);
                                }

                                // Set dialog information
                                if (dialogInstance instanceof SweetAlertDialog) {
                                    dialogInstance.setTitle(customDialogInfo.getTitle());
                                } else if (dialogInstance instanceof AlertDialog) {
                                    ((AlertDialog) dialogInstance).setTitle(customDialogInfo.getTitle());
                                }

                                if (dialogInstance instanceof SweetAlertDialog && customDialogInfo.getMessage() != null) {
                                    ((SweetAlertDialog) dialogInstance).setContentText(customDialogInfo.getMessage());
                                }

                                if (dialogInstance instanceof SweetAlertDialog && customDialogInfo.getPositiveButtonText() != null) {
                                    ((SweetAlertDialog) dialogInstance).setConfirmText(customDialogInfo.getPositiveButtonText());
                                }

                                if (dialogInstance instanceof SweetAlertDialog && customDialogInfo.getNegativeButtonText() != null) {
                                    ((SweetAlertDialog) dialogInstance).setCancelText(customDialogInfo.getNegativeButtonText());
                                }

                                if (dialogInstance instanceof SweetAlertDialog) {
                                    ((SweetAlertDialog) dialogInstance).showCancelButton(customDialogInfo.isShowNegativeButton());
                                }

                                int newDialogType = translateCustomDialogTypeToSweetAlertType(customDialogInfo.getDefaultDialogType());

                                if (dialogInstance.isShowing()) {
                                    // Is showing
                                    if (dialogInstance instanceof SweetAlertDialog && ((SweetAlertDialog) dialogInstance).getAlertType() != newDialogType) {
                                        if (newDialogType == SweetAlertDialog.WARNING_TYPE) {
                                            newDialogType = SweetAlertDialog.CUSTOM_IMAGE_TYPE;
                                            ((SweetAlertDialog) dialogInstance).setCustomImage(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_sweet_alert_warning));
                                        }
                                        ((SweetAlertDialog) dialogInstance).changeAlertType(newDialogType);
                                    }
                                } else {
                                    dialogInstance.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            // Dismissed dialog
                                            // customDialogInstances.remove(customDialogInfo.getGuid());
                                            removeCustomDialogReferences(customDialogInfo.getGuid(), false);
                                            // Notify onDismissListener for custom dialogs
                                            if (customDialogInfo.getCustomDialogListener() != null) {
                                                customDialogInfo.getCustomDialogListener().onDismiss();
                                            }
                                        }
                                    });

                                    if (dialogInstance instanceof SweetAlertDialog) {
                                        ((SweetAlertDialog) dialogInstance).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                notifyDialogListenerClick(2, customDialogInfo);
                                            }
                                        });
                                        ((SweetAlertDialog) dialogInstance).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                notifyDialogListenerClick(1, customDialogInfo);
                                            }
                                        });
                                    } else if (dialogInstance instanceof AlertDialog) {
                                        if (customDialogInfo.getNegativeButtonText() != null && customDialogInfo.isShowNegativeButton()) {
                                            ((AlertDialog) dialogInstance).setButton(DialogInterface.BUTTON_NEGATIVE, customDialogInfo.getNegativeButtonText(), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    notifyDialogListenerClick(2, customDialogInfo);
                                                }
                                            });
                                        }
                                        if (customDialogInfo.getPositiveButtonText() != null) {
                                            ((AlertDialog) dialogInstance).setButton(DialogInterface.BUTTON_POSITIVE, customDialogInfo.getPositiveButtonText(), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    notifyDialogListenerClick(1, customDialogInfo);
                                                }
                                            });
                                        }
                                    }

                                    dialogInstance.show();
                                    customDialogInstances.put(customDialogInfo.getGuid(), dialogInstance);

                                    if (customDialogInfo.isTransparentBg()) {
                                        Window dialogWindow = dialogInstance.getWindow();
                                        if (dialogWindow != null) {
                                            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        }
                                    }

                                    if (fullVmInstance != null) {
                                        BaseDialogViewModel vmInstance = (BaseDialogViewModel) fullVmInstance[0];
                                        CustomViewModelStoreOwner lifecycleOwner = (CustomViewModelStoreOwner) fullVmInstance[1];

                                        vmInstance.safeConstructor(retrieveLifecycleOwner(), customDialogInfo.getDialogArgs(), customDialogInfo.getFragArgs(), getIntent() != null ? getIntent().getExtras() : null);

                                        if (customDialogInfo.getCustomDialogListener() != null) {
                                            customDialogInfo.notifyCustomDialogListenerOnCreate(lifecycleOwner, vmInstance);
                                        }
                                    }
                                }
                            }
                            // new SweetAlertDialog(BaseAppCompatActivity.this)
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getLocationIsEnabledState().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean search) {
                    if (!isDestroyed) {
                        if (search != null) {
                            LocationManager lm = (LocationManager) BaseAppCompatActivity.this.getSystemService(Context.LOCATION_SERVICE);
                            boolean gpsEnabled = false;
                            boolean networkEnabled = false;

                            try {
                                gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }

//                        if(!gps_enabled && !network_enabled) {
//
//                        }

                            baseActivityViewModelInstance.onLocationIsEnabledResult(gpsEnabled, networkEnabled);
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getOnReturnActivityResultState().observe(this, new Observer<ActivityResultInfo>() {
                @Override
                public void onChanged(ActivityResultInfo activityResultInfo) {
                    if (!isDestroyed) {
                        if (activityResultInfo != null) {
                            Intent data = activityResultInfo.getData();
                            setResult(activityResultInfo.getResultCode(), data);
                            finish();
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getOnReturnFragmentResultState().observe(this, new Observer<FragmentResultInfo>() {
                @Override
                public void onChanged(FragmentResultInfo fragmentResultInfo) {
                    if (!isDestroyed) {
                        if (fragmentResultInfo != null) {
                            getSupportFragmentManager().setFragmentResult(navigateResultGuid.getData(), fragmentResultInfo.toBundle());
                            onBackPressed();
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getSpeechRecognizerState().observe(this, new Observer<SpeechRecognizerInfo>() {
                @Override
                public void onChanged(SpeechRecognizerInfo speechRecognizerInfo) {
                    if (speechRecognizerInfo != null) {
                        baseTextToSpeechResultLauncher.launch(speechRecognizerInfo.getSpeechIntent());
                    }
                }
            });

            baseActivityViewModelInstance.getKeyboardInfoState().observe(this, new Observer<KeyboardInfo>() {
                @Override
                public void onChanged(KeyboardInfo keyboardInfo) {
                    if (keyboardInfo != null) {
                        if (
                            keyboardInfo.getAction() == KeyboardInfoAction.CHANGE_KEYBOARD_STATE
                        ) {
                            // Change keyboard state
                            if (keyboardInfo.isShowKeyboard() && keyboardInfo.getViewIdOfKeyboardAction() != View.NO_ID) {
                                // Open keyboard
                                if (keyboardInfo.getDialogGuid() != null) {
                                    // In dialog
                                    if (customDialogInstances.containsKey(keyboardInfo.getDialogGuid())) {
                                        Dialog dialogInst = customDialogInstances.get(keyboardInfo.getDialogGuid());
                                        if (dialogInst != null && dialogInst.getWindow() != null) {
                                            if (ConstantsParamsBase.LOG_ENABLED) {
                                                Log.d(TAG, "KEYBOARD: showKeyboardForDialog");
                                            }

                                            View viewToFocus = dialogInst.findViewById(keyboardInfo.getViewIdOfKeyboardAction());
                                            viewToFocus.requestFocus();

                                            dialogInst.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(viewToFocus, InputMethodManager.SHOW_IMPLICIT);
                                        } else {
                                            Log.d(TAG, "KEYBOARD: dialogInstanceNotFound2 - " + (dialogInst != null ? dialogInst.getWindow() : null));
                                        }
                                    } else {
                                        Log.d(TAG, "KEYBOARD: dialogInstanceNotFound");
                                    }
                                } else {
                                    // For current screen
                                    if (ConstantsParamsBase.LOG_ENABLED) {
                                        Log.d(TAG, "KEYBOARD: showKeyboard");
                                    }
                                    findViewById(keyboardInfo.getViewIdOfKeyboardAction()).requestFocus();

                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                                    if (!keyboardIsOpen) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                    }

//                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                    imm.showSoftInput(findViewById(keyboardInfo.getViewIdOfKeyboardAction()), InputMethodManager.SHOW_IMPLICIT);
                                }
                            } else if (!keyboardInfo.isShowKeyboard()) {
                                // Close keyboard
                                if (keyboardInfo.getDialogGuid() != null) {
                                    // In dialog
                                    if (customDialogInstances.containsKey(keyboardInfo.getDialogGuid())) {
                                        Dialog dialogInst = customDialogInstances.get(keyboardInfo.getDialogGuid());
                                        if (dialogInst != null && dialogInst.getWindow() != null) {
                                            if (ConstantsParamsBase.LOG_ENABLED) {
                                                Log.d(TAG, "KEYBOARD: hideKeyboardForDialog");
                                            }

                                            View currentFocusView = dialogInst.getCurrentFocus();
                                            if (currentFocusView != null) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
                                            }
                                        } else {
                                            Log.d(TAG, "KEYBOARD: dialogInstanceNotFound2 - " + (dialogInst != null ? dialogInst.getWindow() : null));
                                        }
                                    } else {
                                        Log.d(TAG, "KEYBOARD: dialogInstanceNotFound");
                                    }
                                } else {
                                    // For current screen
                                    View currentFocusView = getCurrentFocus();
                                    if (currentFocusView != null) {
                                        if (ConstantsParamsBase.LOG_ENABLED) {
                                            Log.d(TAG, "KEYBOARD: hideKeyboard");
                                        }
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
                                    }
                                }
                            }
                        } else if (
                            keyboardInfo.getAction() == KeyboardInfoAction.TOGGLE_KEYBOARD
                        ) {
                            if (keyboardInfo.getViewIdOfKeyboardAction() != View.NO_ID) {
                                if (keyboardInfo.getDialogGuid() != null) {
                                    // In dialog
                                    if (customDialogInstances.containsKey(keyboardInfo.getDialogGuid())) {
                                        Dialog dialogInst = customDialogInstances.get(keyboardInfo.getDialogGuid());
                                        if (dialogInst != null && dialogInst.getWindow() != null) {
                                            if (ConstantsParamsBase.LOG_ENABLED) {
                                                Log.d(TAG, "KEYBOARD: toggleKeyboardForDialog");
                                            }

                                            View viewToFocus = dialogInst.findViewById(keyboardInfo.getViewIdOfKeyboardAction());
                                            viewToFocus.requestFocus();

                                            dialogInst.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(keyboardInfo.getShowFlags(), keyboardInfo.getHideFlags());
                                        } else {
                                            Log.d(TAG, "KEYBOARD: dialoInstanceNotFound2 - " + (dialogInst != null ? dialogInst.getWindow() : null));
                                        }
                                    } else {
                                        Log.d(TAG, "KEYBOARD: dialoInstanceNotFound");
                                    }
                                } else {
                                    if (ConstantsParamsBase.LOG_ENABLED) {
                                        Log.d(TAG, "KEYBOARD: toggleKeyboard");
                                    }
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                                    findViewById(keyboardInfo.getViewIdOfKeyboardAction()).requestFocus();
                                    KeyboardUtils.toggleKeyboard(getApplicationContext(), keyboardInfo.getShowFlags(), keyboardInfo.getHideFlags());
                                }
                            }
                        } else if (
                            keyboardInfo.getAction() == KeyboardInfoAction.CONFIGURE_KEYBOARD
                        ) {
//                            if (keyboardInfo.getCloseKeyboardOnClickOutside() != null && keyboardInfo.getCloseKeyboardOnClickOutside()) {
//                                getWindow().getDecorView().getRootView().setFocusable(true);
//                                getWindow().getDecorView().getRootView().setFocusableInTouchMode(true);
//                            }
                        }
                    }
                }
            });

            baseActivityViewModelInstance.getActionBarInfoState().observe(this, new Observer<ActionBarInfo>() {
                @Override
                public void onChanged(@Nullable ActionBarInfo actionBarInfo) {
                    if (actionBarInfo != null) {
                        ActionBar actionBarInst = getSupportActionBar();
                        Toolbar toolbar = findViewById(androidx.appcompat.R.id.action_bar);

                        if (actionBarInst != null) {
                            if (actionBarInfo.getVisible() != null) {
                                if (actionBarInfo.getVisible()) {
                                    if (!actionBarInst.isShowing()) {
                                        actionBarInst.show();
                                    }
                                } else {
                                    if (actionBarInst.isShowing()) {
                                        actionBarInst.hide();
                                    }
                                }
                            }
                            if (actionBarInfo.getTitle() != null) {
                                actionBarInst.setTitle(actionBarInfo.getTitle());
                            }
                            if (actionBarInfo.getSubTitle() != null) {
                                actionBarInst.setSubtitle(actionBarInfo.getSubTitle());
                            }
                            if (actionBarInfo.getHomeAsUpEnabled() != null) {
                                if (actionBarInfo.getHomeAsUpEnabled()) {
                                    actionBarInst.setDisplayHomeAsUpEnabled(true);
                                } else {
                                    actionBarInst.setDisplayHomeAsUpEnabled(false);
                                }
                            }
                            if (actionBarInfo.getHomeAsUpIcon() != null) {
                                actionBarInst.setHomeAsUpIndicator(actionBarInfo.getHomeAsUpIcon());
                            }
                            if (toolbar != null) {
                                // Default 72dp
                                toolbar.setContentInsetStartWithNavigation(0);
                            }
                        }
                    }
                }
            });

            baseActivityViewModelInstance.setActivityAdapterRequires(new ActivityRequires() {
                @Override
                public ViewModel getViewModelInst(@Nullable String uniqueVmOwnerGroupGuid, Class<? extends ViewModel> vmClass) {
                    return retrieveUniqueVmInstance(uniqueVmOwnerGroupGuid, null, vmClass);
                    // return retrieveViewModelForClass(BaseAppCompatActivity.this, vmClass);
                }

                @Override
                public CompositeDisposable getCompositeDisposable() {
                    return null;
                }

                @Override
                public void clearUniqueVmOwnersForGroup(@NonNull String groupGuid) {
                    clearAllViewModelStoreOwnersForGroup(groupGuid);
                }

                @Override
                public void clearUniqueVmOwnerItem(@NonNull String groupGuid, @NonNull String itemGuid) {
                    BaseAppCompatActivity.this.clearUniqueVmOwnerItem(groupGuid, itemGuid);
                }

                @Override
                public FragmentManager getActFragManager() {
                    return getSupportFragmentManager();
                }

                @Override
                public Lifecycle getLifecycle() {
                    return retrieveLifecycleOwner().getLifecycle();
                }

                @Override
                public LifecycleOwner getLifecycleOwner() {
                    return retrieveLifecycleOwner();
                }
            });

            baseActivityViewModelInstance.getTakePictureInfoState().observe(this, new Observer<TakePictureInfo>() {
                @SuppressLint("QueryPermissionsNeeded")
                @Override
                public void onChanged(TakePictureInfo takePictureInfo) {
                    try {
                        if (takePictureInfo != null) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = FileHelper.createEmptyFile(takePictureInfo.getImgPath());

                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    currentTakePictureFilePath = photoFile.getPath();

                                    Uri photoURI = FileProvider.getUriForFile(
                                        BaseAppCompatActivity.this,
                                        BaseApplication.getBaseFileProviderName(),
                                        photoFile
                                    );

                                    if (ConstantsParamsBase.LOG_ENABLED) {
                                        Log.d(TAG, "photoUri: " + photoURI);
                                    }
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                    takePictureResultLauncher.launch(takePictureIntent);
                                }
                            }
                        }
                    } catch (Exception e) {
                        onBaseAppCompatException(e);
                        baseActivityViewModelInstance.onTakePictureResult(TakePictureResultInfo.getErrorInstance(
                            0,
                            "Não foi possível abrir a câmera, tente novamente."
                        ));
                    }
                }
            });

            baseActivityViewModelInstance.getEditPictureInfoState().observe(this, new Observer<EditPictureInfo>() {
                @Override
                public void onChanged(EditPictureInfo editPictureInfo) {
                    if (editPictureInfo != null) {
                        TypedValue typedValue = new TypedValue();
                        TypedArray a = obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary, R.attr.colorPrimaryDark });
                        int colorPrimary = a.getColor(0, 0);
                        int colorPrimaryDark = a.getColor(1, 0);
                        a.recycle();

                        UCrop uCrop = editPictureInfo.getuCrop();
                        UCrop.Options uCropOptions = editPictureInfo.getuCropOptions();
                        uCropOptions.setToolbarColor(colorPrimary);
                        uCropOptions.setStatusBarColor(colorPrimaryDark);
                        uCropOptions.setToolbarWidgetColor(Color.WHITE);
                        uCrop.withOptions(uCropOptions);

                        Intent intent = uCrop.getIntent(BaseAppCompatActivity.this);

                        editPictureResultLauncher.launch(intent);
                    }
                }
            });

            baseActivityViewModelInstance.getPickPictureInfoState().observe(this, new Observer<PickPictureInfo>() {
                @Override
                public void onChanged(PickPictureInfo pickPictureInfo) {
                    if (pickPictureInfo != null) {
                        currentPickPictureFilePath = pickPictureInfo.getCopyToPath();
                        Intent intentPick = new Intent(Intent.ACTION_PICK);
                        intentPick.setType("image/*");
                        pickPictureResultLauncher.launch(intentPick);
                    }
                }
            });

            baseActivityViewModelInstance.getDisplayInfoState().observe(this, new Observer<DisplayInfo>() {
                @Override
                public void onChanged(DisplayInfo displayInfo) {
                    if (displayInfo != null) {
                        Rect rect = new Rect();
                        Window win = getWindow();
                        win.getDecorView().getWindowVisibleDisplayFrame(rect);
                        int statusHeight = rect.top;
                        View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
                        int contentViewTop = contentView.getTop();
                        int contentViewBottom = contentView.getBottom();
                        int contentViewLeft = contentView.getLeft();
                        int contentViewRight = contentView.getRight();
                        int titleHeight = contentViewTop - statusHeight;
                        int height = contentViewBottom - contentViewTop;
                        int width = contentViewRight - contentViewLeft;

                        //Log.i(TAG, "titleHeight = " + titleHeight + " statusHeight = " + statusHeight + " contentViewTop = " + contentViewTop + " contentViewBottom = " + contentViewBottom);

                        baseActivityViewModelInstance.onDisplayInfoResult(DisplayResultInfo.getInstance(statusHeight, height, width, contentViewTop, contentViewBottom, contentViewLeft, contentViewRight));
                    }
                }
            });

            baseActivityViewModelInstance.getPopupMenuInfoState().observe(this, new Observer<PopupMenuInfo>() {
                @Override
                public void onChanged(PopupMenuInfo popupMenuInfo) {
                    if (popupMenuInfo != null) {
                        int viewIdToBind = popupMenuInfo.getViewIdToBindPopupMenu();
                        View viewToBind = findDialogViewById(viewIdToBind);

                        if (viewToBind == null) {
                            viewToBind = findViewById(viewIdToBind);
                        }

                        PopupMenu popupMenu = new PopupMenu(BaseAppCompatActivity.this, viewToBind);
                        Menu menu = popupMenu.getMenu();
                        MenuInflater menuInflater = popupMenu.getMenuInflater();
                        menuInflater.inflate(popupMenuInfo.getMenuToInflate(), menu);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (popupMenuInfo.getMenuListener() != null) {
                                    return popupMenuInfo.getMenuListener().onMenuItemClick(item.getItemId());
                                }
                                return true;
                            }
                        });

                        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                            @Override
                            public void onDismiss(PopupMenu menu) {
                                if (popupMenuInfo.getMenuListener() != null) {
                                    popupMenuInfo.getMenuListener().onDismiss();
                                }
                                removePopupMenuReferences(popupMenuInfo.getGuid(), false);
                            }
                        });

                        // Configure menu itens
                        if (popupMenuInfo.getMenuItensConfigs() != null) {
                            for (PopupMenuInfo.MenuItemConfig menuItemConfig : popupMenuInfo.getMenuItensConfigs()) {
                                Boolean menuItemConfigVisibile = menuItemConfig.isVisible();

                                MenuItem menuItem = menu.findItem(menuItemConfig.getMenuItemId());

                                if (menuItem != null) {
                                    if (menuItemConfigVisibile != null) {
                                        menuItem.setVisible(menuItemConfigVisibile);
                                    }
                                }
                            }
                        }

                        popupMenu.show();

                        popupMenuInstances.put(popupMenuInfo.getGuid(), popupMenu);
                    }
                }
            });

            baseActivityViewModelInstance.getOpenSystemAppConfigState().observe(this, new Observer<SystemAppConfigInfo>() {
                @Override
                public void onChanged(SystemAppConfigInfo systemAppConfigInfo) {
                    if (systemAppConfigInfo != null) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        String packageName = getApplication().getPackageName();
                        if (systemAppConfigInfo.getPackageName() != null) {
                            packageName = systemAppConfigInfo.getPackageName();
                        }
                        Uri uri = Uri.fromParts("package", packageName, null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            onBaseAppCompatException(e);
        }
    }

//    public int getCameraPhotoOrientation(Context context, String imagePath) {
//        int rotate = 0;
//        try {
//            Uri photoURI = FileProvider.getUriForFile(
//                BaseAppCompatActivity.this,
//                "com.infinity.architecture.base.fileprovider",
//                new File(imagePath)
//            );
//
//            context.getContentResolver().notifyChange(photoURI, null);
//            File imageFile = new File(imagePath);
//
//            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }
//
//            Log.i("RotateImage", "Exif orientation: " + orientation);
//            Log.i("RotateImage", "Rotate value: " + rotate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rotate;
//    }

//    private File createRandomImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getFilesDir();
//        if (!storageDir.exists()) {
//            storageDir.mkdirs();
//        }
//
//        File image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",  /* suffix */
//            storageDir      /* directory */
//        );
//
//        Log.d(TAG, "path:" + image.getPath());
//
//        // Save a file: path for use with ACTION_VIEW intents
//        // currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    /**
     * Show the toasty for received info
     */
    private void showToastyForInfo(@NonNull ToastyInfo toastyInfo) {
        int toastLength = toastyInfo.getToastyLength() == ToastyLength.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
        Toast toast = null;

        if (toastyInfo.getToastyType() == ToastyType.DEFAULT) {
            toast = Toast.makeText(this, toastyInfo.getMessage(), toastLength);
        } else if (toastyInfo.getToastyType() == ToastyType.NORMAL) {
            toast = Toasty.normal(this, toastyInfo.getMessage(), toastLength);
        } else if (toastyInfo.getToastyType() == ToastyType.WARNING) {
            toast = Toasty.warning(this, toastyInfo.getMessage(), toastLength);
        } else if (toastyInfo.getToastyType() == ToastyType.INFO) {
            toast = Toasty.info(this, toastyInfo.getMessage(), toastLength);
        } else if (toastyInfo.getToastyType() == ToastyType.ERROR) {
            toast = Toasty.error(this, toastyInfo.getMessage(), toastLength);
        } else if (toastyInfo.getToastyType() == ToastyType.SUCCESS) {
            toast = Toasty.success(this, toastyInfo.getMessage(), toastLength);
        }

        if (lastToasts.size() > 10) {
            lastToasts.remove(0);
        }

        lastToasts.add(toast);

        if (toast != null) {
            toast.show();
        }
    }

    /**
     * Open a new screen using the information provided
     * @param openScreenInfo
     */
    private void openScreenForInfo(@Nullable OpenScreenInfo openScreenInfo) {
        if (openScreenInfo != null) {
            Intent intent = new Intent(BaseAppCompatActivity.this, openScreenInfo.getScreenToOpen());
            Bundle bundle = openScreenInfo.getBundle();
            if (bundle == null) {
                bundle = new Bundle();
            }

            bundle.putBoolean(ConstantsBundleBase.BUNDLE_ACTIVITY_IS_OPEN_FOR_RESULT, openScreenInfo.isOpenForResult());
            intent.putExtras(bundle);

//            if (openScreenInfo.getBundle() != null) {
//                intent.putExtras(openScreenInfo.getBundle());
//            }

            if (openScreenInfo.getFlags() != null) {
                intent.addFlags(openScreenInfo.getFlags());
            }

            if (!openScreenInfo.isOpenForResult()) {
                startActivity(intent);

                if (openScreenInfo.isFinishCurrentScreen()) {
                    finish();
                }
            } else {
                baseActivityResultLauncher.launch(intent);
            }
        }
    }

    private NavController getNavController() {
        if (navController == null) {
            navController = retrieveNavController();
        }
        return navController;
    }

    private int translateCustomDialogTypeToSweetAlertType(@NonNull DefaultDialogType defaultDialogType) {
        switch(defaultDialogType) {
            case ERROR:
                return SweetAlertDialog.ERROR_TYPE;
            case WARNING:
                return SweetAlertDialog.WARNING_TYPE;
            case SUCCESS:
                return SweetAlertDialog.SUCCESS_TYPE;
            case INFO:
            case NORMAL:
            default:
                return SweetAlertDialog.NORMAL_TYPE;
        }
    }

    private void setupCustomDialogVmObservers(CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo, BaseDialogViewModel dialogVm) {
        dialogVm.getDismissDialogState().observe(BaseAppCompatActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dismiss) {
                if (ConstantsParamsBase.LOG_ENABLED) {
                    Log.d(TAG, "customDialogInstancesContainsKey: " + customDialogInstances.containsKey(customDialogInfo.getGuid()));
                }
                if (dismiss != null && dismiss && customDialogInstances.containsKey(customDialogInfo.getGuid())) {
                    if (customDialogInfo.getUniqueVmOwnerGroupGuid() != null) {
                        clearUniqueVmOwnerItem(customDialogInfo.getUniqueVmOwnerGroupGuid(), customDialogInfo.getGuid());
                    }
                    removeCustomDialogReferences(customDialogInfo.getGuid(), true);
                }
            }
        });
    }

    private void removeCustomDialogReferences(String guid, boolean shouldDismiss) {
        if (customDialogVmInstances.containsKey(guid)) {
            BaseDialogViewModel mDialogVm = customDialogVmInstances.get(guid);
            if (mDialogVm != null) {
                mDialogVm.getDismissDialogState().removeObservers(BaseAppCompatActivity.this);
            }
        }
        customDialogVmInstances.remove(guid);

        if (customDialogInstances.containsKey(guid)) {
            Dialog dialogAt = customDialogInstances.get(guid);
            if (dialogAt != null) {
                if (dialogAt instanceof SweetAlertDialog) {
                    ((SweetAlertDialog) dialogAt).setCancelClickListener(null);
                    ((SweetAlertDialog) dialogAt).setConfirmClickListener(null);
                }
                if (dialogAt.isShowing()) {
                    if (shouldDismiss) {
                        dialogAt.dismiss();
                    }
                }
                dialogAt.setOnDismissListener(null);
            }
        }
        customDialogInstances.remove(guid);
    }

    private void removeAllCustomDialogReferences(boolean shouldDismiss) {
        for (Map.Entry<String, Dialog> entry : customDialogInstances.entrySet()) {
            removeCustomDialogReferences(entry.getKey(), shouldDismiss);
        }
        customDialogInstances.clear();
    }

    /**
     *
     * @param clickType 1=positive, 2=negative 3=neutral
     */
    private void notifyDialogListenerClick(int clickType, @NonNull CustomDialogInfo<? extends ViewDataBinding, ? extends BaseDialogViewModel> customDialogInfo, Object... params) {
        if (customDialogInfo.getCustomDialogListener() != null) {
            boolean dismiss = false;
            if (clickType == 1) {
                // Positive
                dismiss = customDialogInfo.getCustomDialogListener().onPositiveButtonClick(params);
            } else if (clickType == 2) {
                // Negative
                dismiss = customDialogInfo.getCustomDialogListener().onNegativeButtonClick(params);
            } else if (clickType == 3) {
                // Neutral
            }

            if (dismiss) {
                removeCustomDialogReferences(customDialogInfo.getGuid(), true);
            }
        } else {
            removeCustomDialogReferences(customDialogInfo.getGuid(), true);
        }
    }

    /**
     * Remove popup menu reference o avoid memory leaks
     *
     * @param guid          Guid of the popup menu being removed
     * @param shouldDismiss true=Dismiss if visible, false=Don't dismiss
     */
    private void removePopupMenuReferences(String guid, boolean shouldDismiss) {
        if (popupMenuInstances.containsKey(guid)) {
            PopupMenu popupMenu = popupMenuInstances.get(guid);
            if (popupMenu != null) {
                popupMenu.setOnMenuItemClickListener(null);
                popupMenu.setOnDismissListener(null);
                if (shouldDismiss) {
                    popupMenu.dismiss();
                }
            }

            popupMenuInstances.remove(guid);
        }
    }

    private void removeAllPopupMenuReferences(boolean shouldDismiss) {
        for (Map.Entry<String, PopupMenu> entry : popupMenuInstances.entrySet()) {
            removePopupMenuReferences(entry.getKey(), shouldDismiss);
        }
        customDialogInstances.clear();
    }


    public BaseActivityViewModel getBaseActivityViewModelInstance() {
        if (baseActivityViewModelInstance == null) {
            throw new RuntimeException("BaseActivityViewModel instance is null. You should call this method after the super.onCreate() method.");
        }
        return baseActivityViewModelInstance;
    }

    public <VM extends BaseCustomViewModel> VM getCustomViewModelInstance(@NonNull Class<VM> vmClass) {
        VM vmInstance = retrieveViewModelForClass(vmClass);
        vmInstance.setBaseActivityViewModel(getBaseActivityViewModelInstance());
        vmInstance.safeConstructor(retrieveLifecycleOwner(), null, getIntent() != null ? getIntent().getExtras() : null);
        return vmInstance;
    }

    public void requestNavController() {
        getNavController();
    }

    @SuppressWarnings("unchecked")
    private <VM extends ViewModel> VM retrieveUniqueVmInstance(@Nullable String uniqueVmOwnerGroupGuid, @Nullable String uniqueVmOwnerGuid, Class<VM> vmClass) {
        return (VM) retrieveUniqueVmInstanceFull(uniqueVmOwnerGroupGuid, uniqueVmOwnerGuid, vmClass)[0];
    }

    private <VM extends ViewModel> Object[] retrieveUniqueVmInstanceFull(@Nullable String uniqueVmOwnerGroupGuid, @Nullable String uniqueVmOwnerGuid, Class<VM> vmClass) {
        Object[] result = new Object[] {null, null};

        Exception ex1 = null;
        Exception ex2 = null;

        try {
            CustomViewModelStoreOwner customViewModelStoreOwner = new CustomViewModelStoreOwner(viewModelStoreOwnerProvider);
            customViewModelStoreOwner.getLifecycleRegistry().setCurrentState(Lifecycle.State.INITIALIZED);

            // VM vmInstance = null;

//            try {
//                if (vmInstance == null) {
//                    vmInstance = new ViewModelProvider(this).get(vmClass);
//                }
//            } catch (Exception e) {
//                ex1 = e;
//            }
//
//            try {
//                if (vmInstance == null) {
//                    vmInstance = new ViewModelProvider(this, baseViewModelFactory).get(vmClass);
//                }
//            } catch (Exception e) {
//                ex2 = e;
//            }

            // if (vmInstance == null) {
            Object instance = retrieveInstance();

            Object viewModelFactory = ((HasDefaultViewModelProviderFactory) instance).getDefaultViewModelProviderFactory();

            Field fieldHiltViewModelkeys = viewModelFactory.getClass().getDeclaredField("hiltViewModelKeys");
            fieldHiltViewModelkeys.setAccessible(true);

            Field fieldHiltDelegateFactory = viewModelFactory.getClass().getDeclaredField("delegateFactory");
            fieldHiltDelegateFactory.setAccessible(true);

            @SuppressWarnings("unchecked")
            Set<String> hiltViewModelkeys = (Set<String>) fieldHiltViewModelkeys.get(viewModelFactory);
            ViewModelProvider.Factory delegateFactory = (ViewModelProvider.Factory) fieldHiltDelegateFactory.get(viewModelFactory);

            customViewModelStoreOwner.getSavedStateRegistryController().performRestore(null);

            ActivityCreatorEntryPoint entryPoint = EntryPoints.get(instance, ActivityCreatorEntryPoint.class);
            HiltViewModelFactory hiltViewModelFactory = new HiltViewModelFactory(customViewModelStoreOwner, null, hiltViewModelkeys, delegateFactory, entryPoint.getViewModelComponentBuilder());

            VM vmInstance = hiltViewModelFactory.create(vmClass);
            // }

            customViewModelStoreOwner.getLifecycleRegistry().setCurrentState(Lifecycle.State.CREATED);
            customViewModelStoreOwner.getLifecycleRegistry().setCurrentState(Lifecycle.State.STARTED);

            String ownerGroupGuid = BASE_OWNER_GROUP_GUID;
            String ownerGuid = StringUtils.generateGuid();

            if (uniqueVmOwnerGroupGuid != null) {
                ownerGroupGuid = uniqueVmOwnerGroupGuid;
            }

            if (uniqueVmOwnerGuid != null) {
                ownerGuid = uniqueVmOwnerGuid;
            }

            if (!customViewModelStoreOwners.containsKey(ownerGroupGuid)) {
                customViewModelStoreOwners.put(ownerGroupGuid, new ConcurrentHashMap<>());
            }

            ConcurrentHashMap<String, CustomViewModelStoreOwner> ownerListForGroup = customViewModelStoreOwners.get(ownerGroupGuid);
            if (ownerListForGroup != null) {
                ownerListForGroup.put(ownerGuid, customViewModelStoreOwner);
            }

            if (ConstantsParamsBase.LOG_ENABLED) {
                Log.d(TAG, "ownerListSize: " + customViewModelStoreOwners.get(ownerGroupGuid).size());
            }

            // printOwnerVmList();

            if (vmInstance == null) {
                throw new Exception("Can't create viewModel " + vmClass.getName() + " instance");
            }

            result[0] = vmInstance;
            result[1] = customViewModelStoreOwner;

            return result;
        } catch (Exception e) {
            if (ex1 != null) {
                ex1.printStackTrace();
            }
            if (ex2 != null) {
                ex2.printStackTrace();
            }
            e.printStackTrace();
        }
        throw new RuntimeException("Can't create viewModel instance, activity instance isn't instanceof ViewModelStoreOwner");
    }

    private void clearAllViewModelStoreOwners() {
        try {
            for (Map.Entry<String, Dialog> entry : customDialogInstances.entrySet()) {
                clearAllViewModelStoreOwnersForGroup(entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onBaseAppCompatException(e);
        }
    }

    private void clearAllViewModelStoreOwnersForGroup(String groupGuid) {
        try {
            if (ConstantsParamsBase.LOG_ENABLED) {
                Log.d(TAG, "clearAllViewModelStoreOwnersForGroup: " + groupGuid);
            }
            // printOwnerVmList();

            ConcurrentHashMap<String, CustomViewModelStoreOwner> ownerList = customViewModelStoreOwners.get(groupGuid);
            if (ownerList != null) {
                for (Map.Entry<String, CustomViewModelStoreOwner> entry : ownerList.entrySet()) {
                    CustomViewModelStoreOwner customVmOwner = entry.getValue();
                    if (customVmOwner != null) {
                        customVmOwner.getLifecycleRegistry().setCurrentState(Lifecycle.State.DESTROYED);
                    }
                    ownerList.remove(entry.getKey());
                }
            }
            // printOwnerVmList();
        } catch (Exception e) {
            e.printStackTrace();
            onBaseAppCompatException(e);
        }
    }

    private void clearUniqueVmOwnerItem(@NonNull String groupGuid, @NonNull String itemGuid) {
        try {
            if (ConstantsParamsBase.LOG_ENABLED) {
                Log.d(TAG, "clearUniqueVmOwnerItem - group: " + groupGuid + " - item: " + itemGuid);
            }

            ConcurrentHashMap<String, CustomViewModelStoreOwner> ownerList = customViewModelStoreOwners.get(groupGuid);
            if (ownerList != null) {
                CustomViewModelStoreOwner customVmOwner = ownerList.get(itemGuid);
                if (customVmOwner != null) {
                    customVmOwner.getLifecycleRegistry().setCurrentState(Lifecycle.State.DESTROYED);
                }

                ownerList.remove(itemGuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onBaseAppCompatException(e);
        }
    }

    private void printOwnerVmList() {
        for (Map.Entry<String, ConcurrentHashMap<String, CustomViewModelStoreOwner>> entry : customViewModelStoreOwners.entrySet()) {
            if (ConstantsParamsBase.LOG_ENABLED) {
                Log.d(TAG, "group: " + entry.getKey());
            }
            ConcurrentHashMap<String, CustomViewModelStoreOwner> ownerList = customViewModelStoreOwners.get(entry.getKey());
            if (ConstantsParamsBase.LOG_ENABLED) {
                if (ownerList != null) {
                    Log.d(TAG, "    - vmOwnerEntries: " + ownerList.size());
//                for (Map.Entry<String, CustomViewModelStoreOwner> entry2 : ownerList.entrySet()) {
//                    Log.d(TAG, "    - " + entry2.getKey());
//                }
                } else {
                    Log.d(TAG, "    - listIsEmpty");
                }
            }
        }
    }

    /**
     * Get current {@link NavController} destination id
     *
     * @return {@link Integer} destination id
     */
    @Nullable
    private Integer getCurrentNavId() {
        Integer currentNavId = null;
        NavController navController = getNavController();
        if (navController != null) {
            NavDestination currentNavDestination = navController.getCurrentDestination();
            if (currentNavDestination != null) {
                currentNavId = navController.getCurrentDestination().getId();
            }
        }
        return currentNavId;
    }

    /**
     * Get {@link BackPressedInfo} configuration for current activity {@link NavController} destination
     *
     * @return  {@link BackPressedInfo} configuration
     */
    @Nullable
    private BackPressedInfo getBackPressedInfoConfigForCurrentNav() {
        BackPressedInfo backPressedInfo = null;
        Integer currentNavId = getCurrentNavId();
        if (currentNavId != null) {
            backPressedInfo = navigationsBackPressedConfig.get(currentNavId);
        }
        return backPressedInfo;
    }

    /**
     * Search in dialog a view by its id
     *
     * @param viewId    View id to be found
     * @return  Null or view if exists
     */
    @Nullable
    private View findDialogViewById(@IdRes int viewId) {
        for(Map.Entry<String, Dialog> entryAt : customDialogInstances.entrySet()) {
            Dialog dialogAt = entryAt.getValue();
            if (dialogAt instanceof android.app.AlertDialog) {
                android.app.AlertDialog alertDialogAt = (android.app.AlertDialog) dialogAt;
                if (alertDialogAt.isShowing()) {
                    Window dialogAtWindow = alertDialogAt.getWindow();
                    if (dialogAtWindow != null) {
                        View viewFound = dialogAtWindow.getDecorView().findViewById(viewId);
                        if (viewFound != null) {
                            return viewFound;
                        }
                    }
                }
            }
        }
        return null;
    }


    @NonNull
    private DisplayResultInfo getDisplayInfo(@NonNull DisplayInfo displayInfo) {
        Rect rect = new Rect();
        Window win = getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusHeight = rect.top;
        View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
        int contentViewTop = contentView.getTop();
        int contentViewBottom = contentView.getBottom();
        int contentViewLeft = contentView.getLeft();
        int contentViewRight = contentView.getRight();
        int titleHeight = contentViewTop - statusHeight;
        int height = contentViewBottom - contentViewTop;
        int width = contentViewRight - contentViewLeft;

        //Log.i(TAG, "titleHeight = " + titleHeight + " statusHeight = " + statusHeight + " contentViewTop = " + contentViewTop + " contentViewBottom = " + contentViewBottom);

        return DisplayResultInfo.getInstance(statusHeight, height, width, contentViewTop, contentViewBottom, contentViewLeft, contentViewRight);
    }
}
