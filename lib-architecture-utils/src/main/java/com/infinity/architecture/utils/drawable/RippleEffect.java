package com.infinity.architecture.utils.drawable;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

public class RippleEffect {

    /**
     * Simulates a button click in center of the view
     * Show ripple effect on view background {@link View#getBackground()} by 200ms
     *
     * @param view {@link View}
     */
    public static void forceRippleAnimation(@NonNull View view) {
        Drawable background = view.getBackground();

        if(Build.VERSION.SDK_INT >= 21 && background instanceof RippleDrawable) {
            final RippleDrawable rippleDrawable = (RippleDrawable) background;

            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});

            view.postDelayed(new Runnable() {
                @Override public void run() {
                    rippleDrawable.setState(new int[]{});
                }
            }, 200);
        }
    }

    public static void addRippleEffect(View view, boolean rippleEnabled, int backgroundColor, int rippleColor) {
        if (rippleEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create RippleDrawable
            view.setBackground(getPressedColorRippleDrawable(backgroundColor, rippleColor));

            //Customize Ripple color
            RippleDrawable rippleDrawable = (RippleDrawable) view.getBackground();
            int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}};
            int[] colors = new int[]{rippleColor};
            ColorStateList colorStateList = new ColorStateList(states, colors);
            rippleDrawable.setColor(colorStateList);
            view.setBackground(rippleDrawable);
        } else if (rippleEnabled) {
            //Create Selector for pre Lollipop
            ViewCompat.setBackground(view, createStateListDrawable(backgroundColor, rippleColor));
        } else {
            //Ripple Disabled
            view.setBackground(new ColorDrawable(backgroundColor));
        }
    }

    private static StateListDrawable createStateListDrawable(int backgroundColor, int rippleColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(rippleColor));
        stateListDrawable.addState(StateSet.WILD_CARD, createDrawable(backgroundColor));
        return stateListDrawable;
    }

    private static Drawable createDrawable(int background) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(background);
        return shapeDrawable;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    private static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor
                        }
        );
    }

    private static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}
