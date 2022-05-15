package com.infinity.architecture.utils.backservices.api.utils;

import androidx.annotation.NonNull;

import com.infinity.architecture.utils.backservices.api.GsonIfNull;

import java.lang.reflect.Field;

public class GsonIfNullInfo {

    private GsonIfNull gsonIfNull;

    public GsonIfNullInfo(@NonNull Field field, boolean isReadType) {
        if (field.isAnnotationPresent(GsonIfNull.class)) {
            this.gsonIfNull = field.getAnnotation(GsonIfNull.class);
        }
    }

    public boolean dontWriteKeyValInJson() {
        if (
            gsonIfNull != null &&
            gsonIfNull.ifNullDontWriteKeyInJson()
        ) {
            return true;
        } else {
            return false;
        }
    }
}
