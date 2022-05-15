package com.infinity.architecture.sampleapp.backservices;

import com.infinity.architecture.base.ArchitectureBaseApplication;

import dagger.hilt.android.HiltAndroidApp;

// Hilt Entry point required
@HiltAndroidApp
public class AppApplication extends ArchitectureBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
