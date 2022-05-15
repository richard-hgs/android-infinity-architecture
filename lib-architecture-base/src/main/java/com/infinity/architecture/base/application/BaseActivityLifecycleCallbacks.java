package com.infinity.architecture.base.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private final String TAG = "BaseActLifecyCallbacks";

    private final boolean DEBUG_LIFECYCLE = true;

    private ConcurrentHashMap<Class<? extends Activity>, ConcurrentHashMap<Integer, ActivityState>> activitiesStates = new ConcurrentHashMap<>();

    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    private LifecycleEventListener lifecycleEventListener;

    public BaseActivityLifecycleCallbacks(LifecycleEventListener lifecycleEventListener) {
        this.lifecycleEventListener = lifecycleEventListener;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        addOrUpdateActivityState(activity.getClass(), ActivityState.CREATED);

        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_CREATE");
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        ++started;
        addOrUpdateActivityState(activity.getClass(), ActivityState.STARTED);

        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_START");
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ++resumed;
        addOrUpdateActivityState(activity.getClass(), ActivityState.RESUMED);

        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_RESUME");
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        ++paused;
        addOrUpdateActivityState(activity.getClass(), ActivityState.PAUSED);

        Log.d(TAG, "application is in foreground: " + (resumed > paused));
        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_PAUSE");
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        ++stopped;
        addOrUpdateActivityState(activity.getClass(), ActivityState.STOPPED);

        Log.d(TAG, "application is visible: " + (started > stopped));

        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_STOP");
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_SAVE_INSTANCE");
        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        addOrUpdateActivityState(activity.getClass(), ActivityState.DESTROYED);
        if (DEBUG_LIFECYCLE) {
            Log.d(TAG, activity.getClass().getSimpleName() + " -> ON_DESTROY");
        }
    }

    public void addOrUpdateActivityState(Class<? extends Activity> mClass, ActivityState state) {
        ConcurrentHashMap<Integer, ActivityState> activityInstancesStateMap = activitiesStates.get(mClass);
        if (activityInstancesStateMap == null) {
            activityInstancesStateMap = new ConcurrentHashMap<>();
        }

        if (activityInstancesStateMap.size() > 3) {
            for (Map.Entry<Integer, ActivityState> entrie : activityInstancesStateMap.entrySet()) {
                if (entrie.getValue() == ActivityState.DESTROYED) {
                    activityInstancesStateMap.remove(entrie.getKey());
                }
            }
        }

        activityInstancesStateMap.put(mClass.hashCode(), state);

        activitiesStates.put(mClass, activityInstancesStateMap);

        lifecycleEventListener.onLifecycleEvent(activitiesStates, ((started == stopped)));
    }

    public interface LifecycleEventListener {
        void onLifecycleEvent(@NonNull ConcurrentHashMap<Class<? extends Activity>, ConcurrentHashMap<Integer, ActivityState>> activitiesStates, boolean appIsInBackground);
    }

    public enum ActivityState {
        UNDEFINED,
        CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        DESTROYED
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    /*
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
    */
}
