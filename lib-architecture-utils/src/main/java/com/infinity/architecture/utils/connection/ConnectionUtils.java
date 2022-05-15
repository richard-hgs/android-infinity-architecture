package com.infinity.architecture.utils.connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

public class ConnectionUtils {

    @SuppressLint("MissingPermission")
    public static boolean hasInternetConnection(Context context) {
        boolean conectado = false;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager != null) {
            if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()
            ) {
                conectado = true;
            } else {
                conectado = false;
            }
        }
        return conectado;
    }

    // Verifica se o gps está ativo
    public static boolean hasGpsConnection(Context activity) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }
}
