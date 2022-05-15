package com.infinity.architecture.base.models.ui;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class ActivityAllBundle implements Parcelable {
    public static final String BUNDLE_KEY = "activity_all_bundle";

    @IdRes
    private int startScreenId;
    @Nullable
    private Bundle bundleArgs;

    private ActivityAllBundle() {

    }

    protected ActivityAllBundle(Parcel in) {
        startScreenId = in.readInt();
        bundleArgs = in.readBundle(getClass().getClassLoader());
    }

    public static ActivityAllBundle getInstance(@IdRes int startScreenId, @Nullable Bundle bundleArgs) {
        ActivityAllBundle activityAllBundle = new ActivityAllBundle();
        activityAllBundle.startScreenId = startScreenId;
        activityAllBundle.bundleArgs = bundleArgs;
        return activityAllBundle;
    }

    public static final Creator<ActivityAllBundle> CREATOR = new Creator<ActivityAllBundle>() {
        @Override
        public ActivityAllBundle createFromParcel(Parcel in) {
            return new ActivityAllBundle(in);
        }

        @Override
        public ActivityAllBundle[] newArray(int size) {
            return new ActivityAllBundle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(startScreenId);
        dest.writeBundle(bundleArgs);
    }

    @IdRes
    public int getStartScreenId() {
        return startScreenId;
    }
    @Nullable
    public Bundle getBundleArgs() {
        return bundleArgs;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, this);
        return bundle;
    }

    public static ActivityAllBundle fromBundle(@Nullable Bundle bundle) {
        ActivityAllBundle activityAllBundle = null;
        if (bundle != null && bundle.containsKey(BUNDLE_KEY)) {
            activityAllBundle = bundle.getParcelable(BUNDLE_KEY);
        }
        return activityAllBundle;
    }

    @Override
    public String toString() {
        return "ActivityAllBundle{" +
                "startScreenId=" + startScreenId +
                ", bundleArgs=" + bundleArgs +
            '}';
    }
}
