package wro.per.others;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

public class Place extends Overlay {
    private final Paint paint1, paint2;
    private final GeoPoint geoPoint;

    private boolean rotation;

    public Place(GeoPoint geoPoint) {
        rotation = false;
        this.geoPoint = geoPoint;
        paint1 = new Paint();
        paint1.setColor(Color.argb(255, 0, 255, 0));
        paint1.setStyle(Paint.Style.FILL);
        paint2 = new Paint();
        paint2.setColor(Color.argb(255, 0, 150, 0));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(7f);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        if (!shadow) {
            Projection projection = mapView.getProjection();
            Point point = new Point();
            projection.toPixels(geoPoint, point);
            canvas.drawCircle(point.x, point.y, 20f, paint1);
            canvas.drawCircle(point.x, point.y, 20f, paint2);
        }
    }

}