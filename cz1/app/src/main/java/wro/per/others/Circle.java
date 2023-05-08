package wro.per.others;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

public class Circle extends Overlay {
    private final Paint paint;
    private final GeoPoint geoPoint;
    private boolean rotation;
    private float radius;
    private float width;
    private Point center;

    @SuppressLint("ClickableViewAccessibility")
    public Circle(GeoPoint geoPoint, MapView mapView, float r) {
        rotation = false;
        this.geoPoint = geoPoint;
        width = r;
        paint = new Paint();
        paint.setColor(Color.argb(150, 0, 150, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);

        mapView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Calculate the initial circle radius based on the current zoom level
                radius = width * (float) Math.pow(2, mapView.getZoomLevelDouble());
                System.out.println("Radius: "+radius);
                // Calculate the center point of the circle on the screen
                center = getScreenCoordinates(mapView, geoPoint);
                mapView.invalidate();
            }
        });

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float newRadius = width * (float) Math.pow(2, mapView.getZoomLevelDouble());
                if (newRadius != radius) {
                    radius = newRadius;
                    System.out.println("Radius: "+radius);
                    // Calculate the center point of the circle on the screen
                    center = getScreenCoordinates(mapView, geoPoint);
                    mapView.invalidate();
                }
            }
            return false;
        });
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        if (!shadow) {
            // Draw the circle using the screen coordinates of its center point
            canvas.drawCircle(center.x, center.y, radius, paint);
        }
    }

    // Helper method to calculate the screen coordinates of a GeoPoint
    private Point getScreenCoordinates(MapView mapView, GeoPoint geoPoint) {
        Projection projection = mapView.getProjection();
        Point point = new Point();
        projection.toPixels(geoPoint, point);
        return point;
    }
}