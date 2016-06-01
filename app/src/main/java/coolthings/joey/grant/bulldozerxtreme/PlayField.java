package coolthings.joey.grant.bulldozerxtreme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Random;

import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;
import coolthings.joey.grant.bulldozerxtreme.objects.Target;

public class PlayField extends RelativeLayout {

    public static final Random random = new Random(123908);

    public interface IPlayController {
        List<BasicObject> getAll();

        void onTargetSelected(Target target);
    }

    private IPlayController playController;

    public PlayField(Context context) {
        super(context);
    }

    public PlayField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPlayController(IPlayController playController) {
        this.playController = playController;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playController.onTargetSelected(new Target(event.getX(), event.getY()));
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        if (playController != null) {
            for (BasicObject bo : playController.getAll()) {
                bo.getBasicObjectDrawer().onDraw(canvas);
            }
        }
    }
}
