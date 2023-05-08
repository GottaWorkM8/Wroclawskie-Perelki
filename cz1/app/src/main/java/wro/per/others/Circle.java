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
import org.osmdroid.views.overlay.GroundOverlay;
import org.osmdroid.views.overlay.Overlay;

public class Circle extends GroundOverlay {
    private final Paint paint;
    private final GeoPoint geoPoint;
    private boolean rotation;

    private float radius;

    public Circle(GeoPoint geoPoint, MapView mapView, float r) {
        rotation = false;
        this.geoPoint = geoPoint;
        this.radius = r;
        paint = new Paint();
        paint.setColor(Color.argb(255, 0, 200, 255));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);

    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        if (!shadow) {
            Projection projection = mapView.getProjection();
            Point point = new Point();
            projection.toPixels(geoPoint, point);
            canvas.drawCircle(point.x, point.y, 20f, paint);
        }
    }
}

