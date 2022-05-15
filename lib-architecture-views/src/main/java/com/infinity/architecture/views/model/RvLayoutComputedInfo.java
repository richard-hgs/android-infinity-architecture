package com.infinity.architecture.views.model;

public class RvLayoutComputedInfo {

    private int childHeightSum;
    private int verticalScrollRange;

    private RvLayoutComputedInfo() {}

    public static RvLayoutComputedInfo getInstance(int childHeightSum, int verticalScrollRange) {
        RvLayoutComputedInfo rvLayoutComputedInfo = new RvLayoutComputedInfo();
        rvLayoutComputedInfo.childHeightSum = childHeightSum;
        rvLayoutComputedInfo.verticalScrollRange = verticalScrollRange;
        return rvLayoutComputedInfo;
    }

    public int getChildHeightSum() {
        return childHeightSum;
    }

    public int getVerticalScrollRange() {
        return verticalScrollRange;
    }

    @Override
    public String toString() {
        return "RvLayoutComputedInfo{" +
                "childHeightSum=" + childHeightSum +
                ", verticalScrollRange=" + verticalScrollRange +
                '}';
    }
}
