package com.infinity.architecture.utils.drawable.shapes;

import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.FloatRange;

import com.infinity.architecture.utils.drawable.shapes.utils.BaseDrawable;
import com.infinity.architecture.utils.drawable.shapes.utils.ClipPathManager;
import com.infinity.architecture.utils.location.PointUtils;
import com.infinity.architecture.utils.location.Point2D;

public class MultiEdgeCenterOutLineDrawable extends BaseDrawable {

    public MultiEdgeCenterOutLineDrawable(
        int edges,
        @FloatRange(from = -360, to = 360) double startAngle,
        int color,
        Paint.Style paintStyle,
        int strokeWidth
    ) {
        super(color, paintStyle, true, strokeWidth);
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                Path path = new Path();

                float radius = width / 2f;
                float edgeAngle = (360f / edges);
                float mStartAngle = edgeAngle / 2f;
                if (startAngle > 0 || startAngle < 0) {
                    mStartAngle += startAngle / 2f;
                }

                if (mStartAngle > 180) {
                    mStartAngle = mStartAngle - 180;
                } else if (mStartAngle < 0) {
                    mStartAngle = 180 - mStartAngle;
                }

                Point2D centerPoint = new Point2D(
                    width / 2f,
                    height / 2f
                );

                Point2D currentPoint = new Point2D(
                    centerPoint.getX(),
                    centerPoint.getY() + radius
                );

//                // Must rotate to set bottom edge aligned bottom
//                currentPoint = LocationUtils.rotatePoint2D(currentPoint, centerPoint, mStartAngle);
//
//                // Set path start
//                path.moveTo((float) centerPoint.getX(), (float) centerPoint.getY());
//                path.lineTo((float) currentPoint.getX(), (float) currentPoint.getY());

                // For each edge draw the lines
                for(int i=0; i<edges; i++) {
                    Point2D prevPoint = currentPoint;
                    if (i == 0) {
                        // Must rotate to set bottom edge aligned bottom
                        currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, mStartAngle);
                        prevPoint = currentPoint;
                    } else {
                        currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);
                    }

                    Point2D nextPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);

                    double maxRadiusDistance = PointUtils.calculateDistanceBetweenPoint(currentPoint, prevPoint);
                    Point2D newPoint1 = PointUtils.calculateBetweenPoint(currentPoint, prevPoint, 30);
                    Point2D newPoint2 = PointUtils.calculateBetweenPoint(currentPoint, nextPoint, 30);
                    double arcWidth = PointUtils.calculateDistanceBetweenPoint(newPoint1, newPoint2);
                    Point2D midlePoint = PointUtils.calculateBetweenPoint(newPoint1, newPoint2, arcWidth / 2);
                    double arcHeight = PointUtils.calculateDistanceBetweenPoint(currentPoint, midlePoint);

                    Point2D curvePoint = PointUtils.getCurvedPoint(newPoint1, newPoint2, arcHeight);

                    path.moveTo((float) newPoint1.getX(),(float) newPoint1.getY());
                    path.cubicTo((float) newPoint1.getX(), (float) newPoint1.getY(), (float) curvePoint.getX(), (float) curvePoint.getY(), (float) newPoint2.getX(), (float) newPoint2.getY());
//                    path.addArc(new RectF((float) arcLeft,(float) arcTop, (float) arcRight, (float) arcBottom), (float) arcStartAngle, 120);

                    path.moveTo((float) centerPoint.getX(), (float) centerPoint.getY());
                    path.lineTo((float) newPoint1.getX(),(float) newPoint1.getY());

                    path.moveTo((float) centerPoint.getX(), (float) centerPoint.getY());
                    path.lineTo((float) newPoint2.getX(),(float) newPoint2.getY());

                    path.moveTo((float) centerPoint.getX(), (float) centerPoint.getY());
                    path.lineTo((float) currentPoint.getX(),(float) currentPoint.getY());
                }

                // path.close();

                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }
}
