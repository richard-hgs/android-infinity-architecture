package com.bumptech.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.transition.TransitionFactory;

public class GlideUtils {
    @NonNull
    public static <T> TransitionOptions<?, T> getDefaultTransitionOptions(RequestManager requestManager, Class<T> transcodeClass) {
        return requestManager.getDefaultTransitionOptions(transcodeClass);
    }

    public static <T, TranscodeType> TransitionFactory<? super TranscodeType> getTransitionFactory(TransitionOptions<?, TranscodeType> transitionOptions) {
        return transitionOptions.getTransitionFactory();
    }

    public static void setCanvasBitmapDensity(@NonNull Bitmap toTransform, @NonNull Bitmap canvasBitmap) {
        canvasBitmap.setDensity(toTransform.getDensity());
    }
}
