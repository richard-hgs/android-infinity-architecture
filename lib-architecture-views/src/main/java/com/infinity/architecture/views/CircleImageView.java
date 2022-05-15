package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


public class CircleImageView extends AppCompatImageView {
    public CircleImageView(Context context) {
        super(context);
        init(null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            // MANTÉM O ASPECTO
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (getMeasuredWidth() < getMeasuredHeight()) {
                layoutParams.height = getMeasuredWidth();
            } else if (getMeasuredWidth() > getMeasuredHeight()) {
                layoutParams.width = getMeasuredHeight();
            }
            setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
                int circleColor = typedArray.getColor(R.styleable.CircleImageView_circle_imgv_color, Color.TRANSPARENT);
                int borderColor = typedArray.getColor(R.styleable.CircleImageView_circle_imgv_border_color, Color.TRANSPARENT);
                float borderWidthDp = typedArray.getDimension(R.styleable.CircleImageView_circle_imgv_border_width, 0);
                typedArray.recycle();


                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.OVAL);
                gradientDrawable.setColor(circleColor);
                gradientDrawable.setStroke((int) borderWidthDp, borderColor);

                setBackground(gradientDrawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
