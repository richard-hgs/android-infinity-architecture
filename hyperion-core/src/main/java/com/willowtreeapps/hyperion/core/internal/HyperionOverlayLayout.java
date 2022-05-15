package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;

import java.util.ArrayList;
import java.util.List;

public class HyperionOverlayLayout extends FrameLayout implements OverlayContainer {

    final List<OnOverlayViewChangedListener> listeners = new ArrayList<>(5);

    public HyperionOverlayLayout(@NonNull Context context) {
        super(context);
        setId(R.id.hyperion_overlay);
        ViewCompat.setImportantForAccessibility(
                this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
    }

    @Override
    public void setOverlayView(@NonNull View view) {
        removeViewIfExists(1);
        addView(view);
        notifyOverlayViewChanged(view);
    }

    @Override
    public void setOverlayView(@LayoutRes int view) {
        removeViewIfExists(1);
        inflate(getContext(), view, this);
        View overlayView = getChildAt(1);
        notifyOverlayViewChanged(overlayView);
    }

    @Nullable
    @Override
    public View getOverlayView() {
        return getChildCount() > 1 ? getChildAt(1) : null;
    }

    @Override
    public boolean removeOverlayView() {
        if (getChildCount() > 1) {
            removeViewIfExists(1);
            notifyOverlayViewChanged(null);
            return true;
        }
        return false;
    }

    @Override
    public void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        return listeners.remove(listener);
    }

    private void notifyOverlayViewChanged(@Nullable View view) {
        for (OnOverlayViewChangedListener listener : listeners) {
            listener.onOverlayViewChanged(view);
        }
    }

    private void removeViewIfExists(int index) {
        if (getChildCount() > index) {
            removeViewAt(index);
        }
    }
}