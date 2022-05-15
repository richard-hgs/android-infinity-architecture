package com.infinity.architecture.utils.string;

import android.graphics.Rect;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CustomRelativeSizeSpan extends MetricAffectingSpan {

    private final String TAG = "CustomRelativeSizeSpan";

    private final int verticalAlign;
    private final float relativeSize;

    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_TOP = 2;

    @IntDef(flag = true, value = {
        ALIGN_BOTTOM,
        ALIGN_CENTER,
        ALIGN_TOP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface VerticalAlignment {}

    public CustomRelativeSizeSpan(@VerticalAlignment int verticalAlign, float relativeSize) {
        this.verticalAlign = verticalAlign;
        this.relativeSize = relativeSize;
    }

    public float getSizeChange() {
        return relativeSize;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        updateAnyState(ds);
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        updateAnyState(ds);
    }

    private void updateAnyState(TextPaint ds) {
        Rect bounds = new Rect();
        ds.getTextBounds("1A", 0, 2, bounds);
        int shift = bounds.top - bounds.bottom;
        ds.setTextSize(ds.getTextSize() * relativeSize);
        ds.getTextBounds("1A", 0, 2, bounds);
        if (verticalAlign > 0) {
            if (verticalAlign == 2) {
                shift += bounds.bottom - bounds.top;
            } else if (verticalAlign == 1) {
                shift += bounds.bottom - bounds.top;
                shift -= shift / 2;
            }
            ds.baselineShift += shift;
        }
    }
}
