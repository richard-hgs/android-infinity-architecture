package com.infinity.architecture.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class CustomNavigationView extends NavigationView implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "CustomNavigationView";

    private OnNavigationItemSelectedListener navigationItemSelectedListenerSingle;
    private HashMap<Integer, OnNavigationItemSelectedListener> navigationListeners = new HashMap<>();

    public CustomNavigationView(Context context) {
        super(context);
        initUI();
    }

    public CustomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public CustomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        super.setNavigationItemSelectedListener(this);
    }

    @Override
    public void setNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.navigationItemSelectedListenerSingle = onNavigationItemSelectedListener;
    }

    public void addNavigationItemSelectedListener(@NonNull OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        navigationListeners.put(onNavigationItemSelectedListener.hashCode(), onNavigationItemSelectedListener);
    }

    public void removeNavigationItemSelectedListener(@NonNull OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        navigationListeners.remove(onNavigationItemSelectedListener.hashCode());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        boolean shouldHandle = false;

        if (navigationItemSelectedListenerSingle != null) {
            boolean tempShouldHandle = navigationItemSelectedListenerSingle.onNavigationItemSelected(menuItem);
            if (tempShouldHandle) {
                shouldHandle = true;
            }
        }

        for (Map.Entry<Integer, OnNavigationItemSelectedListener> listenerEntry : navigationListeners.entrySet()) {
            OnNavigationItemSelectedListener listener = listenerEntry.getValue();
            boolean tempShouldCloseMenu = listener.onNavigationItemSelected(menuItem);
            if (tempShouldCloseMenu) {
                shouldHandle = true;
            }
        }
        return shouldHandle;
    }
}
