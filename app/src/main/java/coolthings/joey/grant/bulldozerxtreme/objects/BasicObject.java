package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.Collisioner;
import coolthings.joey.grant.bulldozerxtreme.PlayField;
import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public abstract class BasicObject {

    public final String id;
    PointF position = new PointF();
    float speed = .1f;
    float drag = 0f;
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
            vector = target.getVector(PlayField.getAbsolutePosition(getPosition()), speed);
            target = null;
        }
        int offset = 2;
        if (position.x <= offset || position.y <= offset || position.x >= 100 - offset || position.y >= 100 - offset) {
            target = new Target(50, 50);
            getVector().magnitude *= 1.001f;
        }
        position.x += vector.getVelocityX();
        position.y += vector.getVelocityY();

        getVector().magnitude *= 1 - (drag / 100f);
    }


    public void onWallCollide(Collisioner.Wall wall) {
        target = null;
        if (wall.equals(Collisioner.Wall.TOP) || wall.equals(Collisioner.Wall.BOTTOM)) {
            vector.position.y *= -1;
        } else if (wall.equals(Collisioner.Wall.LEFT) || wall.equals(Collisioner.Wall.RIGHT)) {
            vector.position.x *= -1;
        }
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
