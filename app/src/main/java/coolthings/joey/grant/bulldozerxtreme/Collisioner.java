package coolthings.joey.grant.bulldozerxtreme;

import android.graphics.PointF;
import android.util.Log;

import java.util.List;

import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;
import coolthings.joey.grant.bulldozerxtreme.objects.BullDozer;

public class Collisioner {

    private PlayField.IPlayController playController;


    public Collisioner(PlayField.IPlayController playController) {
        this.playController = playController;
    }

    public void update() {
        List<BasicObject> bos = playController.getAll();
        for (int i = 0; i < bos.size(); i++) {
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

    private boolean doesCollide(BasicObject a, BasicObject b) {
        PointF ap = a.getPosition();
        PointF bp = b.getPosition();
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
