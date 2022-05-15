package com.infinity.architecture.utils.dimens;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DimenUtils {
    //convert int to dp e retorna em pixels int
    public static int dpToPixel(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    //convert int px to dp e retorna dp int
    public static int pxToDp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(px / density);
    }

    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dpToSp(Context context, float dp) {
        return (int) (dpToPixel(context, dp) / context.getResources().getDisplayMetrics().scaledDensity);
    }

    // retorna o tamanho total da tela width x height
    public static DisplayMetrics getDimenTela(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics metrics = new DisplayMetrics();

        if (display != null) {
            display.getMetrics(metrics);
        }

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return metrics;
    }

    // retorna o tamanho dipsonível da tela width x height
    public static DisplayMetrics getDimenScreenAvailable(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics metrics = new DisplayMetrics();

        if (display != null) {
            display.getMetrics(metrics);

            Point size = new Point();
            display.getSize(size);

            int width = size.x;
            int height = size.y;

            metrics.heightPixels = height;
            metrics.widthPixels = width;
        }

        return metrics;
    }

    // retorna o tamanho em relacao ao percentual da tela
    public static DisplayMetrics getDimenTelaPercent(Context activity, float percent) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics metrics = new DisplayMetrics();

        if (display != null) {
            display.getMetrics(metrics);
        }

        metrics.widthPixels = (int) (metrics.widthPixels * (percent / 100));
        metrics.heightPixels = (int) (metrics.heightPixels * (percent / 100));

        return metrics;
    }


    public static int getSoftButtonsBarHeight(Context context) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            wm.getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            } else {
                return 0;
            }
        }
        return 0;
    }
}
