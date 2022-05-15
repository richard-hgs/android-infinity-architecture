package com.infinity.architecture.views;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomBorder extends Drawable {

    public Paint paint;
    public Rect bounds_rect;

    public CustomBorder(@ColorInt int colour, int width)
    {
        this.paint = new Paint();
        this.paint.setColor(colour);
        this.paint.setStrokeWidth(width);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(this.bounds_rect, this.paint);
    }

    @Override
    public void setAlpha(int alpha) {
        //throw new NotImplementedException();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        //throw new NotImplementedException();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.bounds_rect = bounds;
    }

    /*public override int Opacity => 0;
    protected override void OnBoundsChange(Rect bounds)
    {

    }

    public override void Draw(Canvas canvas)
    {
        canvas.DrawRect(this.bounds_rect, this.paint);
    }

    public override void SetAlpha(int alpha)
    {
        //throw new NotImplementedException();
    }

    public override void SetColorFilter(ColorFilter colorFilter)
    {
        //throw new NotImplementedException();
    }*/
}
