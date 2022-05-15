package com.infinity.architecture.utils.adapter.layout_managers;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class WrapGridLayoutManagerFix extends GridLayoutManager {

    public WrapGridLayoutManagerFix(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapGridLayoutManagerFix(Context context, int spanCount) {
        super(context, spanCount);
    }

    public WrapGridLayoutManagerFix(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
}