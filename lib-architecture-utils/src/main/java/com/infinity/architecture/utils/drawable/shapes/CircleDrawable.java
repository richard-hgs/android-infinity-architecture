package com.infinity.architecture.utils.drawable.shapes;

import android.graphics.Paint;
import android.graphics.Path;

import com.infinity.architecture.utils.drawable.shapes.utils.BaseDrawable;
import com.infinity.architecture.utils.drawable.shapes.utils.ClipPathManager;

public class CircleDrawable extends BaseDrawable {

    public CircleDrawable(int color, Paint.Style paintStyle, int strokeWidth) {
        super(color, paintStyle, true, strokeWidth);
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                Path path = new Path();

                float centerX = (float) width / 2f;
                float centerY = (float) height / 2f;

                // draw a reference circle
                path.addCircle(centerX, centerY, width / 2f, Path.Direction.CW);

                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }
}
