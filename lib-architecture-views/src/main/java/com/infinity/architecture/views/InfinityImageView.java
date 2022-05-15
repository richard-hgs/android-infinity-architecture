package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.infinity.architecture.utils.reflection.ReflectionUtils;
import com.infinity.architecture.utils.reflection.TypeToken;
import com.infinity.architecture.views.enums.EnBlurType;
import com.infinity.architecture.views.enums.EnScaleType;
import com.infinity.architecture.views.enums.EnSrcType;
import com.infinity.architecture.views.enums.EnTransitionType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.transformations.BlurTransformation;
import com.bumptech.glide.transformations.ColorFilterTransformation;

import java.util.ArrayList;


@SuppressWarnings({ "unused", "UnnecessaryLocalVariable", "ConstantConditions", "StatementWithEmptyBody" })
public class InfinityImageView extends AppCompatImageView {
    private static final String TAG = "InfinityImageView";

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidthDp;
    private int   bgRadius;
    private int   bgRadiusTopLeft;
    private int   bgRadiusTopRight;
    private int   bgRadiusBottomLeft;
    private int   bgRadiusBottomRight;


    private boolean          blurEnabled;
    private int              blurColor;
    private EnBlurType       blurType;
    private int              blurRadius;
    private EnSrcType        srcType;
    private Object           src;
    private int              srcPlaceholder;
    private int              srcError;
    private EnScaleType      srcScaleType;
    private int              srcResizePxWidth;
    private int              srcResizePxHeight;
    private int              srcResizeDimenWidth;
    private int              srcResizeDimenHeight;
    private EnTransitionType srcTransitionType;
    private int              srcTransitionDuration;
    private int              srcRadius;
    private int              srcRadiusTopLeft;
    private int              srcRadiusTopRight;
    private int              srcRadiusBottomRight;
    private int              srcRadiusBottomLeft;

    public InfinityImageView(Context context) {
        this(context, null);
    }

    public InfinityImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.infinityImageViewStyle);
    }

    public InfinityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray taAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.InfinityImageView, defStyleAttr, R.style.InfinityImageViewStyle);
        blurColor   = taAttrs.getColor(R.styleable.InfinityImageView_blur_color, Color.GRAY);
        blurEnabled = taAttrs.getBoolean(R.styleable.InfinityImageView_blur_enabled, false);
        blurType    = EnBlurType.values()[taAttrs.getInt(R.styleable.InfinityImageView_blur_type, EnBlurType.NONE.ordinal())];
        blurRadius  = taAttrs.getInt(R.styleable.InfinityImageView_blur_radius, 0);

        bgColor             = taAttrs.getColor(R.styleable.InfinityImageView_bg_color, Color.TRANSPARENT);
        bgBorderColor       = taAttrs.getColor(R.styleable.InfinityImageView_bg_border_color, Color.TRANSPARENT);
        bgBorderWidthDp     = taAttrs.getDimension(R.styleable.InfinityImageView_bg_border_width, 0);
        bgRadius            = taAttrs.getInt(R.styleable.InfinityImageView_bg_radius, 0);
        bgRadiusTopLeft     = taAttrs.getInt(R.styleable.InfinityImageView_bg_radius_top_left, 0);
        bgRadiusTopRight    = taAttrs.getInt(R.styleable.InfinityImageView_bg_radius_top_right, 0);
        bgRadiusBottomLeft  = taAttrs.getInt(R.styleable.InfinityImageView_bg_radius_bottom_left, 0);
        bgRadiusBottomRight = taAttrs.getInt(R.styleable.InfinityImageView_bg_radius_bottom_right, 0);
        TypedValue tvSrc = taAttrs.peekValue(R.styleable.InfinityImageView_src);
        if (tvSrc == null) {
            try {
                int src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
                src     = src_resource;
                srcType = EnSrcType.REFERENCE;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (tvSrc != null) {
//                    Log.d(TAG, "type("+tvSrc.type+") - tvSrc:" + tvSrc);
            String strResourceId = taAttrs.getString(R.styleable.InfinityImageView_src);
            int resourceId = taAttrs.getResourceId(R.styleable.InfinityImageView_src, 0);

            if (
                strResourceId != null &&
                (strResourceId.startsWith("http://") || strResourceId.startsWith("https://"))
            ) {
                srcType = EnSrcType.STRING;
                src     = taAttrs.getString(R.styleable.InfinityImageView_src);
            } else if (resourceId != 0) {
                src     = resourceId;
                srcType = EnSrcType.REFERENCE;
            }
        }
        srcPlaceholder        = taAttrs.getResourceId(R.styleable.InfinityImageView_src_placeholder, R.drawable.ic_baseline_image_24);
        srcError              = taAttrs.getResourceId(R.styleable.InfinityImageView_src_error, R.drawable.ic_baseline_image_24);
        srcScaleType          = EnScaleType.values()[taAttrs.getInt(R.styleable.InfinityImageView_src_scale_type, EnScaleType.NONE.ordinal())];
        srcResizePxWidth      = taAttrs.getInt(R.styleable.InfinityImageView_src_resize_px_width, -1);
        srcResizePxHeight     = taAttrs.getInt(R.styleable.InfinityImageView_src_resize_px_height, -1);
        srcResizeDimenWidth   = taAttrs.getDimensionPixelSize(R.styleable.InfinityImageView_src_resize_dimen_width, -1);
        srcResizeDimenHeight  = taAttrs.getDimensionPixelSize(R.styleable.InfinityImageView_src_resize_dimen_height, -1);
        srcTransitionType     = EnTransitionType.values()[taAttrs.getInt(R.styleable.InfinityImageView_src_transition_type, EnTransitionType.CROSSFADE.ordinal())];
        srcTransitionDuration = taAttrs.getInt(R.styleable.InfinityImageView_src_transition_duration, 300);
        srcRadius             = taAttrs.getInt(R.styleable.InfinityImageView_src_radius, 0);
        srcRadiusTopLeft      = taAttrs.getInt(R.styleable.InfinityImageView_src_radius_top_left, 0);
        srcRadiusTopRight     = taAttrs.getInt(R.styleable.InfinityImageView_src_radius_top_right, 0);
        srcRadiusBottomRight  = taAttrs.getInt(R.styleable.InfinityImageView_src_radius_bottom_right, 0);
        srcRadiusBottomLeft   = taAttrs.getInt(R.styleable.InfinityImageView_src_radius_bottom_left, 0);
        taAttrs.recycle();

        updateBackgroundChanges();
        updateImageChanges();
    }

    /**
     * Configure the background changes
     */
    public void updateBackgroundChanges() {
        if (bgRadius > 0) {
            bgRadiusTopLeft     = bgRadius;
            bgRadiusTopRight    = bgRadius;
            bgRadiusBottomLeft  = bgRadius;
            bgRadiusBottomRight = bgRadius;
        }

        GradientDrawable gradientDrawable = null;
        if (bgColor != Color.TRANSPARENT) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
            gradientDrawable.setColor(bgColor);
        }
        if (bgBorderColor != Color.TRANSPARENT) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
            gradientDrawable.setStroke((int) bgBorderWidthDp, bgBorderColor);
        }
        if (bgBorderWidthDp != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
            gradientDrawable.setStroke((int) bgBorderWidthDp, (bgBorderColor == Color.TRANSPARENT ? Color.BLACK : bgBorderColor));
        }
        if (bgRadius != 0 || bgRadiusTopLeft != 0 || bgRadiusTopRight != 0 || bgRadiusBottomLeft != 0 || bgRadiusBottomRight != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }

            float[] cornerRadii = new float[] { bgRadiusTopLeft, bgRadiusTopLeft, bgRadiusTopRight, bgRadiusTopRight, bgRadiusBottomRight, bgRadiusBottomRight, bgRadiusBottomLeft, bgRadiusBottomLeft };
            gradientDrawable.setCornerRadii(cornerRadii);
        }

        if (gradientDrawable != null) {
            setBackground(gradientDrawable);
        }
    }

    /**
     * Configure image changes
     */
    public void updateImageChanges() {
        if (src != null && !isInEditMode()) {
            post(new Runnable() {
                @Override
                public void run() {
                    // Glide
                    RequestManager requestManager = Glide.with(InfinityImageView.this);
                    RequestBuilder<Drawable> requestBuilder = null;
                    // RequestCreator requestCreator = null;
                    if (srcType == EnSrcType.STRING) {
                        // requestCreator = picasso.load((String) src);
                        requestBuilder = requestManager.load((String) src);
                    } else if (srcType == EnSrcType.COLOR) {

                    } else if (srcType == EnSrcType.REFERENCE) {
                        requestBuilder = requestManager.load((int) src);
                    } else if (srcType == EnSrcType.URI) {
                        requestBuilder = requestManager.load((Uri) src);
                    }
                    if (requestBuilder != null) {
                        if (srcResizePxHeight >= 0 && srcResizePxWidth >= 0) {
                            requestBuilder = requestBuilder.apply(new RequestOptions().override(srcResizePxWidth, srcResizePxHeight));
                        } else if (srcResizeDimenHeight >= 0 && srcResizeDimenWidth >= 0) {
                            requestBuilder = requestBuilder.apply(new RequestOptions().override(srcResizeDimenWidth, srcResizeDimenHeight));
                        }

                        ArrayList<Transformation<Bitmap>> transformationsList = new ArrayList<>();

                        if (srcScaleType == EnScaleType.CENTER_CROP) {
                            transformationsList.add(new CenterCrop());
                        } else if (srcScaleType == EnScaleType.CENTER_INSIDE) {
                            transformationsList.add(new CenterInside());
                        }

                        if (srcRadius > 0) {
                            transformationsList.add(new RoundedCorners(srcRadius));
                        } else if (
                            srcRadiusTopLeft > 0 ||
                            srcRadiusTopRight > 0 ||
                            srcRadiusBottomRight > 0 ||
                            srcRadiusBottomLeft > 0
                        ) {
                            transformationsList.add(new GranularRoundedCorners(srcRadiusTopLeft, srcRadiusTopRight, srcRadiusBottomRight, srcRadiusBottomLeft));
                        }
                        if (blurType == EnBlurType.DEFOCUS || blurType == EnBlurType.DEFOCUS_OVERLAP) {
                            transformationsList.add(new BlurTransformation(blurRadius));
                        }
                        if (blurType == EnBlurType.OVERLAP || blurType == EnBlurType.DEFOCUS_OVERLAP) {
                            transformationsList.add(new ColorFilterTransformation(blurColor));
                        }

                        if (transformationsList.size() > 0) {
                            try {
                                //noinspection unchecked
                                requestBuilder = requestBuilder.transform(ReflectionUtils.toPrimitiveArray(transformationsList, (Class<Transformation<Bitmap>>) new TypeToken<Transformation<Bitmap>>() {
                                }.getRawType()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        TransitionOptions<?, Drawable> transitionOptions = null;
                        if (srcTransitionType == EnTransitionType.CROSSFADE) {
                            transitionOptions = DrawableTransitionOptions.withCrossFade(srcTransitionDuration);
                        }

                        if (transitionOptions != null) {
                            requestBuilder = requestBuilder.transition(transitionOptions);
                        }
                        requestBuilder = requestBuilder.placeholder(srcPlaceholder);
                        requestBuilder = requestBuilder.error(srcError);
                        requestBuilder.into(InfinityImageView.this);
                    }
                }
            });
        }
    }

    /**
     * Return if "blur" is enabled or not
     *
     * @return true=Enabled, false=Disabled
     */
    public boolean isBlurEnabled() {
        return blurEnabled;
    }

    /**
     * Enable or disable "blur" effect
     *
     * @param blurEnabled true=Enable, false=Disable
     */
    public void setBlurEnabled(boolean blurEnabled) {
        this.blurEnabled = blurEnabled;
    }

    /**
     * Get current blur color
     *
     * @return {@link Integer} color
     */
    public int getBlurColor() {
        return blurColor;
    }

    /**
     * Set current blur color
     *
     * @param blurColor {@link Integer} color
     */
    public void setBlurColor(int blurColor) {
        this.blurColor = blurColor;
    }

    /**
     * Get current blur type
     *
     * @return {@link EnBlurType} blur type
     */
    public EnBlurType getBlurType() {
        return blurType;
    }

    /**
     * Set blur type
     *
     * @param blurType {@link EnBlurType}
     */
    public void setBlurType(@NonNull EnBlurType blurType) {
        this.blurType = blurType;
    }

    /**
     * Get current blur radius
     *
     * @return {@link Float} blur radius
     */
    public float getBlurRadius() {
        return blurRadius;
    }

    /**
     * Set current blur radius
     *
     * @param blurRadius {@link Float}
     */
    public void setBlurRadius(@IntRange(from = 1, to = 25) int blurRadius) {
        this.blurRadius = blurRadius;
    }

    /**
     * Get the background color
     *
     * @return {@link Integer} color
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * Set the background color
     *
     * @param bgColor {@link Integer} color
     */
    public void setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
        updateBackgroundChanges();
    }

    /**
     * Get background corners radius
     *
     * @return {@link Integer} radius
     */
    public int getBgRadius() {
        return bgRadius;
    }

    /**
     * Set background corners radius
     *
     * @param bgRadius {@link Integer} radius
     */
    public void setBgRadius(int bgRadius) {
        this.bgRadius = bgRadius;
        updateBackgroundChanges();
    }

    /**
     * Get background top_left corner radius
     *
     * @return {@link Integer} top_left radius
     */
    public int getBgRadiusTopLeft() {
        return bgRadiusTopLeft;
    }

    /**
     * Set background top_left corner radius
     *
     * @param bgRadiusTopLeft {@link Integer} radius
     */
    public void setBgRadiusTopLeft(int bgRadiusTopLeft) {
        this.bgRadiusTopLeft = bgRadiusTopLeft;
        updateBackgroundChanges();
    }

    /**
     * Get background top_right corner radius
     *
     * @return {@link Integer} radius
     */
    public int getBgRadiusTopRight() {
        return bgRadiusTopRight;
    }

    /**
     * Set background top_right corner radius
     *
     * @param bgRadiusTopRight {@link Integer} radius
     */
    public void setBgRadiusTopRight(int bgRadiusTopRight) {
        this.bgRadiusTopRight = bgRadiusTopRight;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_right corner radius
     *
     * @return {@link Integer} radius
     */
    public int getBgRadiusBottomRight() {
        return bgRadiusBottomRight;
    }

    /**
     * Set background bottom_right corner radius
     *
     * @param bgRadiusBottomRight {@link Integer} radius
     */
    public void setBgRadiusBottomRight(int bgRadiusBottomRight) {
        this.bgRadiusBottomRight = bgRadiusBottomRight;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_left corner radius
     *
     * @return {@link Integer} radius
     */
    public int getBgRadiusBottomLeft() {
        return bgRadiusBottomLeft;
    }

    /**
     * Set background bottom_left corner radius
     *
     * @param bgRadiusBottomLeft {@link Integer} radius
     */
    public void setBgRadiusBottomLeft(int bgRadiusBottomLeft) {
        this.bgRadiusBottomLeft = bgRadiusBottomLeft;
        updateBackgroundChanges();
    }

    /**
     * Get background border color
     *
     * @return {@link Integer} border color
     */
    public int getBgBorderColor() {
        return bgBorderColor;
    }

    /**
     * Set background border color
     *
     * @param bgBorderColor {@link Integer} border color
     */
    public void setBgBorderColor(int bgBorderColor) {
        this.bgBorderColor = bgBorderColor;
        updateBackgroundChanges();

    }

    /**
     * Get background border width
     *
     * @return float border width
     */
    public float getBgBorderWidthDp() {
        return bgBorderWidthDp;
    }

    /**
     * Set backgrund border width in dp
     *
     * @param bgBorderWidthDp float border width dp
     */
    public void setBgBorderWidthDp(float bgBorderWidthDp) {
        this.bgBorderWidthDp = bgBorderWidthDp;
        updateBackgroundChanges();
    }


}
