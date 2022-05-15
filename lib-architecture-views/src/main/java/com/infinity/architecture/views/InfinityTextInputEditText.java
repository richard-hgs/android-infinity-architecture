package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.drawable.DrawableHelper;
import com.infinity.architecture.utils.drawable.EnBgShapeType;
import com.infinity.architecture.utils.locale.LocaleHelper;
import com.infinity.architecture.utils.masks.MaskEditFormatValid;
import com.infinity.architecture.utils.masks.MaskEditType;
import com.infinity.architecture.utils.masks.MaskEditUtil;
import com.infinity.architecture.views.enums.EnDrawablePos;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings("unused")
public class InfinityTextInputEditText extends TextInputEditText {

    private static final String TAG = "InfinityTextInputEditText";

    // Styles variables
    private int bgColor;
    private int bgColorFocused;
    private int bgBorderColor;
    private int bgBorderColorFocused;

    private float bgBorderWidth;
    private float bgBorderWidthFocused;

    private int bgRadius;
    private int bgRadiusFocused;
    private int bgRadiusTopLeft;
    private int bgRadiusTopLeftFocused;
    private int bgRadiusTopRight;
    private int bgRadiusTopRightFocused;
    private int bgRadiusBottomLeft;
    private int bgRadiusBottomLeftFocused;
    private int bgRadiusBottomRight;
    private int bgRadiusBottomRightFocused;

    private EnBgShapeType bgShapeType;

    private MaskEditType maskType;

    // Mask edit variables
    @Nullable
    private MaskEditUtil maskEditUtil;
    private String       maskFormat;
    private boolean      maskReverse;
    private boolean      allowPassMaskLength;

    private int     monetaryMaskDecimalPlaces = 2;
    private int     monetaryMaskLocale        = 0;
    private boolean monetaryMaskShowCurrency  = true;
    private String  customMonetaryCurrency;

    // Listeners/Watchers variables
    @Nullable
    private DrawableClickListener drawableClickListener;

    private ArrayList<TextWatcher> textChangeListeners = new ArrayList<>();

    private final TextWatcher maskEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            for (TextWatcher cursor : textChangeListeners) {
                cursor.beforeTextChanged(s, start, count, after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for (TextWatcher cursor : textChangeListeners) {
                cursor.onTextChanged(s, start, before, count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            for (TextWatcher cursor : textChangeListeners) {
                cursor.afterTextChanged(s);
            }
        }
    };

    public InfinityTextInputEditText(Context context) {
        this(context, null);
    }

    public InfinityTextInputEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public InfinityTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.addTextChangedListener(maskEditTextWatcher);
        TypedArray taAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.InfinityTextInputEditText, R.attr.infinityTextInputEditTextStyle, R.style.InfinityTextInputEditTextStyle);

//            Log.d(TAG, "attrs count: " + attrs.getAttributeCount());
//            for (int i=0; i<attrs.getAttributeCount(); i++) {
//                Log.d(TAG, "attrs(" + attrs.getAttributeNamespace(i) + " - " + attrs.getAttributeName(i) + "): " + attrs.getAttributeValue(i));
//            }

        bgColor                    = taAttrs.getColor(R.styleable.InfinityTextInputEditText_bg_color, Color.TRANSPARENT);
        bgColorFocused             = taAttrs.getColor(R.styleable.InfinityTextInputEditText_bg_color_focused, Color.TRANSPARENT);
        bgBorderColor              = taAttrs.getColor(R.styleable.InfinityTextInputEditText_bg_border_color, Color.TRANSPARENT);
        bgBorderColorFocused       = taAttrs.getColor(R.styleable.InfinityTextInputEditText_bg_border_color_focused, Color.TRANSPARENT);
        bgBorderWidth              = taAttrs.getDimension(R.styleable.InfinityTextInputEditText_bg_border_width, 0);
        bgBorderWidthFocused       = taAttrs.getDimension(R.styleable.InfinityTextInputEditText_bg_border_width_focused, -1);
        bgRadius                   = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius, 0);
        bgRadiusFocused            = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_focused, 0);
        bgRadiusTopLeft            = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_top_left, 0);
        bgRadiusTopLeftFocused     = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_top_left_focused, 0);
        bgRadiusTopRight           = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_top_right, 0);
        bgRadiusTopRightFocused    = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_top_right_focused, 0);
        bgRadiusBottomLeft         = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_bottom_left, 0);
        bgRadiusBottomLeftFocused  = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_bottom_left_focused, 0);
        bgRadiusBottomRight        = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_bottom_right, 0);
        bgRadiusBottomRightFocused = taAttrs.getInteger(R.styleable.InfinityTextInputEditText_bg_radius_bottom_right_focused, 0);
        bgShapeType                = EnBgShapeType.values()[taAttrs.getInt(R.styleable.InfinityTextInputEditText_bg_shape_type, DrawableHelper.SHAPE_TYPE_RECTANGLE)];

        maskType                  = MaskEditType.values()[taAttrs.getInt(R.styleable.InfinityTextInputEditText_mask_type, MaskEditType.NONE.ordinal())];
        monetaryMaskDecimalPlaces = taAttrs.getInt(R.styleable.InfinityTextInputEditText_mask_monetary_decimal_places, 2);
        monetaryMaskLocale        = taAttrs.getInt(R.styleable.InfinityTextInputEditText_mask_monetary_locale, 0);
        monetaryMaskShowCurrency  = taAttrs.getBoolean(R.styleable.InfinityTextInputEditText_mask_monetary_show_currency, true);
        customMonetaryCurrency    = taAttrs.getString(R.styleable.InfinityTextInputEditText_mask_monetary_currency);
        allowPassMaskLength        = taAttrs.getBoolean(R.styleable.InfinityTextInputEditText_mask_allow_pass_mask_length, false);

        taAttrs.recycle();

        if (bgRadius > 0) {
            bgRadiusTopLeft     = bgRadius;
            bgRadiusTopRight    = bgRadius;
            bgRadiusBottomLeft  = bgRadius;
            bgRadiusBottomRight = bgRadius;
        }

        if (bgRadiusFocused > 0) {
            bgRadiusTopLeftFocused     = bgRadiusFocused;
            bgRadiusTopRightFocused    = bgRadiusFocused;
            bgRadiusBottomLeftFocused  = bgRadiusFocused;
            bgRadiusBottomRightFocused = bgRadiusFocused;
        }

        if (bgRadiusFocused == 0) {
            bgRadiusFocused = bgRadius;
        }
        if (bgRadiusTopLeftFocused == 0) {
            bgRadiusTopLeftFocused = bgRadiusTopLeft;
        }
        if (bgRadiusTopRightFocused == 0) {
            bgRadiusTopRightFocused = bgRadiusTopRight;
        }
        if (bgRadiusBottomLeftFocused == 0) {
            bgRadiusBottomLeftFocused = bgRadiusBottomLeft;
        }
        if (bgRadiusBottomRightFocused == 0) {
            bgRadiusBottomRightFocused = bgRadiusBottomRight;
        }


        if (bgBorderWidthFocused == 0) {
            bgBorderWidthFocused = bgBorderWidth;
        }

        if (bgBorderColorFocused == Color.TRANSPARENT) {
            bgBorderColorFocused = bgBorderColor;
        }

        if (bgColorFocused == Color.TRANSPARENT) {
            bgColorFocused = bgColor;
        }

        updateBackgroundChanges();
        updateMaskChanges();

//        if (maskType != MaskEditType.NONE) {
//            // RECEBE O FORMATO DA MASCARA PELO TIPO
//            maskFormat = MaskEditUtil.getMaskFormatFromMaskType(maskType);
//
//            if (maskType == MaskEditType.MONETARY || maskFormat != null && maskFormat.length() > 0 && maskFormat.contains("#")) {
//                maskEditUtil = new MaskEditUtil(this, maskFormat, maskType);
//                maskEditUtil.setMonetaryMaskDecimalPlaces(monetaryMaskDecimalPlaces);
//                maskEditUtil.setMonetaryMaskLocale(LocaleHelper.getLocaleByIntType(monetaryMaskLocale));
//                maskEditUtil.setMonetaryMaskShowCurrency(monetaryMaskShowCurrency);
//                maskEditUtil.setCustomMonetaryCurrency(customMonetaryCurrency);
//                maskEditUtil.setTextChangedListener(maskEditTextWatcher);
//                super.removeTextChangedListener(maskEditTextWatcher);
//                super.addTextChangedListener(maskEditUtil);
//            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // VALIDA O CLICK DO BOTAO TOGGLE SE TIVER HABILITADO
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean clickHandled = false;
            Drawable[] compoundDrawables = getDrawables();

            if (compoundDrawables[EnDrawablePos.RIGHT.ordinal()] != null && event.getRawX() >= (getRight() - (compoundDrawables[EnDrawablePos.RIGHT.ordinal()].getBounds().width() + (getCompoundDrawablePadding() * 2) + getPaddingEnd()))) {
                if (drawableClickListener != null) {
                    clickHandled = drawableClickListener.onDrawableRightClick();
                }
                // return true;
            } else if (compoundDrawables[EnDrawablePos.LEFT.ordinal()] != null && event.getX() <= (getLeft() + (compoundDrawables[EnDrawablePos.LEFT.ordinal()]).getBounds().width() + (getCompoundDrawablePadding() * 2) + getPaddingStart())) {
                if (drawableClickListener != null) {
                    clickHandled = drawableClickListener.onDrawableLeftClick();
                }
            } else if (
                compoundDrawables[EnDrawablePos.BOTTOM.ordinal()] != null && event.getY() >= (getHeight() - (compoundDrawables[EnDrawablePos.BOTTOM.ordinal()]).getBounds().height() - (getCompoundDrawablePadding() * 2)) &&
                event.getY() <= (getHeight() - getPaddingBottom()) &&
                event.getX() >= (getWidth() / 2f - compoundDrawables[EnDrawablePos.BOTTOM.ordinal()].getBounds().width() / 2f) &&
                event.getX() <= (getWidth() / 2f + compoundDrawables[EnDrawablePos.BOTTOM.ordinal()].getBounds().width() / 2f)
            ) {
                if (drawableClickListener != null) {
                    clickHandled = drawableClickListener.onDrawableBottomClick();
                }
            } else if (
                compoundDrawables[EnDrawablePos.TOP.ordinal()] != null && event.getY() <= ((compoundDrawables[EnDrawablePos.TOP.ordinal()]).getBounds().height() + (getCompoundDrawablePadding() * 2)) &&
                event.getY() >= (getPaddingTop()) &&
                event.getX() >= (getWidth() / 2f - compoundDrawables[EnDrawablePos.TOP.ordinal()].getBounds().width() / 2f) &&
                event.getX() <= (getWidth() / 2f + compoundDrawables[EnDrawablePos.TOP.ordinal()].getBounds().width() / 2f)
            ) {
                if (drawableClickListener != null) {
                    clickHandled = drawableClickListener.onDrawableTopClick();
                }
            }

            if (clickHandled) {
                performClick();
                event.setAction(MotionEvent.ACTION_CANCEL);
            } else if (!hasFocus()) {
                performClick();
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Overrided to allow mask formatter text watchers and user text watchers
     *
     * @param watcher TextWatcher
     */
    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (textChangeListeners == null) {
            textChangeListeners = new ArrayList<>();
        }
        this.textChangeListeners.add(watcher);
    }

    /**
     * Overrided to allow mask formatter text watchers and user text watchers
     *
     * @param watcher TextWatcher
     */
    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        this.textChangeListeners.remove(watcher);
    }

    /**
     * Get all drawables of this edit making more important the relative drawables
     *
     * @return Drawable[]
     */
    @NonNull
    private Drawable[] getDrawables() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();

        return new Drawable[] {
            compoundDrawablesRelative[EnDrawablePos.LEFT.ordinal()] != null ? compoundDrawablesRelative[EnDrawablePos.LEFT.ordinal()] : compoundDrawables[EnDrawablePos.LEFT.ordinal()],
            compoundDrawablesRelative[EnDrawablePos.TOP.ordinal()] != null ? compoundDrawablesRelative[EnDrawablePos.TOP.ordinal()] : compoundDrawables[EnDrawablePos.TOP.ordinal()],
            compoundDrawablesRelative[EnDrawablePos.RIGHT.ordinal()] != null ? compoundDrawablesRelative[EnDrawablePos.RIGHT.ordinal()] : compoundDrawables[EnDrawablePos.RIGHT.ordinal()],
            compoundDrawablesRelative[EnDrawablePos.BOTTOM.ordinal()] != null ? compoundDrawablesRelative[EnDrawablePos.BOTTOM.ordinal()] : compoundDrawables[EnDrawablePos.BOTTOM.ordinal()],
        };
    }

    /**
     * Update/Configure background changes
     */
    private void updateBackgroundChanges() {
        // CONFIGURA O BACKGROUND
        if (
            bgColor != Color.TRANSPARENT ||
            bgColorFocused != Color.TRANSPARENT ||
            bgRadius > 0 ||
            bgRadiusFocused > 0 ||
            bgRadiusTopLeft > 0 ||
            bgRadiusTopLeftFocused > 0 ||
            bgRadiusTopRight > 0 ||
            bgRadiusTopRightFocused > 0 ||
            bgRadiusBottomRight > 0 ||
            bgRadiusBottomRightFocused > 0 ||
            bgRadiusBottomLeft > 0 ||
            bgRadiusBottomLeftFocused > 0 ||
            bgBorderWidth > 0 ||
            bgBorderWidthFocused > 0 ||
            bgBorderColor != Color.TRANSPARENT ||
            bgBorderColorFocused != Color.TRANSPARENT
        ) {
            DrawableHelper.setBackgroundKeepingPaddings(
                this,
                DrawableHelper.getDrawable(
                    bgColor, bgColorFocused,
                    bgRadius, bgRadiusFocused,
                    bgRadiusTopLeft, bgRadiusTopLeftFocused,
                    bgRadiusTopRight, bgRadiusTopRightFocused,
                    bgRadiusBottomRight, bgRadiusBottomRightFocused,
                    bgRadiusBottomLeft, bgRadiusBottomLeftFocused,
                    bgShapeType,
                    (int) bgBorderWidth, (int) bgBorderWidthFocused,
                    bgBorderColor, bgBorderColorFocused,
                    0,
                    false
                )
            );
        }
//        else if (getBackground() != defaultBg) {
//            setBackground(defaultBg);
//        }
    }

    /**
     * Update/Configure the initial mask formatter
     */
    private void updateMaskChanges() {
        if (maskType != MaskEditType.NONE) {
            // Get mask type by mask format
            maskFormat = MaskEditUtil.getMaskFormatFromMaskType(maskType);

            if (maskType == MaskEditType.MONETARY || maskFormat != null && maskFormat.length() > 0 && maskFormat.contains("#")) {
                maskEditUtil = new MaskEditUtil(this, maskFormat, maskType);
                maskEditUtil.setMonetaryMaskDecimalPlaces(monetaryMaskDecimalPlaces);
                maskEditUtil.setMonetaryMaskLocale(LocaleHelper.getLocaleByIntType(monetaryMaskLocale));
                maskEditUtil.setMonetaryMaskShowCurrency(monetaryMaskShowCurrency);
                maskEditUtil.setAllowPassMaskLength(allowPassMaskLength);
                maskEditUtil.setTextChangedListener(maskEditTextWatcher);
                super.removeTextChangedListener(maskEditTextWatcher);
                super.addTextChangedListener(maskEditUtil);
            }
        }
    }

    /**
     * Get current background color
     *
     * @return  int color
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * Change background color
     *
     * @param bgColor   int color
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        updateBackgroundChanges();
    }

    /**
     * Get focused background color
     *
     * @return int color
     */
    public int getBgColorFocused() {
        return bgColorFocused;
    }

    /**
     * Set focused background color
     *
     * @param bgColorFocused    int color
     */
    public void setBgColorFocused(int bgColorFocused) {
        this.bgColorFocused = bgColorFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background border color
     *
     * @return  int color
     */
    public int getBgBorderColor() {
        return bgBorderColor;
    }

    /**
     * Set background border color
     *
     * @param bgBorderColor int color
     */
    public void setBgBorderColor(int bgBorderColor) {
        this.bgBorderColor = bgBorderColor;
        updateBackgroundChanges();
    }

    /**
     * Get background focused border color
     *
     * @return  int color
     */
    public int getBgBorderColorFocused() {
        return bgBorderColorFocused;
    }

    /**
     * Set background focused border color
     *
     * @param bgBorderColorFocused  int color
     */
    public void setBgBorderColorFocused(int bgBorderColorFocused) {
        this.bgBorderColorFocused = bgBorderColorFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background border width
     *
     * @return  float width
     */
    public float getBgBorderWidth() {
        return bgBorderWidth;
    }

    /**
     * Set background border width
     *
     * @param bgBorderWidth float width
     */
    public void setBgBorderWidth(float bgBorderWidth) {
        this.bgBorderWidth = bgBorderWidth;
        updateBackgroundChanges();
    }

    /**
     * Get background border width focused
     *
     * @return float width
     */
    public float getBgBorderWidthFocused() {
        return bgBorderWidthFocused;
    }

    /**
     * Set background border width focused
     *
     * @param bgBorderWidthFocused  float width
     */
    public void setBgBorderWidthFocused(float bgBorderWidthFocused) {
        this.bgBorderWidthFocused = bgBorderWidthFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background corners radius
     *
     * @return  radius
     */
    public int getBgRadius() {
        return bgRadius;
    }

    /**
     * Set background corners radius
     *
     * @param bgRadius radius
     */
    public void setBgRadius(int bgRadius) {
        this.bgRadius = bgRadius;
        updateBackgroundChanges();
    }

    /**
     * Get background focused corners radius
     *
     * @return radius
     */
    public int getBgRadiusFocused() {
        return bgRadiusFocused;
    }

    /**
     * Set background focused corners radius
     *
     * @param bgRadiusFocused radius
     */
    public void setBgRadiusFocused(int bgRadiusFocused) {
        this.bgRadiusFocused = bgRadiusFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background top_left corner radius
     *
     * @return top_left radius
     */
    public int getBgRadiusTopLeft() {
        return bgRadiusTopLeft;
    }

    /**
     * Set background top_left corner radius
     *
     * @param bgRadiusTopLeft top_left radius
     */
    public void setBgRadiusTopLeft(int bgRadiusTopLeft) {
        this.bgRadiusTopLeft = bgRadiusTopLeft;
        updateBackgroundChanges();
    }

    /**
     * Get focused background top_left corner radius
     *
     * @return top_left radius
     */
    public int getBgRadiusTopLeftFocused() {
        return bgRadiusTopLeftFocused;
    }

    /**
     * Set focused background top_left corner radius
     *
     * @param bgRadiusTopLeftFocused top_left radius
     */
    public void setBgRadiusTopLeftFocused(int bgRadiusTopLeftFocused) {
        this.bgRadiusTopLeftFocused = bgRadiusTopLeftFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background top_right corner radius
     *
     * @return  top_right corner radius
     */
    public int getBgRadiusTopRight() {
        return bgRadiusTopRight;
    }

    /**
     * Set background top_right corner radius
     *
     * @param bgRadiusTopRight top_right corner radius
     */
    public void setBgRadiusTopRight(int bgRadiusTopRight) {
        this.bgRadiusTopRight = bgRadiusTopRight;
        updateBackgroundChanges();
    }

    /**
     * Get focused background top_right corner radius
     *
     * @return top_right corner radius
     */
    public int getBgRadiusTopRightFocused() {
        return bgRadiusTopRightFocused;
    }

    /**
     * Set focused background top_right corner radius
     *
     * @param bgRadiusTopRightFocused top_right corner radius
     */
    public void setBgRadiusTopRightFocused(int bgRadiusTopRightFocused) {
        this.bgRadiusTopRightFocused = bgRadiusTopRightFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_left corner radius
     *
     * @return  bottom_left radius
     */
    public int getBgRadiusBottomLeft() {
        return bgRadiusBottomLeft;
    }

    /**
     * Set focused background bottom_left corner radius
     *
     * @param bgRadiusBottomLeft bottom_left radius
     */
    public void setBgRadiusBottomLeft(int bgRadiusBottomLeft) {
        this.bgRadiusBottomLeft = bgRadiusBottomLeft;
        updateBackgroundChanges();
    }

    /**
     * Get focused background bottom_left corner radius
     *
     * @return  bottom_left radius
     */
    public int getBgRadiusBottomLeftFocused() {
        return bgRadiusBottomLeftFocused;
    }

    /**
     * Set focused background bottom_left corner radius
     *
     * @param bgRadiusBottomLeftFocused bottom_left radius
     */
    public void setBgRadiusBottomLeftFocused(int bgRadiusBottomLeftFocused) {
        this.bgRadiusBottomLeftFocused = bgRadiusBottomLeftFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background bottom_right corner radius
     *
     * @return  bottom_right radius
     */
    public int getBgRadiusBottomRight() {
        return bgRadiusBottomRight;
    }

    /**
     * Set background bottom_right corner radius
     *
     * @param bgRadiusBottomRight bottom_right radius
     */
    public void setBgRadiusBottomRight(int bgRadiusBottomRight) {
        this.bgRadiusBottomRight = bgRadiusBottomRight;
        updateBackgroundChanges();
    }

    /**
     * Get focused background bottom_right corner radius
     *
     * @return  bottom_right radius
     */
    public int getBgRadiusBottomRightFocused() {
        return bgRadiusBottomRightFocused;
    }

    /**
     * Set focused background bottom_right corner radius
     *
     * @param bgRadiusBottomRightFocused bottom_right radius
     */
    public void setBgRadiusBottomRightFocused(int bgRadiusBottomRightFocused) {
        this.bgRadiusBottomRightFocused = bgRadiusBottomRightFocused;
        updateBackgroundChanges();
    }

    /**
     * Get background shape type
     *
     * @return {@link EnBgShapeType}
     */
    public EnBgShapeType getBgShapeType() {
        return bgShapeType;
    }

    /**
     * Set background shape type
     *
     * @param bgShapeType {@link EnBgShapeType}
     */
    public void setBgShapeType(EnBgShapeType bgShapeType) {
        this.bgShapeType = bgShapeType;
        updateBackgroundChanges();
    }


    /**
     * Re-enable the previous disabled mask
     */
    public void enableMask() {
        // Remove previous listener
        disableMask();

        // Create a new mask formatter instance
        maskEditUtil = new MaskEditUtil(this, this.maskFormat, this.maskType, this.maskReverse);
        maskEditUtil.setTextChangedListener(maskEditTextWatcher);

        // Add mask formatter watcher to this edit
        super.addTextChangedListener(maskEditUtil);
    }

    /**
     * Set/Change custom mask format
     */
    public void enableMask(@MaskEditFormatValid String maskFormat) {
        this.maskFormat = maskFormat;
        this.maskType   = MaskEditUtil.getMaskTypeFromStrMask(maskFormat);

        enableMask();
    }

    /**
     * Set/Change custom mask
     */
    public void enableCustomMask(String maskFormat, boolean maskReverse) {
        this.maskFormat  = maskFormat;
        this.maskReverse = maskReverse;
        this.maskType    = MaskEditUtil.getMaskTypeFromStrMask(maskFormat);

        enableMask();
    }

    /**
     * Disable the mask
     */
    public void disableMask() {
        if (maskEditUtil != null) {
            maskEditUtil.setTextChangedListener(null);
            super.removeTextChangedListener(maskEditUtil);
            super.addTextChangedListener(maskEditTextWatcher);
        }
    }

    /**
     * Get text without formatted mask
     *
     * @return unformated text
     */
    @Nullable
    public String getUnmaskedText() {
        if (maskEditUtil != null) {
            if (maskEditUtil.isAllowPassMaskLength()) {
                return getText() != null ? getText().toString().replaceAll("[^a-zA-Z0-9]", "") : null;
            } else {
                return maskEditUtil.unformat(getText());
            }
        } else {
            return (getText() != null ? getText().toString() : null);
        }
    }

    /**
     * Get mask type
     *
     * @return Mask type
     */
    public MaskEditType getMaskType() {
        return maskType;
    }

    /**
     * Set/Change mask format type
     *
     * @param maskType Mask format type
     */
    public void setMaskType(MaskEditType maskType) {
        this.maskType   = maskType;
        this.maskFormat = MaskEditUtil.getMaskFormatFromMaskType(maskType);

        if (this.maskEditUtil != null) {
            this.maskEditUtil.setMaskType(this.maskType);
        } else {
            this.maskEditUtil = new MaskEditUtil(this, maskFormat, maskType);
        }
    }

    /**
     * Set a custom mask format for this EditText
     * Ex: setMaskFormat("##.###.##.##-##")
     *
     * @param maskFormat The format
     */
    public void setMaskFormat(@NonNull String maskFormat) {
        this.maskType = MaskEditType.NONE;
        if (this.maskEditUtil != null) {
            this.maskEditUtil.setMaskFormat(maskFormat);
        }
    }

    /**
     * Enable pass max mask length
     *
     * @param allow true=allowed, false=disallowed
     */
    public void allowPassMaskLength(boolean allow) {
        if (maskEditUtil != null) {
            maskEditUtil.setAllowPassMaskLength(allow);
        }
    }

    /**
     * Set mask format max length
     *
     * @param maxLength The max length
     */
    public void setMaskMaxLength(int maxLength) {
        if (this.maskEditUtil != null) {
            this.maskEditUtil.setMaxLength(maxLength);
        }
    }

    /**
     * Get current mask max length
     *
     * @return length
     */
    public int getMaskMaxLength() {
        return this.maskEditUtil != null ? this.maskEditUtil.getMaxLength() : -1;
    }

    /**
     * Return if is allowed to pass the max mask length
     *
     * @return true=Allowed, false=Disallowed
     */
    public boolean isAllowPassMaskLength() {
        if (maskEditUtil != null) {
            return maskEditUtil.isAllowPassMaskLength();
        } else {
            return false;
        }
    }

    /**
     * Get monetary mask locale
     *
     * @return locale
     */
    @Nullable
    public Locale getMonetaryMaskLocale() {
        return maskEditUtil != null ? maskEditUtil.getMonetaryMaskLocale() : null;
    }

    /**
     * Set monetary mask locale
     *
     * @param monetaryMaskLocale locale
     */
    public void setMonetaryMaskLocale(Locale monetaryMaskLocale) {
        if (maskEditUtil != null) {
            maskEditUtil.setMonetaryMaskLocale(monetaryMaskLocale);
        }
    }

    /**
     * Return monetary mask format decimal places max length
     * Or -1 if mask format isn't monetary
     *
     * @return Decimal places quantity
     */
    public int getMonetaryMaskDecimalPlaces() {
        return maskEditUtil != null ? maskEditUtil.getMonetaryMaskDecimalPlaces() : -1;
    }

    /**
     * Change decimal places of monetary mask
     *
     * @param monetaryMaskDecimalPlaces Decimal places
     */
    public void setMonetaryMaskDecimalPlaces(int monetaryMaskDecimalPlaces) {
        if (maskEditUtil != null) {
            maskEditUtil.setMonetaryMaskDecimalPlaces(monetaryMaskDecimalPlaces);
        }
    }

    /**
     * Return if moentary symbol is visible
     *
     * @return true=Show monetary symbol false=Don't show
     */
    public boolean isMonetaryMaskShowSign() {
        if (this.maskEditUtil != null) {
            return this.maskEditUtil.isMonetaryMaskShowCurrency();
        } else {
            return false;
        }
    }

    /**
     * Show/hide monetary symbol in money mask
     *
     * @param monetaryMaskShowSign true=Show monetary symbol false=Don't show
     */
    public void setMonetaryMaskShowSign(boolean monetaryMaskShowSign) {
        if (this.maskEditUtil != null) {
            maskEditUtil.setMonetaryMaskShowCurrency(monetaryMaskShowSign);
        }
    }

    /**
     * Notify mask formatter that text has changed
     */
    public void maskNotifyChange() {
        if (this.maskEditUtil != null) {
            this.maskEditUtil.notifyChange();
        }
    }

    /**
     * Get the monetary mask BigDecimal value
     *
     * @return BigDecimal
     */
    @Nullable
    public BigDecimal getMonetaryMaskBigDec() {
        return maskEditUtil != null ? maskEditUtil.getMonetaryBigDec() : null;
    }

    /**
     * Click listener on compound drawables
     *
     * @param drawableClickListener Listener to receive click events
     */
    public void setCompoundDrawablesClickListener(@Nullable DrawableClickListenerAbs drawableClickListener) {
        this.drawableClickListener = drawableClickListener;
    }

    public interface DrawableClickListener {
        boolean onDrawableLeftClick();

        boolean onDrawableTopClick();

        boolean onDrawableRightClick();

        boolean onDrawableBottomClick();
    }

    public static abstract class DrawableClickListenerAbs implements DrawableClickListener {
        @Override
        public boolean onDrawableLeftClick() {
            return false;
        }

        @Override
        public boolean onDrawableTopClick() {
            return false;
        }

        @Override
        public boolean onDrawableRightClick() {
            return false;
        }

        @Override
        public boolean onDrawableBottomClick() {
            return false;
        }
    }
}
