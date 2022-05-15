package com.infinity.architecture.utils.drawable.shapes.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.Nullable;

public class ClipPathManager implements ClipManager {

    protected final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ClipPathCreator createClipPath = null;

    public ClipPathManager() {
        this(Color.BLACK, Paint.Style.FILL, true, 1);
    }

    public ClipPathManager(int color) {
        this(color, Paint.Style.FILL, true, 1);
    }

    public ClipPathManager(int color, Paint.Style paintStyle, boolean antiAlias, int strokeWidth) {
        paint.setColor(color);
        paint.setStyle(paintStyle);
        paint.setAntiAlias(antiAlias);
        paint.setStrokeWidth(strokeWidth);
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public boolean requiresBitmap() {
        return createClipPath != null && createClipPath.requiresBitmap();
    }

    @Nullable
    protected final Path createClipPath(int width, int height) {
        if (createClipPath != null) {
            return createClipPath.createClipPath(width, height);
        }
        return null;
    }

    public void setClipPathCreator(ClipPathCreator createClipPath) {
        this.createClipPath = createClipPath;
    }

    @Override
    public Path createMask(int width, int height) {
        return path;
    }

    @Nullable
    @Override
    public Path getShadowConvexPath() {
        return path;
    }

    @Override
    public void setupClipLayout(int width, int height) {
        path.reset();
        final Path clipPath = createClipPath(width, height);
        if (clipPath != null) {
            path.set(clipPath);
        }
    }

    public interface ClipPathCreator {
        Path createClipPath(int width, int height);
        boolean requiresBitmap();
    }
}
