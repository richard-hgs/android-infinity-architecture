package com.willowtreeapps.hyperion.sqlite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    // VARIAVEIS
    private boolean scrollEnable;

    // CONSTRUTORES
    public CustomScrollView(Context context) {
        super(context);
        scrollEnable = true;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollEnable = true;
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scrollEnable = true;
    }

    // MÉTODOS
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                return scrollEnable && super.onTouchEvent(ev);
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollEnable && super.onInterceptTouchEvent(ev);
    }

    // GETTERS AND SETTERS
    public boolean isScrollEnable() {
        return scrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }
}
