package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.ObservableField;

import android.util.AttributeSet;
import android.view.ViewGroup;

public class CustomTextView extends AppCompatTextView {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgRadius;
    private float bgRadiusTopLeft;
    private float bgRadiusTopRight;
    private float bgRadiusBottomLeft;
    private float bgRadiusBottomRight;

    private ObservableField<ViewGroup.LayoutParams> layParamsState = new ObservableField<>();

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);

                bgColor = typedArray.getColor(R.styleable.CustomTextView_custom_text_view_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomTextView_custom_text_view_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomTextView_custom_text_view_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomTextView_custom_text_view_bg_radius, 0f);
                bgRadiusTopLeft = typedArray.getFloat(R.styleable.CustomTextView_custom_text_view_bg_radius_top_left, 0f);
                bgRadiusTopRight = typedArray.getFloat(R.styleable.CustomTextView_custom_text_view_bg_radius_top_right, 0f);
                bgRadiusBottomLeft = typedArray.getFloat(R.styleable.CustomTextView_custom_text_view_bg_radius_bottom_left, 0f);
                bgRadiusBottomRight = typedArray.getFloat(R.styleable.CustomTextView_custom_text_view_bg_radius_bottom_right, 0f);

                typedArray.recycle();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (layParamsState.get() == null) {
            layParamsState.set(getLayoutParams());
        }
    }

    public ObservableField<ViewGroup.LayoutParams> getLayParamsState() {
        return layParamsState;
    }
}
