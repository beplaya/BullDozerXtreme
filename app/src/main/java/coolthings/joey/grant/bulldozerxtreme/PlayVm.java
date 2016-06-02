package coolthings.joey.grant.bulldozerxtreme;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import coolthings.joey.grant.bulldozerxtreme.objects.Ball;
import coolthings.joey.grant.bulldozerxtreme.objects.BasicObject;
import coolthings.joey.grant.bulldozerxtreme.objects.BullDozer;
import coolthings.joey.grant.bulldozerxtreme.objects.Target;
import coolthings.joey.grant.bulldozerxtreme.socket.SocketManager;

public class PlayVm implements TickerTimer.TickerTimerListener, PlayField.IPlayController {

    private Handler handler = new Handler();
    private TickerTimer tickerTimer = new TickerTimer();
    private List<BasicObject> basicObjects = new ArrayList<>();
    private PlayField playField;
    private BullDozer player;
    private Collisioner collisioner;
    private Activity activity;
    private BullDozer otherPlayer;
    private List<Ball> balls;

    public void onCreate(Activity activity) {
        this.activity = activity;
        tickerTimer.registerNewListener(this);
        playField = (PlayField) activity.findViewById(R.id.play_field);
        playField.setPlayController(this);
        player = new BullDozer(BullDozer.LOCAL_PLAYER);
        otherPlayer = new BullDozer(BullDozer.REMOTE_PLAYER);

        otherPlayer.getBasicObjectDrawer().setColor(Color.CYAN);
        collisioner = new Collisioner(this);

        player.getPosition().set(0, 0);
        otherPlayer.getPosition().set(0, 0);

        basicObjects.add(player);
        basicObjects.add(otherPlayer);
        balls = new ArrayList<>();

        balls.add(new Ball(1, 20, 50));
        balls.add(new Ball(2, 40, 50));
        balls.add(new Ball(3, 50, 50));
        balls.add(new Ball(4, 60, 50));
        balls.add(new Ball(5, 80, 50));
        basicObjects.addAll(balls);
        SocketManager.registerListener(new MultiplayManager(this));
        SocketManager.connect(activity.getApplicationContext());

    }

    public void onResume(Activity activity) {

        tickerTimer.start(1000L / 33L);
    }

    public void onPause(Activity activity) {
        tickerTimer.stop();
    }

    @Override
    public void onTimerTick(int tick, long period) {
        if (SocketManager.isConnected() && SocketManager.inRoom()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    playField.invalidate();
                }
            });
            for (BasicObject bo : basicObjects) {
                bo.update();
            }

            collisioner.update();
            SocketManager.getInstance().sendPositionAndVector(player.getPosition(), player.getVector());
            if (SocketManager.getRoom().getPlayerNumber() == 0) {
                for (Ball b : balls) {
                    SocketManager.getInstance().sendBallPosition(b);
                }
            }
        }
    }

    @Override
    public List<BasicObject> getAll() {
        return basicObjects;
    }

    @Override
    public void onTargetSelected(Target target) {
        player.setTarget(target);
    }

    public void onJoinedRoom() {
        int playerNumber = SocketManager.getRoom().getPlayerNumber();
        if (playerNumber == 0) {
            player.setPosition(new PointF(50, 80));
            otherPlayer.setPosition(new PointF(50, 20));
        } else if (playerNumber == 1) {
            player.setPosition(new PointF(50, 20));
            otherPlayer.setPosition(new PointF(50, 80));
        }
        toast("player number: " + playerNumber + "\nRoom: " + SocketManager.getRoom().id);
    }

    private void toast(final String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onReceiveOtherPlayerVectorAndPosition(PointF position, Vector vector) {
        otherPlayer.setPosition(position);
        otherPlayer.setVector(vector);
    }

    public void onReceiveBallVectorAndPosition(String ballId, PointF position, Vector vector, int ownerNumber) {
        if (SocketManager.getRoom().getPlayerNumber() != 0) {
            for (Ball b : balls) {
                if (ballId.equals(b.id)) {
                    b.setPosition(position);
                    b.setVector(vector);
                    b.setOwner(ownerNumber);
                    if (ownerNumber == SocketManager.getRoom().getPlayerNumber()) {
                        b.getBasicObjectDrawer().setColor(player.getBasicObjectDrawer().getColor());
                    } else {
                        b.getBasicObjectDrawer().setColor(otherPlayer.getBasicObjectDrawer().getColor());
                    }
                    return;
                }
            }
        }
    }
}
