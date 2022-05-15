package com.infinity.architecture.views.model;

import androidx.annotation.NonNull;

import com.infinity.architecture.views.enums.EnDrawablePos;

public class CompoundDrawableClickInfo {

    private EnDrawablePos enDrawablePos;

    private void CompoundDrawableClickInfo() { }

    private CompoundDrawableClickInfo getInstance(@NonNull EnDrawablePos enDrawablePos) {
        CompoundDrawableClickInfo compoundDrawableClickInfo = new CompoundDrawableClickInfo();
        compoundDrawableClickInfo.enDrawablePos = enDrawablePos;
        return compoundDrawableClickInfo;
    }

    public EnDrawablePos getDrawablePos() {
        return enDrawablePos;
    }
}
