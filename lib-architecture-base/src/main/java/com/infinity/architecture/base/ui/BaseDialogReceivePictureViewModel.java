package com.infinity.architecture.base.ui;

import androidx.databinding.ObservableField;

import com.infinity.architecture.base.enums.ui.ToastyLength;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.utils.MessageUtils;

public abstract class BaseDialogReceivePictureViewModel extends BaseDialogViewModel {

    private ObservableField<Boolean> btnTakePictureClickState = new ObservableField<>();
    private ObservableField<Boolean> btnPickPictureClickState = new ObservableField<>();

    /**
     * View Binded
     */
    public void onBtnBackClick() {
        dismissDialog();
    }

    /**
     * View Binded
     * Must be called to open camera to take a picture
     */
    public void onBtnTakePictureClick() {
        btnTakePictureClickState.set(true);
        btnTakePictureClickState.set(null);
    }

    /**
     * View Binded
     * Must be called to open the gallery to pick a picture
     */
    public void onBtnPickPictureClick() {
        btnPickPictureClickState.set(true);
        btnPickPictureClickState.set(null);
    }

    /**
     * View binded
     * @param elapsedTime
     * @param totalTime
     * @param remainingTime
     * @param message
     */
    public void onBtnThrottling(long elapsedTime, long totalTime, long remainingTime, String message) {
        showToasty(ToastyInfo.defaul(ToastyLength.LONG, MessageUtils.getDefaultThrottleEventListenerMessage(elapsedTime, totalTime, remainingTime, message)));
    }

    ObservableField<Boolean> getBtnTakePictureClickState() {
        return btnTakePictureClickState;
    }

    ObservableField<Boolean> getBtnPickPictureClickState() {
        return btnPickPictureClickState;
    }
}
