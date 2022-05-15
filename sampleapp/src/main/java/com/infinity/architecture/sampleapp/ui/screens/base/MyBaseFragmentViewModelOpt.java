package com.infinity.architecture.sampleapp.ui.screens.base;

import com.infinity.architecture.base.enums.ui.ToastyLength;
import com.infinity.architecture.base.models.ui.ToastyInfo;
import com.infinity.architecture.base.ui.BaseFragmentViewModelOpt;
import com.infinity.architecture.base.utils.MessageUtils;

public abstract class MyBaseFragmentViewModelOpt extends BaseFragmentViewModelOpt {

    /**
     * View binded - when a throttleClick is fired for more than one click on a view
     * @param elapsedTime       The elapsed time since the first event
     * @param totalTime         The total time to allow click again
     * @param remainingTime     The remaining time that button will be clickable
     * @param message           The message for this block operation
     */
    public void onBtnThrottling(long elapsedTime, long totalTime, long remainingTime, String message) {
        showToasty(ToastyInfo.defaul(ToastyLength.LONG, MessageUtils.getDefaultThrottleEventListenerMessage(elapsedTime, totalTime, remainingTime, message)));
    }
}
