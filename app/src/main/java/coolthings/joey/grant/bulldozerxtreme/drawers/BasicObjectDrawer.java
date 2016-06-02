package coolthings.joey.grant.bulldozerxtreme.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;

public class BasicObjectDrawer {

    private BasicObject basicObject;
    private Paint paint;

    public BasicObjectDrawer(BasicObject basicObject, int color) {
        this.basicObject = basicObject;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public void onDraw(Canvas canvas) {
        if (basicObject != null) {
            PointF position = basicObject.getPosition();
            canvas.drawCircle(position.x, position.y, basicObject.getHitRadius(), paint);
        }
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public int getColor() {
        return paint.getColor();
    }
}
