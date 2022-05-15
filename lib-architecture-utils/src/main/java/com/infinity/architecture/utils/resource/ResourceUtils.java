package com.infinity.architecture.utils.resource;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ResourceUtils {

    public static int getResourceIdByName(Context context, String resType, String resName) {
        return context.getResources().getIdentifier(resName, resType, context.getPackageName());
    }

    public static Drawable getDrawableFromAttrRes(Context context, int attrRes) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attrRes});
        try {
            return a.getDrawable(0);
        } finally {
            a.recycle();
        }
    }

    public static Drawable getDrawableFromAttrResLayerList(Context context, int attrRes, Drawable drawable) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attrRes});
        try {
            Drawable[] drawables = {drawable, a.getDrawable(0)};
            // Drawable attrResDrawable = a.getDrawable(0);

            return new LayerDrawable(drawables);
        } finally {
            a.recycle();
        }
    }

}
