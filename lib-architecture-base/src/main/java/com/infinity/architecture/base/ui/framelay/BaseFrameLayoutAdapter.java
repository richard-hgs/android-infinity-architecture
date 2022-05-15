package com.infinity.architecture.base.ui.framelay;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.views.adapters.FrameLayoutAdapter;

public class BaseFrameLayoutAdapter extends FrameLayoutAdapter {

    private final String TAG = "BaseFrameLayoutAdapter";

    /*
    add to Buttons to fix the button passing over FrameLayout
    android:stateListAnimator="@null"
     */

    public BaseFrameLayoutAdapter(@NonNull AdapterRequires adapterRequires, int frameLayId) {
        super(
            adapterRequires.getFragChildFragManager() != null ? adapterRequires.getFragChildFragManager() : adapterRequires.getActFragManager(),
            frameLayId
        );

        adapterRequires.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy() {
                Log.d(TAG, "onDestroy");
                BaseFrameLayoutAdapter.this.clearInstances();
                adapterRequires.getLifecycle().removeObserver(this);
            }
        });
    }
}
