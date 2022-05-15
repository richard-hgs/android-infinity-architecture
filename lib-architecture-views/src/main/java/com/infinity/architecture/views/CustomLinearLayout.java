package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class CustomLinearLayout extends LinearLayout {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgBorderLeftWidth;
    private float bgBorderTopWidth;
    private float bgBorderRightWidth;
    private float bgBorderBottomWidth;

    private float bgRadius;
    private float bgRadiusTopLeft;
    private float bgRadiusTopRight;
    private float bgRadiusBottomLeft;
    private float bgRadiusBottomRight;

    public CustomLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout);

                bgColor = typedArray.getColor(R.styleable.CustomLinearLayout_custom_linlay_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomLinearLayout_custom_linlay_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomLinearLayout_custom_linlay_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomLinearLayout_custom_linlay_bg_radius, 0f);
                bgRadiusTopLeft = typedArray.getFloat(R.styleable.CustomLinearLayout_custom_linlay_bg_radius_top_left, 0f);
                bgRadiusTopRight = typedArray.getFloat(R.styleable.CustomLinearLayout_custom_linlay_bg_radius_top_right, 0f);
                bgRadiusBottomLeft = typedArray.getFloat(R.styleable.CustomLinearLayout_custom_linlay_bg_radius_bottom_left, 0f);
                bgRadiusBottomRight = typedArray.getFloat(R.styleable.CustomLinearLayout_custom_linlay_bg_radius_bottom_right, 0f);

                bgBorderLeftWidth = typedArray.getDimension(R.styleable.CustomLinearLayout_custom_linlay_bg_border_left_width, 0);
                bgBorderTopWidth = typedArray.getDimension(R.styleable.CustomLinearLayout_custom_linlay_bg_border_top_width, 0);
                bgBorderRightWidth = typedArray.getDimension(R.styleable.CustomLinearLayout_custom_linlay_bg_border_right_width, 0);
                bgBorderBottomWidth = typedArray.getDimension(R.styleable.CustomLinearLayout_custom_linlay_bg_border_bottom_width, 0);


                typedArray.recycle();

                configureBackground();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureBackground() {
        configureBackground(false);
    }

    private void configureBackground(boolean canSetNullBackground) {
        if (bgRadius > 0) {
            bgRadiusTopLeft = bgRadius;
            bgRadiusTopRight = bgRadius;
            bgRadiusBottomLeft = bgRadius;
            bgRadiusBottomRight = bgRadius;
        }

        float maxBorderWidthCorner = 0;

        // RECEBE O TAMANHO DA MAIOR BORDA PARA CORRIGIR AS BORDAS NO TAMANHO
        if (maxBorderWidthCorner < bgBorderLeftWidth) {
            maxBorderWidthCorner = bgBorderLeftWidth;
        }
        if (maxBorderWidthCorner < bgBorderTopWidth) {
            maxBorderWidthCorner = bgBorderTopWidth;
        }
        if (maxBorderWidthCorner < bgBorderRightWidth) {
            maxBorderWidthCorner = bgBorderRightWidth;
        }
        if (maxBorderWidthCorner < bgBorderBottomWidth) {
            maxBorderWidthCorner = bgBorderBottomWidth;
        }
        if (maxBorderWidthCorner == 0){
            maxBorderWidthCorner = bgBorderWidth;
        }

        LayerDrawable layerFixBorderWidths = null;
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
            gradientDrawable.setStroke((int) bgBorderWidth, bgBorderColor);
        }
        if (maxBorderWidthCorner != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
            gradientDrawable.setStroke((int) maxBorderWidthCorner, (bgBorderColor == Color.TRANSPARENT ? Color.BLACK : bgBorderColor));
        }
        if (bgRadius != 0 || bgRadiusTopLeft != 0 || bgRadiusTopRight != 0 || bgRadiusBottomLeft != 0 || bgRadiusBottomRight != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }

            float[] cornerRadii = new float[]{bgRadiusTopLeft, bgRadiusTopLeft, bgRadiusTopRight, bgRadiusTopRight, bgRadiusBottomRight, bgRadiusBottomRight, bgRadiusBottomLeft, bgRadiusBottomLeft};
            gradientDrawable.setCornerRadii(cornerRadii);
        }

        // CORRECAO DAS BORDAS MENORES DO QUE A MAIOR BORDA
        if (bgBorderLeftWidth > 0 || bgBorderTopWidth > 0 || bgBorderRightWidth > 0 || bgBorderBottomWidth > 0) {
            layerFixBorderWidths = new LayerDrawable(new Drawable[]{gradientDrawable});
            layerFixBorderWidths.setLayerInset(0,
                    (int)(bgBorderLeftWidth < maxBorderWidthCorner ? bgBorderLeftWidth - maxBorderWidthCorner : 0),
                    (int)(bgBorderTopWidth < maxBorderWidthCorner ? bgBorderTopWidth - maxBorderWidthCorner : 0),
                    (int)(bgBorderRightWidth < maxBorderWidthCorner ? bgBorderRightWidth - maxBorderWidthCorner : 0),
                    (int)(bgBorderBottomWidth < maxBorderWidthCorner ? bgBorderBottomWidth - maxBorderWidthCorner : 0)
            );
        }

        if (layerFixBorderWidths != null) {
            setBackground(layerFixBorderWidths);
        } else if (gradientDrawable != null) {
            setBackground(gradientDrawable);
        } else if (canSetNullBackground) {
            setBackground(null);
        }
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        configureBackground(true);
    }

    public int getBgBorderColor() {
        return bgBorderColor;
    }

    public void setBgBorderColor(int bgBorderColor) {
        this.bgBorderColor = bgBorderColor;
        configureBackground(true);
    }

    public float getBgBorderWidth() {
        return bgBorderWidth;
    }

    public void setBgBorderWidth(float bgBorderWidth) {
        this.bgBorderWidth = bgBorderWidth;
        configureBackground(true);
    }

    public float getBgBorderLeftWidth() {
        return bgBorderLeftWidth;
    }

    public void setBgBorderLeftWidth(float bgBorderLeftWidth) {
        this.bgBorderLeftWidth = bgBorderLeftWidth;
        configureBackground(true);
    }

    public float getBgBorderTopWidth() {
        return bgBorderTopWidth;
    }

    public void setBgBorderTopWidth(float bgBorderTopWidth) {
        this.bgBorderTopWidth = bgBorderTopWidth;
        configureBackground(true);
    }

    public float getBgBorderRightWidth() {
        return bgBorderRightWidth;
    }

    public void setBgBorderRightWidth(float bgBorderRightWidth) {
        this.bgBorderRightWidth = bgBorderRightWidth;
        configureBackground(true);
    }

    public float getBgBorderBottomWidth() {
        return bgBorderBottomWidth;
    }

    public void setBgBorderBottomWidth(float bgBorderBottomWidth) {
        this.bgBorderBottomWidth = bgBorderBottomWidth;
        configureBackground(true);
    }

    public float getBgRadius() {
        return bgRadius;
    }

    public void setBgRadius(float bgRadius) {
        this.bgRadius = bgRadius;
        configureBackground(true);
    }

    public float getBgRadiusTopLeft() {
        return bgRadiusTopLeft;
    }

    public void setBgRadiusTopLeft(float bgRadiusTopLeft) {
        this.bgRadiusTopLeft = bgRadiusTopLeft;
        configureBackground(true);
    }

    public float getBgRadiusTopRight() {
        return bgRadiusTopRight;
    }

    public void setBgRadiusTopRight(float bgRadiusTopRight) {
        this.bgRadiusTopRight = bgRadiusTopRight;
        configureBackground(true);
    }

    public float getBgRadiusBottomLeft() {
        return bgRadiusBottomLeft;
    }

    public void setBgRadiusBottomLeft(float bgRadiusBottomLeft) {
        this.bgRadiusBottomLeft = bgRadiusBottomLeft;
        configureBackground(true);
    }

    public float getBgRadiusBottomRight() {
        return bgRadiusBottomRight;
    }

    public void setBgRadiusBottomRight(float bgRadiusBottomRight) {
        this.bgRadiusBottomRight = bgRadiusBottomRight;
        configureBackground(true);
    }
}
