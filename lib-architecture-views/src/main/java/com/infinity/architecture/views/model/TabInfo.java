package com.infinity.architecture.views.model;

public class TabInfo {
    private int index;

    private TabInfo() { }

    public static TabInfo getInstance(int index) {
        TabInfo tabInfo = new TabInfo();
        tabInfo.index = index;
        return tabInfo;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "TabInfo{" +
                "index=" + index +
                '}';
    }
}
