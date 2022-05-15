package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import com.infinity.architecture.views.enums.EnBoxBackgroundMode;
import com.google.android.material.internal.ReflCollapsingTextHelper;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.textfield.ReflCutoutDrawable;
import com.google.android.material.textfield.ReflTextInputLayout;
import com.google.android.material.textfield.TextInputLayout;


@SuppressWarnings("unused")
public class InfinityTextInputLayout extends TextInputLayout {

    private static final String TAG = "CustomTextInputLay";

    // Styles variables
    private int incrementCollapsedPaddingLeft;
    private int incrementCollapsedPaddingTop;
    private int incrementCollapsedPaddingRight;

    private EditText editText;

    private Rect editRectBounds = new Rect();

    @NonNull
    private static Context wrap(@NonNull Context context, AttributeSet attrs, int defStyle, int defStyleAttr) {
//        if (attrs != null) {
        TypedArray taAttrs = context.obtainStyledAttributes(attrs, R.styleable.InfinityTextInputLayout, defStyleAttr, R.style.InfinityTextInputLayoutStyle);

        Log.d(TAG, "attrs count: " + attrs.getAttributeCount());
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.d(TAG, "attrs(" + attrs.getAttributeName(i) + "): " + attrs.getAttributeValue(i));
        }

        Log.d(TAG, "taAttrs count: " + taAttrs.getIndexCount());
        for (int i = 0; i < taAttrs.getIndexCount(); i++) {
            int resId = taAttrs.getResourceId(i, 0);
            Log.d(TAG, "taAttrs(" + (resId > 0 ? context.getResources().getResourceName(resId) : "0") + ": ");
        }

        EnBoxBackgroundMode enBoxBackgroundMode = EnBoxBackgroundMode.values()[taAttrs.getInt(R.styleable.InfinityTextInputLayout_customBoxBackgroundMode, EnBoxBackgroundMode.NONE.ordinal())];
        int noneStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutNoneStyle, R.style.InfinityTextInputLayoutNoneStyle);
        int noneDenseStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutNoneDenseStyle, R.style.InfinityTextInputLayoutNoneDenseStyle);
        int filledStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutFilledStyle, R.style.InfinityTextInputLayoutFilledStyle);
        int filledDenseStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutFilledDenseStyle, R.style.InfinityTextInputLayoutFilledDenseStyle);
        int outlinedStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutOutlinedStyle, R.style.InfinityTextInputLayoutOutlinedStyle);
        int outlinedDenseStyleAttr = taAttrs.getResourceId(R.styleable.InfinityTextInputLayout_customTextInputLayoutOutlinedDenseStyle, R.style.InfinityTextInputLayoutOutlinedDenseStyle);

        Log.d(TAG, "enBoxBackgroundMode: " + enBoxBackgroundMode);

        if (enBoxBackgroundMode == EnBoxBackgroundMode.NONE) {
            defStyle = noneStyleAttr;
        } else if (enBoxBackgroundMode == EnBoxBackgroundMode.NONE_DENSE) {
            defStyle = noneDenseStyleAttr;
        } else if (enBoxBackgroundMode == EnBoxBackgroundMode.FILLED) {
            defStyle = filledStyleAttr;
        } else if (enBoxBackgroundMode == EnBoxBackgroundMode.FILLED_DENSE) {
            defStyle = filledDenseStyleAttr;
        } else if (enBoxBackgroundMode == EnBoxBackgroundMode.OUTLINED) {
            defStyle = outlinedStyleAttr;
        } else if (enBoxBackgroundMode == EnBoxBackgroundMode.OUTLINED_DENSE) {
            defStyle = outlinedDenseStyleAttr;
        }

        TypedArray tArray = context.obtainStyledAttributes(defStyle, new int[] { android.R.attr.editTextStyle });
        int editTextStyle = tArray.getResourceId(0, 0);
        tArray.recycle();

        Log.d(TAG, "editTextStyle: " + editTextStyle + " - " + context.getResources().getResourceName(editTextStyle));

        taAttrs.recycle();
//        }

        return new ContextThemeWrapper(context, defStyle);
//        return context;
    }

    public InfinityTextInputLayout(Context context) {
        this(context, null);
    }

    public InfinityTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.textInputStyle);
    }

    public InfinityTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, R.style.InfinityTextInputLayoutNoneStyle, R.attr.customTextInputLayoutStyle), attrs, defStyleAttr);
        try {
            TypedArray taAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.InfinityTextInputLayout, R.attr.customTextInputLayoutStyle, R.style.InfinityTextInputLayoutStyle);

            incrementCollapsedPaddingLeft = taAttrs.getDimensionPixelSize(R.styleable.InfinityTextInputLayout_incrementCollapsedPaddingLeft, 0);
            incrementCollapsedPaddingTop = taAttrs.getDimensionPixelSize(R.styleable.InfinityTextInputLayout_incrementCollapsedPaddingTop, 0);
            incrementCollapsedPaddingRight = taAttrs.getDimensionPixelSize(R.styleable.InfinityTextInputLayout_incrementCollapsedPaddingRight, 0);

            taAttrs.recycle();

            // updateBackgroundChanges();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull final ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof EditText) {
            editText = (EditText) child;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configurePlaceholder();
    }

    private void configurePlaceholder() {
        if (editText != null) {
            try {
                if (isHintEnabled()) {
                    editRectBounds = ReflCollapsingTextHelper.getCollapsedBounds(ReflTextInputLayout.getCollapsingTextHelper(this));

                    if (editRectBounds != null) {
                        if (incrementCollapsedPaddingLeft > 0) {
                            editRectBounds.left += incrementCollapsedPaddingLeft;
                        }
                        if (incrementCollapsedPaddingTop > 0) {
                            editRectBounds.top += incrementCollapsedPaddingTop;
                        }
                        if (incrementCollapsedPaddingRight > 0) {
                            editRectBounds.right += incrementCollapsedPaddingRight;
                        }

                        ReflCollapsingTextHelper.setCollapsedBounds(ReflTextInputLayout.getCollapsingTextHelper(this), (int) editRectBounds.left, (int) editRectBounds.top, (int) editRectBounds.right, (int) editRectBounds.bottom);

                        ReflCollapsingTextHelper.recalculate(ReflTextInputLayout.getCollapsingTextHelper(this));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean cutoutEnabled() {
        boolean cutOutEnabled = false;
        try {
            CharSequence hint = ReflTextInputLayout.getHint(this);

            MaterialShapeDrawable boxBackground = ReflTextInputLayout.getBoxBackground(this);

            cutOutEnabled = isHintEnabled() && !TextUtils.isEmpty(hint) && ReflCutoutDrawable.shapeDrawableIsCutoutDrawable(boxBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cutOutEnabled;
    }

    private void openCutout() {
        try {
            if (!cutoutEnabled()) {
                return;
            }
            final RectF cutoutBounds = ReflTextInputLayout.getTmpRectF(this);
            ReflCollapsingTextHelper.getCollapsedTextActualBounds(
                ReflTextInputLayout.getCollapsingTextHelper(this), cutoutBounds, editText.getWidth(), editText.getGravity());

            applyCutoutPadding(cutoutBounds);

            // Offset the cutout bounds by the TextInputLayout's paddings, half of the cutout height, and
            // the box stroke width to ensure that the cutout is aligned with the actual collapsed text
            // drawing area.
            cutoutBounds.offset(
                -getPaddingLeft(), -getPaddingTop() - cutoutBounds.height() / 2 + ReflTextInputLayout.getBoxStrokeWidthPx(this));

            ReflCutoutDrawable.setCutout(ReflTextInputLayout.getBoxBackground(this), cutoutBounds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recalculateCutout() {
        try {
            if (cutoutEnabled() && !ReflTextInputLayout.isHintExpanded(this)) {
                closeCutout();
                openCutout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeCutout() {
        try {
            if (cutoutEnabled()) {
                ReflCutoutDrawable.removeCutout(ReflTextInputLayout.getBoxBackground(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyCutoutPadding(@NonNull RectF cutoutBounds) {
        try {
            cutoutBounds.left -= ReflTextInputLayout.getBoxLabelCutoutPaddingPx(this);
            cutoutBounds.right += ReflTextInputLayout.getBoxLabelCutoutPaddingPx(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean cutoutIsOpen() throws Exception {
        return cutoutEnabled() && ReflCutoutDrawable.hasCutout(ReflTextInputLayout.getBoxBackground(this));
    }
}
