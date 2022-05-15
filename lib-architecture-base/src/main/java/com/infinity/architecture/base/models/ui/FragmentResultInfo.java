package com.infinity.architecture.base.models.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class FragmentResultInfo implements Parcelable {
    private static final String BUNDLE_FRAG_RESULT_INFO = "bundle_frag_result_info";

    private int resultCode;
    private Intent data;

    private FragmentResultInfo() {

    }

    protected FragmentResultInfo(Parcel in) {
        resultCode = in.readInt();
        data = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<FragmentResultInfo> CREATOR = new Creator<FragmentResultInfo>() {
        @Override
        public FragmentResultInfo createFromParcel(Parcel in) {
            return new FragmentResultInfo(in);
        }

        @Override
        public FragmentResultInfo[] newArray(int size) {
            return new FragmentResultInfo[size];
        }
    };

    public static FragmentResultInfo getResultOkInstance(@Nullable Bundle data) {
        FragmentResultInfo fragmentResultInfo = new FragmentResultInfo();
        fragmentResultInfo.resultCode = Activity.RESULT_OK;
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtras(data);
            fragmentResultInfo.data = intent;
        }
        return fragmentResultInfo;
    }

    public static FragmentResultInfo getResultCancelInstance() {
        FragmentResultInfo fragmentResultInfo = new FragmentResultInfo();
        fragmentResultInfo.resultCode = Activity.RESULT_CANCELED;
        return fragmentResultInfo;
    }

    public int getResultCode() {
        return resultCode;
    }

    @Nullable
    public Intent getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resultCode);
        dest.writeParcelable(data, flags);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_FRAG_RESULT_INFO, this);

        return bundle;
    }

    @Nullable
    public static FragmentResultInfo fromBundle(@Nullable Bundle bundle) {
        FragmentResultInfo fragmentResultInfo = null;
        if (bundle != null && bundle.containsKey(BUNDLE_FRAG_RESULT_INFO)) {
            fragmentResultInfo = bundle.getParcelable(BUNDLE_FRAG_RESULT_INFO);
        }
        return fragmentResultInfo;
    }

    @Override
    public String toString() {
        return "FragmentResultInfo{" +
                "resultCode=" + resultCode +
                ", data=" + data +
                '}';
    }
}
