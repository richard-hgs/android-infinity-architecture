package com.infinity.architecture.views.model;

import android.view.Gravity;

import androidx.annotation.IntDef;
import androidx.core.view.GravityCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerStateInfo {
    private boolean open;
    private int gravity;

    private DrawerStateInfo() {}

    @IntDef(value = {Gravity.LEFT, Gravity.RIGHT, GravityCompat.START, GravityCompat.END,
            Gravity.NO_GRAVITY}, flag = true)
    @Retention(RetentionPolicy.SOURCE)
    private @interface EdgeGravity {}

    public static DrawerStateInfo open(@EdgeGravity int gravity) {
        DrawerStateInfo drawerStateInfo = new DrawerStateInfo();
        drawerStateInfo.open = true;
        drawerStateInfo.gravity = gravity;
        return drawerStateInfo;
    }

    public static DrawerStateInfo close(@EdgeGravity int gravity) {
        DrawerStateInfo drawerStateInfo = new DrawerStateInfo();
        drawerStateInfo.open = false;
        drawerStateInfo.gravity = gravity;
        return drawerStateInfo;
    }

    public boolean isOpen() {
        return open;
    }

    @EdgeGravity
    public int getGravity() {
        return gravity;
    }

    @Override
    public String toString() {
        return "DrawerStateInfo{" +
                "open=" + open +
                ", gravity=" + gravity +
                '}';
    }
}
