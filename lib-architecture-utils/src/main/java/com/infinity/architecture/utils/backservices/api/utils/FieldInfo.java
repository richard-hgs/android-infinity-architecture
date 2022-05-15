package com.infinity.architecture.utils.backservices.api.utils;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

public class FieldInfo {

    private Field field;
    private GsonReadWriteInfo gsonReadWriteInfo;

    private FieldInfo() {}

    @NonNull
    public static FieldInfo getInstance(@NonNull Field field, @NonNull GsonReadWriteInfo gsonReadWriteInfo) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.field = field;
        fieldInfo.gsonReadWriteInfo = gsonReadWriteInfo;
        return fieldInfo;
    }

    public Field getField() {
        return field;
    }

    public GsonReadWriteInfo getGsonReadWriteInfo() {
        return gsonReadWriteInfo;
    }
}
