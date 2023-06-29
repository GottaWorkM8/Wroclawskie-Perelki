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

public class CircleImage extends Overlay {
    private final Paint paint;
    private final GeoPoint geoPoint;
    private boolean rotation;
    private float radius;
    private float width;

    @SuppressLint("ClickableViewAccessibility")
    public CircleImage(GeoPoint geoPoint, MapView mapView, float r) {
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
                radius = mapView.getProjection().metersToPixels(width, geoPoint.getLatitude(), mapView.getZoomLevelDouble());
                // Calculate the center point of the circle on the screen

            }
        });

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                radius = mapView.getProjection().metersToPixels(width, geoPoint.getLatitude(), mapView.getZoomLevelDouble());

            }
            return false;
        });
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        mapView.requestLayout();
        Projection projection = mapView.getProjection();
        Point point = new Point();
        projection.toPixels(geoPoint, point);
        if (!shadow) {
            canvas.drawCircle(point.x, point.y, radius, paint);
        }
    }

    private Point getScreenCoordinates(MapView mapView, GeoPoint geoPoint) {
        Projection projection = mapView.getProjection();
        Point point = new Point();
        projection.toPixels(geoPoint, point);
        return point;
    }
}