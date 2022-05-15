package com.infinity.architecture.utils.string;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.SuperscriptSpan;
import android.util.Log;

import androidx.annotation.NonNull;

public class TopAlignSuperscriptSpan extends SuperscriptSpan implements ParcelableSpan {
    private final String TAG = "TopAlignSuperscriptSpan";

    //divide superscript by this number
    protected int fontScale = 2;

    //shift value, 0 to 1.0
    protected float shiftPercentage = 0;

    //align 0=BOTTOM, 1=CENTER, 2=TOP
    protected int verticalAlign = 0;

    //doesn't shift
    public TopAlignSuperscriptSpan() {}

    //sets the shift percentage
    public TopAlignSuperscriptSpan(int verticalAlign, float relativeTextSize) {
        this.verticalAlign = verticalAlign;
        if(relativeTextSize > 0.0 && relativeTextSize < 1.0) {
            this.shiftPercentage = relativeTextSize;
        }
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        // original ascent
        float oldAscent = tp.ascent();
        float oldFontHeight = tp.getFontMetrics().ascent + tp.getFontMetrics().descent;

        Log.d(TAG, "tp.top:" + tp.getFontMetrics().top);
        Log.d(TAG, "tp.bottom: " + tp.getFontMetrics().bottom);
        Log.d(TAG, "tp.ascent: " + tp.getFontMetrics().ascent);
        Log.d(TAG, "tp.descent: " + tp.getFontMetrics().descent);
        Log.d(TAG, "oldFontHeight: " + oldFontHeight);

        // scale down the font
        tp.setTextSize(tp.getTextSize() / fontScale);

        if (verticalAlign > 0) {
            // get the new font ascent
            float newAscent = tp.getFontMetrics().ascent;
            float newFontHeight = tp.getFontMetrics().ascent - tp.getFontMetrics().descent;

            int shiftToAdd = (int) ((oldAscent * shiftPercentage) - (newAscent * shiftPercentage));

            Log.d(TAG, "ascent:" + oldAscent);
            Log.d(TAG, "newAscent:" + newAscent);

            Log.d(TAG, "new tp.top:" + tp.getFontMetrics().top);
            Log.d(TAG, "new tp.bottom: " + tp.getFontMetrics().bottom);
            Log.d(TAG, "new tp.ascent: " + tp.getFontMetrics().ascent);
            Log.d(TAG, "new tp.descent: " + tp.getFontMetrics().descent);
            Log.d(TAG, "newfontHeight: " + newFontHeight);

            Log.d(TAG, "curShift: " + tp.baselineShift);
            Log.d(TAG, "siftToAdd: " + shiftToAdd);
            Log.d(TAG, "-------------------------------------------------------------------");


            //move baseline to top of old font, then move down size of new font
            //adjust for errors with shift percentage
            tp.baselineShift += shiftToAdd;
        }
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        updateDrawState(tp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(@NonNull Parcel dest, int flags) {
    }
}
