package com.infinity.architecture.base.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.backservices.Resource;

public class MessageUtils {

    public static String getDefaultThrottleEventListenerMessage(long elapsedTime, long totalTime, long remainingTime, String message) {
        return "Você já clicou no botão, aguarde " + ((float) remainingTime / 1000.0f) + " segundos e tente novamente!";
    }

    public static <T>  String extractResourceErrorOrDefMessage(@Nullable Resource<T> resource, @NonNull String defMessage) {
        String errorMsg = defMessage;
        if (resource != null && resource.getErrorMsg() != null) {
            errorMsg = resource.getErrorMsg();
        }
        return errorMsg;
    }
}
