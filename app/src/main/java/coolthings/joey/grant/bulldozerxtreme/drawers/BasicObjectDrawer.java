package coolthings.joey.grant.bulldozerxtreme.drawers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.PlayField;
import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;

public class BasicObjectDrawer {

    private BasicObject basicObject;
    private Paint paint;

    public BasicObjectDrawer(BasicObject basicObject, int color) {
        this.basicObject = basicObject;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
    }

    public void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);

        if (basicObject != null) {
            PointF absPosition = PlayField.getAbsolutePosition(basicObject.getPosition());
            canvas.drawCircle(absPosition.x, absPosition.y, basicObject.getHitRadius(), paint);
        }
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public int getColor() {
        return paint.getColor();
    }
}
