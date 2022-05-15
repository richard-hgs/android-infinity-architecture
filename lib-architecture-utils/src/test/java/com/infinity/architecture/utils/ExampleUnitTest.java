package com.infinity.architecture.utils;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.FloatRange;

import com.infinity.architecture.utils.location.Point2D;
import com.infinity.architecture.utils.location.PointUtils;
import com.infinity.architecture.utils.variables.VarUtils;

import org.junit.Test;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void drawableTest() {
         testDrawable(3, 0, 40, Color.RED, Paint.Style.STROKE, 5);
        // System.out.println("circ: " + PointUtils.calculateCircleCircunference(10));
    }

    @Test
    public void arrayConcatTest() {
        int[] myArr1 = {0, 1, 2};
        int[] myArr2 = {3, 4, 5, 6};

        System.out.println("concated: " + Arrays.toString(VarUtils.concat(myArr1, myArr2)));
    }

    private void testDrawable(int edges,
                              @FloatRange(from = -360, to = 360) double startAngle,
                              float cornerRadius,
                              int color,
                              Paint.Style paintStyle,
                              int strokeWidth) {
        int width = 300;
        int height = 300;
        int mPadding = 0;
        int mEdges = edges;
        double minPaintX = width;
        double minPaintY = height;
        double maxPaintX = 0;
        double maxPaintY = 0;

//                if (mPadding < 0) {
//                    mPadding = 0;
//                }

        if (paintStyle == Paint.Style.STROKE) {
            mPadding += (strokeWidth / 2) + 1;
        }

        float radius = (width / 2f) -mPadding;

        if (mEdges >= 3) {
            for (int x=0; x<2; x++) {
                float mCornerRadius = cornerRadius;

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

                if (x == 1) {
                    // Align the image in center
                    double paintWidth = maxPaintX - minPaintX;
                    double paintHeight = maxPaintY - minPaintY;

                    double left = (width - paintWidth) / 2;
                    double top = (height - paintHeight) / 2;
                    double right = left + paintWidth;
                    double bottom = top + paintHeight;

                    double newCenterX = ((right - left) / 2) + left;
                    double newCenterY = ((bottom - top) / 2) + top;

                    /*
                    width: 300
                    height: 300

                    centerX: 150.0
                    centerY 150.0

                    newPaintX: 22.6945
                    newPaintY:

                    paintWidth: 254.61145973205566
                    paintHeight: 220.50000023841858

                    minPaintX: 22.694265365600586
                    minPaintY: 2.999999761581421

                    maxPaintX: 277.30572509765625
                    maxPaintY: 223.5

                    newCenterX: 150.0
                    newCenterY: 167.05572974681854



                     */

                    System.out.println( "width: " + width);
                    System.out.println( "height: " + height);
                    System.out.println( "paintWidth: " + paintWidth);
                    System.out.println( "paintHeight: " + paintHeight);
                    System.out.println( "minPaintX: " + minPaintX);
                    System.out.println( "minPaintY: " + minPaintY);
                    System.out.println( "maxPaintX: " + maxPaintX);
                    System.out.println( "maxPaintY: " + maxPaintY);
                    System.out.println( "newCenterX: " + newCenterX);
                    System.out.println( "newCenterY: " + newCenterY);
                    System.out.println( "centerX: " + centerPoint.getX());
                    System.out.println( "centerY " + centerPoint.getY());

                    centerPoint.setX(newCenterX);
                    centerPoint.setY(newCenterY);
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
                    Point2D prevPoint = currentPoint;
                    if (i == 0) {
                        // Must rotate to set bottom edge aligned bottom
                        currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, mStartAngle);
                        prevPoint = currentPoint;
                    } else {
                        currentPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);
                    }
                    Point2D nextPoint = PointUtils.rotatePoint2D(currentPoint, centerPoint, edgeAngle);

                    double maxRadiusDistance = PointUtils.calculateDistanceBetweenPoint(currentPoint, nextPoint) / 2f;
                    if (mCornerRadius > maxRadiusDistance) {
                        mCornerRadius = (float) maxRadiusDistance;
                    }
                    Point2D newPoint1 = PointUtils.calculateBetweenPoint(currentPoint, prevPoint, mCornerRadius);
                    Point2D newPoint2 = PointUtils.calculateBetweenPoint(currentPoint, nextPoint, mCornerRadius);
                    double arcWidth = PointUtils.calculateDistanceBetweenPoint(newPoint1, newPoint2);
                    Point2D midlePoint = PointUtils.calculateBetweenPoint(newPoint1, newPoint2, arcWidth / 2);
                    double arcHeight = PointUtils.calculateDistanceBetweenPoint(currentPoint, midlePoint);
                    Point2D curvePoint = PointUtils.getCurvedPoint(newPoint1, newPoint2, arcHeight);

                    if (x == 0) {
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

                    if (x == 1) {
//                        if (i == 0) {
//                            if (mCornerRadius > 0) {
//                                path.moveTo((float) newPoint1.getX(), (float) newPoint1.getY());
//                            } else {
//                                path.moveTo((float) currentPoint.getX(), (float) currentPoint.getY());
//                            }
//                        }

//                        if (mCornerRadius > 0) {
//                            path.lineTo((float) newPoint1.getX(), (float) newPoint1.getY());
//                            path.cubicTo((float) newPoint1.getX(), (float) newPoint1.getY(), (float) curvePoint.getX(), (float) curvePoint.getY(), (float) newPoint2.getX(), (float) newPoint2.getY());
//                        } else {
//                            path.lineTo((float) currentPoint.getX(), (float) currentPoint.getY());
//                        }
                    }
                }

                if (x == 1) {
//                    path.close();
                }
            }
        } else {
            // Draws a circle
            float centerX = (float) width / 2f;
            float centerY = (float) height / 2f;

            // draw a reference circle
//            path.addCircle(centerX, centerY, (width / 2f) - mPadding, Path.Direction.CW);
        }

//        if (dashCount > 0) {
//            PathMeasure pathMeasure = new PathMeasure(path, false);
//            pathMeasure.getLength();
//
//            float dashSpace = 0;
//            dashSpace = pathMeasure.getLength() / (dashCount * 2);
//            getPaint().setPathEffect(new DashPathEffect(new float[]{dashSpace, dashSpace}, 0f));
//        }
    }
}