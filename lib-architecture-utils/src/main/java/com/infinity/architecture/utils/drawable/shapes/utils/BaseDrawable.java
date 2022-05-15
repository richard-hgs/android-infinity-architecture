package com.infinity.architecture.utils.drawable.shapes.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseDrawable extends Drawable {
    private final Path clipPath = new Path();

//    protected PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    //    @Nullable
//    protected Drawable drawable = null;
    private ClipManager clipManager;
//    private boolean requiersShapeUpdate = true;
//    private Bitmap clipBitmap;

    // final Path rectView = new Path();

    public BaseDrawable(int color) {
        clipManager = new ClipPathManager(color);
//        clipPaint.setAntiAlias(true);
//
////        setDrawingCacheEnabled(true);
////
////        setWillNotDraw(false);
//
//        clipPaint.setColor(Color.BLUE);
//        clipPaint.setStyle(Paint.Style.FILL);
//        clipPaint.setStrokeWidth(1);
//
//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
//            clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
////            setLayerType(LAYER_TYPE_SOFTWARE, clipPaint); //Only works for software layers
//        } else {
//            clipPaint.setXfermode(pdMode);
////            setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers
//        }
    }

    public BaseDrawable(int color, Paint.Style paintStyle, boolean antiAlias, int strokeWidth) {
        clipManager = new ClipPathManager(color, paintStyle, antiAlias, strokeWidth);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int left = getBounds().left;
        int top = getBounds().top;
        int width = getBounds().width() - left;
        int height = getBounds().height() - top;

        canvas.translate(left, top);

//        rectView.reset();
//        rectView.addRect(0f, 0f, 1f * width, 1f * height, Path.Direction.CW);

        if (clipManager != null) {
            if (width > 0 && height > 0) {
                clipManager.setupClipLayout(width, height);
                clipPath.reset();
                clipPath.set(clipManager.createMask(width, height));

                //invert the path for android P
//                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
//                    final boolean success = rectView.op(clipPath, Path.Op.DIFFERENCE);
//                }

                canvas.drawPath(clipPath, clipManager.getPaint());

                //this needs to be fixed for 25.4.0
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
//                    try {
//                        setOutlineProvider(getOutlineProvider());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

//        postInvalidate();
//        calculateLayout(getBounds().width(), getBounds().height());
//        if (clipBitmap != null) {
//            canvas.drawBitmap(clipBitmap, 0, 0, clipManager.getPaint());
//        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

//    private boolean requiresBitmap() {
//        return (clipManager != null && clipManager.requiresBitmap()) || drawable != null;
//    }

//    private void calculateLayout(int width, int height) {
//        rectView.reset();
//        rectView.addRect(0f, 0f, 1f * width, 1f * height, Path.Direction.CW);
//
//        if (clipManager != null) {
//            if (width > 0 && height > 0) {
//                clipManager.setupClipLayout(width, height);
//                clipPath.reset();
//                clipPath.set(clipManager.createMask(width, height));
//
//                if (requiresBitmap()) {
//                    if (clipBitmap != null) {
//                        clipBitmap.recycle();
//                    }
//                    clipBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                    final Canvas canvas = new Canvas(clipBitmap);
//
//                    if (drawable != null) {
//                        drawable.setBounds(0, 0, width, height);
//                        drawable.draw(canvas);
//                    } else {
//                        canvas.drawPath(clipPath, clipManager.getPaint());
//                    }
//                }
//
//                //invert the path for android P
//                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
//                    final boolean success = rectView.op(clipPath, Path.Op.DIFFERENCE);
//                }
//
//                //this needs to be fixed for 25.4.0
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
////                    try {
////                        setOutlineProvider(getOutlineProvider());
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
//            }
//        }
//
////        postInvalidate();
//    }

    public void setClipPathCreator(ClipPathManager.ClipPathCreator createClipPath) {
        ((ClipPathManager) clipManager).setClipPathCreator(createClipPath);
        invalidateSelf();
    }

    public Paint getPaint() {
        return clipManager.getPaint();
    }
}
