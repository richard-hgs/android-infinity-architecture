package com.infinity.architecture.base.ui.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
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
import com.infinity.architecture.base.ui.ActivityResultListener;
import com.infinity.architecture.base.ui.BaseActivityViewModel;
import com.infinity.architecture.base.ui.BaseActivityViewModelFunctions;
import com.infinity.architecture.base.ui.BaseDialogViewModel;
import com.infinity.architecture.base.ui.FragmentResultListener;
import com.infinity.architecture.base.ui.LocationIsEnabledListener;
import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.base.ui.listeners.DisplayInfoListener;
import com.infinity.architecture.base.ui.listeners.EditPictureListener;
import com.infinity.architecture.base.ui.listeners.PickPictureListener;
import com.infinity.architecture.base.ui.listeners.ReceivePictureDialogListener;
import com.infinity.architecture.base.ui.listeners.TakePictureListener;
import com.infinity.architecture.utils.permission.InterfacePermissao;
import com.infinity.architecture.utils.speech.SpeechRecognizerListener;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseRecyclerViewModel<B extends ViewDataBinding, I> extends ViewModel
        implements BaseAdapterListener<I>, BaseActivityViewModelFunctions {

    private BaseActivityViewModel baseActivityViewModel;
    private CompositeDisposable compositeDisposable;
    private Integer position = null;
    private ArrayList<I> itemList = null;
    private Object[] params = new Object[]{};

    @Nullable
    private BaseAdapterListener<I> baseAdapterListener;

    abstract protected void onBindViewHolder(B binding, ArrayList<I> itemList, I item, int pos);

    @Override
    public void onItemClick(int actionId, I item, int position) {

    }

    @Override
    public boolean onItemLongClick(int actionId, I item, int position) {
        return false;
    }

    void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    void setPosition(@Nullable Integer position) {
        this.position = position;
    }

    void setItemList(ArrayList<I> itemList) {
        this.itemList = itemList;
    };

    void setParams(@NonNull Object[] params) {
        this.params = params;
    }

    public void setBaseActivityViewModel(BaseActivityViewModel baseActivityViewModel) {
        this.baseActivityViewModel = baseActivityViewModel;
    }

    void setBaseAdapterListener(@Nullable BaseAdapterListener<I> baseAdapterListener) {
        this.baseAdapterListener = baseAdapterListener;
    }

    // -------------------------------- BASE_ACTIVITY_VIEW_MODEL_FUNCTIONS -----------------------------------
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
    // -------------------------------- BASE_ACTIVITY_VIEW_MODEL_FUNCTIONS -----------------------------------

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Nullable
    public Integer getPosition() {
        return position;
    }

    @Nullable
    public ArrayList<I> getItemList() {
        return this.itemList;
    }

    @NonNull
    public Object[] getParams() {
        return params;
    }

    @Nullable
    public Object getParamAt(int position) {
        if (position < params.length) {
            return params[position];
        }
        return null;
    }

    public void dispatchOnItemClickAction(int actionId, I item, int position) {
        if (baseAdapterListener != null) {
            baseAdapterListener.onItemClick(actionId, item, position);
        }
    }

    public void dispatchOnItemLongClickAction(int actionId, I item, int position) {
        if (baseAdapterListener != null) {
            baseAdapterListener.onItemLongClick(actionId, item, position);
        }
    }
}
