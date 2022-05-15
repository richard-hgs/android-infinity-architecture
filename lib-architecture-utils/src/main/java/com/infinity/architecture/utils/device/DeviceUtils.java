package com.infinity.architecture.utils.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    /**
     * Get device imei request permission if needed
     *
     * @param activity {@link Activity}
     * @return {@link String} or null if no imei found
     */
    @Nullable
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getImei(Activity activity) {
        String deviceId = null;

        final int MY_PERMISSIONS_REQUEST = 0;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            assert mTelephony != null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceId = Settings.Secure.getString(
                        activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
        } else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST);
            }
        }

        if (deviceId != null) {
            Log.d(TAG, "imei:" + deviceId);
            return deviceId;
        } else {
            return null;
        }
    }

    /**
     * Get device imei without requesting permission
     *
     * @param context {@link Context}
     * @return {@link String} or null if no imei found
     */
    @Nullable
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getImei(Context context) {
        String deviceId = null;

        final int MY_PERMISSIONS_REQUEST = 0;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            assert mTelephony != null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
        }

        if (deviceId != null) {
            Log.d(TAG, "imei:" + deviceId);
            return deviceId;
        } else {
            return null;
        }
    }

    /**
     * Get android version name (e.g. 4.4.2)
     *
     * @return {@link String} Android version name
     */
    public static String getAndroidVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Get android version code (e.g. 19)
     *
     * @return {@link String} Android version code
     */
    public static int getAndroidVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Get android model name (e.g. Nexus 6P)
     *
     * @return {@link String} device model name
     */
    public static String getAndroidModel() {
        return Build.MANUFACTURER;
    }

    /**
     * Get all information about current device and running application
     *
     * @param context   Context
     * @return {@link DeviceInfo} Device information
     * @throws Exception    Exception if exists
     */
    public static DeviceInfo getDeviceInformation(@NonNull Context context) throws Exception {
        String packageName = context.getPackageName();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float screenDensity = metrics.density;
        int densityDpi = metrics.densityDpi;

        double x = Math.pow(screenWidth / metrics.xdpi, 2);
        double y = Math.pow(screenHeight / metrics.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        //  rounded = df2.format(screenInches);

        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        ApplicationInfo app = pm.getApplicationInfo(packageName, 0);

        // Drawable icon = packageManager.getApplicationIcon(app);
        String appName = pm.getApplicationLabel(app).toString();
        String appPackageName = pInfo.packageName;
        String appVersionName = pInfo.versionName;
        int appVersionCode = pInfo.versionCode;
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String model = Build.MODEL;
        String board = Build.BOARD;
        String hardware = Build.HARDWARE;
        String serialNum = Build.SERIAL;
        String uid = String.valueOf(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String screenResolution = screenWidth + " * " + screenHeight + " Pixels";
        // String screenSize = rounded + " Inches";
        String strScreenDensity = String.valueOf(densityDpi) + " dpi";
        String bootloader = Build.BOOTLOADER;
        String user = Build.USER;
        String host = Build.HOST;
        String version = Build.VERSION.RELEASE;
        String androidApiLvl = Build.VERSION.SDK_INT + "";
        String buildId = Build.ID;
        String buildTime = Build.TIME + "";
        String fingerPrint = Build.FINGERPRINT;

        DeviceInfo deviceInfo = DeviceInfo.getInstance(
            screenWidth,
            screenHeight,
            screenDensity,
            densityDpi,
            screenInches,
            appName,
            appPackageName,
            appVersionName,
            appVersionCode,
            manufacturer,
            brand,
            model,
            board,
            hardware,
            serialNum,
            uid,
            android_id,
            screenResolution,
            strScreenDensity,
            bootloader,
            user,
            host,
            version,
            androidApiLvl,
            buildId,
            buildTime,
            fingerPrint
        );

        return deviceInfo;
    }

    /**
     * Check if context is in debug mode
     *
     * @param ctx   Context
     * @return  true=IsDebug, false=IsNotDebug
     */
    public static boolean isDebuggable(@NonNull Context ctx) {
        boolean debuggable = false;

        PackageManager pm = ctx.getPackageManager();
        try
        {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }
        catch(PackageManager.NameNotFoundException e)
        {
            /*debuggable variable will remain false*/
        }

        return debuggable;
    }

    /**
     * If this device has a soft keys over screen return size > 0
     *
     * @param windowManager Window manager
     * @return SoftKey size or 0=Dont have soft keysdd
     */
    public static int getSoftKeysHeight(@NonNull WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 ? (realWidth - displayWidth) : Math.max((realHeight - displayHeight), 0);
    }


    /**
     * Get app package name for given {@link Context}
     * @param context   {@link Context}
     * @return  Package name {@link String}
     */
    @Nullable
    public static String getAppPackageName(@NonNull Context context) {
        String appPackageName = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appPackageName = pInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appPackageName;
    }
}
