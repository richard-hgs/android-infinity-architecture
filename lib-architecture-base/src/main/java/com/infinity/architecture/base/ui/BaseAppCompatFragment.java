package com.infinity.architecture.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.infinity.architecture.base.constants.ConstantsBundleBase;
import com.infinity.architecture.base.constants.ConstantsParamsBase;
import com.infinity.architecture.base.enums.ui.KeyboardInfoAction;
import com.infinity.architecture.base.models.ui.ActivityAllBundle;
import com.infinity.architecture.base.models.ui.KeyboardInfo;
import com.infinity.architecture.base.ui.adapter.FragmentRequires;
import com.infinity.architecture.utils.base.FragmentListener;
import com.infinity.architecture.utils.base.viewpager.ViewPagerFragmentStateAdapterCom;
import com.infinity.architecture.utils.keyboard.KeyboardListener;
import com.infinity.architecture.utils.keyboard.KeyboardUtils;
import com.infinity.architecture.utils.string.StringUtils;

import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings("unused")
public abstract class BaseAppCompatFragment extends Fragment implements ViewPagerFragmentStateAdapterCom {

    private final String TAG = "BaseAppCompatFragment";

    private BaseActivityViewModel baseActivityViewModel;
    private BaseFragmentViewModel baseFragmentViewModel;

    /**
     * Var to check if keyboard is visible
     */
    private boolean keyboardIsOpen;

    private final KeyboardListener.SoftKeyboardToggleListener keyboardToggleListener = new KeyboardListener.SoftKeyboardToggleListener() {
        @Override
        public void onToggleSoftKeyboard(boolean isVisible) {
            keyboardIsOpen = isVisible;
        }
    };

    @Nullable
    private FragmentListener fragmentListener = null;

    private View createdView = null;

    private final String fragmentGuid = getClass().getSimpleName() + ":" + StringUtils.generateGuid();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Provides the {@link ViewModel}
     * @param vmClass   Class to get viewModel instance
     * @return          Instance
     */
    @NonNull
    public abstract <T extends ViewModel> T retrieveViewModelForClass(Class<T> vmClass);

    /**
     * Disable the auto dispatch ViewModel safeConstructor
     * @return true=Dispatch manually, false=Dispatch Automatically
     */
    protected abstract boolean isDispatchVmSafeConstructorManually();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearReferences();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return baseFragmentViewModel.onOptionsItemSelected(item.getItemId());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Log.d(TAG, "onCreateView:" + requireActivity());
        baseActivityViewModel = ((BaseAppCompatActivity) requireActivity()).getBaseActivityViewModelInstance();
        baseFragmentViewModel = retrieveBaseViewModel();
        baseFragmentViewModel.setBaseActivityViewModel(baseActivityViewModel);

        // Setup observers
        setupObservers();

        boolean isDispatchVmSafeCtorManually = isDispatchVmSafeConstructorManually();
        if (!isDispatchVmSafeCtorManually) {
            dispatchVmSafeConstructor();
        }

        // Notify onViewCreatedListener
        if (fragmentListener != null) {
            fragmentListener.onCreateView(inflater, container, savedInstanceState, baseActivityViewModel, baseFragmentViewModel);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createdView = view;
    }

    /**
     * Provides the {@link BaseFragmentViewModel} instance to bind commons activity boilerplates
     * @return {@link BaseFragmentViewModel} instance
     */
    @NonNull
    public abstract BaseFragmentViewModel retrieveBaseViewModel();

    /**
     * Provides the {@link androidx.lifecycle.LifecycleOwner} instance to bind commons activity boilerplates
     * @return {@link androidx.lifecycle.LifecycleOwner} instance
     */
    @NonNull
    public abstract LifecycleOwner retrieveLifecycleOwner();

    public <VM extends BaseCustomViewModel> VM getCustomViewModelInstance(@NonNull Class<VM> vmClass) {
        VM vmInstance = retrieveViewModelForClass(vmClass);
        vmInstance.setBaseActivityViewModel(baseActivityViewModel);
        vmInstance.safeConstructor(retrieveLifecycleOwner(), getArguments(), getActivity() != null && getActivity().getIntent() != null ? getActivity().getIntent().getExtras() : null);
        return vmInstance;
    }

    @Override
    public void setFragmentListener(@Nullable FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    private void setupObservers() {
        KeyboardListener.addKeyboardToggleListener(getContext(), getActivity().getWindow(), keyboardToggleListener);

        baseFragmentViewModel.getKeyboardInfoState().observe(retrieveLifecycleOwner(), new Observer<KeyboardInfo>() {
            @Override
            public void onChanged(KeyboardInfo keyboardInfo) {
                if (keyboardInfo != null) {
                    if (
                        keyboardInfo.getAction() == KeyboardInfoAction.CHANGE_KEYBOARD_STATE
                    ) {
                        // Change keyboard state
                        if (keyboardInfo.isShowKeyboard() && keyboardInfo.getViewIdOfKeyboardAction() != View.NO_ID) {
                            // Open keyboard
                            // For current screen
                            if (ConstantsParamsBase.LOG_ENABLED) {
                                Log.d(TAG, "KEYBOARD: showKeyboard");
                            }
                            createdView.findViewById(keyboardInfo.getViewIdOfKeyboardAction()).requestFocus();

                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                            if (!keyboardIsOpen) {
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        } else if (!keyboardInfo.isShowKeyboard()) {
                            // Close keyboard
                            // For current screen
                            View currentFocusView = getActivity().getWindow().getCurrentFocus();
                            if (currentFocusView != null) {
                                if (ConstantsParamsBase.LOG_ENABLED) {
                                    Log.d(TAG, "KEYBOARD: hideKeyboard");
                                }
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
                            }
                        }
                    } else if (
                        keyboardInfo.getAction() == KeyboardInfoAction.TOGGLE_KEYBOARD
                    ) {
                        if (keyboardInfo.getViewIdOfKeyboardAction() != View.NO_ID) {
                            if (ConstantsParamsBase.LOG_ENABLED) {
                                Log.d(TAG, "KEYBOARD: toggleKeyboard");
                            }
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                            createdView.findViewById(keyboardInfo.getViewIdOfKeyboardAction()).requestFocus();
                            KeyboardUtils.toggleKeyboard(getContext().getApplicationContext(), keyboardInfo.getShowFlags(), keyboardInfo.getHideFlags());
                        }
                    } else if (
                        keyboardInfo.getAction() == KeyboardInfoAction.CONFIGURE_KEYBOARD
                    ) {

                    }
                }
            }
        });

        baseFragmentViewModel.setFragmentRequires(new FragmentRequires() {
            @Override
            public String getViewOwnerGroupGuid() {
                return fragmentGuid;
            }

            @Override
            public FragmentManager getFragChildFragManager() {
                return getChildFragmentManager();
            }

            @Override
            public FragmentManager getFragParentFragManager() {
                return getParentFragmentManager();
            }

            @Override
            public Lifecycle getLifecycle() {
                return retrieveLifecycleOwner().getLifecycle();
            }

            @Nullable
            @Override
            public Bundle getArguments() {
                return BaseAppCompatFragment.this.getArguments();
            }
        });
    }

    private void clearReferences() {
        compositeDisposable.dispose();

        KeyboardListener.removeKeyboardToggleListener(keyboardToggleListener);

        if (baseFragmentViewModel != null) {
            baseFragmentViewModel.clearUniqueVmOwnersForGroup(fragmentGuid);
        }

        baseActivityViewModel = null;
        if (baseFragmentViewModel != null) {
            baseFragmentViewModel.setBaseActivityViewModel(null);
        }

        createdView = null;

        fragmentListener = null;
    }

    protected void dispatchVmSafeConstructor() {
        Bundle activityBundle = null;
        if (getActivity() != null) {
            if (getActivity().getIntent() != null) {
                activityBundle = getActivity().getIntent().getExtras();

                ActivityAllBundle activityAllBundle = ActivityAllBundle.fromBundle(activityBundle);
                if (activityAllBundle != null) {
                    if (activityBundle == null) {
                        activityBundle = new Bundle();
                    }

                    if (activityAllBundle.getBundleArgs() != null) {
                        activityBundle.putAll(activityAllBundle.getBundleArgs());
                    }
                    activityBundle.putInt(ConstantsBundleBase.BUNDLE_ACTIVITY_ALL_FIRST_FRAG_ID, activityAllBundle.getStartScreenId());

//                    if (activityBundle != null) {
//                        for (String bundleKey : activityBundle.keySet()) {
//                            Log.d(TAG, "ACTIVITY_BUNDLE - bundleKey: " + bundleKey + " = " + activityBundle.get(bundleKey));
//                        }
//                    } else {
//                        Log.d(TAG, "ACTIVITY_BUNDLE - isNull");
//                    }
                }
            }
        }

        baseFragmentViewModel.safeConstructor(retrieveLifecycleOwner(), getArguments(), activityBundle);
    }
}
