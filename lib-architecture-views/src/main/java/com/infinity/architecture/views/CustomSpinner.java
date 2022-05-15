package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;


public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner {

    private int bgColor;
    private int bgBorderColor;

    private float bgBorderWidth;
    private float bgRadius;

    public CustomSpinner(Context context) {
        super(context);
        init(null);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        init(null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        try {
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner);

                bgColor = typedArray.getColor(R.styleable.CustomSpinner_custom_spinner_bg_color, Color.TRANSPARENT);
                bgBorderColor = typedArray.getColor(R.styleable.CustomSpinner_custom_spinner_bg_border_color, Color.TRANSPARENT);
                bgBorderWidth = typedArray.getDimension(R.styleable.CustomSpinner_custom_spinner_bg_border_width, 0);
                bgRadius = typedArray.getFloat(R.styleable.CustomSpinner_custom_spinner_bg_radius, 0f);

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

    /**
     * RETORNA O SPINNER PADRAO
     * @param context   CONTEXTO DA APLICACAO
     * @param array     LISTA CONTENDO OS DADOS
     * @param <T>       TIPO DO ITEM DA LISTA
     * @return          ADAPTER CONTENDO AS INFORMACOES
     */
    public static <T> ArrayAdapter<T> getDefSpinAdapter(Context context, T[] array) {
        ArrayAdapter<T> aa = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, array);

        aa.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );

        return aa;
    }
}
