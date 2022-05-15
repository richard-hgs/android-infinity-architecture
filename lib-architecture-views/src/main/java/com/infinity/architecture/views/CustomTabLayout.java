package com.infinity.architecture.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.views.adapters.TabLayoutAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class CustomTabLayout extends TabLayout implements TabLayout.OnTabSelectedListener {

    private TabLayoutMediator tabLayoutMediator;
    private TabLayoutAdapter<?> tabLayoutAdapter;
    private OnTabSelectedListener onMyTabSelectedListener;

    private final ConcurrentHashMap<Integer, OnTabSelectedListener> onTabSelectedListeners = new ConcurrentHashMap<>();

    public CustomTabLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        super.addOnTabSelectedListener(this);
    }

    @Override
    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        onTabSelectedListeners.put(listener.hashCode(), listener);
        // super.addOnTabSelectedListener(listener);
    }

    @Override
    public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        onTabSelectedListeners.remove(listener.hashCode());
        // super.removeOnTabSelectedListener(listener);
    }

    @Override
    public void onTabSelected(Tab tab) {
        if (onMyTabSelectedListener != null) {
            onMyTabSelectedListener.onTabSelected(tab);
        }

        for (Map.Entry<Integer, TabLayout.OnTabSelectedListener> listenerEntry : onTabSelectedListeners.entrySet()) {
            listenerEntry.getValue().onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(Tab tab) {
        if (onMyTabSelectedListener != null) {
            onMyTabSelectedListener.onTabUnselected(tab);
        }
        for (Map.Entry<Integer, TabLayout.OnTabSelectedListener> listenerEntry : onTabSelectedListeners.entrySet()) {
            listenerEntry.getValue().onTabUnselected(tab);
        }
    }

    @Override
    public void onTabReselected(Tab tab) {
        if (onMyTabSelectedListener != null) {
            onMyTabSelectedListener.onTabReselected(tab);
        }
        for (Map.Entry<Integer, TabLayout.OnTabSelectedListener> listenerEntry : onTabSelectedListeners.entrySet()) {
            listenerEntry.getValue().onTabReselected(tab);
        }
    }

    @Nullable
    public TabLayoutMediator getTabLayoutMediator() {
        return tabLayoutMediator;
    }

    public void setTabLayoutMediator(@Nullable TabLayoutMediator tabLayoutMediator) {
        this.tabLayoutMediator = tabLayoutMediator;
    }

    public TabLayoutAdapter<?> getAdapter() {
        return tabLayoutAdapter;
    }

    public void setAdapter(TabLayoutAdapter<?> tabLayoutAdapter) {
        this.tabLayoutAdapter = tabLayoutAdapter;
    }

    public OnTabSelectedListener getOnMyTabSelectedListener() {
        return onMyTabSelectedListener;
    }

    public void setOnMyTabSelectedListener(OnTabSelectedListener onMyTabSelectedListener) {
        this.onMyTabSelectedListener = onMyTabSelectedListener;
    }
}
