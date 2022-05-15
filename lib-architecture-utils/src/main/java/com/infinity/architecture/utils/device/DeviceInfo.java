package com.infinity.architecture.utils.device;

import com.google.gson.annotations.SerializedName;


public class DeviceInfo {
    @SerializedName("screen_width")
    private int screenWidth;
    @SerializedName("screen_height")
    private int screenHeight;
    @SerializedName("screen_density")
    private float screenDensity;
    @SerializedName("screen_dpi")
    private int densityDpi;
    @SerializedName("screen_inches")
    private double screenInches;

    @SerializedName("app_name")
    private String appName;
    @SerializedName("app_package_name")
    private String appPackageName;
    @SerializedName("app_version_name")
    private String appVersionName;
    @SerializedName("app_version_code")
    private int appVersionCode;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("brand")
    private String brand;
    @SerializedName("model")
    private String model;
    @SerializedName("board")
    private String board;
    @SerializedName("hardware")
    private String hardware;
    @SerializedName("serial_num")
    private String serialNum;
    @SerializedName("uid")
    private String uid;
    @SerializedName("android_id")
    private String android_id;
    @SerializedName("screen_resolution")
    private String screenResolution;
    // String screenSize = rounded + " Inches";
    @SerializedName("str_screen_density")
    private String strScreenDensity;
    @SerializedName("bootloader")
    private String bootloader;
    @SerializedName("user")
    private String user;
    @SerializedName("host")
    private String host;
    @SerializedName("version")
    private String version;
    @SerializedName("android_api_lvl")
    private String androidApiLvl;
    @SerializedName("build_id")
    private String buildId;
    @SerializedName("build_time")
    private String buildTime;
    @SerializedName("fingerprint")
    private String fingerPrint;

    private DeviceInfo(){}

    public static DeviceInfo getInstance(
        int screenWidth,
        int screenHeight,
        float screenDensity,
        int densityDpi,
        double screenInches,
        String appName,
        String appPackageName,
        String appVersionName,
        int appVersionCode,
        String manufacturer,
        String brand,
        String model,
        String board,
        String hardware,
        String serialNum,
        String uid,
        String android_id,
        String screenResolution,
        String strScreenDensity,
        String bootloader,
        String user,
        String host,
        String version,
        String androidApiLvl,
        String buildId,
        String buildTime,
        String fingerPrint
    ) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.screenWidth = screenWidth;
        deviceInfo.screenHeight = screenHeight;
        deviceInfo.screenDensity = screenDensity;
        deviceInfo.densityDpi = densityDpi;
        deviceInfo.screenInches = screenInches;
        deviceInfo.appName = appName;
        deviceInfo.appPackageName = appPackageName;
        deviceInfo.appVersionName = appVersionName;
        deviceInfo.appVersionCode = appVersionCode;
        deviceInfo.manufacturer = manufacturer;
        deviceInfo.brand = brand;
        deviceInfo.model = model;
        deviceInfo.board = board;
        deviceInfo.hardware = hardware;
        deviceInfo.serialNum = serialNum;
        deviceInfo.uid = uid;
        deviceInfo.android_id = android_id;
        deviceInfo.screenResolution = screenResolution;
        deviceInfo.strScreenDensity = strScreenDensity;
        deviceInfo.bootloader = bootloader;
        deviceInfo.user = user;
        deviceInfo.host = host;
        deviceInfo.version = version;
        deviceInfo.androidApiLvl = androidApiLvl;
        deviceInfo.buildId = buildId;
        deviceInfo.buildTime = buildTime;
        deviceInfo.fingerPrint = fingerPrint;

        return deviceInfo;
    }


    @Override
    public String toString() {
        return "DeviceInfo{" +
                "screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", screenDensity=" + screenDensity +
                ", densityDpi=" + densityDpi +
                ", screenInches=" + screenInches +
                ", appName='" + appName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", board='" + board + '\'' +
                ", hardware='" + hardware + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", uid='" + uid + '\'' +
                ", android_id='" + android_id + '\'' +
                ", screenResolution='" + screenResolution + '\'' +
                ", strScreenDensity='" + strScreenDensity + '\'' +
                ", bootloader='" + bootloader + '\'' +
                ", user='" + user + '\'' +
                ", host='" + host + '\'' +
                ", version='" + version + '\'' +
                ", androidApiLvl='" + androidApiLvl + '\'' +
                ", buildId='" + buildId + '\'' +
                ", buildTime='" + buildTime + '\'' +
                ", fingerPrint='" + fingerPrint + '\'' +
                '}';
    }
}
