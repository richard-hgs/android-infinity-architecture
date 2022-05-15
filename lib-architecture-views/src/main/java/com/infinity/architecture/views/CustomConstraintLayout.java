package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.ObservableField;

import android.util.AttributeSet;


public class CustomConstraintLayout extends ConstraintLayout {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgRadius;
    private float bgRadiusTopLeft;
    private float bgRadiusTopRight;
    private float bgRadiusBottomLeft;
    private float bgRadiusBottomRight;

    private ObservableField<ConstraintSet> constraintSetState = new ObservableField<>();
    ConstraintSet constraintSet = new ConstraintSet();

    public CustomConstraintLayout(Context context) {
        super(context);
        init(null);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomConstraintLayout);

                bgColor = typedArray.getColor(R.styleable.CustomConstraintLayout_custom_constlay_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomConstraintLayout_custom_constlay_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomConstraintLayout_custom_constlay_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomConstraintLayout_custom_constlay_bg_radius, 0f);
                bgRadiusTopLeft = typedArray.getFloat(R.styleable.CustomConstraintLayout_custom_constlay_bg_radius_top_left, 0f);
                bgRadiusTopRight = typedArray.getFloat(R.styleable.CustomConstraintLayout_custom_constlay_bg_radius_top_right, 0f);
                bgRadiusBottomLeft = typedArray.getFloat(R.styleable.CustomConstraintLayout_custom_constlay_bg_radius_bottom_left, 0f);
                bgRadiusBottomRight = typedArray.getFloat(R.styleable.CustomConstraintLayout_custom_constlay_bg_radius_bottom_right, 0f);

                typedArray.recycle();

                updateBg();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (constraintSetState.get() == null) {
            constraintSet.clone(this);
            constraintSetState.set(constraintSet);
        }
    }

    private void updateBg() {
        if (bgRadius > 0) {
            bgRadiusTopLeft = bgRadius;
            bgRadiusTopRight = bgRadius;
            bgRadiusBottomLeft = bgRadius;
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
            gradientDrawable.setStroke((int) bgBorderWidth, bgBorderColor);
        }
        if (bgBorderWidth != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
            gradientDrawable.setStroke((int) bgBorderWidth, (bgBorderColor == Color.TRANSPARENT ? Color.BLACK : bgBorderColor));
        }
        if (bgRadius != 0 || bgRadiusTopLeft != 0 || bgRadiusTopRight != 0 || bgRadiusBottomLeft != 0 || bgRadiusBottomRight != 0) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }

            float[] cornerRadii = new float[]{bgRadiusTopLeft, bgRadiusTopLeft, bgRadiusTopRight, bgRadiusTopRight, bgRadiusBottomRight, bgRadiusBottomRight, bgRadiusBottomLeft, bgRadiusBottomLeft};
            gradientDrawable.setCornerRadii(cornerRadii);
        }

        if (gradientDrawable != null) {
            setBackground(gradientDrawable);
        }
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        updateBg();
    }

    public int getBgBorderColor() {
        return bgBorderColor;
    }

    public void setBgBorderColor(int bgBorderColor) {
        this.bgBorderColor = bgBorderColor;
        updateBg();
    }

    public float getBgBorderWidth() {
        return bgBorderWidth;
    }

    public void setBgBorderWidth(float bgBorderWidth) {
        this.bgBorderWidth = bgBorderWidth;
        updateBg();
    }

    public float getBgRadius() {
        return bgRadius;
    }

    public void setBgRadius(float bgRadius) {
        this.bgRadius = bgRadius;
        updateBg();
    }

    public float getBgRadiusTopLeft() {
        return bgRadiusTopLeft;
    }

    public void setBgRadiusTopLeft(float bgRadiusTopLeft) {
        this.bgRadiusTopLeft = bgRadiusTopLeft;
        updateBg();
    }

    public float getBgRadiusTopRight() {
        return bgRadiusTopRight;
    }

    public void setBgRadiusTopRight(float bgRadiusTopRight) {
        this.bgRadiusTopRight = bgRadiusTopRight;
        updateBg();
    }

    public float getBgRadiusBottomLeft() {
        return bgRadiusBottomLeft;
    }

    public void setBgRadiusBottomLeft(float bgRadiusBottomLeft) {
        this.bgRadiusBottomLeft = bgRadiusBottomLeft;
        updateBg();
    }

    public float getBgRadiusBottomRight() {
        return bgRadiusBottomRight;
    }

    public void setBgRadiusBottomRight(float bgRadiusBottomRight) {
        this.bgRadiusBottomRight = bgRadiusBottomRight;
        updateBg();
    }

    public ObservableField<ConstraintSet> getConstraintSetState() {
        return constraintSetState;
    }
}
