package com.willowtreeapps.hyperion.plugin.v1;

import android.graphics.Rect;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import android.view.View;

@MainThread
public interface MeasurementHelper {

    void getParentRelativeRect(@NonNull View view, @NonNull Rect rect);

    int getRelativeLeft(@NonNull View view);

    int getRelativeTop(@NonNull View view);

    int getRelativeRight(@NonNull View view);

    int getRelativeBottom(@NonNull View view);

    void getScreenLocation(@NonNull View view, Rect rect);

    @Px
    int toPx(int dp);

    int toDp(@Px int px);

    int toSp(float px);

}