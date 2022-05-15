package com.infinity.architecture.utils.string;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;

import androidx.annotation.ColorInt;

public class InsetSpan implements LeadingMarginSpan, ParcelableSpan {
    private final String TAG = "InsetSpan";
    private int mStripWidth = 2;
    private int mGapWidth = 2;
    private final int mColor;
    public InsetSpan() {
        super();
        mColor = 0xff0000ff;
    }
    public InsetSpan(@ColorInt int color, int stripeWidth, int gapWidth) {
        super();
        mColor = color;
        mStripWidth = stripeWidth;
        mGapWidth = gapWidth;
    }
    public InsetSpan(Parcel src) {
        mColor = src.readInt();
    }
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }
    /** @hide */
    public int getSpanTypeIdInternal() {
        //return TextUtils.QUOTE_SPAN;
        return 9;
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }
    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mColor);
    }
    @ColorInt
    public int getColor() {
        return mColor;
    }
    public int getLeadingMargin(boolean first) {
        return mStripWidth + mGapWidth;
    }
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        Log.d(TAG, "text(" + start + "-" + end + "):" + text);
        Paint.Style style = p.getStyle();
        int color = p.getColor();
        p.setStyle(Paint.Style.FILL);
        p.setColor(mColor);
        c.drawRect(x, top, x + dir * mStripWidth, bottom, p);
        p.setStyle(style);
        p.setColor(color);
    }
}
