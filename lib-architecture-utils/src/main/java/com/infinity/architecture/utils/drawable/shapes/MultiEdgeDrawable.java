package com.infinity.architecture.utils.drawable.shapes;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.drawable.shapes.utils.BaseDrawable;
import com.infinity.architecture.utils.drawable.shapes.utils.ClipPathManager;
import com.infinity.architecture.utils.location.PointUtils;
import com.infinity.architecture.utils.location.Point2D;

import java.util.ArrayList;

public class MultiEdgeDrawable extends BaseDrawable {
    private final String TAG = "MultiEdgeDrawable";

    public MultiEdgeDrawable(
        int edges,
        @FloatRange(from = -360, to = 360) double startAngle,
        float cornerRadius,
        @Nullable ArrayList<Float> cornersRadiusList,
        int dashCount,
        float dashPhase,
        float padding,
        float increaseArcAngle,
        int color,
        @NonNull Paint.Style paintStyle,
        int strokeWidth
    ) {
        super(color, paintStyle, true, strokeWidth);
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                Path path = new Path();
                float mPadding = padding;
                int mEdges = edges;
                double minPaintX = width;
                double minPaintY = height;
                double maxPaintX = 0;
                double maxPaintY = 0;

//                if (mPadding < 0) {
//                    mPadding = 0;
//                }

                if (paintStyle == Paint.Style.STROKE) {
                    mPadding += (float) (strokeWidth / 2);
                }

                float radius = (width / 2f) -mPadding;

                if (mEdges >= 3) {
                    ArrayList<Point2D> point2DArrayList = new ArrayList<>();
                    for (int x=0; x<3; x++) {

                        float edgeAngle = (360f / mEdges);
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

                        if (x > 0) {
                            // Align the image in center
                            double paintWidth = maxPaintX - minPaintX;
                            double paintHeight = maxPaintY - minPaintY;

                            double left = (width - paintWidth) / 2;
                            double top = (height - paintHeight) / 2;

                            double newCenterX = centerPoint.getX() + Math.abs(minPaintX - left);
                            double newCenterY = centerPoint.getY() + Math.abs(minPaintY - top);

                            if (x == 1) {
                                double sizeToIncrease = Math.min(left, top);
                                radius += sizeToIncrease - mPadding;
                            }

                            if (x == 2) {
                                centerPoint.setX(newCenterX);
                                centerPoint.setY(newCenterY);
                            }
                        }

                        if (mEdges == 4) {
                            // Fill square size
                            radius = (float) PointUtils.calculateDistanceBetweenPoint(centerPoint, new Point2D(0, 0));
                            radius -= mPadding;
                        }

                        Point2D currentPoint = new Point2D(
                            centerPoint.getX(),
                            centerPoint.getY() + radius
                        );

                        // For each edge draw the lines
                        for (int i = 0; i < mEdges; i++) {
                            float mCornerRadius = cornerRadius;

                            if (mCornerRadius == 0 && cornersRadiusList != null && cornersRadiusList.size() > i) {
                                mCornerRadius = cornersRadiusList.get(i);
                            }

                            Point2D prevPoint = currentPoint;
                            if (i == 0) {
                                // Must rotate to set bottom edge aligned bottom
                                currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, mStartAngle);
                                prevPoint = currentPoint;
                            } else {
                                currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);
                            }
                            Point2D nextPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);

                            if (x == 0) {
                                point2DArrayList.add(currentPoint);
                            }

                            double maxRadiusDistance = PointUtils.calculateDistanceBetweenPoint(currentPoint, nextPoint) / 2f;
                            if (mCornerRadius > maxRadiusDistance) {
                                mCornerRadius = (float) maxRadiusDistance;
                            }
                            Point2D newPoint1 = PointUtils.calculateBetweenPoint(currentPoint, prevPoint, mCornerRadius);
                            Point2D newPoint2 = PointUtils.calculateBetweenPoint(currentPoint, nextPoint, mCornerRadius);
                            double arcWidth = PointUtils.calculateDistanceBetweenPoint(newPoint1, newPoint2);
                            Point2D midlePoint = PointUtils.calculateBetweenPoint(newPoint1, newPoint2, arcWidth / 2);
                            double arcHeight = PointUtils.calculateDistanceBetweenPoint(currentPoint, midlePoint);
                            Point2D curvePoint = PointUtils.getCurvedPoint(newPoint1, newPoint2, arcHeight + increaseArcAngle);

                            if (x < 2) {
                                if (currentPoint.getX() > maxPaintX) {
                                    maxPaintX = currentPoint.getX();
                                }
                                if (currentPoint.getY() > maxPaintY) {
                                    maxPaintY = currentPoint.getY();
                                }
                                if (currentPoint.getX() < minPaintX) {
                                    minPaintX = currentPoint.getX();
                                }
                                if (currentPoint.getY() < minPaintY) {
                                    minPaintY = currentPoint.getY();
                                }
                            }

                            if (x == 2) {
                                if (i == 0) {
                                    if (mCornerRadius > 0) {
                                        path.moveTo((float) newPoint1.getX(), (float) newPoint1.getY());
                                    } else {
                                        path.moveTo((float) currentPoint.getX(), (float) currentPoint.getY());
                                    }
                                }

                                if (mCornerRadius > 0) {
                                    path.lineTo((float) newPoint1.getX(), (float) newPoint1.getY());
                                    path.cubicTo((float) newPoint1.getX(), (float) newPoint1.getY(), (float) curvePoint.getX(), (float) curvePoint.getY(), (float) newPoint2.getX(), (float) newPoint2.getY());
                                } else {
                                    path.lineTo((float) currentPoint.getX(), (float) currentPoint.getY());
                                }
                            }
                        }

                        if (x == 2) {
                            path.close();
                        }
                    }
                } else {
                    // Draws a circle
                    float centerX = (float) width / 2f;
                    float centerY = (float) height / 2f;

                    // draw a reference circle
                    path.addCircle(centerX, centerY, (width / 2f) - mPadding, Path.Direction.CW);
                }

                if (dashCount > 0) {
                    PathMeasure pathMeasure = new PathMeasure(path, false);
                    pathMeasure.getLength();

                    float dashSpace = 0;
                    dashSpace = pathMeasure.getLength() / (dashCount * 2);
                    getPaint().setPathEffect(new DashPathEffect(new float[]{dashSpace, dashSpace}, dashPhase));
                }

                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }
}
