package com.infinity.architecture.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class CustomBottomNavigationView extends BottomNavigationView implements NavigationBarView.OnItemSelectedListener {

    private final String TAG = "CustBottNavigationView";

    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListenerSingle;
    private HashMap<Integer, NavigationBarView.OnItemSelectedListener> navigationListeners = new HashMap<>();

    public CustomBottomNavigationView(@NonNull Context context) {
        super(context);
        initUI();
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI();
    }

    private void initUI() {
        super.setOnItemSelectedListener(this);
    }

    @Override
    public void setOnItemSelectedListener(@Nullable NavigationBarView.OnItemSelectedListener listener) {
        navigationItemSelectedListenerSingle = listener;
    }

    public void addOnItemSelectedListener(@NonNull NavigationBarView.OnItemSelectedListener onNavigationItemSelectedListener) {
        navigationListeners.put(onNavigationItemSelectedListener.hashCode(), onNavigationItemSelectedListener);
    }

    public void removeOnItemSelectedListener(@NonNull NavigationBarView.OnItemSelectedListener onNavigationItemSelectedListener) {
        navigationListeners.remove(onNavigationItemSelectedListener.hashCode());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean shouldHandle = false;

        if (navigationItemSelectedListenerSingle != null) {
            boolean tempShouldCloseMenu = navigationItemSelectedListenerSingle.onNavigationItemSelected(item);
            if (tempShouldCloseMenu) {
                shouldHandle = true;
            }
        }

        for (Map.Entry<Integer, NavigationBarView.OnItemSelectedListener> listenerEntry : navigationListeners.entrySet()) {
            NavigationBarView.OnItemSelectedListener listener = listenerEntry.getValue();
            boolean tempShouldCloseMenu = listener.onNavigationItemSelected(item);
            if (tempShouldCloseMenu) {
                shouldHandle = true;
            }
        }
        return shouldHandle;
    }
}
