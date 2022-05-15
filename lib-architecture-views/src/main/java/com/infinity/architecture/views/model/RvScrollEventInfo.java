package com.infinity.architecture.views.model;

import androidx.annotation.NonNull;

public class RvScrollEventInfo {
    private int xOld;
    private int yOld;

    private int x;
    private int y;

    private RvScrollEventInfo() {}

    @NonNull
    public static RvScrollEventInfo getInstance(int xOld, int yOld, int x, int y) {
        RvScrollEventInfo rvScrollEventInfo = new RvScrollEventInfo();
        rvScrollEventInfo.xOld = xOld;
        rvScrollEventInfo.yOld = yOld;
        rvScrollEventInfo.x = x;
        rvScrollEventInfo.y = y;
        return rvScrollEventInfo;
    }

    public int getxOld() {
        return xOld;
    }

    public int getyOld() {
        return yOld;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "RvScrollEventInfo{" +
                "dxOld=" + xOld +
                ", dyOld=" + yOld +
                ", dx=" + x +
                ", dy=" + y +
                '}';
    }
}
