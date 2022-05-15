package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;


public class CustomRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgRadius;

    public CustomRadioButton(Context context) {
        super(context);
        init(null);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRadioButton);

                bgColor = typedArray.getColor(R.styleable.CustomRadioButton_custom_radbtn_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomRadioButton_custom_radbtn_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomRadioButton_custom_radbtn_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomRadioButton_custom_radbtn_bg_radius, 0f);

                typedArray.recycle();



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
                if (bgRadius != 0) {
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                    }
                    gradientDrawable.setCornerRadius(bgRadius);
                }

                if (gradientDrawable != null) {
                    setBackground(gradientDrawable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
