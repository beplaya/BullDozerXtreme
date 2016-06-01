package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public abstract class BasicObject {

    public final String id;
    PointF position = new PointF();
    float speed = 1;

    float hitRadius = 10;
    Target target;

    Vector vector = new Vector();

    protected BasicObject(String id) {
        this.id = id;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void update() {
        if (target != null) {
            vector = target.getVector(position, speed);
        }
        position.x += vector.getVelocityX();
        position.y += vector.getVelocityY();
    }

    public abstract BasicObjectDrawer getBasicObjectDrawer();

    public float getHitRadius() {
        return hitRadius;
    }

    public PointF getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public Target getTarget() {
        return target;
    }

    public Vector getVector() {
        return vector;
    }

    public abstract void onCollide(BasicObject basicObject);

    public void setPosition(PointF position) {
        this.position = position;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

}
