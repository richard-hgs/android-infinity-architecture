package com.willowtreeapps.hyperion.attr.collectors;

import android.annotation.SuppressLint;

import com.willowtreeapps.hyperion.attr.AttributeValue;

import static android.view.Gravity.*;

public class GravityValue implements AttributeValue {

    private final int gravity;

    public GravityValue(int gravity) {
        this.gravity = gravity;
    }

    int getGravity() {
        return this.gravity;
    }

    @Override
    @SuppressLint("RtlHardcoded")
    public CharSequence getDisplayValue() {
        StringBuilder sb = new StringBuilder();
        if ((gravity & START) == START) {
            sb.append("Start|");
        }
        if ((gravity & LEFT) == LEFT) {
            sb.append("Left|");
        }
        if ((gravity & END) == END) {
            sb.append("End|");
        }
        if ((gravity & RIGHT) == RIGHT) {
            sb.append("Right|");
        }
        if ((gravity & TOP) == TOP) {
            sb.append("Top|");
        }
        if ((gravity & BOTTOM) == BOTTOM) {
            sb.append("Bottom|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}