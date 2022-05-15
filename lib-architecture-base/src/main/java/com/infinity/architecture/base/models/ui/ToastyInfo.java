package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.enums.ui.ToastyLength;
import com.infinity.architecture.base.enums.ui.ToastyType;


public class ToastyInfo {
    private ToastyType toastyType;
    private ToastyLength toastyLength;
    private String message;

    public ToastyInfo(@NonNull ToastyType toastyType, @NonNull ToastyLength toastyLength, @NonNull String message) {
        this.toastyType = toastyType;
        this.toastyLength = toastyLength;
        this.message = message;
    }

    public static ToastyInfo all(@NonNull ToastyType toastyType, @NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(toastyType, toastyLength, message);
    }

    public static ToastyInfo defaul(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.DEFAULT, toastyLength, message);
    }

    public static ToastyInfo normal(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.NORMAL, toastyLength, message);
    }

    public static ToastyInfo warning(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.WARNING, toastyLength, message);
    }

    public static ToastyInfo info(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.INFO, toastyLength, message);
    }

    public static ToastyInfo error(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.ERROR, toastyLength, message);
    }

    public static ToastyInfo success(@NonNull ToastyLength toastyLength, @NonNull String message) {
        return new ToastyInfo(ToastyType.SUCCESS, toastyLength, message);
    }

    @NonNull
    public ToastyType getToastyType() {
        return toastyType;
    }

    @NonNull
    public ToastyLength getToastyLength() {
        return toastyLength;
    }

    @NonNull
    public String getMessage() {
        return message;
    }
}
