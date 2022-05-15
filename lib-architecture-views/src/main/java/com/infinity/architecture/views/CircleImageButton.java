package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.infinity.architecture.utils.drawable.DrawableHelper;
import com.infinity.architecture.utils.drawable.EnBgShapeType;


public class CircleImageButton extends AppCompatImageButton {
    private final String TAG = "CircleImageButton";

    private int circleColor;
    private int bgBorderWidth;
    private int bgBorderColor;
    private int bgBorderDashSpace;
    private int padding;

    public CircleImageButton(Context context) {
        super(context);
        init(null);
    }

    public CircleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageButton);
                circleColor = typedArray.getColor(R.styleable.CircleImageButton_circle_imgbtn_color, Color.parseColor("#DDDDDD"));
                bgBorderWidth = typedArray.getInteger(R.styleable.CircleImageButton_circle_imgbtn_border_width, 0);
                bgBorderColor = typedArray.getColor(R.styleable.CircleImageButton_circle_imgbtn_border_color, Color.TRANSPARENT);
                bgBorderDashSpace = typedArray.getInteger(R.styleable.CircleImageButton_circle_imgbtn_border_dash_space, 0);
                padding = (int) typedArray.getDimension(R.styleable.CircleImageButton_android_padding, 0);
                typedArray.recycle();

                setBtnColor(circleColor);

                // padding = DimenUtils.dpToPixel(getContext(), (int) paddingDimen);
                setPaddingRelative(padding, padding, padding, padding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDrawable() {
        setBackground(
            DrawableHelper.getAdaptiveRippleDrawableRadiusShape(
                circleColor,
                0,
                EnBgShapeType.OVAL,
                bgBorderWidth,
                bgBorderColor,
                bgBorderDashSpace
            )
        );
    }

    @ColorInt
    public int getBtnColor() {
        return circleColor;
    }

    public void setBtnColor(@ColorInt int color) {
        this.circleColor = color;
        updateDrawable();
    }

    public int getBorderWidth() {
        return bgBorderWidth;
    }

    public void setBorderWidth(int bgBorderWidth) {
        this.bgBorderWidth = bgBorderWidth;
        updateDrawable();
    }

    public int getBorderColor() {
        return bgBorderColor;
    }

    public void setBorderColor(int bgBorderColor) {
        this.bgBorderColor = bgBorderColor;
        updateDrawable();
    }

    public int getBorderDashSpace() {
        return bgBorderDashSpace;
    }

    public void setBorderDashSpace(int bgBorderDashSpace) {
        this.bgBorderDashSpace = bgBorderDashSpace;
        updateDrawable();
    }
}
