package com.google.android.material.internal;

import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflCollapsingTextHelper {
    public static void getCollapsedTextActualBounds(
        @NonNull CollapsingTextHelper collapsingTextHelper,
        @NonNull RectF bounds,
        int labelWidth,
        int textGravity
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CollapsingTextHelper.class.getDeclaredMethod("getCollapsedTextActualBounds", RectF.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(collapsingTextHelper, bounds, labelWidth, textGravity);
    }

    public static void setCollapsedBounds(
        @NonNull CollapsingTextHelper collapsingTextHelper,
        int left, int top, int right, int bottom
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CollapsingTextHelper.class.getDeclaredMethod("setCollapsedBounds", int.class, int.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(collapsingTextHelper, left, top, right, bottom);

    }

    public static Rect getCollapsedBounds(
        @NonNull CollapsingTextHelper collapsingTextHelper
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = CollapsingTextHelper.class.getDeclaredField("collapsedBounds");
        field.setAccessible(true);
        return (Rect) field.get(collapsingTextHelper);
    }

    public static void recalculate(
        @NonNull CollapsingTextHelper collapsingTextHelper
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CollapsingTextHelper.class.getDeclaredMethod("recalculate");
        method.setAccessible(true);
        method.invoke(collapsingTextHelper);
    }
}
