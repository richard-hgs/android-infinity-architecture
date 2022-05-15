package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;

public class PermissionInfo {

    private String strPermission;
    private int intPermission;

    public PermissionInfo(@NonNull String strPermission, int intPermission) {
        this.strPermission = strPermission;
        this.intPermission = intPermission;
    }

    @NonNull
    public String getStrPermission() {
        return strPermission;
    }

    public void setStrPermission(@NonNull String strPermission) {
        this.strPermission = strPermission;
    }

    public int getIntPermission() {
        return intPermission;
    }

    public void setIntPermission(int intPermission) {
        this.intPermission = intPermission;
    }
}
