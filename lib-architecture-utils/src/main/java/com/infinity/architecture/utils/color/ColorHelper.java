package com.infinity.architecture.utils.color;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

public class ColorHelper {
    private static final String TAG = "ColorHelper";

    /**
     * Check if desired color is darker
     *
     * @param color Color to be checked
     * @return true=is_dark, false=is_lighter
     */
    public static boolean colorIsDark(int color) {
        return colorIsDark(color, 0.6f);
    }

    /**
     * Check if desired color is darker
     *
     * @param color Color to be checked
     * @return true=is_dark, false=is_lighter
     */
    public static boolean colorIsDark(int color, float lumFactor) {
        Log.d(TAG, "colorIsDark - luminance:" + ColorUtils.calculateLuminance(color) + " - lumnFactor: " + lumFactor);
        return ColorUtils.calculateLuminance(color) < lumFactor;
    }

    public static int themeAttributeToColor(int themeAttributeId,
                                            Context context,
                                            int fallbackColorId) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean wasResolved = theme.resolveAttribute(
                themeAttributeId, outValue, true
        );

        if (wasResolved) {
            return outValue.data;
        } else {
            // fallback colour handling
            return fallbackColorId;
        }
    }

    /**
     * Blend between two ARGB colors using the given ratio.
     *
     * <p>A blend ratio of 0.0 will result in {@code color1}, 0.5 will give an even blend,
     * 1.0 will result in {@code color2}.</p>
     *
     * @param colorToBlend  the first ARGB color
     * @param blendColor    the second ARGB color
     * @param ratio         the blend ratio of {@code color1} to {@code color2}
     */
    @ColorInt
    public static int blendColor(@ColorInt int colorToBlend, @ColorInt int blendColor, @FloatRange(from = 0.0, to = 1.0) float ratio) {
        return ColorUtils.blendARGB(colorToBlend, Color.BLACK, ratio);
    }


    /**
     * Makes a color lighten from 0.0 to 1.0 value
     * @param color     Color int
     * @param value     Value from 0.0 to 1.0 value of strength of ligten
     * @return The new color
     */
    @ColorInt
    public static int lightenColor(@ColorInt int color,
                                   float value) {
        float[] hsl = new float[3];
        colorToHSL(color, hsl);
        hsl[2] += value;
        hsl[2] = Math.max(0f, Math.min(hsl[2], 1f));
        return HSLToColor(hsl);
    }

    /**
     * Makes a color darker from 0.0 to 1.0 value
     *
     * @param color Color int
     * @param value Value from 0.0 to 1.0 value of strength of darker
     * @return The new color
     */
    @ColorInt
    public static int darkenColor(@ColorInt int color,
                                  float value) {
        float[] hsl = new float[3];
        colorToHSL(color, hsl);
        hsl[2] -= value;
        hsl[2] = Math.max(0f, Math.min(hsl[2], 1f));
        return HSLToColor(hsl);
    }

    /**
     * Convert the ARGB color to its HSL (hue-saturation-lightness) components.
     * <ul>
     * <li>outHsl[0] is Hue [0 .. 360)</li>
     * <li>outHsl[1] is Saturation [0...1]</li>
     * <li>outHsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param color  the ARGB color to convert. The alpha component is ignored
     * @param outHsl 3-element array which holds the resulting HSL components
     */
    public static void colorToHSL(@ColorInt int color, @NonNull float[] outHsl) {
        RGBToHSL(Color.red(color), Color.green(color), Color.blue(color), outHsl);
    }

    /**
     * Convert HSL (hue-saturation-lightness) components to a RGB color.
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param hsl 3-element array which holds the input HSL components
     * @return the resulting RGB color
     */
    @ColorInt
    public static int HSLToColor(@NonNull float[] hsl) {
        final float h = hsl[0];
        final float s = hsl[1];
        final float l = hsl[2];

        final float c = (1f - Math.abs(2 * l - 1f)) * s;
        final float m = l - 0.5f * c;
        final float x = c * (1f - Math.abs((h / 60f % 2f) - 1f));

        final int hueSegment = (int) h / 60;

        int r = 0, g = 0, b = 0;

        switch (hueSegment) {
            case 0:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * (x + m));
                b = Math.round(255 * m);
                break;
            case 1:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * (c + m));
                b = Math.round(255 * m);
                break;
            case 2:
                r = Math.round(255 * m);
                g = Math.round(255 * (c + m));
                b = Math.round(255 * (x + m));
                break;
            case 3:
                r = Math.round(255 * m);
                g = Math.round(255 * (x + m));
                b = Math.round(255 * (c + m));
                break;
            case 4:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (c + m));
                break;
            case 5:
            case 6:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (x + m));
                break;
        }

        r = (int) constrain(r, 0, 255);
        g = (int) constrain(g, 0, 255);
        b = (int) constrain(b, 0, 255);

        return Color.rgb(r, g, b);
    }

    /**
     * Convert RGB components to HSL (hue-saturation-lightness).
     * <ul>
     * <li>outHsl[0] is Hue [0 .. 360)</li>
     * <li>outHsl[1] is Saturation [0...1]</li>
     * <li>outHsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param r      red component value [0..255]
     * @param g      green component value [0..255]
     * @param b      blue component value [0..255]
     * @param outHsl 3-element array which holds the resulting HSL components
     */
    public static void RGBToHSL(@IntRange(from = 0x0, to = 0xFF) int r,
                                @IntRange(from = 0x0, to = 0xFF) int g, @IntRange(from = 0x0, to = 0xFF) int b,
                                @NonNull float[] outHsl) {
        final float rf = r / 255f;
        final float gf = g / 255f;
        final float bf = b / 255f;

        final float max = Math.max(rf, Math.max(gf, bf));
        final float min = Math.min(rf, Math.min(gf, bf));
        final float deltaMaxMin = max - min;

        float h, s;
        float l = (max + min) / 2f;

        if (max == min) {
            // Monochromatic
            h = s = 0f;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6f;
            } else if (max == gf) {
                h = ((bf - rf) / deltaMaxMin) + 2f;
            } else {
                h = ((rf - gf) / deltaMaxMin) + 4f;
            }

            s = deltaMaxMin / (1f - Math.abs(2f * l - 1f));
        }

        h = (h * 60f) % 360f;
        if (h < 0) {
            h += 360f;
        }

        outHsl[0] = constrain(h, 0f, 360f);
        outHsl[1] = constrain(s, 0f, 1f);
        outHsl[2] = constrain(l, 0f, 1f);
    }

    /**
     * Converts int color to HEX color
     * @param intColor  Color to be converted
     * @return  string of hex color
     */
    public static String colorToHexStr(int intColor) {
        return String.format("#%06X", 0xFFFFFF & intColor);
    }

    private static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
