package coolthings.joey.grant.bulldozerxtreme;

import android.graphics.PointF;

import coolthings.joey.grant.bulldozerxtreme.socket.SocketManager;

import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.JOINED_ROOM;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.REC_BALL_VECTOR_POSITION;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.REC_VECTOR_POSITION;

public class MultiplayManager implements SocketManager.ISocketListener {
    private PlayVm playVm;

    public MultiplayManager(PlayVm playVm) {
        this.playVm = playVm;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onEvent(SocketManager.Events event, Object o) {
        if (event.equals(JOINED_ROOM)) {
            playVm.onJoinedRoom();
        } else if (event.equals(REC_VECTOR_POSITION)) {
            onReceiveOtherPlayerVectorAndPosition(o);
        } else if (event.equals(REC_BALL_VECTOR_POSITION)) {
            onReceiveBallVectorAndPosition(o);
        }
    }

    private void onReceiveBallVectorAndPosition(Object o) {
        //roomid, ballid, position.x, position.y, vector.magnitude, vector.angle, vector.position.x, vector.position.y
        String csv = o.toString();
        String[] split = csv.split(",");
        String ballId = split[1];
        int start = 2;
        PointF position = getPosition(start, split);
        Vector vector = getVector(start, split);
        playVm.onReceiveBallVectorAndPosition(ballId, position, vector);
    }

    private void onReceiveOtherPlayerVectorAndPosition(Object o) {
        //roomid, position.x, position.y, vector.magnitude, vector.angle, vector.position.x, vector.position.y
        String csv = o.toString();
        String[] split = csv.split(",");
        int start = 1;
        PointF position = getPosition(start, split);
        Vector vector = getVector(start, split);
        playVm.onReceiveOtherPlayerVectorAndPosition(position, vector);
    }

    private Vector getVector(int start, String[] split) {
        Vector vector = new Vector();
        vector.magnitude = Float.parseFloat(split[start+2]);
        vector.angle = Float.parseFloat(split[start+3]);
        vector.position.x = Float.parseFloat(split[start+4]);
        vector.position.y = Float.parseFloat(split[start+5]);
        return vector;
    }
    private PointF getPosition(int start, String[] split) {
        PointF position = new PointF();
        position.x = Float.parseFloat(split[start]);
        position.y = Float.parseFloat(split[start+1]);
        return position;
    }
}
