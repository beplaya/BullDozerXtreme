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
        float boostVX = 1;
        float boostVY = 1;
        if (target == null) {
            float offset = 5f;
            if (position.x < offset) {
                boostVX = 1.1f;
            } else if (position.x > 100 - offset) {
                boostVX = .9f;
            }
            if (position.y < offset) {
                boostVY = 1.1f;
            } else if (position.y > 100 - offset) {
                boostVY = .9f;
            }
        } else {
            vector = target.getVector(PlayField.getAbsolutePosition(getPosition()), speed);
            target = null;
        }

        position.x += vector.getVelocityX() * boostVX;
        position.y += vector.getVelocityY() * boostVY;

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
