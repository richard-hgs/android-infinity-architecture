package com.google.android.material.textfield;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.google.android.material.shape.MaterialShapeDrawable;

public class ReflCutoutDrawable {
    public static boolean shapeDrawableIsCutoutDrawable(MaterialShapeDrawable materialShapeDrawable) {
        return materialShapeDrawable instanceof CutoutDrawable;
    }

    public static void setCutout(@NonNull MaterialShapeDrawable materialShapeDrawable, @NonNull RectF cutoutBounds) {
        ((CutoutDrawable) materialShapeDrawable).setCutout(cutoutBounds);
    }

    public static void removeCutout(@NonNull MaterialShapeDrawable materialShapeDrawable) {
        ((CutoutDrawable) materialShapeDrawable).removeCutout();
    }

    public static boolean hasCutout(@NonNull MaterialShapeDrawable materialShapeDrawable) {
        return ((CutoutDrawable) materialShapeDrawable).hasCutout();
    }
}
