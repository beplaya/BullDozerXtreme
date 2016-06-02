package coolthings.joey.grant.bulldozerxtreme.objects;

import android.graphics.Color;

import coolthings.joey.grant.bulldozerxtreme.drawers.BasicObjectDrawer;
import coolthings.joey.grant.bulldozerxtreme.socket.SocketManager;

public class BullDozer extends BasicObject {

    public static final String LOCAL_PLAYER = "LOCAL_PLAYER";
    public static final String REMOTE_PLAYER = "REMOTE_PLAYER";
    private final BasicObjectDrawer drawer;

    public BullDozer(String id) {
        super(id);
        drawer = new BasicObjectDrawer(this, Color.WHITE);
        speed = 1;
        hitRadius = 40;
        position.x = 100;
        position.y = 200;
        drag = .1f;
    }

    @Override
    public BasicObjectDrawer getBasicObjectDrawer() {
        return drawer;
    }

    @Override
    public void onCollide(BasicObject basicObject) {
        if (basicObject instanceof Ball) {
            Ball ball = (Ball) basicObject;
            ball.onHitByDozer(getOwnerNumber(), vector, drawer.getColor());
        }
    }

    private int getOwnerNumber() {
        int localNumber = SocketManager.getRoom().getPlayerNumber();
        if (id.equals(LOCAL_PLAYER)) {
            return localNumber;
        } else if (id.equals(REMOTE_PLAYER)) {
            return localNumber == 0 ? 1 : 0;
        }
        return -1;
    }
}
