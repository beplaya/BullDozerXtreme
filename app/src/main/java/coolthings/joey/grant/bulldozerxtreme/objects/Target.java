package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.Vector;

public class Target {

    PointF position = new PointF();

    public Target(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public Vector getVector(PointF absPosition, float speed) {
        Vector vector = new Vector();


        float dx = this.position.x - absPosition.x;

        float dy = this.position.y - absPosition.y;

        vector.angle = Math.atan(dy / dx);
        vector.position.x = (float) Math.cos(vector.angle);
        vector.position.y = (float) Math.sin(vector.angle);
        vector.magnitude = dx < 0 ? -speed : speed;
        return vector;
    }
}
