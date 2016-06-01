package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.Color;

import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;

public class BullDozer extends BasicObject {

    private final BasicObjectDrawer drawer;

    public BullDozer(String id) {
        super(id);
        drawer = new BasicObjectDrawer(this, Color.WHITE);
        speed = 3;
        hitRadius = 40;
        position.x = 100;
        position.y = 200;
    }

    @Override
    public BasicObjectDrawer getBasicObjectDrawer() {
        return drawer;
    }

    @Override
    public void onCollide(BasicObject basicObject) {
        if (basicObject instanceof Ball){
            Ball ball = (Ball) basicObject;
            ball.onHitByDozer(getVector());
        }
    }
}
