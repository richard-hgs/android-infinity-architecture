package com.infinity.architecture.utils.location;

import android.graphics.RectF;
import android.util.Log;

public class PointUtils {
    private static final String TAG = "PointUtils";

    public static Point2D calculateBetweenPoint(Point2D point1, Point2D point2, double distance) {
        double xDistance = Math.abs(point1.getX() - point2.getX());
        double yDistance = Math.abs(point2.getX() - point2.getY());
        double distanceAB = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

        double angleAB = Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
        double deltaXAC = distance * Math.cos(angleAB);
        double deltaYAC = distance * Math.sin(angleAB);

//        double x3 = point1.getX() + deltaXAC;
//        double y3 = point1.getY() + deltaYAC;

        return new Point2D(
                point1.getX() + deltaXAC,
                point1.getY() + deltaYAC
        );
    }

    public static double calculateDistanceBetweenPoint(Point2D point1, Point2D point2) {
        return Math.sqrt(
                Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2)
        );
    }

    public static Point2D rotatePoint2D(Point2D startPoint, Point2D centerPoint, double angle) {
        double radianAngle = Math.toRadians(angle);
        double cos = Math.cos(radianAngle);
        double sin = Math.sin(radianAngle);

        double dx = startPoint.getX() - centerPoint.getX();
        double dy = startPoint.getY() - centerPoint.getY();

        float mx = (float) (centerPoint.getX() + dx * cos - dy * sin);
        float my = (float) (centerPoint.getY() + dx * sin + dy * cos);

        return new Point2D(mx,my);
    }

    public static Point2D getCirclePointByAngle(Point2D centerPoint, double width, double height, double angle) {
        double x = centerPoint.getX() + width * Math.cos(angle * Math.PI / 180);
        double y = centerPoint.getY() + height * Math.sin(angle * Math.PI / 180);
        return new Point2D(x, y);
    }

    public static Point2D getRectanglePointByAngle(RectF rect, double angle) {
        double theta = angleFix(angle * -1);
        theta = Math.toRadians(theta);

        // Move theta to range -M_PI .. M_PI
        double twoPI = Math.PI * 2.;
        while (theta < -Math.PI) {
            theta += twoPI;
        }

        while (theta > Math.PI) {
            theta -= twoPI;
        }

        // find edge ofview
        // Ref: http://stackoverflow.com/questions/4061576/finding-points-on-a-rectangle-at-a-given-angle
        float aa = rect.width();                                          // "a" in the diagram
        float bb = rect.height();                                         // "b"

        // Find our region (diagram)
        double rectAtan = Math.atan2(bb, aa);
        double tanTheta = Math.tan(theta);

        int region;
        if ((theta > -rectAtan) && (theta <= rectAtan)) {
            region = 1;
        } else if ((theta > rectAtan) && (theta <= (Math.PI - rectAtan))) {
            region = 2;
        } else if ((theta > (Math.PI - rectAtan)) || (theta <= -(Math.PI - rectAtan))) {
            region = 3;
        } else {
            region = 4;
        }

        Point2D edgePoint = new Point2D(rect.centerX(), rect.centerY());
        float xFactor = 1;
        float yFactor = 1;

        switch (region) {
            case 1: yFactor = -1;       break;
            case 2: yFactor = -1;       break;
            case 3: xFactor = -1;       break;
            case 4: xFactor = -1;       break;
        }

        if ((region == 1) || (region == 3)) {
            edgePoint.setX(edgePoint.getX() + xFactor * (aa / 2.));                                     // "Z0"
            edgePoint.setY(edgePoint.getY() + yFactor * (aa / 2.) * tanTheta);
        } else {                                                                                        // region 2 or 4
            edgePoint.setX(edgePoint.getX() + xFactor * (bb / (2. * tanTheta)));                        // "Z1"
            edgePoint.setY(edgePoint.getY() + yFactor * (bb /  2.));
        }

        return edgePoint;
    }

    public static Point2D getRectanglePointByAngle2(RectF rect, double angle) {
        if (angle >= 45 && angle <= 135) {
            // bottom
            double angleBaseNinety = (angle - 45);
            Log.d(TAG, "rect: " + rect.toString() + " - angle: " + angle + " - angleBaseNinety: " + angleBaseNinety + " - x: " + (rect.width() - (angleBaseNinety * rect.width() / 90)));
            return new Point2D(rect.width() - ((angleBaseNinety * rect.width()) / 90), rect.height());
        } else if (angle >= 135 && angle <= 225) {
            // left
            double angleBaseNinety = angle - 135;
            return new Point2D(0, (angleBaseNinety * 100 / 90));
        } else if (angle >= 225 && angle <= 315) {
            // top
            double angleBaseNinety = angle - 225;
            return new Point2D((angleBaseNinety * 100 / 90), 0);
        } else if ((angle >= 315 && angle <= 360) || angle >= 0 && angle <= 45) {
            // right
            double angleBaseNinety = 0;
            if (angle >= 315 && angle <= 360) {
                angleBaseNinety = angle - 315;
            } else {
                angleBaseNinety = angle + 45;
            }
            return new Point2D(rect.width(), (angleBaseNinety * 100 / 90));
        }

        return new Point2D(0, 0);
    }

    /**
     * Fetches angle relative to screen centre point
     * where 3 O'Clock is 0 and 12 O'Clock is 270 degrees
     *
     * @param point
     * @param centerPoint
     * @return angle in degress from 0-360.
     */
    public static double getAngle(Point2D point, Point2D centerPoint) {
        double angle = Math.atan2(point.getY()-centerPoint.getY(), point.getX()-centerPoint.getX());
        return Math.toDegrees(angle);
    }

    public static Point2D getCurvedPoint(Point2D startPoint, Point2D endPoint, double radius) {
        double midX         = startPoint.getX() + ((endPoint.getX() - startPoint.getX()) / 2);
        double midY         = startPoint.getY() + ((endPoint.getY() - startPoint.getY()) / 2);
        double xDiff        = midX - startPoint.getX();
        double yDiff        = midY - startPoint.getY();
        double angle        = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) - 90;
        double angleRadians = Math.toRadians(angle);
        double pointX        = (midX + radius * Math.cos(angleRadians));
        double pointY        = (midY + radius * Math.sin(angleRadians));

        return new Point2D(pointX, pointY);
    }

    public static double calculateCircleCircunference(double diameter) {
        return Math.PI * diameter;
    }

    public static Point2D balancePoint(Point2D point2D) {
        double x = point2D.getX();
        double y = point2D.getY();
        if (x > y) {
            double diff = (x - y) / 2;
            x -= diff;
            y += diff;
        } else if (y > x) {
            double diff = (y - x) / 2;
            x += diff;
            y -= diff;
        }
        return new Point2D(x, y);
    }

    public static Point2D movePointToCenterDraw(
            Point2D point2D,
            double minPaintX,
            double minPaintY,
            double maxPaintX,
            double maxPaintY,
            double width,
            double height
    ) {
        // Align the image in center
        double paintWidth = maxPaintX - minPaintX;
        double paintHeight = maxPaintY - minPaintY;

        double leftSpace = minPaintX;
        double topSpace = minPaintY;
        double rightSpace = width - maxPaintX;
        double bottomSpace = height - maxPaintY;

        double newPointX = point2D.getX();
        double newPointY = point2D.getY();

        double hDiff = 0;
        double vDiff = 0;

        if (leftSpace > rightSpace) {
            hDiff = (leftSpace - rightSpace) / 2;
            newPointX -= hDiff;
        } else if (rightSpace > leftSpace) {
            hDiff = (rightSpace - leftSpace) / 2;
            newPointX += hDiff;
        }
        if (topSpace > bottomSpace) {
            vDiff = (topSpace - bottomSpace) / 2;
            newPointY -= vDiff;
        } else if (bottomSpace > topSpace) {
            vDiff = (bottomSpace - topSpace) / 2;
            newPointY += vDiff;
        }

        return new Point2D(newPointX, newPointY);
    }

    public static double angleFix(double angle) {
        angle = angle % 360;
        if (angle < 0) {
            angle = 360 + angle;
        }
        return angle % 360;
    }

    private static Point2D normalize(Point2D point2D) {
        // Makes this vector have a ::ref::magnitude of 1.
        Point2D newPoint2D = new Point2D(point2D.getX(), point2D.getY());

        double mag = Math.sqrt(point2D.getX() * point2D.getX() + point2D.getY() * point2D.getY());
        double kEpsilon = 0.00001F;

        if (mag > kEpsilon) {
            newPoint2D = new Point2D(newPoint2D.getX() / mag, newPoint2D.getY() / mag);
        } else {
            newPoint2D = new Point2D(0, 0);
        }

        return newPoint2D;
    }

    private static Point2D extents(RectF rectF) {
        return new Point2D((rectF.right - rectF.left) / 2, (rectF.bottom - rectF.top) / 2);
    }

    private static double negMod(double val1, double val2) {
        return ((val1 % val2) + val2) % val2;
    }

    /** Given a heading which may be outside the +/- PI range, 'unwind' it back into that range. */
    private static double unwindRadians(double A) {
        while (A > Math.PI)
        {
            A -= (double) Math.PI * 2.0f;
        }

        while (A < -Math.PI)
        {
            A += (double) Math.PI * 2.0f;
        }

        return A;
    }
}

