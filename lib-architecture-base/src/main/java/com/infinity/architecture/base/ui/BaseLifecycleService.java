package com.infinity.architecture.base.ui;

import android.app.Service;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseLifecycleService extends Service implements LifecycleOwner {

    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean isServiceDestroyed;

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @NonNull
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isServiceDestroyed) {
            Lifecycle lifecycle = getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.CREATED);
                ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.STARTED);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isServiceDestroyed = true;
        compositeDisposable.dispose();

        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.DESTROYED);
        }
        super.onDestroy();
    }
}
