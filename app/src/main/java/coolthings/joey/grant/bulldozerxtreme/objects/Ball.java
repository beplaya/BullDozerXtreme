package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.Color;

import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public class Ball extends BasicObject {

    private BasicObjectDrawer basicObjectDrawer;
    private String owner = "?";

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

    public void onHitByDozer(String id, Vector vector, int color) {
        getVector().angle = vector.angle;
        getVector().position.x = vector.position.x;
        getVector().position.y = vector.position.y;
        getVector().magnitude = vector.magnitude * 4;
        vector.magnitude *= .7;
        owner = id;
        basicObjectDrawer.setColor(color);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
