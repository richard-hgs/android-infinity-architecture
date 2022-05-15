package com.infinity.architecture.base.models.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;

public class ActivityResultInfo {
    private int resultCode;
    private Intent data;

    private ActivityResultInfo() {

    }

    public static ActivityResultInfo getResultOkInstance(@Nullable Bundle data) {
        ActivityResultInfo activityResultInfo = new ActivityResultInfo();
        activityResultInfo.resultCode = Activity.RESULT_OK;
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtras(data);
            activityResultInfo.data = intent;
        }
        return activityResultInfo;
    }

    public static ActivityResultInfo getResultCancelInstance() {
        ActivityResultInfo activityResultInfo = new ActivityResultInfo();
        activityResultInfo.resultCode = Activity.RESULT_CANCELED;
        return activityResultInfo;
    }

    public static ActivityResultInfo from(ActivityResult activityResult) {
        ActivityResultInfo activityResultInfo = new ActivityResultInfo();
        activityResultInfo.resultCode = activityResult.getResultCode();
        activityResultInfo.data = activityResult.getData();
        return activityResultInfo;
    }

    public int getResultCode() {
        return resultCode;
    }

    @Nullable
    public Intent getData() {
        return data;
    }

    @Nullable
    public Bundle getBundle() {
        if (data != null && data.getExtras() != null) {
            return data.getExtras();
        } else {
            return null;
        }
    }
}
