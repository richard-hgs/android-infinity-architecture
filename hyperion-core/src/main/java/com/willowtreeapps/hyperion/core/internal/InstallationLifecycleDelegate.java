package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;

import javax.inject.Inject;

@AppScope
class InstallationLifecycleDelegate extends LifecycleDelegate {

    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final CoreComponentContainer container;
    private final ApplicationInstaller applicationInstaller;

    @Inject
    InstallationLifecycleDelegate(CoreComponentContainer container,
                                  ApplicationInstaller applicationInstaller) {
        this.container = container;
        this.applicationInstaller = applicationInstaller;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        applicationInstaller.installIfNeeded();

        // reorganize the layout
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final View contentView = decorView.getChildAt(0);
        if (decorView.getChildCount() < 1) {
            // no content, abort install
            return;
        }
        decorView.removeView(contentView);

        // embed content view within overlay
        final HyperionOverlayLayout overlayLayout = new HyperionOverlayLayout(activity);
        overlayLayout.addView(contentView);

        // embed overlay + content within menu
        final HyperionMenuLayout menuLayout = new HyperionMenuLayout(activity);
        decorView.addView(menuLayout);

        FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);

        ActivityResults activityResults = fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
        if (activityResults == null) {
            activityResults = fragmentManager.isSupport() ? new ActivityResultsSupportFragment() : new ActivityResultsFragment();
            fragmentManager.beginTransaction()
                .add(activityResults, ACTIVITY_RESULT_TAG)
                .commit();
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance(activity))
                .activity(activity)
                .pluginSource(container.getPluginSource())
                .overlayContainer(overlayLayout)
                .activityResults(activityResults)
                .build();

        container.putComponent(activity, component);

        // embed plugins list into menu
        final Context coreContext = new ComponentContextThemeWrapper(activity, component);
        final HyperionPluginView pluginView = new HyperionPluginView(coreContext);
        pluginView.setAlpha(0.0f);
        menuLayout.addView(pluginView);
        menuLayout.addView(overlayLayout);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        container.removeComponent(activity);
    }

    public static boolean hasSoftKeys(@NonNull WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static int getSoftKeysHeight(@NonNull WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 ? (realWidth - displayWidth) : Math.max((realHeight - displayHeight), 0);
    }
}