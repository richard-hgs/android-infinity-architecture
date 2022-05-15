package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class CustomRecyclerView extends RecyclerView {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgRadius;

    public CustomRecyclerView(Context context) {
        super(context);
        init(null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView);

                bgColor = typedArray.getColor(R.styleable.CustomRecyclerView_custom_rv_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomRecyclerView_custom_rv_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomRecyclerView_custom_rv_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomRecyclerView_custom_rv_bg_radius, 0f);

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
