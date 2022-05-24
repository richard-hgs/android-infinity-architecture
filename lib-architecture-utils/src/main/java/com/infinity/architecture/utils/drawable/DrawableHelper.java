package com.infinity.architecture.utils.drawable;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import android.view.View;

import androidx.annotation.AnyRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.color.ColorHelper;
import com.infinity.architecture.utils.regex.RegexMatchGroupInfo;
import com.infinity.architecture.utils.regex.RegexMatchInfo;
import com.infinity.architecture.utils.regex.RegexUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class DrawableHelper {
    private static final String TAG = "DrawableHelper";

    public static final int SHAPE_TYPE_OVAL = 1;
    public static final int SHAPE_TYPE_RECTANGLE = 2;

    // CONSTRUTORES
    public DrawableHelper() {

    }

    public static void background(@NonNull View v, Drawable d) {
        v.setBackground(d);
    }

    public static void background(@NonNull View v, Bitmap b) {
        background(v, new BitmapDrawable(v.getResources(), b));
    }

    public static void backgroundRipple(@NonNull View v, int color) {
        background(v, getAdaptiveRippleDrawable(color));
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawable(int normalColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, 3, EnBgShapeType.RECTANGLE), null);
        } else {
            return getStateListDrawable(normalColor, ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f), 0, EnBgShapeType.RECTANGLE);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawable(int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(pressedColor), getRippleMask(normalColor, 3, EnBgShapeType.RECTANGLE), null);
        } else {
            return getStateListDrawable(normalColor, pressedColor, 0, EnBgShapeType.RECTANGLE);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadius(int normalColor, int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, EnBgShapeType.RECTANGLE), null);
        } else {
            return getStateListDrawable(normalColor, ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f), radius, EnBgShapeType.RECTANGLE);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadius(int normalColor, int pressedColor, int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, EnBgShapeType.RECTANGLE), null);
        } else {
            return getStateListDrawable(normalColor, pressedColor, radius, EnBgShapeType.RECTANGLE);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadius(int normalColor, int radius, int radiusTopLeft, int radiusTopRight, int radiusBottomRight, int radiusBottomLeft) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, EnBgShapeType.RECTANGLE, 0, Color.TRANSPARENT, 0), null);
        } else {
            return getStateListDrawable(normalColor, ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f), radius, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, EnBgShapeType.RECTANGLE, 0, Color.TRANSPARENT, 0);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadius(int normalColor, int radius, int radiusTopLeft, int radiusTopRight, int radiusBottomRight, int radiusBottomLeft, @NonNull EnBgShapeType shapeType, int borderWidth, int borderColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, shapeType, borderWidth, borderColor, 0), null);
        } else {
            return getStateListDrawable(normalColor, ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f), radius, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, shapeType, borderWidth, borderColor, 0);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadiusShape(int normalColor, int radius, @NonNull EnBgShapeType shapeType, int borderWidth, int borderColor, int dashSpace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, 0, 0, 0, 0, shapeType, borderWidth, borderColor, dashSpace), null);
        } else {
            return getStateListDrawable(normalColor, ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f), radius, 0, 0, 0, 0, shapeType, borderWidth, borderColor, dashSpace);
        }
    }

    @NonNull
    public static Drawable getAdaptiveRippleDrawableRadiusShape(int normalColor, int pressedColor, int radius, @NonNull EnBgShapeType shapeType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(ColorHelper.colorIsDark(normalColor) ? ColorHelper.lightenColor(normalColor, 0.6f) : ColorHelper.darkenColor(normalColor, 0.3f)), getRippleMask(normalColor, radius, shapeType), null);
        } else {
            return getStateListDrawable(normalColor, darker(normalColor), radius, shapeType);
        }
    }


    @NonNull
    private static Drawable getRippleMask(int color, int radius, @NonNull EnBgShapeType shapeType) {
        return getRippleMask(color, radius, 0, 0, 0, 0, shapeType, 0, Color.TRANSPARENT, 0);
    }

    @NonNull
    private static Drawable getRippleMask(int color, int radius, int topLeftRadius, int topRightRadius, int bottomRightRadius, int bottomLeftRadius, @NonNull EnBgShapeType shapeType, int borderWidth, int borderColor, int dashSpace) {
        float[] outerRadii = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        if (radius > 0) {
            Arrays.fill(outerRadii, radius);
        }

        // 0-1 -> topLeft, 2-3 -> topRight, 4-5 -> bottomRight, 6-7 -> bottomLeft
        if (topLeftRadius > 0) {
            outerRadii[0] = topLeftRadius;
            outerRadii[1] = topLeftRadius;
        }

        if (topRightRadius > 0) {
            outerRadii[2] = topRightRadius;
            outerRadii[3] = topRightRadius;
        }

        if (bottomRightRadius > 0) {
            outerRadii[4] = bottomRightRadius;
            outerRadii[5] = bottomRightRadius;
        }

        if (bottomLeftRadius > 0) {
            outerRadii[6] = bottomLeftRadius;
            outerRadii[7] = bottomLeftRadius;
        }

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable;
        if (shapeType == EnBgShapeType.OVAL) {
            shapeDrawable = new ShapeDrawable(new OvalShape());
        } else {
            shapeDrawable = new ShapeDrawable(r);
        }
        shapeDrawable.getPaint().setColor(color);

        Drawable[] layers = null;

        if (borderWidth > 0 || borderColor != Color.TRANSPARENT) {

            ShapeDrawable shapeDrawableBorder;
            if (shapeType == EnBgShapeType.OVAL) {
                shapeDrawableBorder = new ShapeDrawable(new OvalShape());
            } else {
                shapeDrawableBorder = new ShapeDrawable(r);
            }
            shapeDrawableBorder.getPaint().setColor(borderColor != Color.TRANSPARENT ? borderColor : Color.BLACK);
            shapeDrawableBorder.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawableBorder.getPaint().setStrokeWidth(borderWidth);
            if (dashSpace > 0) {
                shapeDrawableBorder.getPaint().setPathEffect(new DashPathEffect(new float[]{(float) dashSpace, (float) dashSpace}, 0f));
            }

            InsetDrawable insetDrawable = new InsetDrawable(shapeDrawableBorder, Math.round(borderWidth / 2f));

            layers = new Drawable[]{insetDrawable, shapeDrawable};
        } else {
            layers = new Drawable[]{shapeDrawable};
        }

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        return layerDrawable;
    }


    @NonNull
    private static Drawable getStateListDrawable(int normalColor, int pressedColor, int radius, @NonNull EnBgShapeType shapeType) {
        return getStateListDrawable(normalColor, pressedColor, radius, 0, 0, 0, 0, shapeType, 0, Color.TRANSPARENT, 0);
    }

    @NonNull
    private static Drawable getStateListDrawable(int normalColor, int pressedColor, int radius, int topLeftRadius, int topRightRadius, int bottomRightRadius, int bottomLeftRadius, @NonNull EnBgShapeType shapeType, int borderWidth, int borderColor, int dashSpace) {
        return getDrawable(
            normalColor, pressedColor,
            radius, radius,
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius,
            shapeType,
            borderWidth, borderWidth,
            borderColor, borderColor,
            dashSpace,
            false
        );
    }


    @NonNull
    public static Drawable getDrawable(
        int normalColor, int pressedColor,
        int radius, int radiusFocused,
        int topLeftRadius, int topLeftRadiusFocused,
        int topRightRadius, int topRightRadiusFocused,
        int bottomRightRadius, int bottomRightRadiusFocused,
        int bottomLeftRadius, int bottomLeftRadiusFocused,
        @NonNull EnBgShapeType shapeType,
        int borderWidth, int borderWidthFocused,
        int borderColor, int borderColorFocused,
        int dashSpace,
        boolean disableStateList
    ) {
        float[] outerRadiiNormal = new float[8];
        float[] outerRadiiFocused = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        if (radius > 0) {
            Arrays.fill(outerRadiiNormal, radius);
        }
        if (radiusFocused > 0) {
            Arrays.fill(outerRadiiFocused, radiusFocused);
        }

        // 0-1 -> topLeft, 2-3 -> topRight, 4-5 -> bottomRight, 6-7 -> bottomLeft
        if (topLeftRadius > 0) {
            outerRadiiNormal[0] = topLeftRadius;
            outerRadiiNormal[1] = topLeftRadius;
        }
        if (topLeftRadiusFocused > 0) {
            outerRadiiFocused[0] = topLeftRadiusFocused;
            outerRadiiFocused[1] = topLeftRadiusFocused;
        }

        if (topRightRadius > 0) {
            outerRadiiNormal[2] = topRightRadius;
            outerRadiiNormal[3] = topRightRadius;
        }
        if (topRightRadiusFocused > 0) {
            outerRadiiFocused[2] = topRightRadiusFocused;
            outerRadiiFocused[3] = topRightRadiusFocused;
        }

        if (bottomRightRadius > 0) {
            outerRadiiNormal[4] = bottomRightRadius;
            outerRadiiNormal[5] = bottomRightRadius;
        }
        if (bottomRightRadiusFocused > 0) {
            outerRadiiFocused[4] = bottomRightRadiusFocused;
            outerRadiiFocused[5] = bottomRightRadiusFocused;
        }

        if (bottomLeftRadius > 0) {
            outerRadiiNormal[6] = bottomLeftRadius;
            outerRadiiNormal[7] = bottomLeftRadius;
        }
        if (bottomLeftRadiusFocused > 0) {
            outerRadiiFocused[6] = bottomLeftRadiusFocused;
            outerRadiiFocused[7] = bottomLeftRadiusFocused;
        }

        RoundRectShape roundRectShapeNormal = new RoundRectShape(outerRadiiNormal, null, null);
        RoundRectShape roundRectShapeFocused = new RoundRectShape(outerRadiiFocused, null, null);

        ShapeDrawable colorDrawableNormal;
        if (shapeType == EnBgShapeType.OVAL) {
            colorDrawableNormal = new ShapeDrawable(new OvalShape());
        } else {
            colorDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        }

        ShapeDrawable colorDrawableFocused;
        colorDrawableNormal.getPaint().setColor(normalColor);
        if (shapeType == EnBgShapeType.OVAL) {
            colorDrawableFocused = new ShapeDrawable(new OvalShape());
        } else {
            colorDrawableFocused = new ShapeDrawable(roundRectShapeFocused);
        }
        colorDrawableFocused.getPaint().setColor(pressedColor);

        StateListDrawable firstDrawableStates = new StateListDrawable();
        firstDrawableStates.addState(new int[]{android.R.attr.state_pressed}, colorDrawableFocused);
        firstDrawableStates.addState(new int[]{android.R.attr.state_focused}, colorDrawableFocused);
        firstDrawableStates.addState(new int[]{android.R.attr.state_activated}, colorDrawableFocused);
        firstDrawableStates.addState(new int[]{}, colorDrawableNormal);
        firstDrawableStates.addState(StateSet.WILD_CARD, colorDrawableNormal);

        Drawable firstDrawable = firstDrawableStates;

        if (disableStateList) {
            firstDrawable = colorDrawableNormal;
        }

        Drawable[] layers = null;

        if (borderWidth > 0 || borderColor != Color.TRANSPARENT) {
            ShapeDrawable shapeDrawableBorderNormal;
            if (shapeType == EnBgShapeType.OVAL) {
                shapeDrawableBorderNormal = new ShapeDrawable(new OvalShape());
            } else {
                shapeDrawableBorderNormal = new ShapeDrawable(roundRectShapeNormal);
            }
            shapeDrawableBorderNormal.getPaint().setColor(borderColor != Color.TRANSPARENT ? borderColor : Color.BLACK);
            shapeDrawableBorderNormal.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawableBorderNormal.getPaint().setStrokeWidth(borderWidth);
            if (dashSpace > 0) {
                shapeDrawableBorderNormal.getPaint().setPathEffect(new DashPathEffect(new float[]{(float) dashSpace, (float) dashSpace}, 0f));
            }

            ShapeDrawable shapeDrawableBorderFocused;
            if (shapeType == EnBgShapeType.OVAL) {
                shapeDrawableBorderFocused = new ShapeDrawable(new OvalShape());
            } else {
                shapeDrawableBorderFocused = new ShapeDrawable(roundRectShapeFocused);
            }
            shapeDrawableBorderFocused.getPaint().setColor(borderColorFocused != Color.TRANSPARENT ? borderColorFocused : Color.BLACK);
            shapeDrawableBorderFocused.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawableBorderFocused.getPaint().setStrokeWidth(borderWidthFocused);
            if (dashSpace > 0) {
                shapeDrawableBorderFocused.getPaint().setPathEffect(new DashPathEffect(new float[]{(float) dashSpace, (float) dashSpace}, 0f));
            }

            StateListDrawable borderDrawableStates = new StateListDrawable();
            borderDrawableStates.addState(new int[]{android.R.attr.state_pressed}, shapeDrawableBorderFocused);
            borderDrawableStates.addState(new int[]{android.R.attr.state_focused}, shapeDrawableBorderFocused);
            borderDrawableStates.addState(new int[]{android.R.attr.state_activated}, shapeDrawableBorderFocused);
            borderDrawableStates.addState(new int[]{}, shapeDrawableBorderNormal);
            borderDrawableStates.addState(StateSet.WILD_CARD, shapeDrawableBorderNormal);

            InsetDrawable insetDrawable;
            if (disableStateList) {
                insetDrawable = new InsetDrawable(shapeDrawableBorderNormal, Math.round(borderWidth / 2f));
            } else {
                insetDrawable = new InsetDrawable(borderDrawableStates, Math.round(borderWidth / 2f));
            }

            layers = new Drawable[]{insetDrawable, firstDrawable};
        } else {
            layers = new Drawable[]{firstDrawable};
        }

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        return layerDrawable;
    }

    public static void setBackgroundKeepingPaddings(@NonNull View view, @NonNull Drawable drawable) {
        int paddingLeft = view.getPaddingStart();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingEnd();
        int paddingBottom = view.getPaddingBottom();
        view.setBackground(drawable);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * ESCURECE UMA COR
     *
     * @param color COR A SER ESCURECIDA
     * @return COR ESCURECIDA
     */
    public static int darker(int color) {
        return darker(color, 0.9f);
    }

    /**
     * ESCURECE UMA COR
     *
     * @param color  COR A SER ESCURECIDA
     * @param factor FATOR DE ESCURECIMENTO DA COR AONDE 0 MUITO ESCURA E 1 MUITO CLARA
     * @return COR ESCURECIDA
     */
    public static int darker(int color, @FloatRange(from = 0, to = 1) float factor) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);

        return Color.rgb((int) (r * .9), (int) (g * .9), (int) (b * .9));
    }

    public static boolean isDark(int color, @FloatRange(from = 0.1, to = 0.9) float factor) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return !(darkness < factor || color == 0);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap toBitmap(View view, int maxWidth, int maxHeight) {
//        view.setLayoutParams(new ConstraintLayout.LayoutParams(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.MATCH_PARENT
//        ));

        // DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();

        view.measure(
            View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
        );

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(
            view.getMeasuredWidth(),
            view.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);

        // storeImage(view.getContext(), bitmap, "teste.bmp");


        return bitmap;
    }

    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    /**
     * get uri to any resource type Via Context Resource instance
     * @param context - context
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        Uri resUri = getUriToResource(res,resId);
        return resUri;
    }

    /**
     * get uri to any resource type via given Resource Instance
     * @param res - resources instance
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    public static final Uri getUriToResource(@NonNull Resources res,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }

//    @Nullable
//    public static Bitmap toBitmap(View view) {
//        if (view != null) {
//            System.out.println("view is not null.....");
//            view.setDrawingCacheEnabled(true);
//            view.buildDrawingCache();
//            Bitmap bm = view.getDrawingCache();
//
//            try {
//                if (bm != null) {
//                    String dir = Environment.getExternalStorageDirectory().toString();
//                    System.out.println("bm is not null.....");
//                    OutputStream fos = null;
//                    File file = new File(dir,"sample.JPEG");
//                    fos = new FileOutputStream(file);
//                    BufferedOutputStream bos = new BufferedOutputStream(fos);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
//                    bos.flush();
//                    bos.close();
//
//                    return bm;
//                }
//            } catch(Exception e) {
//                System.out.println("Error="+e);
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    @Nullable
    public static String storeImage(Context context, Bitmap image, String imgPath) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getFilesDir();
        if (!directory.exists()) {
            directory.mkdir();
        }

        File pictureFile = new File(directory, imgPath);
        if (imgPath.contains("/")) {
            // File dirPart
            File pictureDir = pictureFile.getParentFile();
            if (pictureDir != null && !pictureDir.exists()) {
                pictureDir.mkdirs();
            }
        }

        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pictureFile.getPath();
    }

    public static Bitmap fixImageOrientation(Context context, String imgPath) {
        try {
            ExifInterface ei = new ExifInterface(imgPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            storeImage(context, rotatedBitmap, imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    /**
     * Converts an Android vector path to an svg image
     * @param androidXmlVector The android vector being converted
     * @return  The converted svg image
     */
    @NonNull
    public static String androidVectorPathToSvg(@NonNull String androidXmlVector) {
        String svg = androidXmlVector;
        svg = svg.replaceAll("xmlns:android=\"http://schemas.android.com/apk/res/android\"", "xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
        svg = svg.replaceAll("<vector", "<svg");
        svg = svg.replaceAll("</vector", "</svg");
        svg = svg.replaceAll("android:pathData", "d");
        svg = svg.replaceAll("android:fillColor", "fill");
        svg = svg.replaceAll("android:strokeAlpha", "stroke-opacity");
        svg = svg.replaceAll("android:fillAlpha", "fill-opacity");

        String height = getFirstMatchFirstGroupValue("android:height=\"+(?<heightGroup>[0-9.]*)dp\"+", svg);
        String width = getFirstMatchFirstGroupValue("android:height=\"+(?<widthGroup>[0-9.]*)dp\"+", svg);

        String viewPortHeight = getFirstMatchFirstGroupValue("android:viewportHeight=\"+(?<heightGroup>[0-9.]*)\"+", svg);
        String viewPortWidth = getFirstMatchFirstGroupValue("android:viewportWidth=\"+(?<widthGroup>[0-9.]*)\"+", svg);

        svg = svg.replaceAll("android:height=\"+(?<heightGroup>[0-9.]*)dp\"+", "height=\""+height+"\"");
        svg = svg.replaceAll("android:width=\"+(?<widthGroup>[0-9.]*)dp\"+", "width=\""+width+"\"");

        svg = svg.replaceAll("android:viewportHeight=\"+[0-9.]*\"+", "");
        svg = svg.replaceAll("android:viewportWidth=\"+[0-9.]*\"+", "viewBox=\"0 0 "+viewPortWidth+" "+viewPortHeight+"\"");

        return svg;
    }

    /**
     * Geet firsst match group for pattern
     *
     * @param pattern   Pattern
     * @param str       String to search
     * @return          Group match value
     */
    @Nullable
    private static String getFirstMatchFirstGroupValue(String pattern, String str) {
        ArrayList<RegexMatchInfo> regexInfoViewPortHeight = RegexUtils.searchPatternInStr(pattern, str);
        if (regexInfoViewPortHeight.size() > 0) {
            RegexMatchInfo regexMatchAt = regexInfoViewPortHeight.get(0);
            ArrayList<RegexMatchGroupInfo> groups = regexMatchAt.getGroups();
            if (groups.size() > 0) {
                return groups.get(0).getValue();
            }
        }
        return null;
    }
}
