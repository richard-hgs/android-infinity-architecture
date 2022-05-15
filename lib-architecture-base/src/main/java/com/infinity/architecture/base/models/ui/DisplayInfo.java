package com.infinity.architecture.base.models.ui;

public class DisplayInfo {

    private DisplayInfo() {}

    public static DisplayInfo getClientDisplayInfo() {
        DisplayInfo displayInfo = new DisplayInfo();
        return displayInfo;
    }
}
