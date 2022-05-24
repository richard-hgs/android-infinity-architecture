package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("UnnecessaryLocalVariable")
public class SystemAppConfigInfo {

    private String packageName;

    private SystemAppConfigInfo() {}

    @NonNull
    public static SystemAppConfigInfo currentApp() {
        SystemAppConfigInfo systemAppConfigInfo = new SystemAppConfigInfo();
        return systemAppConfigInfo;
    }

    @NonNull
    public static SystemAppConfigInfo appPackageName(@NonNull String packageName) {
        SystemAppConfigInfo systemAppConfigInfo = new SystemAppConfigInfo();
        systemAppConfigInfo.packageName = packageName;
        return systemAppConfigInfo;
    }

    @Nullable
    public String getPackageName() {
        return packageName;
    }
}
