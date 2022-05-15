package com.willowtreeapps.hyperion.plugin.v1;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

public interface PluginExtension {

    @NonNull
    Activity getActivity();

    @NonNull
    ViewGroup getContentRoot();

    @NonNull
    OverlayContainer getOverlayContainer();

    @NonNull
    ActivityResults getActivityResults();

    @NonNull
    AttributeTranslator getAttributeTranslator();

    @NonNull
    MeasurementHelper getMeasurementHelper();

    @Nullable
    HyperionMenu getHyperionMenu();

}