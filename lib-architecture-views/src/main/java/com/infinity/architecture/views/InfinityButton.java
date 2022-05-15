package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.infinity.architecture.utils.drawable.DrawableHelper;
import com.infinity.architecture.utils.drawable.EnBgShapeType;

@SuppressWarnings("unused")
public class InfinityButton extends AppCompatButton {
    // TAG
    private static final String TAG = "InfinityButton";

    // Styles variables
    private int           bgColor;
    private int           bgRadius;
    private int           bgRadiusTopLeft;
    private int           bgRadiusTopRight;
    private int           bgRadiusBottomRight;
    private int           bgRadiusBottomLeft;
    private int           bgBorderColor;
    private float         bgBorderWidthDp;
    @NonNull
    private EnBgShapeType bgShapeType = EnBgShapeType.UNDEFINED;

    public InfinityButton(Context context) {
        this(context, null);
    }

    public InfinityButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.infinityButtonStyle);
    }

    public InfinityButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // RECEBE OS ATRIBUTOS
        TypedArray taAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.InfinityButton, defStyleAttr, R.style.InfinityButtonStyle);
        bgColor             = taAttrs.getColor(R.styleable.InfinityButton_bg_color, Color.LTGRAY);
        bgRadius            = taAttrs.getInt(R.styleable.InfinityButton_bg_radius, 0);
        bgRadiusTopLeft     = taAttrs.getInt(R.styleable.InfinityButton_bg_radius_top_left, 0);
        bgRadiusTopRight    = taAttrs.getInt(R.styleable.InfinityButton_bg_radius_top_right, 0);
        bgRadiusBottomRight = taAttrs.getInt(R.styleable.InfinityButton_bg_radius_bottom_right, 0);
        bgRadiusBottomLeft  = taAttrs.getInt(R.styleable.InfinityButton_bg_radius_bottom_left, 0);
        bgBorderColor       = taAttrs.getColor(R.styleable.InfinityButton_bg_border_color, Color.TRANSPARENT);
        bgBorderWidthDp     = taAttrs.getDimension(R.styleable.InfinityButton_bg_border_width, 0);
        bgShapeType         = EnBgShapeType.values()[taAttrs.getInt(R.styleable.InfinityButton_bg_shape_type, DrawableHelper.SHAPE_TYPE_RECTANGLE)];
        taAttrs.recycle();

        if (bgRadius > 0) {
            bgRadiusTopLeft     = bgRadius;
            bgRadiusTopRight    = bgRadius;
            bgRadiusBottomLeft  = bgRadius;
            bgRadiusBottomRight = bgRadius;
        }

        updateBackgroundChanges();
    }

    /**
     * Update the background changes to view
     */
    private void updateBackgroundChanges() {
        // CONFIGURA O BACKGROUND
        setBackground(DrawableHelper.getAdaptiveRippleDrawableRadius(bgColor, bgRadius, bgRadiusTopLeft, bgRadiusTopRight, bgRadiusBottomRight, bgRadiusBottomLeft, bgShapeType, (int) bgBorderWidthDp, bgBorderColor));
    }

    /**
     * Get the background color
     *
     * @return int color
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * Set the background color
     *
     * @param bgColor int color
     */
    public void setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
        updateBackgroundChanges();
    }

    /**
     * Get background corners radius
     *
     * @return int radius
     */
    public int getBgRadius() {
        return bgRadius;
    }

    /**
     * Set background corners radius
     *
     * @param bgRadius int radius
     */
    public void setBgRadius(int bgRadius) {
        this.bgRadius = bgRadius;
        updateBackgroundChanges();
    }

    /**
     * Get background top_left corner radius
     *
     * @return int top_left radius
     */
    public int getBgRadiusTopLeft() {
        return bgRadiusTopLeft;
    }

    /**
     * Set background top_left corner radius
     *
     * @param bgRadiusTopLeft int radius
     */
    public void setBgRadiusTopLeft(int bgRadiusTopLeft) {
        this.bgRadiusTopLeft = bgRadiusTopLeft;
        updateBackgroundChanges();
    }

    /**
     * Get background top_right corner radius
     *
     * @return int radius
     */
    public int getBgRadiusTopRight() {
        return bgRadiusTopRight;
    }

    /**
     * Set background top_right corner radius
     *
     * @param bgRadiusTopRight int radius
     */
    public void setBgRadiusTopRight(int bgRadiusTopRight) {
        this.bgRadiusTopRight = bgRadiusTopRight;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_right corner radius
     *
     * @return int radius
     */
    public int getBgRadiusBottomRight() {
        return bgRadiusBottomRight;
    }

    /**
     * Set background bottom_right corner radius
     *
     * @param bgRadiusBottomRight int radius
     */
    public void setBgRadiusBottomRight(int bgRadiusBottomRight) {
        this.bgRadiusBottomRight = bgRadiusBottomRight;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_left corner radius
     *
     * @return int radius
     */
    public int getBgRadiusBottomLeft() {
        return bgRadiusBottomLeft;
    }

    /**
     * Set background bottom_left corner radius
     *
     * @param bgRadiusBottomLeft int radius
     */
    public void setBgRadiusBottomLeft(int bgRadiusBottomLeft) {
        this.bgRadiusBottomLeft = bgRadiusBottomLeft;
        updateBackgroundChanges();
    }

    /**
     * Get background border color
     *
     * @return int border color
     */
    public int getBgBorderColor() {
        return bgBorderColor;
    }

    /**
     * Set background border color
     *
     * @param bgBorderColor int border color
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
}
