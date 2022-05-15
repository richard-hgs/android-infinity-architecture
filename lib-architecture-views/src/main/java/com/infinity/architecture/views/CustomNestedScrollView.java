package com.infinity.architecture.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomNestedScrollView extends NestedScrollView {

    /**
     * VALIDA SE O SCROLL ESTÁ SENDO TOCADO NO EXATO MOMENTO
     */
    private boolean currentlyTouching;

    /**
     * LISTENER DO SCROLL CUSTOMIZADO
     */
    private CustomScrollListener customScrollListener;

    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentlyTouching = true;
            case MotionEvent.ACTION_UP:
                currentlyTouching = false;

        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);

        if (scrollY > oldScrollY) {
            //Log.d(TAG, "Scroll DOWN");
        }
        if (scrollY < oldScrollY) {
            //Log.d(TAG, "Scroll UP");
        }

        if (scrollY == 0) {
            //Log.d(TAG, "TOP SCROLL");
        }

        if (scrollY == (getMeasuredHeight() - getChildAt(0).getMeasuredHeight())) {
            //Log.d(TAG, "BOTTOM SCROLL");
        }
    }

    public interface CustomScrollListener {

    }
}
