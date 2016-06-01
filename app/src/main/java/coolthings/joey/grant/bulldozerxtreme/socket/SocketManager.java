package coolthings.joey.grant.bulldozerxtreme.socket;

import android.content.Context;
import android.graphics.PointF;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import coolthings.joey.grant.bulldozerxtreme.Vector;
import coolthings.joey.grant.bulldozerxtreme.objects.Ball;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.JOINED_ROOM;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.REC_BALL_VECTOR_POSITION;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.REC_VECTOR_POSITION;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.SEND_BALL_VECTOR_POSITION;
import static coolthings.joey.grant.bulldozerxtreme.socket.SocketManager.Events.SEND_VECTOR_POSITION;

public class SocketManager {

    private static Room room = null;

    public enum Events {
        REC_VECTOR_POSITION, SEND_VECTOR_POSITION, JOINED_ROOM, SEND_BALL_VECTOR_POSITION, REC_BALL_VECTOR_POSITION
    }

    public interface ISocketListener {
        void onConnected();

        void onDisconnected();

        void onEvent(Events event, Object o);
    }

//    public static final String DEFAULT_URL = "http://192.168.1.69:8080";
    public static final String DEFAULT_URL = "http://10.206.4.56:8080";
    public static final String URL = DEFAULT_URL;
    private static boolean isConnected = false;
    private static String deviceId;
    private static SocketManager instance;
    private static Context context;
    private static Socket socketIO;
    private static String socketId = "";
    private static List<ISocketListener> listeners = new ArrayList<>();

    public static SocketManager getInstance() {
        if (SocketManager.instance == null) {
            SocketManager.instance = new SocketManager();
        }
        return SocketManager.instance;
    }

    public static void registerListener(ISocketListener listener) {
        listeners.add(listener);
    }

    public static void clearListeners() {
        listeners.clear();
    }

    private SocketManager() {
        createSocket();
    }

    private void createSocket() {
        try {
            //~
//            SSLContext sslContext = SSLContext.getDefault();
//            // default settings for all sockets
//            IO.setDefaultSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            IO.setDefaultHostnameVerifier(hostnameVerifier);
            //~
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socketIO = IO.socket(URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void connect(Context context) {

        SocketManager.context = context;
        SocketManager.deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getInstance()._connect();
    }

    private void _connect() {
        for (ISocketListener l : listeners) {
            l.onConnected();
        }
        disconnect();

        socketIO.connect();
        createConnectionStatusEventListeners();

        //
        socketIO.on(JOINED_ROOM.name(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    SocketManager.room = new Room(jsonObject.getString(Room.JSON.id));
                    SocketManager.room.setPlayerNumber(jsonObject.getInt(Room.JSON.playerNumber));
                    for (ISocketListener l : listeners) {
                        l.onEvent(JOINED_ROOM, SocketManager.room);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socketIO.on(REC_VECTOR_POSITION.name(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                for (ISocketListener l : listeners) {
                    l.onEvent(REC_VECTOR_POSITION, args[0]);
                }
            }
        });
        socketIO.on(REC_BALL_VECTOR_POSITION.name(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                for (ISocketListener l : listeners) {
                    l.onEvent(REC_BALL_VECTOR_POSITION, args[0]);
                }
            }
        });
        //
    }

    private void createConnectionStatusEventListeners() {
        socketIO.on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onConnectionError();
            }
        });
        socketIO.on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onConnectionError();
            }
        });
        socketIO.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onConnectionError();
            }
        });
        socketIO.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onConnectionError();
            }
        });
        socketIO.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isConnected = false;
            }
        });

        socketIO.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isConnected = true;
                //sendCommand("c:androidDeviceId", SocketManager.deviceId);
            }
        });

    }

    private void onConnectionError() {
        Log.e("", "onConnectionError");
    }

    private void disconnect() {
        isConnected = false;
        room = null;
        if (socketIO != null) {
            try {
                for (ISocketListener bcl : listeners) {
                    bcl.onDisconnected();
                }
                socketIO.disconnect();
                socketIO.close();
                socketIO = null;
                createSocket();
            } catch (Exception e) {
                Log.e("", "socket disconnect error!", e);
            }
        }
    }

    public static Room getRoom() {
        return room;
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static boolean inRoom() {
        return room != null;
    }

    public void sendPositionAndVector(PointF position, Vector vector) {
        sendCommand(SEND_VECTOR_POSITION, room.id, position.x, position.y,
                vector.magnitude, vector.angle, vector.position.x, vector.position.y);
    }

    public void sendBallPosition(Ball ball) {
        PointF position = ball.getPosition();
        Vector vector = ball.getVector();
        sendCommand(SEND_BALL_VECTOR_POSITION, room.id, ball.id, position.x, position.y,
                vector.magnitude, vector.angle, vector.position.x, vector.position.y);
    }

    public void sendCommand(Events event, Object... args) {
        sendCommand(event.name(), args);
    }

    public void sendCommand(String methodName, Object... args) {
        if (socketIO != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    builder.append(",");
                }
                builder.append(args[i]);
            }
            //Log.i("emit ", methodName + " " + builder.toString());
            socketIO.emit(methodName, builder.toString());
        }
    }

}
