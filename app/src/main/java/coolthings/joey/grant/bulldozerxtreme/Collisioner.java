package coolthings.joey.grant.bulldozerxtreme;

import android.graphics.PointF;
import android.util.Log;

import java.util.List;

import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;
import coolthings.joey.grant.bulldozerxtreme.objects.BullDozer;

public class Collisioner {

    private PlayField.IPlayController playController;

    public enum Wall {
        LEFT, TOP, RIGHT, BOTTOM;
    }

    public Collisioner(PlayField.IPlayController playController) {
        this.playController = playController;
    }

    public void update() {
        List<BasicObject> bos = playController.getAll();
        for (int i = 0; i < bos.size(); i++) {
            Wall wall = detectWall(bos.get(i));
            if (wall != null) {
                bos.get(i).onWallCollide(wall);
            }
            for (int j = 0; j < bos.size(); j++) {
                if (i != j) {
                    if (doesCollide(bos.get(i), bos.get(j))) {
                        bos.get(i).onCollide(bos.get(j));
                        bos.get(j).onCollide(bos.get(i));
                    }
                }
            }
        }

    }

    private Wall detectWall(BasicObject basicObject) {
        PointF percentPosition = basicObject.getPosition();
        float offset = 2f;
        if (percentPosition.x < offset) {
            return Wall.LEFT;
        } else if (percentPosition.y < offset) {
            return Wall.TOP;
        } else if (percentPosition.x > 100 - offset) {
            return Wall.RIGHT;
        } else if (percentPosition.y > 100 - offset) {
            return Wall.BOTTOM;
        }
        return null;
    }

    private boolean doesCollide(BasicObject a, BasicObject b) {
        PointF ap = PlayField.getAbsolutePosition(a.getPosition());
        PointF bp = PlayField.getAbsolutePosition(b.getPosition());
        float dx = Math.abs(ap.x - bp.x);
        float dy = Math.abs(ap.y - bp.y);
        float h = (float) Math.sqrt((dx * dx) + (dy * dy));
        if (a instanceof BullDozer || b instanceof BullDozer) {
            Log.e("", h + " " + a.getHitRadius() + " " + b.getHitRadius());
        }
        if (h <= a.getHitRadius() || h <= b.getHitRadius()) {
            if (a instanceof BullDozer || b instanceof BullDozer) {
                Log.e("", "HIT");
            }
            return true;
        }
        return false;
    }
}
