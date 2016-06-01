package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.Color;

import coolthings.joey.grant.bulldozerxtreme.PlayField;
import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public class Ball extends BasicObject {

    private BasicObjectDrawer basicObjectDrawer;

    public Ball(int id) {
        super("ball_" + id);
        basicObjectDrawer = new BasicObjectDrawer(this, Color.MAGENTA);
        position.x = 100f + (float) (PlayField.random.nextFloat() * 400f);
        position.y = 200f + (float) (PlayField.random.nextFloat() * 800f);
    }

    @Override
    public BasicObjectDrawer getBasicObjectDrawer() {
        return basicObjectDrawer;
    }

    @Override
    public void onCollide(BasicObject basicObject) {

    }

    public void onHitByDozer(Vector vector) {
        getVector().angle = vector.angle;
        getVector().position.x = vector.position.x;
        getVector().position.y = vector.position.y;
        getVector().magnitude = vector.magnitude * 4;
        vector.magnitude *= .7;
    }

    @Override
    public void update() {
        super.update();
        getVector().magnitude *= .9f;
    }
}
