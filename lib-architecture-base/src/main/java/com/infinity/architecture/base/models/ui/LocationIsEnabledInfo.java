package com.infinity.architecture.base.models.ui;

public class LocationIsEnabledInfo {
    private boolean gpsEnabled;
    private boolean networkEnabled;

    private LocationIsEnabledInfo() {

    }

    public static LocationIsEnabledInfo getInstance(boolean gpsEnabled, boolean networkEnabled) {
        LocationIsEnabledInfo locationIsEnabledInfo = new LocationIsEnabledInfo();
        locationIsEnabledInfo.gpsEnabled = gpsEnabled;
        locationIsEnabledInfo.networkEnabled = networkEnabled;

        return locationIsEnabledInfo;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public boolean isNetworkEnabled() {
        return networkEnabled;
    }
}
