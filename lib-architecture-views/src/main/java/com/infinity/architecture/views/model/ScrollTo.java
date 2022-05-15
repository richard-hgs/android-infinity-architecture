package com.infinity.architecture.views.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScrollTo {
    public static final int SIDE_BOTTOM = 1;
    public static final int SIDE_TOP = 2;

    private Boolean smoothScroll;
    private Integer x;
    private Integer y;

    private Integer position;

    private Integer side;

    private ScrollTo() {}

    public static ScrollTo getInstance(@Nullable Integer side, @Nullable Boolean smoothScroll) {
        ScrollTo scrollTo = new ScrollTo();
        scrollTo.side = side;
        scrollTo.smoothScroll = smoothScroll;
        return scrollTo;
    }

    public static ScrollTo getInstance(@Nullable Integer x, @Nullable Integer y, @Nullable Boolean smoothScroll) {
        ScrollTo scrollTo = new ScrollTo();
        scrollTo.x = x;
        scrollTo.y = y;
        scrollTo.smoothScroll = smoothScroll;
        return scrollTo;
    }

    @NonNull
    public static ScrollTo getInstance2(@Nullable Integer position, @Nullable Boolean smoothScroll) {
        ScrollTo scrollTo = new ScrollTo();
        scrollTo.position = position;
        scrollTo.smoothScroll = smoothScroll;

        return scrollTo;
    }

    @Nullable
    public Boolean getSmoothScroll() {
        return smoothScroll;
    }

    @Nullable
    public Integer getX() {
        return x;
    }

    @Nullable
    public Integer getY() {
        return y;
    }

    public Integer getSide() {
        return side;
    }

    public Integer getPosition() {
        return position;
    }
}
