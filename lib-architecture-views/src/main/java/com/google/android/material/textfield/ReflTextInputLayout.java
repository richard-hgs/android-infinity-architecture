package com.google.android.material.textfield;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.infinity.architecture.utils.variables.VarUtils;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.lang.reflect.Field;

public class ReflTextInputLayout {
    public static CollapsingTextHelper getCollapsingTextHelper(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("collapsingTextHelper");
        field.setAccessible(true);
        return (CollapsingTextHelper) field.get(textInputLayout);
    }

    public static MaterialShapeDrawable getBoxBackground(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("boxBackground");
        field.setAccessible(true);
        return (MaterialShapeDrawable) field.get(textInputLayout);
    }

    public static CharSequence getHint(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("hint");
        field.setAccessible(true);
        return (CharSequence) field.get(textInputLayout);
    }

    public static RectF getTmpRectF(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("tmpRectF");
        field.setAccessible(true);
        return (RectF) field.get(textInputLayout);
    }

    public static int getBoxLabelCutoutPaddingPx(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("boxLabelCutoutPaddingPx");
        field.setAccessible(true);
        return (Integer) VarUtils.ifNull(field.get(textInputLayout), 0);
    }

    public static int getBoxStrokeWidthPx(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("boxStrokeWidthPx");
        field.setAccessible(true);
        return (Integer) VarUtils.ifNull(field.get(textInputLayout), 0);
    }

    public static boolean isHintExpanded(
        @NonNull TextInputLayout textInputLayout
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = TextInputLayout.class.getDeclaredField("hintExpanded");
        field.setAccessible(true);
        return (Boolean) VarUtils.ifNull(field.get(textInputLayout), false);
    }
}
