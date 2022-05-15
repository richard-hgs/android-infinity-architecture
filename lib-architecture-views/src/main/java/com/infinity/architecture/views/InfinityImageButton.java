package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.infinity.architecture.utils.dimens.DimenUtils;
import com.infinity.architecture.utils.drawable.DrawableHelper;
import com.infinity.architecture.utils.drawable.EnBgShapeType;
import com.infinity.architecture.views.enums.EnScaleType;
import com.infinity.architecture.views.enums.EnSrcType;
import com.infinity.architecture.views.enums.EnTransitionType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

@SuppressWarnings({ "unused", "UnnecessaryLocalVariable", "StatementWithEmptyBody" })
public class InfinityImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    // TAG
    private static final String TAG = "InfinityImgButton";

    // Styles variables
    private int              bgColor;
    private int              bgRadius;
    private int              bgRadiusTopLeft;
    private int              bgRadiusTopRight;
    private int              bgRadiusBottomRight;
    private int              bgRadiusBottomLeft;
    private int              bgBorderColor;
    private float            bgBorderWidthDp;
    @NonNull
    private EnBgShapeType    bgShapeType;
    @NonNull
    private EnSrcType        srcType;
    private Object           src;
    private int              srcPlaceholder;
    private int              srcError;
    @NonNull
    private EnScaleType      srcScaleType;
    private int              srcResizePxWidth;
    private int              srcResizePxHeight;
    private float            srcResizeDimenWidth;
    private float            srcResizeDimenHeight;
    @NonNull
    private EnTransitionType srcTransitionType;
    private int              srcTransitionDuration;

    public InfinityImageButton(Context context) {
        this(context, null);
    }

    public InfinityImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.infinityImageButtonStyle);
    }

    public InfinityImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray taAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.InfinityImageButton, defStyleAttr, R.style.InfinityImageButtonStyle);
        bgColor             = taAttrs.getColor(R.styleable.InfinityImageButton_bg_color, Color.GRAY);
        bgRadius            = taAttrs.getInt(R.styleable.InfinityImageButton_bg_radius, 0);
        bgRadiusTopLeft     = taAttrs.getInt(R.styleable.InfinityImageButton_bg_radius_top_left, 0);
        bgRadiusTopRight    = taAttrs.getInt(R.styleable.InfinityImageButton_bg_radius_top_right, 0);
        bgRadiusBottomRight = taAttrs.getInt(R.styleable.InfinityImageButton_bg_radius_bottom_right, 0);
        bgRadiusBottomLeft  = taAttrs.getInt(R.styleable.InfinityImageButton_bg_radius_bottom_left, 0);
        bgBorderColor       = taAttrs.getColor(R.styleable.InfinityImageButton_bg_border_color, Color.TRANSPARENT);
        bgBorderWidthDp     = taAttrs.getDimension(R.styleable.InfinityImageButton_bg_border_width, 0);
        bgShapeType         = EnBgShapeType.values()[taAttrs.getInt(R.styleable.InfinityImageButton_bg_shape_type, DrawableHelper.SHAPE_TYPE_RECTANGLE)];
        srcType             = EnSrcType.UNDEFINED;
        TypedValue tvSrc = taAttrs.peekValue(R.styleable.InfinityImageButton_src);
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
            String strResourceId = taAttrs.getString(R.styleable.InfinityImageButton_src);
            int resourceId = taAttrs.getResourceId(R.styleable.InfinityImageButton_src, 0);
            if (
                strResourceId != null &&
                (strResourceId.startsWith("http://") || strResourceId.startsWith("https://"))
            ) {
                srcType = EnSrcType.STRING;
                src     = taAttrs.getString(R.styleable.InfinityImageButton_src);
            } else if (resourceId != 0) {
                src     = resourceId;
                srcType = EnSrcType.REFERENCE;
            }
        }
        srcPlaceholder        = taAttrs.getResourceId(R.styleable.InfinityImageButton_src_placeholder, R.drawable.ic_baseline_image_24);
        srcError              = taAttrs.getResourceId(R.styleable.InfinityImageButton_src_error, R.drawable.ic_baseline_image_24);
        srcScaleType          = EnScaleType.values()[taAttrs.getInt(R.styleable.InfinityImageButton_src_scale_type, EnScaleType.NONE.ordinal())];
        srcResizePxWidth      = taAttrs.getInt(R.styleable.InfinityImageButton_src_resize_px_width, -1);
        srcResizePxHeight     = taAttrs.getInt(R.styleable.InfinityImageButton_src_resize_px_height, -1);
        srcResizeDimenWidth   = taAttrs.getDimension(R.styleable.InfinityImageButton_src_resize_dimen_width, -1);
        srcResizeDimenHeight  = taAttrs.getDimension(R.styleable.InfinityImageButton_src_resize_dimen_height, -1);
        srcTransitionType     = EnTransitionType.values()[taAttrs.getInt(R.styleable.InfinityImageButton_src_transition_type, EnTransitionType.CROSSFADE.ordinal())];
        srcTransitionDuration = taAttrs.getInt(R.styleable.InfinityImageButton_src_transition_duration, 300);
        taAttrs.recycle();

        if (bgRadius > 0) {
            bgRadiusTopLeft     = bgRadius;
            bgRadiusTopRight    = bgRadius;
            bgRadiusBottomLeft  = bgRadius;
            bgRadiusBottomRight = bgRadius;
        }

        updateBackgroundChanges();
        updateImageChanges();
    }

    /**
     * Configure the background changes
     */
    public void updateBackgroundChanges() {
        setBackground(DrawableHelper.getAdaptiveRippleDrawableRadius(bgColor, bgRadius, bgRadiusTopLeft, bgRadiusTopRight, bgRadiusBottomRight, bgRadiusBottomLeft, bgShapeType, (int) bgBorderWidthDp, bgBorderColor));
    }

    /**
     * Configure image changes
     */
    public void updateImageChanges() {
        if (src != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    // Glide
                    RequestManager requestManager = Glide.with(InfinityImageButton.this);
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
                        if (srcResizePxWidth >= 0 && srcResizePxHeight >= 0) {
                            requestBuilder = requestBuilder.apply(new RequestOptions().override(srcResizePxWidth, srcResizePxHeight));
                        } else if (srcResizeDimenWidth >= 0 && srcResizeDimenHeight >= 0) {
                            int srcDimenPxWidth = DimenUtils.dpToPixel(getContext(), srcResizeDimenWidth);
                            int srcDimenPxHeight = DimenUtils.dpToPixel(getContext(), srcResizeDimenHeight);

                            requestBuilder = requestBuilder.apply(new RequestOptions().override(srcDimenPxWidth, srcDimenPxHeight));
                        }

                        if (srcScaleType == EnScaleType.CENTER_CROP) {
                            requestBuilder = requestBuilder.centerCrop();
                        } else if (srcScaleType == EnScaleType.CENTER_INSIDE) {
                            requestBuilder = requestBuilder.centerInside();
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
                        requestBuilder.into(InfinityImageButton.this);
                    }
                }
            });
        }
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
     * Set background border width in dp
     *
     * @param bgBorderWidthDp float border width dp
     */
    public void setBgBorderWidthDp(float bgBorderWidthDp) {
        this.bgBorderWidthDp = bgBorderWidthDp;
        updateBackgroundChanges();
    }

    /**
     * Get background shape type
     *
     * @return {@link EnBgShapeType}
     */
    @NonNull
    public EnBgShapeType getBgShapeType() {
        return bgShapeType;
    }

    /**
     * Set background shape type
     *
     * @param bgShapeType {@link EnBgShapeType}
     */
    public void setBgShapeType(@NonNull EnBgShapeType bgShapeType) {
        this.bgShapeType = bgShapeType;
        updateBackgroundChanges();
    }

    /**
     * Get the current image src type
     *
     * @return {@link EnSrcType}
     */
    @NonNull
    public EnSrcType getSrcType() {
        return srcType;
    }

    /**
     * Get src of the image as an object
     *
     * @return The result can be one of ({@link java.lang.Integer} or {@link java.lang.String} or null)
     */
    public Object getSrc() {
        return src;
    }

    /**
     * Set src of the image as an object
     *
     * @param src Can be one of ({@link java.lang.Integer} or {@link java.lang.String} or null)
     */
    public void setSrc(Object src) {
        this.src = src;
        if (src instanceof String) {
            srcType = EnSrcType.STRING;
        } else if (src instanceof Integer) {
            srcType = EnSrcType.REFERENCE;
        } else {
            srcType = EnSrcType.UNDEFINED;
        }
    }

    /**
     * Get the default image placeholder resource drawable
     *
     * @return {@link Integer} drawable
     */
    public int getSrcPlaceholder() {
        return srcPlaceholder;
    }

    /**
     * Set the default image placeholder resource drawable
     *
     * @param srcPlaceholder {@link Integer} drawable
     */
    public void setSrcPlaceholder(int srcPlaceholder) {
        this.srcPlaceholder = srcPlaceholder;
    }

    /**
     * Get src error image drawable
     *
     * @return {@link Integer} drawable
     */
    public int getSrcError() {
        return srcError;
    }

    /**
     * Set src error image drawable
     *
     * @param srcError {@link Integer} drawable
     */
    public void setSrcError(int srcError) {
        this.srcError = srcError;
    }

    /**
     * Set {@link EnSrcType} to be applied to current image
     *
     * @param srcType {@link EnSrcType}
     */
    public void setSrcType(@NonNull EnSrcType srcType) {
        this.srcType = srcType;
        updateImageChanges();
    }

    /**
     * Get src scale type applied to current src image
     *
     * @return {@link EnScaleType} the scale type applied to current src image
     */
    @NonNull
    public EnScaleType getSrcScaleType() {
        return srcScaleType;
    }

    /**
     * Set scale type to be applied to current image
     *
     * @param srcScaleType {@link EnScaleType} scale type
     */
    public void setSrcScaleType(@NonNull EnScaleType srcScaleType) {
        this.srcScaleType = srcScaleType;
        updateImageChanges();
    }

    /**
     * Get current resize width of src
     *
     * @return {@link Integer} Width in Pixels
     */
    public int getSrcResizePxWidth() {
        return srcResizePxWidth;
    }

    /**
     * Set current resize width of src
     *
     * @param srcResizePxWidth {@link Integer} Width in Pixels
     */
    public void setSrcResizePxWidth(int srcResizePxWidth) {
        this.srcResizePxWidth = srcResizePxWidth;
        updateImageChanges();
    }

    /**
     * Get current resize height of src
     *
     * @return {@link Integer} Height in Pixels
     */
    public int getSrcResizePxHeight() {
        return srcResizePxHeight;
    }

    /**
     * Set current resize height of src
     *
     * @param srcResizePxHeight {@link Integer} Height in Pixels
     */
    public void setSrcResizePxHeight(int srcResizePxHeight) {
        this.srcResizePxHeight = srcResizePxHeight;
        updateImageChanges();
    }

    /**
     * Get current resize width of src
     *
     * @return {@link Integer} Width in Dips
     */
    public float getSrcResizeDimenWidth() {
        return srcResizeDimenWidth;
    }

    /**
     * Set current resize width of src
     *
     * @param srcResizeDimenWidth {@link Integer} Width in Dips
     */
    public void setSrcResizeDimenWidth(float srcResizeDimenWidth) {
        this.srcResizeDimenWidth = srcResizeDimenWidth;
        updateImageChanges();
    }

    /**
     * Get current resize width of src
     *
     * @return {@link Integer} Width in Dips
     */
    public float getSrcResizeDimenHeight() {
        return srcResizeDimenHeight;
    }

    /**
     * Set current resize height of src
     *
     * @param srcResizeDimenHeight {@link Integer} Height in Dips
     */
    public void setSrcResizeDimenHeight(float srcResizeDimenHeight) {
        this.srcResizeDimenHeight = srcResizeDimenHeight;
        updateImageChanges();
    }

    /**
     * Get the transition effect applied to src when view is attached
     *
     * @return {@link EnTransitionType} Transition effect
     */
    @NonNull
    public EnTransitionType getSrcTransitionType() {
        return srcTransitionType;
    }

    /**
     * Set the transition effect applied to src when view is attached
     *
     * @param srcTransitionType {@link EnTransitionType} Transition effect
     */
    public void setSrcTransitionType(@NonNull EnTransitionType srcTransitionType) {
        this.srcTransitionType = srcTransitionType;
        updateImageChanges();
    }

    /**
     * Get the current src transition duration time in millis
     *
     * @return {@link Integer} Duration in millis
     */
    public int getSrcTransitionDuration() {
        return srcTransitionDuration;
    }

    /**
     * Set the current src transition duration time in millis
     *
     * @param srcTransitionDuration {@link Integer} Duration in millis
     */
    public void setSrcTransitionDuration(int srcTransitionDuration) {
        this.srcTransitionDuration = srcTransitionDuration;
        updateImageChanges();
    }
}
