package com.infinity.architecture.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomProgressBar extends View {
    private String TAG = "CustomProgressBar";

    private Paint paint;
    private int maxValue = 100;
    private int minValue = 0;
    private int currentValue = 0;
    private int previousValue = 0;
    private int barColor = Color.WHITE;
    private int dotColor = Color.WHITE;
    private int dotSize = 20;
    private int barSize = 5;
    private int indicatorXPos = dotSize;

    private OnChangeListener onChangeListener;

    private boolean disabled = false;

    public CustomProgressBar(Context context) {
        super(context);
        init(null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dotSize == -1) {
            dotSize = getHeight();
        }

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(barColor);
        canvas.drawRect(0, (getHeight() / 2f) - (barSize / 2.0f), getWidth(), (getHeight() / 2f) + (barSize / 2.0f), paint);

        paint.setColor(dotColor);
        canvas.drawCircle(indicatorXPos, getHeight() / 2f, dotSize, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE && !disabled) {
            if (event.getX() > getWidth() - dotSize) {
                indicatorXPos = getWidth() - dotSize;
                currentValue = maxValue;
            } else if (event.getX() < dotSize) {
                indicatorXPos = dotSize;
                currentValue = minValue;
            } else {
                indicatorXPos = (int) event.getX();
                currentValue = ((indicatorXPos * maxValue) / getWidth());
            }

            if (onChangeListener != null && previousValue != currentValue) {
                onChangeListener.onChange(currentValue);
            }

            previousValue = currentValue;

            this.invalidate();
        }
        return true;
    }

    public interface OnChangeListener {
        void onChange(int value);
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void setProgressValue(int value) {

        // getWidth - maxValue
        // indicatorXPos - currentValue
        if (value <= minValue) {
            this.indicatorXPos = dotSize;
            this.currentValue = value;
        } else if (value >= maxValue) {
            this.indicatorXPos = getWidth() - dotSize;
            this.currentValue = value;
        } else {
            this.indicatorXPos = dotSize + ((value * (getWidth() - (2 * dotSize))) / maxValue);
//            if (this.indicatorXPos < dotSize) {
//                this.indicatorXPos = dotSize;
//            } else if (this.indicatorXPos > getWidth() - dotSize) {
//                this.indicatorXPos = getWidth() - dotSize;
//            }
            this.currentValue = value;
        }
        invalidate();
    }

    public void setProgressMaxValue(int value) {
        maxValue = value;
        if (currentValue > maxValue) {
            setProgressValue(maxValue);
        }
    }

    public void setProgressMinValue(int value) {
        minValue = value;
        if (currentValue < minValue) {
            setProgressValue(minValue);
        }
    }

    public void setBarColor(int color) {
        this.barColor = color;
        invalidate();
    }

    public void setDotColor(int color) {
        this.dotColor = color;
        invalidate();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    //    public class GestureListener extends
//            GestureDetector.SimpleOnGestureListener {
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//        @Override
//        public boolean onDown(MotionEvent e) {
//            Log.d(TAG, "onDown");
//            return true;
//        }
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffY = e2.getY() - e1.getY();
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            onSwipeRight();
//                        } else {
//                            onSwipeLeft();
//                        }
//                        result = true;
//                    }
//                }
//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        onSwipeBottom();
//                    } else {
//                        onSwipeTop();
//                    }
//                    result = true;
//                }
//            }
//            catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return result;
//        }
//    }
//
//    private void onSwipeLeft() {
//        Log.d(TAG, "onSwipeLeft");
//    }
//    private void onSwipeRight() {
//        Log.d(TAG, "onSwipeRight");
//    }
//    private void onSwipeTop() {
//        Log.d(TAG, "onSwipeTop");
//    }
//    private void onSwipeBottom() {
//        Log.d(TAG, "onSwipeBottom");
//    }
}
