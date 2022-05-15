package com.infinity.architecture.utils.drawable.shapes;

import android.graphics.Paint;
import android.graphics.Path;

import com.infinity.architecture.utils.drawable.shapes.utils.BaseDrawable;
import com.infinity.architecture.utils.drawable.shapes.utils.ClipPathManager;
import com.infinity.architecture.utils.location.Point2D;
import com.infinity.architecture.utils.location.PointUtils;

import java.util.ArrayList;

@SuppressWarnings("SuspiciousNameCombination")
public class MultiEdgeDrawable2 extends BaseDrawable {

    private final String TAG = "MultiEdgeDrawable2";

    public MultiEdgeDrawable2(
            int edgeCount,
            int angle,
            int color,
            Paint.Style paintStyle,
            int strokeWidth
    ) {
        super(color, paintStyle, true, strokeWidth);
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                Path path = new Path();

                int maxMeasureStep = 1;

                double minPaintX = width;
                double minPaintY = height;
                double maxPaintX = 0;
                double maxPaintY = 0;

                ArrayList<Point2D> measuredPoints = new ArrayList<>();

                for (int curMeasureStep=0; curMeasureStep<maxMeasureStep + 1; curMeasureStep++) {
                    boolean keepAspectio = true;
                    double radius = (Math.min(width, height) / 2f);
                    double angleStep = 360f / edgeCount;

                    double currentAngle = 0;
                    Point2D centerPoint = new Point2D(width / 2f, height / 2f);
                    Point2D currentPoint = new Point2D(0, 0);

                    if (curMeasureStep > 0) {
//                        radius = Math.sqrt(width*width + height*height)*0.5;
//                        radius -= strokeWidth;
//                    }
                        double leftSpace = minPaintX;
                        double topSpace = minPaintY;
                        double rightSpace = width - maxPaintX;
                        double bottomSpace = height - maxPaintY;

                        double toIncreaseRadius = Math.min((leftSpace + rightSpace) / 2, (topSpace + bottomSpace) / 2) - strokeWidth;
                        radius += toIncreaseRadius;
                    }

                    for (int i = 0; i < edgeCount; i++) {
                        currentPoint = PointUtils.getCirclePointByAngle(centerPoint, radius, radius, currentAngle);

                        if (i == 0) {
                            Point2D nextPoint = PointUtils.getCirclePointByAngle(centerPoint, radius, radius, currentAngle + angleStep);
                            // Point2D prevPoint = PointUtils.getCirclePointByAngle(centerPoint, radius, radius, currentAngle - angleStep);
                            double distanceBetweenPoint = PointUtils.calculateDistanceBetweenPoint(currentPoint, nextPoint);
                            Point2D middlePoint = PointUtils.calculateBetweenPoint(currentPoint, nextPoint, distanceBetweenPoint / 2);
                            double angleOfMiddlePoint = PointUtils.getAngle(middlePoint, centerPoint);
                            Point2D bottomMiddlePoint = PointUtils.getCirclePointByAngle(centerPoint, radius, radius, 90f);
                            double angleOfBottomPoint = PointUtils.getAngle(bottomMiddlePoint, centerPoint);

//                            path.addCircle((float) nextPoint.getX(), (float) nextPoint.getY(), 10f, Path.Direction.CW);
//                            path.addCircle((float) middlePoint.getX(), (float) middlePoint.getY(), 10f, Path.Direction.CW);
//                            path.addCircle((float) bottomMiddlePoint.getX(), (float) bottomMiddlePoint.getY(), 10f, Path.Direction.CW);
//                            path.addCircle((float) currentPoint.getX(), (float) currentPoint.getY(), 15f, Path.Direction.CW);
//                            path.addCircle((float) centerPoint.getX(), (float) centerPoint.getY(), (float) radius, Path.Direction.CW);

//                            Log.d(TAG, "toDraw: " + point2D.toString());

//                            path.addCircle((float) point2D.getX(), (float) point2D.getY(), (float) 10, Path.Direction.CW);
//                            path.moveTo((float) centerPoint.getX(), (float) centerPoint.getY());
//                            path.lineTo((float) point2D.getX(), (float) point2D.getY());

                            double edgeBottomAlignedAngle = angleOfBottomPoint - angleOfMiddlePoint;

                            currentAngle += edgeBottomAlignedAngle + angle;
                            currentPoint = PointUtils.getCirclePointByAngle(centerPoint, radius, radius, currentAngle);
                        }

                        if (curMeasureStep > 0) {
                            currentPoint = PointUtils.movePointToCenterDraw(currentPoint, minPaintX, minPaintY, maxPaintX, maxPaintY, width, height);
                        }

                        if (curMeasureStep < maxMeasureStep) {
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

                        if (curMeasureStep == maxMeasureStep) {
                            if (i == 0) {
                                path.moveTo((float) currentPoint.getX(), (float) currentPoint.getY());
                            } else {
                                path.lineTo((float) currentPoint.getX(), (float) currentPoint.getY());
                            }
                        }

                        currentAngle += angleStep;
                        currentAngle = PointUtils.angleFix(currentAngle);
                    }

                    if (curMeasureStep == maxMeasureStep) {
                        path.close();
                    }
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
