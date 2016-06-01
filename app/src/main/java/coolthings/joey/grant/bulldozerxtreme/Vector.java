package coolthings.joey.grant.bulldozerxtreme;

import android.graphics.PointF;

public class Vector {
    public PointF position = new PointF();
    public double magnitude = 0;
    public double angle = 0;

    public float getVelocityX() {
        float v = (float) (position.x * magnitude);
        return v;
    }

    public float getVelocityY() {
        float v = (float) (position.y * magnitude);
        return v;
    }
}
