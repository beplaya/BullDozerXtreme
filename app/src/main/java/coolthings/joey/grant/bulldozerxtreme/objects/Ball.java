package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.Color;

import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public class Ball extends BasicObject {

    private BasicObjectDrawer basicObjectDrawer;
    private int owner = -1;

    public Ball(int id, float x, float y) {
        super("ball_" + id);
        basicObjectDrawer = new BasicObjectDrawer(this, Color.MAGENTA);
        position.x = x;
        position.y = y;
        drag = 10f;
    }

    @Override
    public BasicObjectDrawer getBasicObjectDrawer() {
        return basicObjectDrawer;
    }

    @Override
    public void onCollide(BasicObject basicObject) {
    }

    public void onHitByDozer(int owner, Vector vector, int color) {
        getVector().angle = vector.angle;
        getVector().position.x = vector.position.x;
        getVector().position.y = vector.position.y;
        getVector().magnitude = vector.magnitude * 4;
        vector.magnitude *= .7;
        this.owner = owner;
        basicObjectDrawer.setColor(color);
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
