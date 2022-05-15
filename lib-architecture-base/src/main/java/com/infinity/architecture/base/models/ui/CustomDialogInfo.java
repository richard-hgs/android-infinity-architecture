package com.infinity.architecture.base.models.ui;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;

import com.infinity.architecture.base.enums.ui.DefaultDialogType;
import com.infinity.architecture.base.ui.BaseDialogViewModel;
import com.infinity.architecture.base.ui.CustomDialogListener;
import com.infinity.architecture.base.ui.CustomDialogListenerBase;


public class CustomDialogInfo<B extends ViewDataBinding, VM extends BaseDialogViewModel> {
    private boolean show;
    private String guid;
    private DefaultDialogType defaultDialogType;
    private String title;
    private String message;
    private boolean cancelable = true;
    private boolean showNegativeButton;
    private String negativeButtonText;
    private String positiveButtonText;

    private int layout = 0;
    private Class<B> bindingClass;
    private Class<VM> vmClass;
    private CustomDialogListener<VM> customDialogListener;
    private boolean transparentBg;
    private Bundle dialogArgs;
    private Bundle fragArgs;

    private String uniqueVmOwnerGroupGuid;

    private CustomDialogInfo() {

    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogNormal(@NonNull String guid, @NonNull String title, @Nullable String message) {
        return defDialogNormal(guid, title, message, true);
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogNormal(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.NORMAL;
        customDialogInfo.title = title;
        customDialogInfo.message = message;
        customDialogInfo.show = true;
        customDialogInfo.cancelable = cancelable;
        return customDialogInfo;
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogInfo(@NonNull String guid, @NonNull String title, @Nullable String message) {
        return defDialogInfo(guid, title, message, true, null, null, false, null);
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogInfo(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable, @Nullable String positiveButtonText, @Nullable String negativeButtonText, boolean showNegativeButton, @Nullable CustomDialogListener<BaseDialogViewModel> customDialogListener) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.INFO;
        customDialogInfo.title = title;
        customDialogInfo.message = message;
        customDialogInfo.show = true;
        customDialogInfo.cancelable = cancelable;
        customDialogInfo.positiveButtonText = positiveButtonText;
        customDialogInfo.negativeButtonText = negativeButtonText;
        customDialogInfo.showNegativeButton = showNegativeButton;
        customDialogInfo.customDialogListener = customDialogListener;
        return customDialogInfo;
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogError(@NonNull String guid, @NonNull String title, @Nullable String message) {
        return defDialogError(guid, title, message, true);
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogError(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.ERROR;
        customDialogInfo.title = title;
        customDialogInfo.message = message;
        customDialogInfo.show = true;
        customDialogInfo.cancelable = cancelable;
        return customDialogInfo;
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogSuccess(@NonNull String guid, @NonNull String title, @Nullable String message) {
        return defDialogSuccess(guid, title, message, true);
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogSuccess(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.SUCCESS;
        customDialogInfo.title = title;
        customDialogInfo.message = message;
        customDialogInfo.show = true;
        customDialogInfo.cancelable = cancelable;
        return customDialogInfo;
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogAndroid(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable) {
        return defDialogAndroid(guid, title, message, cancelable,"OK", null, false);
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> defDialogAndroid(@NonNull String guid, @NonNull String title, @Nullable String message, boolean cancelable, @Nullable String positiveButtonText, @Nullable String negativeButtonText, boolean showNegativeButton) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.ANDROID;
        customDialogInfo.title = title;
        customDialogInfo.message = message;
        customDialogInfo.show = true;
        customDialogInfo.cancelable = cancelable;
        customDialogInfo.positiveButtonText = positiveButtonText;
        customDialogInfo.negativeButtonText = negativeButtonText;
        customDialogInfo.showNegativeButton = showNegativeButton;
        return customDialogInfo;
    }

    public static <B extends ViewDataBinding, VM extends BaseDialogViewModel> CustomDialogInfo<B, VM> dialogCustom(@NonNull String guid, @LayoutRes int layout, @NonNull Class<B> bindingClass, @NonNull Class<VM> vmClass, boolean cancelable, boolean transparentBg, @Nullable Bundle dialogArgs, @Nullable CustomDialogListenerBase<VM> customDialogListener) {
        CustomDialogInfo<B, VM> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.NORMAL;
        customDialogInfo.title = "";
        customDialogInfo.show = true;
        customDialogInfo.layout = layout;
        customDialogInfo.bindingClass = bindingClass;
        customDialogInfo.vmClass = vmClass;
        customDialogInfo.cancelable = cancelable;
        customDialogInfo.transparentBg = transparentBg;
        customDialogInfo.dialogArgs = dialogArgs;
        customDialogInfo.customDialogListener = customDialogListener;
        return customDialogInfo;
    }

    public static CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> dismissDialog(@NonNull String guid) {
        CustomDialogInfo<ViewDataBinding, BaseDialogViewModel> customDialogInfo = new CustomDialogInfo<>();
        customDialogInfo.guid = guid;
        customDialogInfo.defaultDialogType = DefaultDialogType.SUCCESS;
        customDialogInfo.title = "";
        customDialogInfo.show = false;
        return customDialogInfo;
    }

    /**
     * Fix the generic type
     * @param genericVm Class<? extends {@link BaseDialogViewModel}>
     */
    public void notifyCustomDialogListenerOnCreate(@NonNull LifecycleOwner lifecycleOwner, @Nullable BaseDialogViewModel genericVm) {
        //noinspection unchecked
        customDialogListener.onCreated(lifecycleOwner, (VM) genericVm);
    }

    @NonNull
    public String getGuid() {
        return guid;
    }

    @NonNull
    public DefaultDialogType getDefaultDialogType() {
        return defaultDialogType;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public boolean isShowing() {
        return show;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public boolean isShowNegativeButton() {
        return showNegativeButton;
    }

    @Nullable
    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    @Nullable
    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    @LayoutRes
    public int getLayout() {
        return layout;
    }

    @Nullable
    public Class<B> getBindingClass() {
        return bindingClass;
    }

    @Nullable
    public Class<VM> getVmClass() {
        return vmClass;
    }

    @Nullable
    public CustomDialogListener<VM> getCustomDialogListener() {
        return customDialogListener;
    }

    public boolean isTransparentBg() {
        return transparentBg;
    }

    public void setTransparentBg(boolean transparentBg) {
        this.transparentBg = transparentBg;
    }

    @Nullable
    public String getUniqueVmOwnerGroupGuid() {
        return uniqueVmOwnerGroupGuid;
    }

    public void setUniqueVmOwnerGroupGuid(@Nullable String uniqueVmOwnerGroupGuid) {
        this.uniqueVmOwnerGroupGuid = uniqueVmOwnerGroupGuid;
    }

    @Nullable
    public Bundle getDialogArgs() {
        return dialogArgs;
    }

    @Nullable
    public Bundle getFragArgs() {
        return fragArgs;
    }

    public void setFragArgs(Bundle fragArgs) {
        this.fragArgs = fragArgs;
    }


    @Override
    public String toString() {
        return "CustomDialogInfo{" +
                "show=" + show +
                ", guid='" + guid + '\'' +
                ", defaultDialogType=" + defaultDialogType +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
